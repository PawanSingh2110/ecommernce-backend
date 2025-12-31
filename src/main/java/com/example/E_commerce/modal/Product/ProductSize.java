package com.example.E_commerce.modal.Product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "product_sizes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "size"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // S, M, L, XL, XXL
    @Column(nullable = false)
    private String size;

    // Quantity for this size
    @Column(nullable = false)
    private int quantity;

    // MANY sizes → ONE product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
