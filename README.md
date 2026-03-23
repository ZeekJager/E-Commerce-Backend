# E-Commerce Backend

A RESTful e-commerce backend built with Spring Boot, implementing Clean Architecture, Domain-Driven Design (DDD), and CQRS patterns.

## Tech Stack

- **Java 17** · **Spring Boot 4.0.3** · **Maven**
- **PostgreSQL** (production) · **H2** (testing)
- **Spring Security** — stateless API key authentication
- **Spring Data JPA** · **Spring Boot Validation** · **Spring Boot Actuator**

## Getting Started

### Prerequisites

- Java 17+
- Docker (for PostgreSQL)

### Run Locally

```bash
# 1. Start PostgreSQL
docker compose up -d db

# 2. Run the app
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

### Docker (full stack)

```bash
cp .env.example .env   # fill in credentials
make dev_up
```

## Environment Variables

Copy `.env.example` to `.env` and set:

| Variable | Default | Description |
|---|---|---|
| `APP_API_KEY` | `dev-secret-key` | API authentication key — change in production |
| `POSTGRES_DB` | `e-commerce` | Database name |
| `POSTGRES_USER` | `postgres` | Database user |
| `POSTGRES_PASSWORD` | — | Database password (required) |
| `JPA_DDL_AUTO` | `update` | Use `validate` in production |
| `SERVER_PORT` | `8080` | Server port |

## API Endpoints

All write endpoints require the `X-API-Key` header.

### Products

| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/api/products` | required | Create product |
| `GET` | `/api/products` | — | List all products |
| `GET` | `/api/products/{id}` | — | Get product by ID |
| `PUT` | `/api/products/{id}` | required | Update product |
| `PATCH` | `/api/products/{id}/stock` | required | Update stock quantity |
| `DELETE` | `/api/products/{id}` | required | Delete product |
| `GET` | `/api/products/category/{category}` | — | List by category (`?page=0&size=20`) |

### Orders

| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/api/orders` | required | Create order |
| `GET` | `/api/orders` | — | List all orders |
| `GET` | `/api/orders/{id}` | — | Get order by ID |

### Other

| Method | Path | Description |
|---|---|---|
| `GET` | `/actuator/health` | Health check |

### Example Requests

**Create a product:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "X-API-Key: dev-secret-key" \
  -d '{"name": "Laptop", "category": "Electronics", "price": 999.99, "stock": 50}'
```

**Create an order:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "X-API-Key: dev-secret-key" \
  -d '{
    "customerId": "cust-001",
    "productIds": ["<product-uuid>"],
    "shippingAddress": {
      "street": "123 Main St",
      "city": "Springfield",
      "zipCode": "12345",
      "country": "US"
    }
  }'
```

A full Postman collection with mock data and error scenarios is at `postman/E-Commerce-Backend-Full-Functionality.postman_collection.json`.

## Testing

```bash
# Run all tests (uses H2 — no database required)
./mvnw test

# Run a single test class
./mvnw test -Dtest=CreateOrderHandlerTest
```

63 tests across unit (Mockito) and integration (@SpringBootTest) levels.

## Makefile

```bash
make dev_up    # Start PostgreSQL + run app locally
make dev_down  # Stop Docker services
make logs      # Tail app container logs
make ps        # Show running containers
make build     # Build the Docker image
make test      # Run tests
make clean     # Stop services and remove volumes
```

## Architecture

Clean Architecture with DDD and CQRS across four layers:

```
api/            → REST controllers, request DTOs, security filter
application/    → Commands + handlers, queries + handlers, event handlers
domain/         → Entities, aggregates, value objects, domain events, repository interfaces
infrastructure/ → JPA entities, repository implementations
```

- **Commands** handle writes; **Queries** handle reads — controllers never touch repositories directly
- **`Result<T>`** wrapper used throughout for typed success/failure instead of exceptions
- Repository interfaces live in `domain/` — implementations in `infrastructure/`
- Domain entities and JPA entities are separate classes with explicit mapping
