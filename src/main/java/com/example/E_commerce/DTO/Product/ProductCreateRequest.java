package com.example.E_commerce.DTO.Product;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProductCreateRequest {

    private String name;
    private String slug;
    private String description;

    private BigDecimal price;
    private BigDecimal discountPrice;

    // Category slug sent from frontend
    private String categorySlug;

    // Images uploaded from device
    private List<MultipartFile> images;

    // Size → Quantity (S=10, M=20 ...)
    private Map<String, Integer> sizes;

    // ===== NO-ARGS CONSTRUCTOR =====
    public ProductCreateRequest() {
    }

    // ===== ALL-ARGS CONSTRUCTOR =====
    public ProductCreateRequest(
            String name,
            String slug,
            String description,
            BigDecimal price,
            BigDecimal discountPrice,
            String categorySlug,
            List<MultipartFile> images,
            Map<String, Integer> sizes
    ) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.categorySlug = categorySlug;
        this.images = images;
        this.sizes = sizes;
    }

    // ===== GETTERS & SETTERS =====

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

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public Map<String, Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, Integer> sizes) {
        this.sizes = sizes;
    }
}
