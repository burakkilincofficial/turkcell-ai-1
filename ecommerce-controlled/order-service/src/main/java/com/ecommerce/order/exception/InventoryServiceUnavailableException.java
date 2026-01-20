package com.ecommerce.order.exception;

/**
 * Exception thrown when inventory-service is unavailable.
 * Per Q1: after 3 retries, fail with 503 Service Unavailable.
 */
public class InventoryServiceUnavailableException extends RuntimeException {
    
    public InventoryServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public InventoryServiceUnavailableException(String message) {
        super(message);
    }
}
