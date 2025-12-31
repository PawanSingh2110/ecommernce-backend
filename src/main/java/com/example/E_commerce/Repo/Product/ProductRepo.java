package com.example.E_commerce.Repo.Product;

import com.example.E_commerce.modal.Product.Product;
import com.example.E_commerce.modal.Product.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {

    Optional<Product> findBySlugAndStatus(String slug, ProductStatus status);

    // ✅ FIX: dynamic limit using native query
    @Query(
            value = """
            SELECT * FROM products
            WHERE status = :status
            ORDER BY created_at DESC
            LIMIT :limit
        """,
            nativeQuery = true
    )
    List<Product> findLatestProducts(
            @Param("status") String status,
            @Param("limit") int limit
    );

    List<Product> findTop4ByCategory_SlugAndStatusAndSlugNot(
            String categorySlug,
            ProductStatus status,
            String slug
    );

    List<Product> findByCategory_SlugAndStatus(
            String slug,
            ProductStatus status
    );

}
