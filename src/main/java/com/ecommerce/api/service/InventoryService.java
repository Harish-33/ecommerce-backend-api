package com.ecommerce.api.service;

import com.ecommerce.api.dto.request.InventoryUpdateRequest;
import com.ecommerce.api.dto.response.InventoryResponse;

import java.util.List;

public interface InventoryService {
    InventoryResponse getInventoryByProductId(Long productId);
    InventoryResponse updateInventory(Long productId, InventoryUpdateRequest request);
    InventoryResponse replenishStock(Long productId, int quantityToAdd);
    List<InventoryResponse> getLowStockAlerts();
}
