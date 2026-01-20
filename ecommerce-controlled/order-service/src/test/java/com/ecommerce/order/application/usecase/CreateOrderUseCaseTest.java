package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.port.StockCheckPort;
import com.ecommerce.order.application.service.OutboxService;
import com.ecommerce.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockCheckPort stockCheckPort;

    @Mock
    private OutboxService outboxService;

    private CreateOrderUseCase createOrderUseCase;

    @BeforeEach
    void setUp() {
        createOrderUseCase = new CreateOrderUseCase(orderRepository, stockCheckPort, outboxService);
    }

    @Test
    void testUseCaseInitialization() {
        assertNotNull(createOrderUseCase);
    }
}
