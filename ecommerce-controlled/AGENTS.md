## Mini E-Commerce Microservices (Java 21 / Spring Boot)

This repository contains a mini e-commerce microservices ecosystem.

- Services: order-service, payment-service, inventory-service

- Tech: Java 21, Spring Boot 3.X, OpenAPI/Swagger

- Approach: Contract-First

If any rule below is violated, the output is WRONG.

---

## 1) HOW TO WORK (MANDATORY WORKFLOW)

### 1.1 Plan-first, then code

Before generating code, you MUST:

1. Confirm the relevant contract(s) exist and match the request. (OpenAPI)

2. Propose a FILE BREAKDOWN (what files will be created/changed + why)

3. Ask QUESTIONS for missing details instead of guessing.

4. Generate code ONE FILE AT A TIME (or in small batches of max 3 files).

### 1.2 No inventing

You MUST NOT invent:

- endpoints, request/response fields, error models

- event names/payloads

- DB schema/columns

- Business rules, status, money rules

If any detail is missing, ASK.


## 2) CONTRACT FIRST

### 2.1 OpenAPI is the source of truth

- API contracts live under: `/docs/openapi`

- Each service MUST have its own OpenAPI file:
  - `docs/openapi/order-service.yml`
  - `docs/openapi/inventory-service.yml`

- Swagger UI is generated from these contracts.

- Implementation MUST follow the contract; never the other way around.

### 2.2 Code Generation Policy

- We may use OpenAPI tooling ONLY if already present in the repository.

- You MUST NOT add new OpenAPI generator dependencies without explicit approval.

- If no generator is available, implement controllers/DTOs manually to match the spec.

### 2.3 Versioning

- API Changes MUST be versioned. (eg. `api/v1/orders`, `/api/v2/orders`)

- Version MUST be placed in path.

- Breaking changes require a new version; do not silently break clients.