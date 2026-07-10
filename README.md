# FoodVerse API

FoodVerse API is a Spring Boot backend for a food ordering platform. It provides JWT-based authentication, role-based authorization, customer profile management, restaurant and menu management, order placement, Redis caching, email notifications, scheduled order cleanup, and Swagger/OpenAPI documentation.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Authentication and Authorization](#authentication-and-authorization)
- [API Endpoints](#api-endpoints)
- [Order Workflow](#order-workflow)
- [Caching](#caching)
- [Email Notifications](#email-notifications)
- [Error Handling](#error-handling)
- [Swagger Documentation](#swagger-documentation)
- [Development](#development)

## Features

- User registration and login with JWT access tokens
- Password hashing with BCrypt
- Stateless Spring Security configuration
- Role-based access control for `ADMIN` and `USER`
- Customer account and profile management
- Restaurant CRUD operations
- Menu CRUD operations under restaurants
- Order placement, lookup, cancellation, and status updates
- Business validation for restaurant status, menu availability, duplicate items, and order status transitions
- Redis-backed caching for restaurant, menu, and order reads
- Asynchronous email notifications for order creation and order status updates
- Scheduled cancellation for long-running `PREPARING` orders
- Centralized exception handling with consistent API error responses
- Swagger UI and OpenAPI JSON documentation

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 3.5.16 |
| Build Tool | Maven / Maven Wrapper |
| Web | Spring Web |
| Security | Spring Security, JWT, BCrypt |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL |
| Cache | Spring Cache, Redis |
| Email | Spring Mail |
| Validation | Jakarta Bean Validation |
| API Docs | SpringDoc OpenAPI, Swagger UI |
| Utilities | Lombok |
| Tests | Spring Boot Test, Spring Security Test |

## Project Structure

```text
src
├── main
│   ├── java/dev/peacechan/foodverse
│   │   ├── auth          # Registration, login, JWT issuing, users, roles
│   │   ├── common        # Shared exceptions and error payloads
│   │   ├── config        # Security, Redis, async executor, OpenAPI config
│   │   ├── customer      # Customer profile endpoints and business logic
│   │   ├── email         # Async email service
│   │   ├── menu          # Menu endpoints, entities, DTOs, services
│   │   ├── order         # Order placement and lifecycle management
│   │   ├── restaurant    # Restaurant endpoints and business logic
│   │   └── security      # JWT filter, JWT service, user details service
│   └── resources
│       └── application.properties
└── test
    └── java/dev/peacechan/foodverse
```

## Prerequisites

- Java 21
- Maven 3.9+ or the included Maven wrapper
- MySQL 8+
- Redis 7+
- SMTP credentials if you want to send real email notifications

## Configuration

Application configuration lives in:

```text
src/main/resources/application.properties
```

Environment variables supported by the application:

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_HOST` | `localhost` | MySQL host |
| `DB_PORT` | `3306` | MySQL port |
| `DB_NAME` | `foodverse` | MySQL database name |
| `DB_USERNAME` | `root` | MySQL username |
| `DB_PASSWORD` | empty | MySQL password |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `JWT_SECRET` | development fallback | Base64-encoded JWT signing secret |

Important application properties:

| Property | Purpose |
| --- | --- |
| `server.port=8080` | Runs the API on port 8080 |
| `spring.jpa.hibernate.ddl-auto=update` | Updates the schema automatically in development |
| `spring.cache.type=redis` | Uses Redis as the cache provider |
| `jwt.expiration=86400000` | JWT lifetime in milliseconds, 24 hours |
| `springdoc.api-docs.path=/api-docs` | OpenAPI JSON path |
| `springdoc.swagger-ui.path=/swagger-ui.html` | Swagger UI path |

For production, keep database passwords, SMTP credentials, and JWT secrets outside the repository. Load them from environment variables or a secret manager.

## Running the Application

Start MySQL and Redis first.

Example local environment:

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=foodverse
export DB_USERNAME=root
export DB_PASSWORD=your_password
export REDIS_HOST=localhost
export REDIS_PORT=6379
export JWT_SECRET=your_base64_encoded_secret
```

Run the API:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

Build the project:

```bash
./mvnw clean package
```

Run the generated JAR:

```bash
java -jar target/foodverse-0.0.1-SNAPSHOT.jar
```

## Authentication and Authorization

Public endpoints:

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive a JWT |
| `GET` | `/api-docs` | OpenAPI JSON |
| `GET` | `/swagger-ui.html` | Swagger UI |

All other endpoints require a Bearer token:

```http
Authorization: Bearer <jwt-token>
```

Roles:

| Role | Capabilities |
| --- | --- |
| `ADMIN` | Manage customers, restaurants, menus, and order statuses. View any order. |
| `USER` | Manage own profile, view restaurants and menus, place orders, view own orders, and cancel own placed orders. |

The normal registration endpoint creates `USER` accounts. `ADMIN` users must be seeded or inserted separately.

## API Endpoints

### Authentication

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/auth/register` | Public | Create a user account and return a JWT |
| `POST` | `/api/auth/login` | Public | Authenticate and return a JWT |

Register request:

```json
{
  "fullName": "Alice Nguyen",
  "email": "alice@example.com",
  "password": "SecurePass123",
  "phoneNumber": "+84901234567"
}
```

Auth response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "alice@example.com",
  "role": "USER"
}
```

### Customers

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/customers` | `ADMIN` | Create a customer account and profile |
| `GET` | `/api/customers` | `ADMIN` | List all customers |
| `GET` | `/api/customers/me` | `USER` | Get the authenticated user's profile |
| `PATCH` | `/api/customers/me` | `USER` | Update the authenticated user's profile |
| `DELETE` | `/api/customers/{userId}` | `ADMIN` | Delete a customer account |

Create customer request:

```json
{
  "fullName": "Bob Tran",
  "email": "bob@example.com",
  "password": "SecurePass123",
  "phoneNumber": "+84987654321",
  "address": "123 Nguyen Trai Street",
  "city": "Ho Chi Minh City",
  "postalCode": "700000"
}
```

### Restaurants

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/restaurants` | `ADMIN` | Create a restaurant |
| `PUT` | `/api/restaurants/{restaurantId}` | `ADMIN` | Update a restaurant |
| `DELETE` | `/api/restaurants/{restaurantId}` | `ADMIN` | Delete a restaurant |
| `GET` | `/api/restaurants/{restaurantId}` | `ADMIN`, `USER` | Get one restaurant |
| `GET` | `/api/restaurants` | `ADMIN`, `USER` | List restaurants |

Create or update restaurant request:

```json
{
  "name": "Saigon Bites",
  "address": "45 Le Loi, District 1",
  "phoneNumber": "+842812345678",
  "status": "OPEN"
}
```

Restaurant statuses:

```text
OPEN, CLOSED, INACTIVE
```

A restaurant cannot be deleted if existing orders reference it.

### Menus

Menu endpoints are nested under a restaurant:

```text
/api/restaurants/{restaurantId}/menus
```

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/restaurants/{restaurantId}/menus` | `ADMIN` | Add a menu item |
| `PUT` | `/api/restaurants/{restaurantId}/menus/{menuId}` | `ADMIN` | Update a menu item |
| `DELETE` | `/api/restaurants/{restaurantId}/menus/{menuId}` | `ADMIN` | Delete a menu item |
| `GET` | `/api/restaurants/{restaurantId}/menus/{menuId}` | `ADMIN`, `USER` | Get one menu item |
| `GET` | `/api/restaurants/{restaurantId}/menus` | `ADMIN`, `USER` | List menu items for a restaurant |

Create or update menu request:

```json
{
  "name": "Margherita Pizza",
  "description": "Classic pizza with tomato, mozzarella, and basil.",
  "price": 12.50,
  "status": "AVAILABLE"
}
```

Menu statuses:

```text
AVAILABLE, UNAVAILABLE
```

A menu item cannot be deleted if existing order items reference it.

### Orders

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/orders` | `USER` | Place an order |
| `GET` | `/api/orders/{orderId}` | `ADMIN` | Get any order by ID |
| `GET` | `/api/orders/me/{orderId}` | `USER` | Get one order owned by the authenticated user |
| `PATCH` | `/api/orders/me/{orderId}/cancel` | `USER` | Cancel an owned order |
| `PATCH` | `/api/orders/{orderId}/status` | `ADMIN` | Update an order status |

Place order request:

```json
{
  "restaurantId": 3,
  "items": [
    {
      "menuId": 15,
      "quantity": 2
    }
  ]
}
```

Order response:

```json
{
  "id": 21,
  "orderNumber": "ORD-A1B2C3D4",
  "status": "PLACED",
  "totalAmount": 25.00,
  "orderedAt": "2026-07-10T09:30:00",
  "customerProfileId": 10,
  "restaurantId": 3,
  "items": [
    {
      "id": 100,
      "menuId": 15,
      "menuName": "Margherita Pizza",
      "quantity": 2,
      "unitPrice": 12.50,
      "lineTotal": 25.00
    }
  ]
}
```

Update order status request:

```json
{
  "status": "PREPARING"
}
```

## Order Workflow

Order statuses:

```text
PLACED, PREPARING, DELIVERED, CANCELLED
```

Allowed transitions:

| Current Status | Allowed Next Status |
| --- | --- |
| `PLACED` | `PREPARING`, `CANCELLED` |
| `PREPARING` | `DELIVERED`, `CANCELLED` |
| `DELIVERED` | none |
| `CANCELLED` | none |

Order placement rules:

- The authenticated user must have a customer profile.
- The restaurant must exist and have status `OPEN`.
- Every menu item must belong to the selected restaurant.
- Every menu item must have status `AVAILABLE`.
- Duplicate menu IDs are not allowed in a single order request.
- Quantity must be at least `1`.

Cancellation rules:

- Users can cancel their own orders only while the order is still `PLACED`.
- Admins can move orders through valid status transitions.

Scheduled cleanup:

- The scheduler runs every minute.
- Orders that remain `PREPARING` for more than 30 minutes are automatically changed to `CANCELLED`.

## Caching

The project uses Redis through Spring Cache.

| Cache Name | Purpose |
| --- | --- |
| `restaurants` | Restaurant list |
| `restaurant` | Single restaurant |
| `menus` | Restaurant menu list |
| `menu` | Single menu item |
| `orders` | Single order for admin reads |
| `ownOrders` | Single order for owner reads |

Read operations use `@Cacheable`. Create, update, delete, cancel, and status-update operations evict related cache entries.

## Email Notifications

Email notifications are sent asynchronously.

| Event | Recipient | Subject |
| --- | --- | --- |
| Order created | Customer email | `Order Created - {orderNumber}` |
| Order status updated | Customer email | `Order Updated - {orderNumber}` |

If email sending fails, the failure is logged and the API request continues without throwing an error to the client.

## Error Handling

The API uses a shared error response format:

```json
{
  "timestamp": "2026-07-10T09:45:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/restaurants",
  "validationErrors": {
    "name": "Restaurant name is required"
  }
}
```

Common status codes:

| Status | Meaning |
| --- | --- |
| `400 Bad Request` | Validation failure or business rule violation |
| `401 Unauthorized` | Invalid login credentials |
| `403 Forbidden` | Authenticated user does not have the required role |
| `404 Not Found` | Requested resource does not exist |
| `409 Conflict` | Duplicate email or delete conflict |
| `500 Internal Server Error` | Unexpected server error |

## Swagger Documentation

After starting the application, open:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON is available at:

```text
http://localhost:8080/api-docs
```

To call secured endpoints from Swagger UI:

1. Register or login to get a JWT.
2. Click `Authorize`.
3. Enter the token in this format:

```text
Bearer <your-jwt-token>
```

## Development

Run tests:

```bash
./mvnw test
```

Compile the project:

```bash
./mvnw clean compile
```

Useful notes:

- `spring.jpa.hibernate.ddl-auto=update` is useful for local development. For production, use database migrations such as Flyway or Liquibase.
- Redis must be running because the cache type is configured as `redis`.
- The JDBC URL includes `createDatabaseIfNotExist=true`, so MySQL can create the database automatically when the configured user has permission.
- Swagger annotations are already included in controllers and DTOs, so generated API docs stay close to the code.
