package com.satyam.miniorder.notification.dto;

import java.time.LocalDateTime;

public record InventoryReservedEvent(
        Long orderId,
        String customerName,
        String email,
        String phone,
        String productName,
        Integer quantity,
        String inventoryStatus,
        String eventType,
        LocalDateTime eventTime
) {
}
