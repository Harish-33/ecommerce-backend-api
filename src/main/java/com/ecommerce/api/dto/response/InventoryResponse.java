package com.ecommerce.api.dto.response;

import com.ecommerce.api.entity.Inventory;
import java.time.LocalDateTime;

public class InventoryResponse {

    private Long id;
    private Long productId;
    private String productSku;
    private String productName;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer reorderThreshold;
    private boolean lowStock;
    private LocalDateTime lastRestockedAt;
    private LocalDateTime updatedAt;

    public InventoryResponse() {
    }

    public static InventoryResponse fromEntity(Inventory inventory) {
        if (inventory == null) return null;
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        if (inventory.getProduct() != null) {
            response.setProductId(inventory.getProduct().getId());
            response.setProductSku(inventory.getProduct().getSku());
            response.setProductName(inventory.getProduct().getName());
        }
        response.setAvailableQuantity(inventory.getAvailableQuantity());
        response.setReservedQuantity(inventory.getReservedQuantity());
        response.setReorderThreshold(inventory.getReorderThreshold());
        response.setLowStock(inventory.getAvailableQuantity() <= inventory.getReorderThreshold());
        response.setLastRestockedAt(inventory.getLastRestockedAt());
        response.setUpdatedAt(inventory.getUpdatedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(Integer reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    public boolean isLowStock() {
        return lowStock;
    }

    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }

    public LocalDateTime getLastRestockedAt() {
        return lastRestockedAt;
    }

    public void setLastRestockedAt(LocalDateTime lastRestockedAt) {
        this.lastRestockedAt = lastRestockedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
