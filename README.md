# Mini Order Event-Driven Microservices

This is a small but complete backend project for learning:

```text
Spring Boot microservices
Spring Data JPA
MySQL
Kafka asynchronous communication
Docker
Docker Compose
GitHub Actions CI/CD
AWS ECR + ECS deployment flow
```

Angular UI is intentionally not included. UI only needs to call `order-service`.

---

## Services

| Service | Port | Purpose |
|---|---:|---|
| order-service | 8081 | Creates order, saves in MySQL, publishes `ORDER_CREATED` event |
| inventory-service | 8082 | Consumes `ORDER_CREATED`, reserves inventory, publishes `INVENTORY_RESERVED` event |
| notification-service | 8083 | Consumes `INVENTORY_RESERVED`, logs email/SMS notification in MySQL |

---

## Final Event Flow

```text
Angular/Postman
   |
   | POST /api/orders
   v
order-service
   |
   | save order in order_db.orders
   | publish ORDER_CREATED
   v
Kafka topic: order-created-topic
   |
   | consume
   v
inventory-service
   |
   | save inventory in inventory_db.inventory_reservations
   | publish INVENTORY_RESERVED
   v
Kafka topic: inventory-reserved-topic
   |
   | consume
   v
notification-service
   |
   | log email/SMS
   | save in notification_db.notification_logs
   v
Done
```

---

## Run only infrastructure locally

Use this when you want to run Spring Boot services manually from IntelliJ.

```bash
docker compose -f docker-compose.local-infra.yml up -d
```

Then run services manually:

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

Kafka UI:

```text
http://localhost:8090
```

---

## Run full backend using Docker Compose

This runs MySQL, Kafka, Kafka UI, and all three microservices.

```bash
docker compose -f docker-compose.full.yml up --build
```

Stop:

```bash
docker compose -f docker-compose.full.yml down
```

Delete MySQL volume also:

```bash
docker compose -f docker-compose.full.yml down -v
```

---

## Test full Kafka flow

Call only order-service:

```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Satyam",
    "email": "satyam@gmail.com",
    "phone": "9876543210",
    "productName": "Laptop",
    "quantity": 1
  }'
```

Expected logs:

```text
order-service: ORDER_CREATED event published
inventory-service: ORDER_CREATED event consumed and INVENTORY_RESERVED event published
notification-service: INVENTORY_RESERVED event consumed and email/SMS logged
```

---

## Check MySQL tables

```bash
docker exec -it mini-mysql mysql -uroot -proot
```

```sql
USE order_db;
SELECT * FROM orders;

USE inventory_db;
SELECT * FROM inventory_reservations;

USE notification_db;
SELECT * FROM notification_logs;
```

---

## API endpoints

### Create order

```http
POST http://localhost:8081/api/orders
```

### Get order

```http
GET http://localhost:8081/api/orders/{orderId}
```

### Manual inventory reserve

```http
POST http://localhost:8082/api/inventory/reserve
```

### Manual notification send

```http
POST http://localhost:8083/api/notifications/send
```

For the real flow, use only `POST /api/orders`. Inventory and notification happen asynchronously through Kafka.

---

## Angular UI rule

Angular should call only:

```text
POST http://localhost:8081/api/orders
```

Do not call inventory-service or notification-service from Angular.

Reason:

```text
Order service is synchronous REST.
Inventory and Notification are asynchronous through Kafka.
```

---

## CI/CD

Files included:

```text
.github/workflows/ci.yml
.github/workflows/aws-ecr-ecs-deploy.yml
CI-CD-STEPS.md
AWS-ECS-DEPLOYMENT-STEPS.md
```

Start with:

```text
.github/workflows/ci.yml
```

Then move to AWS deployment.
