package com.example.E_commerce.Impliment.Category;

import com.example.E_commerce.DTO.Category.CategoryRequestDTO;
import com.example.E_commerce.DTO.Category.CategoryResponseDTO;
import com.example.E_commerce.DTO.Category.CategorySummaryDTO;
import com.example.E_commerce.Repo.Category.CategoryRepository;
import com.example.E_commerce.Service.Category.CategoryService;
import com.example.E_commerce.modal.Category.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        String baseSlug = request.getName()
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");

        String slug = baseSlug;
        int count = 1;
        while (categoryRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + count++;
        }

        MultipartFile image = request.getImage();
        if (image == null || image.isEmpty()) {
            // 🔥 CHANGE 1: Better error
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is required");
        }

        String original = image.getOriginalFilename();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf("."))
                : ".png";

        String fileName = slug + ext;
        // 🔥 CHANGE 2: ABSOLUTE PATH (MAIN FIX!)
        Path uploadDir = Paths.get("src/main/resources/static/uploads/categories");
        // WAS: Paths.get("uploads/categories");

        try {
            Files.createDirectories(uploadDir);
            Files.copy(
                    image.getInputStream(),
                    uploadDir.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (Exception e) {
            // 🔥 CHANGE 3: Better error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image upload failed: " + e.getMessage());
        }

        String imageUrl = "/uploads/categories/" + fileName;

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(slug);
        category.setImageUrl(imageUrl);
        category.setDescription(request.getDescription());

        Category saved = categoryRepository.save(category);

        return new CategoryResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getSlug(),
                saved.getImageUrl(),
                saved.getDescription()
        );
    }

    @Override
    public List<CategorySummaryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(category -> new CategorySummaryDTO(
                category.getName(),
                category.getSlug(),
                category.getImageUrl()
        )).toList();
    }

    @Override
    public CategoryResponseDTO getCategoryBySlug(String slug) {
        Category c = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return new CategoryResponseDTO(
                c.getId(),
                c.getName(),
                c.getSlug(),
                c.getImageUrl(),
                c.getDescription()
        );
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new RuntimeException("no category is found "));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category update = categoryRepository.save(category);
        return new CategoryResponseDTO(
                update.getId(),
                update.getName(),
                update.getSlug(),
                update.getImageUrl(),
                update.getDescription()

        );
    }

    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );

        categoryRepository.delete(category);

    }
}
