package com.satyam.miniorder.inventory.producer;

import com.satyam.miniorder.inventory.dto.InventoryReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventProducer.class);
    private static final String INVENTORY_RESERVED_TOPIC = "inventory-reserved-topic";

    private final KafkaTemplate<String, InventoryReservedEvent> kafkaTemplate;

    public InventoryEventProducer(KafkaTemplate<String, InventoryReservedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishInventoryReservedEvent(InventoryReservedEvent event) {
        log.info("Publishing INVENTORY_RESERVED event to Kafka topic {}: {}", INVENTORY_RESERVED_TOPIC, event);
        kafkaTemplate.send(INVENTORY_RESERVED_TOPIC, String.valueOf(event.orderId()), event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Failed to publish INVENTORY_RESERVED event for orderId={}", event.orderId(), exception);
                    } else {
                        log.info("INVENTORY_RESERVED event published successfully. topic={}, partition={}, offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
