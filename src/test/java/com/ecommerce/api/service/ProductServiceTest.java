package com.ecommerce.api.service;

import com.ecommerce.api.dto.request.ProductRequest;
import com.ecommerce.api.dto.response.ProductResponse;
import com.ecommerce.api.entity.Category;
import com.ecommerce.api.entity.Inventory;
import com.ecommerce.api.entity.Product;
import com.ecommerce.api.exception.DuplicateResourceException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.CategoryRepository;
import com.ecommerce.api.repository.InventoryRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category category;
    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics", "Devices");
        product = new Product(1L, "SKU-TEST-1", "Smartphone", "Latest phone", new BigDecimal("799.99"), category, "http://img", true);
        inventory = new Inventory(1L, product, 25, 0, 5);
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals("SKU-TEST-1", response.getSku());
        assertEquals("Smartphone", response.getName());
        assertEquals(25, response.getAvailableStock());
    }

    @Test
    void getProductById_NotFound_ThrowsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void createProduct_Success() {
        ProductRequest request = new ProductRequest(
                "SKU-NEW-1", "Tablet", "New Tablet", new BigDecimal("499.99"), 1L, "http://img", true, 50);

        when(productRepository.existsBySku("SKU-NEW-1")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void createProduct_DuplicateSku_ThrowsException() {
        ProductRequest request = new ProductRequest(
                "SKU-TEST-1", "Smartphone", "Desc", new BigDecimal("799.99"), 1L, "http://img", true, 10);

        when(productRepository.existsBySku("SKU-TEST-1")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> productService.createProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }
}
