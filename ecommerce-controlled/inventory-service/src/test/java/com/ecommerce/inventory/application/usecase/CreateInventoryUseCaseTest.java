package com.ecommerce.inventory.application.usecase;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CreateInventoryUseCase.
 * Tests the application layer orchestration.
 */
@ExtendWith(MockitoExtension.class)
class CreateInventoryUseCaseTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private CreateInventoryUseCase createInventoryUseCase;

    @Test
    void shouldCreateInventorySuccessfully() {
        // Given
        String productId = "PROD-001";
        String productName = "Laptop";
        Integer quantity = 100;
        Integer minStockLevel = 10;
        Integer maxStockLevel = 500;
        String location = "Warehouse A";
        String description = "Test description";

        Inventory savedInventory = new Inventory(
                1L, productId, productName, quantity,
                minStockLevel, maxStockLevel, location, description,
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now()
        );

        when(inventoryRepository.save(any(Inventory.class))).thenReturn(savedInventory);

        // When
        Inventory result = createInventoryUseCase.execute(
                productId, productName, quantity,
                minStockLevel, maxStockLevel, location, description
        );

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(productId, result.getProductId());
        assertEquals(productName, result.getProductName());
        assertEquals(quantity, result.getQuantity());
        
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        // Given
        String productId = null;
        String productName = "Laptop";
        Integer quantity = 100;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createInventoryUseCase.execute(productId, productName, quantity, null, null, null, null);
        });

        assertEquals("Product ID cannot be null or empty", exception.getMessage());
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        // Given
        String productId = "PROD-001";
        String productName = "Laptop";
        Integer quantity = -10;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createInventoryUseCase.execute(productId, productName, quantity, null, null, null, null);
        });

        assertEquals("Quantity cannot be negative", exception.getMessage());
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }
}
