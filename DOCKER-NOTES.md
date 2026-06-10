# Docker Notes

## Current infra Docker Compose

Use this for local development:

```bash
docker compose -f docker-compose.local-infra.yml up -d
```

It starts:

```text
MySQL     -> localhost:3306
Kafka     -> localhost:9092
Kafka UI  -> http://localhost:8090
```

## Kafka-only Docker Compose

If you already have MySQL installed locally, you can start Kafka only:

```bash
docker compose -f docker-compose.kafka-only.yml up -d
```

## Backend service Dockerfiles

Each service already has a Dockerfile:

```text
order-service/Dockerfile
inventory-service/Dockerfile
notification-service/Dockerfile
```

When you later run backend services as Docker containers, set these env variables:

```text
MYSQL_HOST=mysql
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=root
KAFKA_BOOTSTRAP_SERVERS=kafka:29092
```

When running from IntelliJ or terminal on your laptop, default values are already correct:

```text
MYSQL_HOST=localhost
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```
