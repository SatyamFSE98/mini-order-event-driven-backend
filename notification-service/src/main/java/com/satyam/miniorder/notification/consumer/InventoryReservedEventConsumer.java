package com.satyam.miniorder.notification.consumer;

import com.satyam.miniorder.notification.dto.InventoryReservedEvent;
import com.satyam.miniorder.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryReservedEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryReservedEventConsumer.class);

    private final NotificationService notificationService;

    public InventoryReservedEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "inventory-reserved-topic", groupId = "notification-service-group")
    public void consumeInventoryReservedEvent(InventoryReservedEvent event) {
        log.info("Consumed INVENTORY_RESERVED event from Kafka: {}", event);
        notificationService.sendNotificationFromInventoryEvent(event);
    }
}
