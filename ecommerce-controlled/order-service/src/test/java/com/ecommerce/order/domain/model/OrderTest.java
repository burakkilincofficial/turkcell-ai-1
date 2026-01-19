package com.ecommerce.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderCreation() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setOrderNumber("ORD-2026-001");
        order.setCustomerId("CUST-001");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.valueOf(100.00));
        order.setCreatedAt(Instant.now());
        order.setItems(new ArrayList<>());

        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals("ORD-2026-001", order.getOrderNumber());
        assertEquals("CUST-001", order.getCustomerId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(BigDecimal.valueOf(100.00), order.getTotalAmount());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void testOrderSettersAndGetters() {
        Order order = new Order();
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        order.setId(id);
        order.setOrderNumber("TEST-001");
        order.setCustomerId("CUST-999");
        order.setCustomerName("Test User");
        order.setCustomerEmail("test@example.com");
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(BigDecimal.valueOf(250.50));
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        order.setNotes("Test notes");

        assertEquals(id, order.getId());
        assertEquals("TEST-001", order.getOrderNumber());
        assertEquals("CUST-999", order.getCustomerId());
        assertEquals("Test User", order.getCustomerName());
        assertEquals("test@example.com", order.getCustomerEmail());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(BigDecimal.valueOf(250.50), order.getTotalAmount());
        assertEquals(now, order.getCreatedAt());
        assertEquals(now, order.getUpdatedAt());
        assertEquals("Test notes", order.getNotes());
    }
}
