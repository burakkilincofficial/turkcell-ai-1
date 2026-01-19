package com.ecommerce.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testOrderItemCreation() {
        OrderItem item = new OrderItem();
        item.setId(UUID.randomUUID());
        item.setProductId("PROD-001");
        item.setProductName("Test Product");
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(50.00));
        item.setTotalPrice(BigDecimal.valueOf(100.00));

        assertNotNull(item);
        assertNotNull(item.getId());
        assertEquals("PROD-001", item.getProductId());
        assertEquals("Test Product", item.getProductName());
        assertEquals(2, item.getQuantity());
        assertEquals(BigDecimal.valueOf(50.00), item.getUnitPrice());
        assertEquals(BigDecimal.valueOf(100.00), item.getTotalPrice());
    }

    @Test
    void testOrderItemSettersAndGetters() {
        OrderItem item = new OrderItem();
        UUID id = UUID.randomUUID();

        item.setId(id);
        item.setProductId("PROD-999");
        item.setProductName("Another Product");
        item.setQuantity(5);
        item.setUnitPrice(BigDecimal.valueOf(25.00));
        item.setTotalPrice(BigDecimal.valueOf(125.00));

        assertEquals(id, item.getId());
        assertEquals("PROD-999", item.getProductId());
        assertEquals("Another Product", item.getProductName());
        assertEquals(5, item.getQuantity());
        assertEquals(BigDecimal.valueOf(25.00), item.getUnitPrice());
        assertEquals(BigDecimal.valueOf(125.00), item.getTotalPrice());
    }
}
