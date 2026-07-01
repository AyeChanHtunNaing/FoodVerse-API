# Foodverse API

Backend API for Foodverse, built with Spring Boot. The project provides JWT-based authentication, role-based access control, customer profiles, restaurant and menu management, order handling, Redis caching, and email notifications for order events.

## Features

- JWT authentication for register and login flows
- Role-based authorization for `ADMIN` and `USER`
- Customer profile management
- Restaurant CRUD operations
- Restaurant menu CRUD operations
- Order placement, lookup, cancellation, and status updates
- Redis-backed caching for restaurant, menu, and order reads
- Async email notifications for order creation and status changes
- Scheduled cleanup that cancels long-running `PREPARING` orders
- OpenAPI and Swagger UI documentation

## Tech Stack

- Java 21
- Spring Boot 3.5.16
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Data Redis
- Spring Mail
- MySQL
- Maven
- SpringDoc OpenAPI

## Main Modules

- `auth`: registration, login, and JWT issuing
- `customer`: customer creation and self-profile management
- `restaurant`: restaurant management and listing
- `menu`: restaurant menu management
- `order`: order placement and order lifecycle updates
- `security`: JWT filter, user details service, and security configuration
- `config`: OpenAPI, Redis, async mail executor, and security setup
- `common`: shared error handling and API error payloads

## Access Rules

- Public endpoints:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
  - `/api-docs`
  - `/swagger-ui.html`
- `ADMIN` can manage customers, restaurants, menus, and order statuses.
- `USER` can manage their own profile and place or cancel their own orders.
- All non-public endpoints require a Bearer token.

## Project Structure

```text
src
├── main
│   ├── java/dev/peacechan/foodverse
│   │   ├── auth
│   │   ├── common
│   │   ├── config
│   │   ├── customer
│   │   ├── email
│   │   ├── entity
│   │   ├── enums
│   │   ├── menu
│   │   ├── order
│   │   ├── repository
│   │   ├── restaurant
│   │   └── security
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

## Configuration

Application settings live in `src/main/resources/application.properties`.

Set these environment variables before starting the app:

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_HOST` | `localhost` | MySQL host |
| `DB_PORT` | `3306` | MySQL port |
| `DB_NAME` | `foodverse` | Database name |
| `DB_USERNAME` | `root` | Database username |
| `DB_PASSWORD` | `helloworld` | Database password |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `JWT_SECRET` | development fallback in properties | JWT signing secret |

Mail delivery is configured through Spring Mail properties. For local development, use your own SMTP credentials instead of relying on placeholder or machine-specific values.

## Running the Application

Start MySQL and Redis first, then run:

```bash
./mvnw spring-boot:run
```

The API starts on:

```text
http://localhost:8080
```

## API Documentation

Swagger and OpenAPI are available after startup:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

Authorize secured endpoints in Swagger UI with:

```text
Bearer <your-jwt-token>
```

## Order Workflow Notes

- New orders are created by authenticated users.
- Admins can move orders through status updates.
- Users can cancel their own orders when business rules allow it.
- A scheduled job runs every minute and cancels orders stuck in `PREPARING` for more than 30 minutes.

## Development

Run the test suite with:

```bash
./mvnw test
```
