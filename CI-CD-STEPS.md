# CI/CD Steps

## First pipeline: local CI build

File:

```text
.github/workflows/ci.yml
```

It does:

```text
Checkout code
Set up Java 17
Build order-service
Build inventory-service
Build notification-service
Build Docker images
```

Run this first before AWS deployment.

## Second pipeline: AWS ECR + ECS deployment

File:

```text
.github/workflows/aws-ecr-ecs-deploy.yml
```

It does:

```text
Checkout code
Configure AWS credentials
Login to ECR
Build Docker images
Push images to ECR
Force ECS service deployment
```

## Important

The AWS deployment workflow assumes you already created:

```text
ECR repositories
ECS cluster
ECS services
Task definitions
```

For first learning, run only CI build. After that, move to AWS.
