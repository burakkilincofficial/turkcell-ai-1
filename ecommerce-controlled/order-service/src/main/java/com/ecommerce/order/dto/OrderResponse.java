package com.ecommerce.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    String orderNumber,
    String customerId,
    String customerName,
    String customerEmail,
    Address shippingAddress,
    Address billingAddress,
    List<OrderItemResponse> items,
    BigDecimal totalAmount,
    String status,
    String notes,
    Instant createdAt,
    Instant updatedAt
) {
}
