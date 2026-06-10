# Mini Order Event-Driven Backend

A mini event-driven microservices backend project built using **Java 17**, **Spring Boot**, **Apache Kafka**, **MySQL**, **Docker**, and **GitHub Actions CI**.

This project demonstrates how multiple backend services communicate asynchronously using Kafka events.

---

## Project Overview

The project contains three Spring Boot microservices:

| Service              | Port | Responsibility                                        |
| -------------------- | ---: | ----------------------------------------------------- |
| order-service        | 8081 | Creates customer orders and publishes order events    |
| inventory-service    | 8082 | Consumes order events and reserves inventory          |
| notification-service | 8083 | Consumes inventory events and sends notification logs |

The complete system runs using Docker Compose.

---

## Architecture Flow

```text
Postman / Client
      |
      v
order-service
      |
      | publishes ORDER_CREATED event
      v
Kafka Topic: order-created-topic
      |
      v
inventory-service
      |
      | publishes INVENTORY_RESERVED event
      v
Kafka Topic: inventory-reserved-topic
      |
      v
notification-service
```

---

## Technologies Used

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Apache Kafka
* MySQL
* Docker
* Docker Compose
* Maven
* GitHub Actions CI
* Kafka UI
* Lombok
* Bean Validation

---

## Microservices Details

### 1. order-service

The `order-service` exposes REST APIs to create orders.

When an order is created:

1. Order details are saved.
2. Order status is marked as `CREATED`.
3. An `ORDER_CREATED` event is published to Kafka.

Main API:

```http
POST /api/orders
```

Example request:

```json
{
  "customerName": "Satyam",
  "email": "satyam@gmail.com",
  "phone": "9876543210",
  "productName": "Laptop",
  "quantity": 1
}
```

---

### 2. inventory-service

The `inventory-service` listens to:

```text
order-created-topic
```

When it receives an `ORDER_CREATED` event:

1. It processes the order event.
2. It reserves inventory.
3. It publishes an `INVENTORY_RESERVED` event to Kafka.

Manual inventory reserve API is also available for testing:

```http
POST /api/inventory/reserve
```

Example request:

```json
{
  "orderId": 1,
  "productName": "Laptop",
  "quantity": 1
}
```

---

### 3. notification-service

The `notification-service` listens to:

```text
inventory-reserved-topic
```

When it receives an `INVENTORY_RESERVED` event, it prints a notification message in the console.

This service demonstrates asynchronous event consumption using Kafka.

---

## Kafka Topics

| Topic Name               | Producer          | Consumer             |
| ------------------------ | ----------------- | -------------------- |
| order-created-topic      | order-service     | inventory-service    |
| inventory-reserved-topic | inventory-service | notification-service |

---

## Run Project Using Docker Compose

From the project root folder, run:

```bash
docker compose -f docker-compose.full.yml up -d --build
```

This starts:

```text
MySQL
Zookeeper
Kafka
Kafka UI
order-service
inventory-service
notification-service
```

Check running containers:

```bash
docker ps
```

Stop all containers:

```bash
docker compose -f docker-compose.full.yml down
```

---

## Kafka UI

Kafka UI is available at:

```text
http://localhost:8090
```

Use Kafka UI to check:

* Kafka brokers
* Topics
* Messages
* Consumer groups

Important topics:

```text
order-created-topic
inventory-reserved-topic
```

---

## API Testing

### Create Order

```http
POST http://localhost:8081/api/orders
```

Request body:

```json
{
  "customerName": "Satyam",
  "email": "satyam@gmail.com",
  "phone": "9876543210",
  "productName": "Laptop",
  "quantity": 1
}
```

Expected response:

```json
{
  "success": true,
  "message": "Order created successfully and ORDER_CREATED event published to Kafka."
}
```

After this request:

1. `order-service` publishes an `ORDER_CREATED` event.
2. `inventory-service` consumes the event.
3. `inventory-service` publishes an `INVENTORY_RESERVED` event.
4. `notification-service` consumes the event and logs notification.

---

## Run Kafka Only

If you want to run only Kafka and Kafka UI:

```bash
docker compose -f docker-compose.kafka-only.yml up -d --build
```

Then run services manually using Maven:

```bash
cd order-service
mvn spring-boot:run
```

```bash
cd inventory-service
mvn spring-boot:run
```

```bash
cd notification-service
mvn spring-boot:run
```

---

## CI/CD Pipeline

This project uses GitHub Actions for CI.

Workflow file:

```text
.github/workflows/ci.yml
```

The CI pipeline runs on every push or pull request to:

```text
main
master
develop
```

Pipeline steps:

1. Checkout source code.
2. Set up Java 17.
3. Build `order-service`.
4. Build `inventory-service`.
5. Build `notification-service`.
6. Build Docker images for all services.

Current CI status:

```text
CI - Build and Test Backend Services
```

---

## Useful Docker Commands

Build and start full project:

```bash
docker compose -f docker-compose.full.yml up -d --build
```

View logs:

```bash
docker logs order-service
docker logs inventory-service
docker logs notification-service
```

Follow logs:

```bash
docker logs -f order-service
docker logs -f inventory-service
docker logs -f notification-service
```

Stop project:

```bash
docker compose -f docker-compose.full.yml down
```

Remove stopped containers:

```bash
docker container prune
```

---

## Interview Explanation

This project demonstrates an event-driven microservices architecture.

When an order is created, `order-service` saves the order and publishes an `ORDER_CREATED` event to Kafka. The `inventory-service` listens to this topic and reserves inventory asynchronously. After that, it publishes an `INVENTORY_RESERVED` event, which is consumed by `notification-service` to send or log a notification.

The services do not call each other directly, which makes the system loosely coupled, scalable, and fault tolerant. Docker Compose is used to run the full environment locally, including Kafka, MySQL, Kafka UI, and all Spring Boot services. GitHub Actions is used to validate the build and Docker images on every code push.

---

## Project Status

Completed:

* Event-driven Kafka communication
* Order service
* Inventory service
* Notification service
* Kafka producer and consumer flow
* Docker Compose setup
* Kafka UI setup
* GitHub repository setup
* GitHub Actions CI pipeline

Future improvements:

* Add unit and integration tests
* Add centralized logging
* Add API documentation using Swagger/OpenAPI
* Add retry and dead-letter topic handling
* Add AWS deployment using ECR and ECS
