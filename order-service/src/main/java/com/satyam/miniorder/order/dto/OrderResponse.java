package com.satyam.miniorder.order.dto;

import com.satyam.miniorder.order.entity.OrderStatus;
import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        String customerName,
        String email,
        String phone,
        String productName,
        Integer quantity,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
