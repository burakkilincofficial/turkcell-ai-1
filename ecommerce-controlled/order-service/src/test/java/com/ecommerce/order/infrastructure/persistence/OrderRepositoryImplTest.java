package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.Address;
import com.ecommerce.order.domain.model.LineItem;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {

    @Mock
    private JpaOrderRepository jpaOrderRepository;

    private OrderRepositoryImpl orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepositoryImpl(jpaOrderRepository);
    }

    @Test
    void testRepositoryInitialization() {
        assertNotNull(orderRepository);
    }

    @Test
    void testSaveOrder() {
        Order order = createTestOrder();
        OrderEntity entity = createTestEntity();

        when(jpaOrderRepository.save(any(OrderEntity.class))).thenReturn(entity);

        Order result = orderRepository.save(order);

        verify(jpaOrderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        OrderEntity entity = createTestEntity();
        entity.setId(id);

        when(jpaOrderRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Order> result = orderRepository.findById(id);

        assertTrue(result.isPresent());
        verify(jpaOrderRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();

        doNothing().when(jpaOrderRepository).deleteById(id);

        orderRepository.deleteById(id);

        verify(jpaOrderRepository, times(1)).deleteById(id);
    }

    private Order createTestOrder() {
        UUID customerId = UUID.randomUUID();
        Address shippingAddress = new Address("123 Main St", "Springfield", "12345", "US");
        LineItem lineItem = new LineItem("PROD-001", 2, BigDecimal.valueOf(50.00));
        List<LineItem> lineItems = List.of(lineItem);
        BigDecimal totalAmount = BigDecimal.valueOf(100.00);
        
        return Order.create(customerId, shippingAddress, lineItems, totalAmount);
    }

    private OrderEntity createTestEntity() {
        OrderEntity entity = new OrderEntity();
        entity.setId(UUID.randomUUID());
        entity.setOrderNumber("ORD-2026-001");
        entity.setCustomerId(UUID.randomUUID().toString());
        entity.setShippingStreet("123 Main St");
        entity.setShippingCity("Springfield");
        entity.setShippingPostalCode("12345");
        entity.setShippingCountry("US");
        entity.setStatus(OrderStatus.PENDING);
        entity.setTotalAmount(BigDecimal.valueOf(100.00));
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setItems(new ArrayList<>());
        return entity;
    }
}
