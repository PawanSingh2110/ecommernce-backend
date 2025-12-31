package com.example.E_commerce.Controller.Product;

import com.example.E_commerce.DTO.Product.ProductCreateRequest;
import com.example.E_commerce.DTO.Product.ProductResponse;
import com.example.E_commerce.Service.Product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ================= PUBLIC ENDPOINTS =================

    /**
     * Get single product by slug
     * PUBLIC
     */
    @GetMapping("/products/{slug}")
    public ResponseEntity<ProductResponse> getProductBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(
                productService.getProductBySlug(slug)
        );
    }

    /**
     * Get latest products
     * PUBLIC
     */
    @GetMapping("/products/latest")
    public ResponseEntity<List<ProductResponse>> getLatestProducts(
            @RequestParam(defaultValue = "6") int limit
    ) {
        return ResponseEntity.ok(
                productService.getLatestProducts(limit)
        );
    }

    /**
     * Get similar products
     * PUBLIC
     */
    @GetMapping("/products/{slug}/similar")
    public ResponseEntity<List<ProductResponse>> getSimilarProducts(
            @PathVariable String slug,
            @RequestParam(defaultValue = "4") int limit
    ) {
        return ResponseEntity.ok(
                productService.getSimilarProducts(slug, limit)
        );
    }

    // ================= ADMIN ENDPOINTS =================

    /**
     * Create new product
     * ADMIN ONLY
     */
    @PostMapping(
            value = "/admin/products",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ProductResponse> createProduct(
            @ModelAttribute ProductCreateRequest request
    ) {
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * Update existing product
     * ADMIN ONLY
     */
    @PutMapping(
            value = "/admin/products/{productId}",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @ModelAttribute ProductCreateRequest request
    ) {
        return ResponseEntity.ok(
                productService.updateProduct(productId, request)
        );
    }

    /**
     * Delete product (soft delete)
     * ADMIN ONLY
     */
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all active products (ADMIN view)
     * NOTE: This still returns ACTIVE products only
     */
    @GetMapping("/admin/products")
    public ResponseEntity<List<ProductResponse>> getAllActiveProducts(
            @RequestParam(defaultValue = "100") int limit
    ) {
        return ResponseEntity.ok(
                productService.getLatestProducts(limit)
        );
    }

    @GetMapping("/products/category/{slug}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(
                productService.getProductsByCategory(slug)
        );
    }

}
