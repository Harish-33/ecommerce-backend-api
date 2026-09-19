package com.ecommerce.api.service.impl;

import com.ecommerce.api.dto.request.InventoryUpdateRequest;
import com.ecommerce.api.dto.response.InventoryResponse;
import com.ecommerce.api.entity.Inventory;
import com.ecommerce.api.entity.Product;
import com.ecommerce.api.exception.BadRequestException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.InventoryRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", productId));
        return InventoryResponse.fromEntity(inventory);
    }

    @Override
    public InventoryResponse updateInventory(Long productId, InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
                    return new Inventory(null, product, 0, 0, 10);
                });

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        if (request.getReorderThreshold() != null) {
            inventory.setReorderThreshold(request.getReorderThreshold());
        }
        inventory.setLastRestockedAt(LocalDateTime.now());

        Inventory saved = inventoryRepository.save(inventory);
        return InventoryResponse.fromEntity(saved);
    }

    @Override
    public InventoryResponse replenishStock(Long productId, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new BadRequestException("Replenishment quantity must be greater than zero");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
                    return new Inventory(null, product, 0, 0, 10);
                });

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantityToAdd);
        inventory.setLastRestockedAt(LocalDateTime.now());

        Inventory saved = inventoryRepository.save(inventory);
        return InventoryResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getLowStockAlerts() {
        return inventoryRepository.findLowStockInventories().stream()
                .map(InventoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
