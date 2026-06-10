# AWS ECR + ECS Deployment Steps

This project is ready for local Docker and CI/CD learning. For AWS deployment, use this order.

## Recommended simple AWS architecture

```text
GitHub Actions
   |
   v
AWS ECR repositories
   |
   v
AWS ECS Fargate services
   |
   v
CloudWatch logs
```

For a real production setup, use RDS MySQL and MSK/Confluent Cloud for Kafka. For interview/demo, you can first deploy only the Spring Boot containers and explain that MySQL/Kafka are external managed services.

## Step 1: Create ECR repositories

Create these repositories in AWS ECR:

```text
mini-order/order-service
mini-order/inventory-service
mini-order/notification-service
```

## Step 2: Create ECS cluster

Create cluster:

```text
mini-order-cluster
```

Use Fargate.

## Step 3: Create task definitions

Create one task definition per service:

```text
order-service-task
inventory-service-task
notification-service-task
```

Use image URI like:

```text
<AWS_ACCOUNT_ID>.dkr.ecr.ap-south-1.amazonaws.com/mini-order/order-service:latest
```

Use ports:

```text
order-service: 8081
inventory-service: 8082
notification-service: 8083
```

Set environment variables in each task definition:

```text
MYSQL_HOST=<your-rds-endpoint>
MYSQL_PORT=3306
MYSQL_USER=<your-db-user>
MYSQL_PASSWORD=<your-db-password>
KAFKA_BOOTSTRAP_SERVERS=<your-kafka-bootstrap-server>
```

Service-specific database names:

```text
order-service: MYSQL_DATABASE=order_db
inventory-service: MYSQL_DATABASE=inventory_db
notification-service: MYSQL_DATABASE=notification_db
```

## Step 4: Create ECS services

Create these services inside the cluster:

```text
order-service
inventory-service
notification-service
```

## Step 5: Add GitHub secrets

In GitHub repository settings, add:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
```

## Step 6: Run deployment pipeline

Open GitHub Actions and run:

```text
Deploy to AWS ECR and ECS
```

This pipeline will:

```text
Build Docker images
Push images to ECR
Force ECS services to redeploy
```

## Interview explanation

```text
I created GitHub Actions CI/CD. On push or manual trigger, the pipeline builds each Spring Boot service, runs tests, creates Docker images, pushes them to AWS ECR, and then updates ECS Fargate services. Configuration like database and Kafka bootstrap servers is passed through environment variables.
```

## Included task definition templates

```text
aws/ecs/order-service-task-definition.json
aws/ecs/inventory-service-task-definition.json
aws/ecs/notification-service-task-definition.json
```

Replace placeholders like:

```text
<AWS_ACCOUNT_ID>
<RDS_ENDPOINT>
<DB_USER>
<DB_PASSWORD>
<KAFKA_BOOTSTRAP_SERVER>
```
