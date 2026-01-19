package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import com.ecommerce.order.domain.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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
    public Page<Order> findByCustomerId(String customerId, Pageable pageable) {
        return jpaOrderRepository.findByCustomerId(customerId, pageable).map(this::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaOrderRepository.deleteById(id);
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setOrderNumber(order.getOrderNumber());
        entity.setCustomerId(order.getCustomerId());
        entity.setCustomerName(order.getCustomerName());
        entity.setCustomerEmail(order.getCustomerEmail());
        entity.setShippingStreet(order.getShippingStreet());
        entity.setShippingCity(order.getShippingCity());
        entity.setShippingPostalCode(order.getShippingPostalCode());
        entity.setShippingCountry(order.getShippingCountry());
        entity.setBillingStreet(order.getBillingStreet());
        entity.setBillingCity(order.getBillingCity());
        entity.setBillingPostalCode(order.getBillingPostalCode());
        entity.setBillingCountry(order.getBillingCountry());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setStatus(order.getStatus());
        entity.setNotes(order.getNotes());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());

        if (order.getItems() != null) {
            entity.setItems(order.getItems().stream()
                    .map(this::toItemEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private Order toDomain(OrderEntity entity) {
        Order order = new Order();
        order.setId(entity.getId());
        order.setOrderNumber(entity.getOrderNumber());
        order.setCustomerId(entity.getCustomerId());
        order.setCustomerName(entity.getCustomerName());
        order.setCustomerEmail(entity.getCustomerEmail());
        order.setShippingStreet(entity.getShippingStreet());
        order.setShippingCity(entity.getShippingCity());
        order.setShippingPostalCode(entity.getShippingPostalCode());
        order.setShippingCountry(entity.getShippingCountry());
        order.setBillingStreet(entity.getBillingStreet());
        order.setBillingCity(entity.getBillingCity());
        order.setBillingPostalCode(entity.getBillingPostalCode());
        order.setBillingCountry(entity.getBillingCountry());
        order.setTotalAmount(entity.getTotalAmount());
        order.setStatus(entity.getStatus());
        order.setNotes(entity.getNotes());
        order.setCreatedAt(entity.getCreatedAt());
        order.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getItems() != null) {
            order.setItems(entity.getItems().stream()
                    .map(this::toItemDomain)
                    .collect(Collectors.toList()));
        }

        return order;
    }

    private OrderItemEntity toItemEntity(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(item.getId());
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setTotalPrice(item.getTotalPrice());
        return entity;
    }

    private OrderItem toItemDomain(OrderItemEntity entity) {
        OrderItem item = new OrderItem();
        item.setId(entity.getId());
        item.setProductId(entity.getProductId());
        item.setProductName(entity.getProductName());
        item.setQuantity(entity.getQuantity());
        item.setUnitPrice(entity.getUnitPrice());
        item.setTotalPrice(entity.getTotalPrice());
        return item;
    }
}
