### GATEWAY.md - API Gateway Spec
Last Update: 2026-01-19T15:00

This document defines the API Gateway rules, dependencies and standards.
It is the source of truth for gateway configuration and cross-cutting concerns.

---

### Purpose

- Single entry point for external traffic.

- Routing to microservices

- Cross-cutting concerns (Auth, CORS, Rate Limiting, Headers Spec.)

### Non-Goals (Forbidden)

- No business logic

- No database access

- No domain validation beyond basic request constraints (size limit, headers, auth)

- No transformation of domain payloads

---

## 1) Technology

- Java: 21

- Spring Boot: 3.X

- Build System: Maven

- Gateway: Spring Cloud Gateway (Reactive)

## 2) Port & Base Path Standard 

- Gateway Port: `8080`

- External base path prefix: `/api`

- Services expose versioned APIs internally: `/api/v1/...`

Examples:

- External: `GET /api/v1/orders`

- Internal: `http://order-service:8081/api/v1/orders`