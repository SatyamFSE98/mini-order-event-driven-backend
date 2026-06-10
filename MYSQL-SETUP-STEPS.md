# MySQL Setup Steps

This project is now configured for a real MySQL database.

## Databases used

| Service | Database | Table created by JPA |
|---|---|---|
| order-service | order_db | orders |
| inventory-service | inventory_db | inventory_reservations |
| notification-service | notification_db | notification_logs |

---

## Option 1: Run MySQL + Kafka using Docker

From project root:

```bash
docker compose -f docker-compose.local-infra.yml up -d
```

This starts:

```text
MySQL     -> localhost:3306
Kafka     -> localhost:9092
Kafka UI  -> http://localhost:8090
```

MySQL credentials:

```text
username: root
password: root
```

The file below creates the databases automatically when the MySQL container starts first time:

```text
mysql/init/01-create-databases.sql
```

---

## Option 2: Use your own installed MySQL

Create databases manually:

```sql
CREATE DATABASE order_db;
CREATE DATABASE inventory_db;
CREATE DATABASE notification_db;
```

Use the same credentials as configured in each service:

```text
username: root
password: root
```

If your MySQL password is different, change this line in each service `application.yml`:

```yaml
spring:
  datasource:
    password: your_password
```

---

## Files changed for MySQL

### 1. order-service/pom.xml

Added/updated:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

Removed H2 runtime and kept H2 only for tests.

### 2. inventory-service/pom.xml

Added:

```xml
spring-boot-starter-data-jpa
mysql-connector-j
```

### 3. notification-service/pom.xml

Added:

```xml
spring-boot-starter-data-jpa
mysql-connector-j
```

### 4. application.yml in all services

Each service now points to MySQL:

```yaml
spring:
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:order_db}?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: ${MYSQL_USER:root}
    password: ${MYSQL_PASSWORD:root}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

Different services use different database names:

```text
order-service        -> order_db
inventory-service    -> inventory_db
notification-service -> notification_db
```

---

## Run services after MySQL is up

Terminal 1:

```bash
cd order-service
mvn spring-boot:run
```

Terminal 2:

```bash
cd inventory-service
mvn spring-boot:run
```

Terminal 3:

```bash
cd notification-service
mvn spring-boot:run
```

---

## Verify tables in MySQL

Login:

```bash
mysql -u root -p
```

Then:

```sql
SHOW DATABASES;
USE order_db;
SHOW TABLES;

USE inventory_db;
SHOW TABLES;

USE notification_db;
SHOW TABLES;
```

Expected tables:

```text
order_db.orders
inventory_db.inventory_reservations
notification_db.notification_logs
```

---

## Important interview point

For local development, we are using:

```yaml
spring.jpa.hibernate.ddl-auto: update
```

Meaning:

```text
JPA automatically creates/updates tables based on entity classes.
```

In production, do not use `update`. Prefer:

```text
Flyway or Liquibase migration scripts
```
