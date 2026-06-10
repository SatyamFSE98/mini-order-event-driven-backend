package com.satyam.miniorder.notification.service;

import com.satyam.miniorder.notification.dto.InventoryReservedEvent;
import com.satyam.miniorder.notification.dto.NotificationResponse;
import com.satyam.miniorder.notification.dto.SendNotificationRequest;
import com.satyam.miniorder.notification.entity.NotificationLog;
import com.satyam.miniorder.notification.repository.NotificationLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationLogRepository notificationLogRepository;

    public NotificationService(NotificationLogRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    public NotificationResponse sendNotification(SendNotificationRequest request) {
        // For day-1 project, we only log email and SMS.
        // Later you can integrate AWS SES/SNS or Twilio.
        System.out.println("EMAIL SENT to " + request.email() + " | Message: " + request.message());
        System.out.println("SMS SENT to " + request.phone() + " | Message: " + request.message());

        LocalDateTime sentAt = LocalDateTime.now();
        String emailStatus = "EMAIL_LOGGED_SUCCESSFULLY";
        String smsStatus = "SMS_LOGGED_SUCCESSFULLY";

        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setOrderId(request.orderId());
        notificationLog.setEmail(request.email());
        notificationLog.setPhone(request.phone());
        notificationLog.setMessage(request.message());
        notificationLog.setEmailStatus(emailStatus);
        notificationLog.setSmsStatus(smsStatus);
        notificationLog.setSentAt(sentAt);
        notificationLogRepository.save(notificationLog);

        return new NotificationResponse(
                request.orderId(),
                emailStatus,
                smsStatus,
                sentAt
        );
    }

    public NotificationResponse sendNotificationFromInventoryEvent(InventoryReservedEvent event) {
        String email = event.email() != null ? event.email() : "demo-email@example.com";
        String phone = event.phone() != null ? event.phone() : "9999999999";

        SendNotificationRequest request = new SendNotificationRequest(
                event.orderId(),
                email,
                phone,
                "Inventory status for order " + event.orderId() + " is " + event.inventoryStatus()
        );

        return sendNotification(request);
    }
}
