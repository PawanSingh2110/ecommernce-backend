package com.example.E_commerce.Controller.Category;

import com.example.E_commerce.DTO.Category.CategoryRequestDTO;
import com.example.E_commerce.DTO.Category.CategoryResponseDTO;
import com.example.E_commerce.DTO.Category.CategorySummaryDTO;
import com.example.E_commerce.Service.Category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ================= USER (PUBLIC) =================
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<CategorySummaryDTO> data = categoryService.getAllCategories();
        return ResponseEntity.ok(
                Map.of("success", true, "data", data)
        );
    }

    @GetMapping("/categories/{slug}")
    public ResponseEntity<?> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponseDTO data = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(
                Map.of("success", true, "data", data)
        );
    }

    // ================= ADMIN =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/categories", consumes = "multipart/form-data")
    public ResponseEntity<?> createCategory(
            @ModelAttribute CategoryRequestDTO request
    ) {
        CategoryResponseDTO data = categoryService.createCategory(request);
        return ResponseEntity.ok(
                Map.of("success", true, "data", data)
        );
    }

    // 🔥 CHANGE 4: UPDATE METHOD FIXED (JSON only, no image)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/categories/{id}")  // REMOVED: consumes = "multipart/form-data"
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO request  // CHANGED: @RequestBody not @ModelAttribute
    ) {
        CategoryResponseDTO data = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(
                Map.of("success", true, "data", data)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                Map.of("success", true)
        );
    }
}