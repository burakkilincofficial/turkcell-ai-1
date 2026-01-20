package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.Address;
import com.ecommerce.order.domain.model.LineItem;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.domain.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity savedEntity = jpaOrderRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaOrderRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return jpaOrderRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Page<Order> findByCustomerId(UUID customerId, Pageable pageable) {
        return jpaOrderRepository.findByCustomerId(customerId.toString(), pageable).map(this::toDomain);
    }

    @Override
    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        // TODO: Add method to JpaOrderRepository when needed
        throw new UnsupportedOperationException("findByStatus not yet implemented");
    }

    @Override
    public Page<Order> findByCreatedAtBetween(Instant startDate, Instant endDate, Pageable pageable) {
        // TODO: Add method to JpaOrderRepository when needed
        throw new UnsupportedOperationException("findByCreatedAtBetween not yet implemented");
    }

    @Override
    public void deleteById(UUID id) {
        jpaOrderRepository.deleteById(id);
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setOrderNumber(order.getOrderNumber());
        entity.setCustomerId(order.getCustomerId().toString());
        
        // Map shippingAddress
        Address shippingAddress = order.getShippingAddress();
        entity.setShippingStreet(shippingAddress.street());
        entity.setShippingCity(shippingAddress.city());
        entity.setShippingPostalCode(shippingAddress.postalCode());
        entity.setShippingCountry(shippingAddress.country());
        
        entity.setTotalAmount(order.getTotalAmount());
        entity.setStatus(order.getStatus());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());

        // Map lineItems
        List<OrderItemEntity> itemEntities = order.getLineItems().stream()
                .map(this::toItemEntity)
                .collect(Collectors.toList());
        entity.setItems(itemEntities);

        return entity;
    }

    private Order toDomain(OrderEntity entity) {
        // Create Address from entity fields
        Address shippingAddress = new Address(
            entity.getShippingStreet(),
            entity.getShippingCity(),
            entity.getShippingPostalCode(),
            entity.getShippingCountry()
        );

        // Create LineItems from entity items
        List<LineItem> lineItems = entity.getItems().stream()
                .map(this::toItemDomain)
                .collect(Collectors.toList());

        // Use factory method to create Order
        Order order = Order.create(
            UUID.fromString(entity.getCustomerId()),
            shippingAddress,
            lineItems,
            entity.getTotalAmount()
        );

        return order;
    }

    private OrderItemEntity toItemEntity(LineItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setProductId(item.productId().toString());
        entity.setQuantity(item.quantity());
        entity.setUnitPrice(item.unitPrice());
        entity.setTotalPrice(item.calculateSubtotal());
        return entity;
    }

    private LineItem toItemDomain(OrderItemEntity entity) {
        return new LineItem(
            UUID.fromString(entity.getProductId()),
            entity.getQuantity(),
            entity.getUnitPrice()
        );
    }
}
