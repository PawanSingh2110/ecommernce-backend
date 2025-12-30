package com.example.E_commerce.Service.Category;

import com.example.E_commerce.DTO.Category.CategoryRequestDTO;
import com.example.E_commerce.DTO.Category.CategoryResponseDTO;
import com.example.E_commerce.DTO.Category.CategorySummaryDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO request);

    List<CategorySummaryDTO> getAllCategories();

    CategoryResponseDTO getCategoryBySlug(String slug);

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request);

    void deleteCategory(Long id);
}
