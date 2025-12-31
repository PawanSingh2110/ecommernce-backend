package com.example.E_commerce.DTO.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProductResponse {

    private Long id;

    private String name;
    private String slug;
    private String description;

    private BigDecimal price;
    private BigDecimal discountPrice;

    private List<String> images;

    // Size → Quantity
    private Map<String, Integer> sizes;

    private String categoryName;
    private String categorySlug;

    // ===== NO-ARGS CONSTRUCTOR =====
    public ProductResponse() {
    }

    // ===== ALL-ARGS CONSTRUCTOR =====
    public ProductResponse(
            Long id,
            String name,
            String slug,
            String description,
            BigDecimal price,
            BigDecimal discountPrice,
            List<String> images,
            Map<String, Integer> sizes,
            String categoryName,
            String categorySlug
    ) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.images = images;
        this.sizes = sizes;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Map<String, Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, Integer> sizes) {
        this.sizes = sizes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }
}
