package com.ecommerce.inventory.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Inventory domain model.
 * Tests business invariants and domain logic.
 */
class InventoryTest {

    @Test
    void shouldCreateInventoryWithValidData() {
        // Given
        String productId = "PROD-001";
        String productName = "Laptop";
        Integer quantity = 100;
        Integer minStockLevel = 10;
        Integer maxStockLevel = 500;
        String location = "Warehouse A";
        String description = "High performance laptop";

        // When
        Inventory inventory = new Inventory(
                productId, productName, quantity, 
                minStockLevel, maxStockLevel, location, description
        );

        // Then
        assertEquals(productId, inventory.getProductId());
        assertEquals(productName, inventory.getProductName());
        assertEquals(quantity, inventory.getQuantity());
        assertEquals(minStockLevel, inventory.getMinStockLevel());
        assertEquals(maxStockLevel, inventory.getMaxStockLevel());
        assertEquals(location, inventory.getLocation());
        assertEquals(description, inventory.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        // Given
        String productId = null;
        String productName = "Laptop";
        Integer quantity = 100;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(productId, productName, quantity, null, null, null, null);
        });
        
        assertEquals("Product ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsEmpty() {
        // Given
        String productId = "   ";
        String productName = "Laptop";
        Integer quantity = 100;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(productId, productName, quantity, null, null, null, null);
        });
        
        assertEquals("Product ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductNameIsNull() {
        // Given
        String productId = "PROD-001";
        String productName = null;
        Integer quantity = 100;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(productId, productName, quantity, null, null, null, null);
        });
        
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNull() {
        // Given
        String productId = "PROD-001";
        String productName = "Laptop";
        Integer quantity = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(productId, productName, quantity, null, null, null, null);
        });
        
        assertEquals("Quantity cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        // Given
        String productId = "PROD-001";
        String productName = "Laptop";
        Integer quantity = -10;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(productId, productName, quantity, null, null, null, null);
        });
        
        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        // Given
        Inventory inventory = new Inventory(
                "PROD-001", "Laptop", 100, 
                10, 500, "Warehouse A", "Test"
        );

        // When
        inventory.updateStock(50);

        // Then
        assertEquals(150, inventory.getQuantity());
    }

    @Test
    void shouldAllowNegativeStockChange() {
        // Given
        Inventory inventory = new Inventory(
                "PROD-001", "Laptop", 100, 
                10, 500, "Warehouse A", "Test"
        );

        // When
        inventory.updateStock(-30);

        // Then
        assertEquals(70, inventory.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenStockChangeIsNull() {
        // Given
        Inventory inventory = new Inventory(
                "PROD-001", "Laptop", 100, 
                10, 500, "Warehouse A", "Test"
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventory.updateStock(null);
        });
        
        assertEquals("Quantity change cannot be null", exception.getMessage());
    }

    @Test
    void shouldUpdateDetailsSuccessfully() {
        // Given
        Inventory inventory = new Inventory(
                1L, "PROD-001", "Laptop", 100, 
                10, 500, "Warehouse A", "Test",
                LocalDateTime.now(), LocalDateTime.now()
        );

        // When
        inventory.updateDetails(
                "PROD-002", "Desktop", 50,
                5, 300, "Warehouse B", "Updated"
        );

        // Then
        assertEquals("PROD-002", inventory.getProductId());
        assertEquals("Desktop", inventory.getProductName());
        assertEquals(50, inventory.getQuantity());
        assertEquals(5, inventory.getMinStockLevel());
        assertEquals(300, inventory.getMaxStockLevel());
        assertEquals("Warehouse B", inventory.getLocation());
        assertEquals("Updated", inventory.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenUpdateDetailsWithInvalidData() {
        // Given
        Inventory inventory = new Inventory(
                1L, "PROD-001", "Laptop", 100, 
                10, 500, "Warehouse A", "Test",
                LocalDateTime.now(), LocalDateTime.now()
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventory.updateDetails(null, "Desktop", 50, 5, 300, "Warehouse B", "Updated");
        });
        
        assertEquals("Product ID cannot be null or empty", exception.getMessage());
    }
}
