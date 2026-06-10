package com.satyam.miniorder.order.producer;

import com.satyam.miniorder.order.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private static final String ORDER_CREATED_TOPIC = "order-created-topic";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Publishing ORDER_CREATED event to Kafka topic {}: {}", ORDER_CREATED_TOPIC, event);
        kafkaTemplate.send(ORDER_CREATED_TOPIC, String.valueOf(event.orderId()), event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Failed to publish ORDER_CREATED event for orderId={}", event.orderId(), exception);
                    } else {
                        log.info("ORDER_CREATED event published successfully. topic={}, partition={}, offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
