package com.ecommerce.order.application.usecase;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetOrdersByCustomerIdUseCase {

    private final OrderRepository orderRepository;

    public GetOrdersByCustomerIdUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Order> execute(String customerId, Pageable pageable) {
        // Empty function - no business logic
        return null;
    }
}
