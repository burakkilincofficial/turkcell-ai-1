package main.java.com.ecommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Gateway Server Application
 * 
 * Single entry point for external traffic to microservices.
 * Routes requests to order-service and inventory-service.
 * 
 * No business logic, no database access, no domain validation.
 */
@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }
}
