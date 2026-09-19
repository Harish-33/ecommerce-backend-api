package com.ecommerce.api.controller;

import com.ecommerce.api.dto.request.InventoryUpdateRequest;
import com.ecommerce.api.dto.response.ApiResponse;
import com.ecommerce.api.dto.response.InventoryResponse;
import com.ecommerce.api.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory Management", description = "Endpoints for inventory tracking, restocking, and low stock alerts")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventory details for a product")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryByProductId(@PathVariable Long productId) {
        InventoryResponse inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success(inventory));
    }

    @PutMapping("/product/{productId}")
    @Operation(summary = "Set inventory stock level and threshold")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryUpdateRequest request) {
        InventoryResponse updated = inventoryService.updateInventory(productId, request);
        return ResponseEntity.ok(ApiResponse.success("Inventory updated successfully", updated));
    }

    @PostMapping("/product/{productId}/replenish")
    @Operation(summary = "Replenish stock for a product")
    public ResponseEntity<ApiResponse<InventoryResponse>> replenishStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {
        InventoryResponse updated = inventoryService.replenishStock(productId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock replenished successfully", updated));
    }

    @GetMapping("/alerts/low-stock")
    @Operation(summary = "List all products with stock below their reorder threshold")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getLowStockAlerts() {
        List<InventoryResponse> alerts = inventoryService.getLowStockAlerts();
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }
}
