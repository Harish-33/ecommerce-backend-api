package com.ecommerce.api.service;

import com.ecommerce.api.dto.request.ProductRequest;
import com.ecommerce.api.dto.response.PagedResponse;
import com.ecommerce.api.dto.response.ProductResponse;

import java.math.BigDecimal;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    ProductResponse getProductBySku(String sku);
    PagedResponse<ProductResponse> searchProducts(
            String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice,
            Boolean activeOnly, int page, int size, String sortBy, String sortDir);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
