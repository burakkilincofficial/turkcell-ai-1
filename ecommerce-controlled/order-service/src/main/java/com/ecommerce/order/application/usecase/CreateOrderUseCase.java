package com.ecommerce.order.application.usecase;

import com.ecommerce.order.domain.model.Address;
import com.ecommerce.order.domain.model.LineItem;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Use case: Create Order
 * Business Rules: docs/rules/order-service-rules.md#3.1
 * 
 * Responsibilities:
 * - Validate stock availability (SYNC call to inventory-service)
 * - Validate request data (delegated to domain model)
 * - Create order with PENDING status
 * - Persist order
 * - Return created order
 */
@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final com.ecommerce.order.application.port.StockCheckPort stockCheckPort;

    public CreateOrderUseCase(
            OrderRepository orderRepository,
            com.ecommerce.order.application.port.StockCheckPort stockCheckPort
    ) {
        this.orderRepository = orderRepository;
        this.stockCheckPort = stockCheckPort;
    }

    /**
     * Creates a new order.
     * 
     * @param customerId customer identifier
     * @param shippingAddress delivery address
     * @param lineItems order line items (min 1, max 50)
     * @param totalAmount total order amount
     * @return created order with PENDING status
     * @throws IllegalArgumentException if validation fails
     */
    @Transactional
    public Order execute(
        UUID customerId,
        Address shippingAddress,
        List<LineItem> lineItems,
        BigDecimal totalAmount
    ) {
        // SYNC stock validation (blocking call to inventory-service)
        // Per AGENTS.md §7.1: Blocking calls MUST be SYNC (Feign)
        for (LineItem lineItem : lineItems) {
            stockCheckPort.validateStockAvailability(
                lineItem.productId(),
                lineItem.quantity()
            );
        }
        
        // Domain model enforces all business rules
        Order order = Order.create(
            customerId,
            shippingAddress,
            lineItems,
            totalAmount
        );
        
        return orderRepository.save(order);
    }
}
