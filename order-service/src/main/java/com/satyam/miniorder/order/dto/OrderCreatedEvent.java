package com.satyam.miniorder.order.dto;

import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        String customerName,
        String email,
        String phone,
        String productName,
        Integer quantity,
        String eventType,
        LocalDateTime eventTime
) {
}
