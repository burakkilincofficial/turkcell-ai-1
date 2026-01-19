package com.ecommerce.order.application.usecase;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.order.dto.CancelOrderRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CancelOrderUseCase {

    private final OrderRepository orderRepository;

    public CancelOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(UUID id, CancelOrderRequest request) {
        // Empty function - no business logic
        return null;
    }
}
