package com.ecommerce.order.infrastructure.adapter;

import com.ecommerce.order.application.port.StockCheckPort;
import com.ecommerce.order.exception.InsufficientStockException;
import com.ecommerce.order.exception.InventoryServiceUnavailableException;
import com.ecommerce.order.exception.ProductNotFoundException;
import com.ecommerce.order.infrastructure.client.InventoryServiceClient;
import com.ecommerce.order.infrastructure.client.dto.InventoryCheckResponse;
import feign.FeignException;
import org.springframework.stereotype.Component;

/**
 * Adapter implementing StockCheckPort using InventoryServiceClient.
 * Infrastructure layer - handles HTTP communication with inventory-service.
 * 
 * Error Mapping (per Q1, Q2):
 * - 404 Not Found → ProductNotFoundException (Q2: treat as error)
 * - 5xx or timeout → InventoryServiceUnavailableException (Q1: after 3 retries)
 * - 200 OK with insufficient quantity → InsufficientStockException (Q4)
 */
@Component
public class StockCheckAdapter implements StockCheckPort {

    private final InventoryServiceClient inventoryServiceClient;

    public StockCheckAdapter(InventoryServiceClient inventoryServiceClient) {
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @Override
    public void validateStockAvailability(String productId, int requestedQuantity) {
        try {
            InventoryCheckResponse inventory = inventoryServiceClient
                .getInventoryByProductId(productId);
            
            // Q4: Check if inventory.quantity >= lineItem.quantity
            if (inventory.quantity() < requestedQuantity) {
                throw new InsufficientStockException(
                    productId,
                    requestedQuantity,
                    inventory.quantity()
                );
            }
            
        } catch (FeignException.NotFound e) {
            // Q2: 404 = treat as error (product misconfigured)
            throw new ProductNotFoundException(productId);
            
        } catch (FeignException.ServiceUnavailable | FeignException.InternalServerError e) {
            // Q1: 5xx errors after retries → 503
            throw new InventoryServiceUnavailableException(
                "Inventory service unavailable after retries",
                e
            );
            
        } catch (FeignException e) {
            // Other Feign errors (timeout, connection refused, etc.)
            throw new InventoryServiceUnavailableException(
                "Failed to check inventory: " + e.getMessage(),
                e
            );
        }
    }
}
