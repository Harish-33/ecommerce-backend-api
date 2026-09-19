package com.ecommerce.api.exception;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(String message) {
        super(message);
    }

    public InsufficientInventoryException(String productName, int available, int requested) {
        super(String.format("Insufficient inventory for product '%s'. Available: %d, Requested: %d",
                productName, available, requested));
    }
}
