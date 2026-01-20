package com.ecommerce.order.exception;

/**
 * Exception thrown when a product is not found in inventory system.
 * Per Q2: treat 404 from inventory-service as error (product misconfigured).
 * Results in 400 Bad Request response to client.
 */
public class ProductNotFoundException extends RuntimeException {
    
    private final String productId;

    public ProductNotFoundException(String productId) {
        super(String.format("Product not found in inventory: %s", productId));
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
