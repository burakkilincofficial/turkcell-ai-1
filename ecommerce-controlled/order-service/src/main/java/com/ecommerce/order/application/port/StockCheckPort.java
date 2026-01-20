package com.ecommerce.order.application.port;

/**
 * Port interface for checking stock availability.
 * Implements dependency inversion principle - application layer defines contract,
 * infrastructure layer provides implementation.
 * 
 * Contract-first design: follows inventory-service OpenAPI spec.
 */
public interface StockCheckPort {

    /**
     * Validates if sufficient stock exists for the requested product quantity.
     * 
     * @param productId product identifier
     * @param requestedQuantity quantity needed for order
     * @throws com.ecommerce.order.exception.InsufficientStockException if stock insufficient
     * @throws com.ecommerce.order.exception.ProductNotFoundException if product not found in inventory
     * @throws com.ecommerce.order.exception.InventoryServiceUnavailableException if service unavailable after retries
     */
    void validateStockAvailability(String productId, int requestedQuantity);
}
