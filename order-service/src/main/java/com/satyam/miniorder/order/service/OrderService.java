package com.satyam.miniorder.order.service;

import com.satyam.miniorder.order.dto.CreateOrderRequest;
import com.satyam.miniorder.order.dto.OrderCreatedEvent;
import com.satyam.miniorder.order.dto.OrderResponse;
import com.satyam.miniorder.order.entity.Order;
import com.satyam.miniorder.order.entity.OrderStatus;
import com.satyam.miniorder.order.exception.ResourceNotFoundException;
import com.satyam.miniorder.order.repository.OrderRepository;
import com.satyam.miniorder.order.producer.OrderEventProducer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setPhone(request.phone());
        order.setProductName(request.productName());
        order.setQuantity(request.quantity());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getPhone(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                "ORDER_CREATED",
                LocalDateTime.now()
        );

        orderEventProducer.publishOrderCreatedEvent(event);

        return toResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerName(),
                order.getEmail(),
                order.getPhone(),
                order.getProductName(),
                order.getQuantity(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
