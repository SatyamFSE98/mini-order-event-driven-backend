# Start Here

## Your current goal

Backend Kafka flow is already completed. You only need to:

```text
1. Run backend
2. Test APIs
3. Build Angular UI
4. Push to GitHub
5. Run CI/CD
6. Deploy to AWS
```

---

## Option A: Run full backend using Docker Compose

Best if you want one command.

```bash
docker compose -f docker-compose.full.yml up --build
```

Open Kafka UI:

```text
http://localhost:8090
```

Test:

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

---

## Option B: Run infra in Docker and services from IntelliJ

```bash
docker compose -f docker-compose.local-infra.yml up -d
```

Then run each service:

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

## Expected successful flow

```text
POST /api/orders
order-service saves order
order-service publishes ORDER_CREATED
inventory-service consumes ORDER_CREATED
inventory-service saves reservation
inventory-service publishes INVENTORY_RESERVED
notification-service consumes INVENTORY_RESERVED
notification-service logs email and SMS
```

---

## After backend works

Build Angular UI with only one API call:

```text
POST http://localhost:8081/api/orders
```

Do not call inventory or notification from Angular.
