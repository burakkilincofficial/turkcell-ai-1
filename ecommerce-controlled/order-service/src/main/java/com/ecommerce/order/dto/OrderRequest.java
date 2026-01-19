package com.ecommerce.order.dto;

import java.util.List;

public record OrderRequest(
    String customerId,
    String customerName,
    String customerEmail,
    Address shippingAddress,
    Address billingAddress,
    List<OrderItemRequest> items,
    String notes
) {
}
