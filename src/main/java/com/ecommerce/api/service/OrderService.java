package com.ecommerce.api.service;

import com.ecommerce.api.dto.request.OrderCreateRequest;
import com.ecommerce.api.dto.request.OrderStatusUpdateRequest;
import com.ecommerce.api.dto.response.OrderResponse;
import com.ecommerce.api.dto.response.PagedResponse;
import com.ecommerce.api.entity.OrderStatus;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
    OrderResponse getOrderById(Long id);
    OrderResponse getOrderByOrderNumber(String orderNumber);
    PagedResponse<OrderResponse> getOrdersByUser(Long userId, int page, int size);
    PagedResponse<OrderResponse> getAllOrders(OrderStatus status, int page, int size, String sortBy, String sortDir);
    OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request);
    OrderResponse cancelOrder(Long id);
}
