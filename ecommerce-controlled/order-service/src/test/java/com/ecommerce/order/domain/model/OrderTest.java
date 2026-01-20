package com.ecommerce.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderCreation() {
        UUID customerId = UUID.randomUUID();
        Address shippingAddress = new Address("123 Main St", "Springfield", "12345", "US");
        LineItem lineItem = new LineItem(UUID.randomUUID(), 2, BigDecimal.valueOf(50.00));
        List<LineItem> lineItems = List.of(lineItem);
        BigDecimal totalAmount = BigDecimal.valueOf(100.00);

        Order order = Order.create(customerId, shippingAddress, lineItems, totalAmount);

        assertNotNull(order);
        assertNotNull(order.getId());
        assertNotNull(order.getOrderNumber());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(totalAmount, order.getTotalAmount());
        assertNotNull(order.getCreatedAt());
        assertEquals(shippingAddress, order.getShippingAddress());
        assertEquals(1, order.getLineItems().size());
    }

    @Test
    void testOrderStatusTransition() {
        UUID customerId = UUID.randomUUID();
        Address shippingAddress = new Address("123 Main St", "Springfield", "12345", "US");
        LineItem lineItem = new LineItem(UUID.randomUUID(), 2, BigDecimal.valueOf(50.00));
        List<LineItem> lineItems = List.of(lineItem);
        BigDecimal totalAmount = BigDecimal.valueOf(100.00);

        Order order = Order.create(customerId, shippingAddress, lineItems, totalAmount);
        
        assertEquals(OrderStatus.PENDING, order.getStatus());
        
        order.updateStatus(OrderStatus.CONFIRMED);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertNotNull(order.getConfirmedAt());
    }
}
