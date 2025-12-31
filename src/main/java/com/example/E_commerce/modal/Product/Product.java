package com.example.E_commerce.modal.Product;

import com.example.E_commerce.modal.Category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "slug")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Product name
    @Column(nullable = false)
    private String name;

    // Used in URL: /product/{slug}
    @Column(nullable = false, unique = true)
    private String slug;

    // Original price
    @Column(nullable = false)
    private BigDecimal price;

    // Discounted price (nullable)
    private BigDecimal discountPrice;

    // Product description
    @Column(length = 1000)
    private String description;

    // Product status (ACTIVE / INACTIVE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    // MANY products → ONE category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // ONE product → MANY images
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductImage> images;

    // ONE product → MANY sizes with quantity
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductSize> sizes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.status = ProductStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
