package com.ecommerce.order.infrastructure.client;

import com.ecommerce.order.infrastructure.client.dto.InventoryCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for inventory-service.
 * Maps to inventory-service OpenAPI spec: GET /api/v1/inventory/product/{productId}
 * 
 * Configuration:
 * - Base URL: ${inventory-service.url}
 * - Connect timeout: 5s
 * - Read timeout: 10s
 * - Retries: Configured via Retryer bean
 */
@FeignClient(
    name = "inventory-service",
    url = "${inventory-service.url}",
    configuration = InventoryServiceClientConfig.class
)
public interface InventoryServiceClient {

    /**
     * Get inventory information by product ID.
     * 
     * @param productId product identifier
     * @return inventory information including available quantity
     * @throws feign.FeignException.NotFound if product not found (404)
     * @throws feign.FeignException if service error (5xx) or timeout
     */
    @GetMapping("/api/v1/inventory/product/{productId}")
    InventoryCheckResponse getInventoryByProductId(@PathVariable("productId") String productId);
}
