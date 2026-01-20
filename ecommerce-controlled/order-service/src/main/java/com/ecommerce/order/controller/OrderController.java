package com.ecommerce.order.controller;

import com.ecommerce.order.application.usecase.*;
import com.ecommerce.order.domain.model.Address;
import com.ecommerce.order.domain.model.LineItem;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final GetOrdersByCustomerIdUseCase getOrdersByCustomerIdUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetAllOrdersUseCase getAllOrdersUseCase,
            GetOrderByIdUseCase getOrderByIdUseCase,
            UpdateOrderUseCase updateOrderUseCase,
            DeleteOrderUseCase deleteOrderUseCase,
            GetOrdersByCustomerIdUseCase getOrdersByCustomerIdUseCase,
            UpdateOrderStatusUseCase updateOrderStatusUseCase,
            CancelOrderUseCase cancelOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getAllOrdersUseCase = getAllOrdersUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.getOrdersByCustomerIdUseCase = getOrdersByCustomerIdUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String status) {
        
        // Create pageable with default sorting by createdAt DESC
        Sort sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sorting);
        
        Page<Order> orderPage;
        
        if (status != null && !status.isBlank()) {
            // Filter by status if provided
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orderPage = getAllOrdersUseCase.executeByStatus(orderStatus, pageable);
        } else {
            // Get all orders
            orderPage = getAllOrdersUseCase.execute(pageable);
        }
        
        // Map to response DTOs
        List<OrderResponse> content = orderPage.getContent().stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
        
        PageResponse<OrderResponse> response = new PageResponse<>(
                content,
                (int) orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.getNumber()
        );
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        // Map DTO to domain objects
        UUID customerId = UUID.fromString(request.customerId());
        Address shippingAddress = toAddress(request.shippingAddress());
        List<LineItem> lineItems = request.items().stream()
                .map(this::toLineItem)
                .collect(Collectors.toList());
        
        // Calculate total amount from items
        BigDecimal totalAmount = lineItems.stream()
                .map(LineItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Execute use case
        Order createdOrder = createOrderUseCase.execute(
                customerId,
                shippingAddress,
                lineItems,
                totalAmount
        );
        
        // Map to response
        OrderResponse response = toOrderResponse(createdOrder);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        Order order = getOrderByIdUseCase.execute(id);
        OrderResponse response = toOrderResponse(order);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable UUID id,
            @RequestBody OrderRequest request) {
        // Note: This endpoint is not in OpenAPI spec
        // Implementing basic version using existing use case
        Order updatedOrder = updateOrderUseCase.execute(id, request);
        
        if (updatedOrder == null) {
            throw new jakarta.persistence.EntityNotFoundException(
                    "Order not found with id: " + id
            );
        }
        
        OrderResponse response = toOrderResponse(updatedOrder);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        // Note: This endpoint is not in OpenAPI spec
        // Implementing basic version using existing use case
        deleteOrderUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<PageResponse<OrderResponse>> getOrdersByCustomerId(
            @PathVariable String customerId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        // Create pageable with default sorting
        Sort sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sorting);
        
        // Execute use case (note: using GetAllOrdersUseCase method)
        UUID customerUuid = UUID.fromString(customerId);
        Page<Order> orderPage = getAllOrdersUseCase.executeByCustomerId(customerUuid, pageable);
        
        // Map to response DTOs
        List<OrderResponse> content = orderPage.getContent().stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
        
        PageResponse<OrderResponse> response = new PageResponse<>(
                content,
                (int) orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.getNumber()
        );
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @RequestBody OrderStatusUpdateRequest request) {
        
        // Parse and validate status
        OrderStatus newStatus = OrderStatus.valueOf(request.status().toUpperCase());
        
        // Execute use case
        Order updatedOrder = updateOrderStatusUseCase.execute(id, newStatus);
        
        // Map to response
        OrderResponse response = toOrderResponse(updatedOrder);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable UUID id,
            @RequestBody(required = false) CancelOrderRequest request) {
        
        // Extract optional reason
        String reason = (request != null) ? request.reason() : null;
        
        // Execute use case
        Order cancelledOrder = cancelOrderUseCase.execute(id, reason);
        
        // Map to response
        OrderResponse response = toOrderResponse(cancelledOrder);
        
        return ResponseEntity.ok(response);
    }
    
    // ========== Mapper Methods ==========
    
    /**
     * Maps domain Order to OrderResponse DTO.
     */
    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getLineItems().stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());
        
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerId().toString(),
                null, // customerName not in domain model
                null, // customerEmail not in domain model
                toAddressDto(order.getShippingAddress()),
                null, // billingAddress not in domain model
                itemResponses,
                order.getTotalAmount(),
                order.getStatus().name(),
                null, // notes not in domain model
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
    
    /**
     * Maps domain LineItem to OrderItemResponse DTO.
     */
    private OrderItemResponse toOrderItemResponse(LineItem lineItem) {
        return new OrderItemResponse(
                null, // LineItem doesn't have ID in domain
                lineItem.productId().toString(),
                null, // productName not in LineItem
                lineItem.quantity(),
                lineItem.unitPrice(),
                lineItem.calculateSubtotal()
        );
    }
    
    /**
     * Maps domain Address to DTO Address.
     */
    private com.ecommerce.order.dto.Address toAddressDto(Address domainAddress) {
        return new com.ecommerce.order.dto.Address(
                domainAddress.street(),
                domainAddress.city(),
                domainAddress.postalCode(),
                domainAddress.country()
        );
    }
    
    /**
     * Maps DTO Address to domain Address.
     */
    private Address toAddress(com.ecommerce.order.dto.Address dtoAddress) {
        return new Address(
                dtoAddress.street(),
                dtoAddress.city(),
                dtoAddress.postalCode(),
                dtoAddress.country()
        );
    }
    
    /**
     * Maps OrderItemRequest DTO to domain LineItem.
     */
    private LineItem toLineItem(OrderItemRequest itemRequest) {
        return new LineItem(
                UUID.fromString(itemRequest.productId()),
                itemRequest.quantity(),
                itemRequest.unitPrice()
        );
    }
}
