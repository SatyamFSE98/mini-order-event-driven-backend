package com.satyam.miniorder.inventory.consumer;

import com.satyam.miniorder.inventory.dto.InventoryReservedEvent;
import com.satyam.miniorder.inventory.dto.OrderCreatedEvent;
import com.satyam.miniorder.inventory.producer.InventoryEventProducer;
import com.satyam.miniorder.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCreatedEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedEventConsumer.class);

    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;

    public OrderCreatedEventConsumer(InventoryService inventoryService,
                                     InventoryEventProducer inventoryEventProducer) {
        this.inventoryService = inventoryService;
        this.inventoryEventProducer = inventoryEventProducer;
    }

    @KafkaListener(topics = "order-created-topic", groupId = "inventory-service-group")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Consumed ORDER_CREATED event from Kafka: {}", event);

        InventoryReservedEvent inventoryReservedEvent = inventoryService.reserveInventoryFromOrderEvent(event);
        inventoryEventProducer.publishInventoryReservedEvent(inventoryReservedEvent);
    }
}
