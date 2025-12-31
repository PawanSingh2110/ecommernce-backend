package com.example.E_commerce.Impliment.Product;

import com.example.E_commerce.DTO.Product.ProductCreateRequest;
import com.example.E_commerce.DTO.Product.ProductResponse;
import com.example.E_commerce.Repo.Category.CategoryRepository;
import com.example.E_commerce.Repo.Product.ProductRepo;
import com.example.E_commerce.Service.Product.ProductService;
import com.example.E_commerce.modal.Category.Category;
import com.example.E_commerce.modal.Product.*;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepository categoryRepo;

    private static final String UPLOAD_DIR = "uploads/products/";

    public ProductServiceImpl(ProductRepo productRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    // ✅ Create upload directory once
    @PostConstruct
    public void init() {
        new File(UPLOAD_DIR).mkdirs();
    }

    // ================= CREATE =================
    private String toSlug(String input) {
        if (input == null) {
            return null;
        }

        String slug = input.trim().toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // remove non-alphanumeric chars
                .replaceAll("\\s+", "-")        // spaces -> single hyphen
                .replaceAll("-+", "-");         // collapse multiple hyphens

        return slug;
    }
    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {

        Category category = categoryRepo.findBySlug(request.getCategorySlug())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (request.getImages() == null || request.getImages().isEmpty()) {
            throw new RuntimeException("At least one product image is required");
        }

        if (request.getImages().size() > 5) {
            throw new RuntimeException("Maximum 5 images allowed");
        }

        // ✅ if slug is null or empty, build from name
        String slug = request.getSlug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = toSlug(request.getName());
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setSlug(slug);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE); // ✅ FIX

        List<ProductImage> images = request.getImages().stream()
                .map(file -> saveImage(file, product))
                .toList();

        product.setImages(images);

        List<ProductSize> sizes = request.getSizes().entrySet().stream()
                .map(e -> new ProductSize(null, e.getKey(), e.getValue(), product))
                .toList();

        product.setSizes(sizes);

        return mapToResponse(productRepo.save(product));
    }

    // ================= UPDATE =================
    @Override
    public ProductResponse updateProduct(Long productId, ProductCreateRequest request) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new RuntimeException("Cannot update inactive product");
        }

        if (request.getName() != null && !request.getName().isBlank())
            product.setName(request.getName());

        if (request.getSlug() != null && !request.getSlug().isBlank())
            product.setSlug(request.getSlug());

        if (request.getDescription() != null && !request.getDescription().isBlank())
            product.setDescription(request.getDescription());

        if (request.getPrice() != null)
            product.setPrice(request.getPrice());

        if (request.getDiscountPrice() != null)
            product.setDiscountPrice(request.getDiscountPrice());

        if (request.getCategorySlug() != null && !request.getCategorySlug().isBlank()) {
            Category category = categoryRepo.findBySlug(request.getCategorySlug())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        // ✅ FIX: enforce total image limit
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            int totalImages = product.getImages().size() + request.getImages().size();
            if (totalImages > 5) {
                throw new RuntimeException("Maximum 5 images allowed per product");
            }

            List<ProductImage> newImages = request.getImages().stream()
                    .map(file -> saveImage(file, product))
                    .toList();

            product.getImages().addAll(newImages);
        }

        if (request.getSizes() != null && !request.getSizes().isEmpty()) {
            product.getSizes().clear();
            product.getSizes().addAll(
                    request.getSizes().entrySet().stream()
                            .map(e -> new ProductSize(null, e.getKey(), e.getValue(), product))
                            .toList()
            );
        }

        return mapToResponse(productRepo.save(product));
    }

    // ================= DELETE =================
    @Override
    public void deleteProduct(Long productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ FIX: delete files from disk
        for (ProductImage img : product.getImages()) {
            deleteImageFile(img.getImageUrl());
        }

        product.getImages().clear();
        product.getSizes().clear();
        product.setStatus(ProductStatus.INACTIVE);

        productRepo.save(product);
    }

    // ================= GET BY SLUG =================
    @Override
    public ProductResponse getProductBySlug(String slug) {
        return productRepo
                .findBySlugAndStatus(slug, ProductStatus.ACTIVE)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ================= LATEST =================
    @Override
    public List<ProductResponse> getLatestProducts(int limit) {
        return productRepo.findLatestProducts("ACTIVE", limit)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= SIMILAR =================
    @Override
    public List<ProductResponse> getSimilarProducts(String slug, int limit) {

        Product current = productRepo
                .findBySlugAndStatus(slug, ProductStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return productRepo.findTop4ByCategory_SlugAndStatusAndSlugNot(
                        current.getCategory().getSlug(),
                        ProductStatus.ACTIVE,
                        slug
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String categorySlug) {

        List<Product> products = productRepo.findByCategory_SlugAndStatus(
                categorySlug,
                ProductStatus.ACTIVE
        );

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ================= IMAGE HELPERS =================
    private ProductImage saveImage(MultipartFile file, Product product) {
        try {
            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + extension;

            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, file.getBytes());

            ProductImage image = new ProductImage();
            image.setImageUrl("/uploads/products/" + fileName);
            image.setProduct(product);
            return image;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private void deleteImageFile(String imageUrl) {
        try {
            String filePath = imageUrl.replace("/uploads/products/", UPLOAD_DIR);
            Files.deleteIfExists(Paths.get(filePath));
        } catch (Exception ignored) {}
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }

    // ================= RESPONSE =================
    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscountPrice(),
                product.getImages().stream()
                        .map(ProductImage::getImageUrl)
                        .toList(),
                product.getSizes().stream()
                        .collect(Collectors.toMap(
                                ProductSize::getSize,
                                ProductSize::getQuantity
                        )),
                product.getCategory().getName(),
                product.getCategory().getSlug()
        );
    }
}
