package com.ecommerce.inventory.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model representing inventory stock information.
 * Pure domain model without framework dependencies.
 */
public class Inventory {

    private Long id;
    private String productId;
    private String productName;
    private Integer quantity;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private String location;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor for new inventory (without ID)
    public Inventory(String productId, String productName, Integer quantity,
                     Integer minStockLevel, Integer maxStockLevel, 
                     String location, String description) {
        validateProductId(productId);
        validateProductName(productName);
        validateQuantity(quantity);
        
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.minStockLevel = minStockLevel;
        this.maxStockLevel = maxStockLevel;
        this.location = location;
        this.description = description;
    }

    // Constructor for existing inventory (with ID and timestamps)
    public Inventory(Long id, String productId, String productName, Integer quantity,
                     Integer minStockLevel, Integer maxStockLevel, 
                     String location, String description,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.minStockLevel = minStockLevel;
        this.maxStockLevel = maxStockLevel;
        this.location = location;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Business method: update stock quantity
    public void updateStock(Integer quantityChange) {
        if (quantityChange == null) {
            throw new IllegalArgumentException("Quantity change cannot be null");
        }
        this.quantity = this.quantity + quantityChange;
    }

    // Business method: update inventory details
    public void updateDetails(String productId, String productName, Integer quantity,
                             Integer minStockLevel, Integer maxStockLevel,
                             String location, String description) {
        validateProductId(productId);
        validateProductName(productName);
        validateQuantity(quantity);
        
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.minStockLevel = minStockLevel;
        this.maxStockLevel = maxStockLevel;
        this.location = location;
        this.description = description;
    }

    // Domain invariants validation
    private void validateProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
    }

    private void validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public Integer getMaxStockLevel() {
        return maxStockLevel;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
