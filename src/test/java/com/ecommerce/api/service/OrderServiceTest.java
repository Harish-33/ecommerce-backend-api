package com.ecommerce.api.service;

import com.ecommerce.api.dto.request.OrderCreateRequest;
import com.ecommerce.api.dto.request.OrderItemRequest;
import com.ecommerce.api.dto.response.OrderResponse;
import com.ecommerce.api.entity.*;
import com.ecommerce.api.exception.InsufficientInventoryException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.InventoryRepository;
import com.ecommerce.api.repository.OrderRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import com.ecommerce.api.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        user = new User(1L, "customer", "cust@example.com", "pass", "Customer One", "123", Role.ROLE_CUSTOMER, "Address");
        Category category = new Category(1L, "Electronics", "Devices");
        product = new Product(1L, "PROD-1", "Laptop", "Laptop desc", new BigDecimal("1000.00"), category, "img", true);
        inventory = new Inventory(1L, product, 10, 0, 2);
    }

    @Test
    void createOrder_Success() {
        OrderCreateRequest request = new OrderCreateRequest(
                1L,
                "123 Street",
                "CREDIT_CARD",
                Collections.singletonList(new OrderItemRequest(1L, 2))
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("2000.00"), response.getTotalAmount());
        assertEquals(8, inventory.getAvailableQuantity()); // 10 - 2 = 8
        verify(inventoryRepository, times(1)).save(inventory);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_InsufficientInventory_ThrowsException() {
        OrderCreateRequest request = new OrderCreateRequest(
                1L,
                "123 Street",
                "CREDIT_CARD",
                Collections.singletonList(new OrderItemRequest(1L, 20)) // 20 requested, only 10 available
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        assertThrows(InsufficientInventoryException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        OrderCreateRequest request = new OrderCreateRequest(
                99L, "123 Street", "CREDIT_CARD", Collections.singletonList(new OrderItemRequest(1L, 1)));

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
    }
}
