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

## API Documentation (Swagger)

After the service starts, you can access the Swagger UI at:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Here you can:
- Explore all REST endpoints
- Try out API calls directly in the browser
- See request/response models

---

## Environment Variables

The following variables are configured in your `docker-compose.yml`:

- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/users`
- `SPRING_DATASOURCE_USERNAME=user`
- `SPRING_DATASOURCE_PASSWORD=pass`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092`

Database and Kafka settings can be changed in `docker-compose.yml` as needed.

---

## Future Improvements

- **Dynamic filtering**  
- **GRpc implementation for internal communication**
