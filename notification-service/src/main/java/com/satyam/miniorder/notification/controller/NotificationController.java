package com.satyam.miniorder.notification.controller;

import com.satyam.miniorder.notification.dto.ApiResponse;
import com.satyam.miniorder.notification.dto.NotificationResponse;
import com.satyam.miniorder.notification.dto.SendNotificationRequest;
import com.satyam.miniorder.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<NotificationResponse>> sendNotificationManually(
            @Valid @RequestBody SendNotificationRequest request) {
        NotificationResponse response = notificationService.sendNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Notification sent manually. Kafka consumer is also enabled.", response));
    }
}
