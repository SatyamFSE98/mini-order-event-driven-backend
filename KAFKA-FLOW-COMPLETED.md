# Kafka Flow Completed

Kafka communication is already implemented in this final ZIP.

## Implemented classes

### order-service

```text
producer/OrderEventProducer.java
```

Publishes to:

```text
order-created-topic
```

### inventory-service

```text
consumer/OrderCreatedEventConsumer.java
producer/InventoryEventProducer.java
```

Consumes from:

```text
order-created-topic
```

Publishes to:

```text
inventory-reserved-topic
```

### notification-service

```text
consumer/InventoryReservedEventConsumer.java
```

Consumes from:

```text
inventory-reserved-topic
```

## Kafka topics

```text
order-created-topic
inventory-reserved-topic
```

Kafka auto-topic creation is enabled in Docker Compose.
