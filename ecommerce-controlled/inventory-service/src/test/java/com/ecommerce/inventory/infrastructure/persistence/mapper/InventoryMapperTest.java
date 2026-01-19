package com.ecommerce.inventory.infrastructure.persistence.mapper;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.infrastructure.persistence.entity.InventoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InventoryMapper.
 * Tests entity-domain mapping logic.
 */
class InventoryMapperTest {

    private InventoryMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new InventoryMapper();
    }

    @Test
    void shouldMapDomainToEntity() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Inventory domain = new Inventory(
                1L, "PROD-001", "Laptop", 100,
                10, 500, "Warehouse A", "Test description",
                now, now
        );

        // When
        InventoryEntity entity = mapper.toEntity(domain);

        // Then
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("PROD-001", entity.getProductId());
        assertEquals("Laptop", entity.getProductName());
        assertEquals(100, entity.getQuantity());
        assertEquals(10, entity.getMinStockLevel());
        assertEquals(500, entity.getMaxStockLevel());
        assertEquals("Warehouse A", entity.getLocation());
        assertEquals("Test description", entity.getDescription());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void shouldMapEntityToDomain() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        InventoryEntity entity = new InventoryEntity();
        entity.setId(1L);
        entity.setProductId("PROD-001");
        entity.setProductName("Laptop");
        entity.setQuantity(100);
        entity.setMinStockLevel(10);
        entity.setMaxStockLevel(500);
        entity.setLocation("Warehouse A");
        entity.setDescription("Test description");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        // When
        Inventory domain = mapper.toDomain(entity);

        // Then
        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("PROD-001", domain.getProductId());
        assertEquals("Laptop", domain.getProductName());
        assertEquals(100, domain.getQuantity());
        assertEquals(10, domain.getMinStockLevel());
        assertEquals(500, domain.getMaxStockLevel());
        assertEquals("Warehouse A", domain.getLocation());
        assertEquals("Test description", domain.getDescription());
        assertEquals(now, domain.getCreatedAt());
        assertEquals(now, domain.getUpdatedAt());
    }

    @Test
    void shouldReturnNullWhenMappingNullDomainToEntity() {
        // When
        InventoryEntity entity = mapper.toEntity(null);

        // Then
        assertNull(entity);
    }

    @Test
    void shouldReturnNullWhenMappingNullEntityToDomain() {
        // When
        Inventory domain = mapper.toDomain(null);

        // Then
        assertNull(domain);
    }

    @Test
    void shouldHandleNullOptionalFields() {
        // Given
        Inventory domain = new Inventory(
                "PROD-001", "Laptop", 100,
                null, null, null, null
        );

        // When
        InventoryEntity entity = mapper.toEntity(domain);

        // Then
        assertNotNull(entity);
        assertNull(entity.getMinStockLevel());
        assertNull(entity.getMaxStockLevel());
        assertNull(entity.getLocation());
        assertNull(entity.getDescription());
    }
}
