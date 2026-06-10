package com.satyam.miniorder.notification.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long orderId,
        String emailStatus,
        String smsStatus,
        LocalDateTime sentAt
) {
}
