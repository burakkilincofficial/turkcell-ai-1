# Gateway Server

API Gateway for E-Commerce Microservices.

## Purpose

- Single entry point for external traffic
- Routes requests to order-service (port 8082) and inventory-service (port 8081)
- Handles cross-cutting concerns (CORS, Rate Limiting)

## Technology Stack

- Java 21
- Spring Boot 3.2.1
- Spring Cloud Gateway (Reactive)
- Maven

## Configuration

- **Port**: 8080
- **Base Path**: `/api`

## Routes

### Order Service (http://localhost:8082)
- `GET /api/v1/orders` - List all orders
- `POST /api/v1/orders` - Create order
- `GET /api/v1/orders/{id}` - Get order by ID
- `PUT /api/v1/orders/{id}` - Update order
- `DELETE /api/v1/orders/{id}` - Delete order

### Inventory Service (http://localhost:8081)
- `GET /api/v1/inventory` - List all inventory
- `POST /api/v1/inventory` - Create inventory
- `GET /api/v1/inventory/{id}` - Get inventory by ID
- `PUT /api/v1/inventory/{id}` - Update inventory
- `DELETE /api/v1/inventory/{id}` - Delete inventory

## How to Run

### Prerequisites
1. Java 21 installed
2. Maven installed
3. Order Service running on port 8082
4. Inventory Service running on port 8081

### Build
```bash
cd gateway-server
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

Gateway will start on **http://localhost:8080**

## Health Check

```bash
curl http://localhost:8080/actuator/health
```

## Testing Routes

### Order Service via Gateway
```bash
# List orders
curl http://localhost:8080/api/v1/orders

# Create order
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"123e4567-e89b-12d3-a456-426614174000","items":[{"productId":"123e4567-e89b-12d3-a456-426614174001","quantity":2,"price":99.99}],"totalAmount":199.98}'

# Get order by ID
curl http://localhost:8080/api/v1/orders/{order-id}
```

### Inventory Service via Gateway
```bash
# List inventory
curl http://localhost:8080/api/v1/inventory

# Create inventory
curl -X POST http://localhost:8080/api/v1/inventory \
  -H "Content-Type: application/json" \
  -d '{"productName":"Sample Product","quantity":100,"reservedQuantity":0}'

# Get inventory by ID
curl http://localhost:8080/api/v1/inventory/1
```

## Architecture Principles

✅ **Follows AGENTS.md Rules:**
- No business logic
- No database access
- No domain validation
- No transformation of domain payloads
- Pure routing only

✅ **Follows GATEWAY.md Spec:**
- Port 8080
- External base path `/api`
- Routes to services with versioned APIs `/api/v1/...`
