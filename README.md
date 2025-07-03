# User Service

A Spring Boot microservice for managing users, built with PostgreSQL and Kafka support.  
Includes Swagger UI for API documentation.

---

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Running with Docker Compose](#running-with-docker-compose)
- [API Documentation (Swagger)](#api-documentation-swagger)
- [Environment Variables](#environment-variables)
- [Troubleshooting](#troubleshooting)

---

## Features

- User CRUD operations (Create, Read, Update, Delete)
- Unique constraints on Email and Nickname
- Password hashing (Spring Security)
- Event publishing to Kafka (`user-events` topic)
- PostgreSQL for persistence
- REST API documented with Swagger/OpenAPI

---

## Prerequisites

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

---

## Running with Docker Compose

1. **Clone the repository:**
    ```sh
    git clone https://github.com/yourusername/user-service.git
    cd user-service
    ```

2. **Build and start all services:**
    ```sh
    docker compose up --build
    ```
   > This will build the app and start PostgreSQL, Kafka, Zookeeper, and your user service.

3. **Service Endpoints:**
    - **User Service API:**  
      [http://localhost:8080](http://localhost:8080)
    - **Swagger UI:**  
      [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Environment Variables

The following variables are configured in your `docker-compose.yml`:

- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/users`
- `SPRING_DATASOURCE_USERNAME=user`
- `SPRING_DATASOURCE_PASSWORD=pass`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092`

Database and Kafka settings can be changed in `docker-compose.yml` as needed.

---

## Development Choices and Assumptions

### Main Decisions

- **Unique Constraints:**  
  Both `email` and `nickname` fields are uniquely indexed at the database level, and the application checks for uniqueness before creating users for more user-friendly error messages.
- **Event Notification:**  
  User-related events (such as creation) are published to Kafka (`user-events` topic), enabling easy integration with other microservices or external consumers.
- **Dockerized Stack:**  
  The entire application stack (Spring Boot app, PostgreSQL, Kafka, Zookeeper) is orchestrated using Docker Compose, ensuring easy local development and environment parity.
- **No Authentication:**  
  Authentication and authorization are explicitly out-of-scope for this version, but can be integrated later as needed.
- **Spring Boot with Spring Data JPA:**  
  Chosen for rapid development, robust transaction support, and ease of integration with PostgreSQL.

### Assumptions

- All requests and responses use JSON.
- Users are uniquely identified by a UUID.
- No authentication or authorization is enforced in this service.
- Kafka and database are run as single-node instances in development, but should be clustered in production.

---

## API Documentation (Swagger)

Once running, access API docs and test endpoints via Swagger UI:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

You can interact with endpoints such as:
- `POST /users` — create a new user
- `GET /users/{id}` — fetch user by ID
- `GET /users` — list users (with filtering and pagination)
- `PUT /users/{id}` — update user
- `DELETE /users/{id}` — delete user

---

## Improvements
- **Grpc Controller**  
  Implement Grpc control for internal communication
- **Health Checks & Monitoring:**  
  Integrate with tools like Prometheus/Grafana for application and infrastructure monitoring.
- **Zero Downtime Deployments:**  
  Use rolling updates and blue/green deployments in Kubernetes.
- **Automated Tests & CI/CD:**  
  Expand test coverage and set up automated builds, tests, and deployments.
- **Multi-tenancy & Sharding:**  
  Support multiple organizations and very large user sets.

