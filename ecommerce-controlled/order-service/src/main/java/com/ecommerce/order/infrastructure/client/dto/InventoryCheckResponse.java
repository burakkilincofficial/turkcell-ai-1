package com.ecommerce.order.infrastructure.client.dto;

/**
 * DTO matching InventoryResponse from inventory-service OpenAPI contract.
 * Used for deserializing responses from inventory-service.
 */
public record InventoryCheckResponse(
    Long id,
    String productId,
    String productName,
    Integer quantity,
    Integer minStockLevel,
    Integer maxStockLevel,
    String location,
    String description
) {
}
