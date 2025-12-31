package com.example.E_commerce.Service.Product;

import com.example.E_commerce.DTO.Product.ProductCreateRequest;
import com.example.E_commerce.DTO.Product.ProductResponse;

import java.util.List;

public interface ProductService {

    // ADMIN
    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse updateProduct(Long productId, ProductCreateRequest request);
    void deleteProduct(Long productId); // soft delete

    // PUBLIC
    ProductResponse getProductBySlug(String slug);
    List<ProductResponse> getLatestProducts(int limit);
    List<ProductResponse> getSimilarProducts(String slug, int limit);
    List<ProductResponse> getProductsByCategory(String categorySlug);

}
