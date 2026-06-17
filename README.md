# Foodverse API

Phase 1 project setup for a Spring Boot backend generated from Spring Initializr.

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Maven
- MySQL
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security
- SpringDoc OpenAPI

## Current Scope

This phase only prepares the project foundation.

- Dependency setup
- Base configuration
- Package scaffolding
- Swagger/OpenAPI configuration

Not included in this phase:

- Security implementation
- JWT
- Entities
- Repositories
- Services
- Controllers

## Project Structure

```text
src/main/java/dev/peacechan/foodverse
├── config
└── common
    ├── constant
    ├── exception
    ├── payload
    └── util
```

## Configuration

Application settings are stored in `src/main/resources/application.properties`.

Database values use environment-variable overrides:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`

Default Swagger URLs:

- `/api-docs`
- `/swagger-ui.html`

## Run

```bash
./mvnw spring-boot:run
```
