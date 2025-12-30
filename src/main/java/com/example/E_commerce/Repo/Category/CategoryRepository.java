package com.example.E_commerce.Repo.Category;

import com.example.E_commerce.modal.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // For /categories/{slug}
    Optional<Category> findBySlug(String slug);

    // For slug uniqueness check during create
    boolean existsBySlug(String slug);
}
