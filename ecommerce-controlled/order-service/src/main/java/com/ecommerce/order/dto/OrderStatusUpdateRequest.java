package com.ecommerce.order.dto;

public record OrderStatusUpdateRequest(
    String status,
    String notes
) {
}
