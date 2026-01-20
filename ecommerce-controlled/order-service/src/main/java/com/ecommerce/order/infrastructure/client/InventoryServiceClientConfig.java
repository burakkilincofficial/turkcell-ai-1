package com.ecommerce.order.infrastructure.client;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for InventoryServiceClient.
 * 
 * Retry Policy (per Q1 answer):
 * - Retry 3 times on connection errors or 5xx responses
 * - 100ms initial interval, 1s max interval
 * - Exponential backoff
 */
@Configuration
public class InventoryServiceClientConfig {

    @Bean
    public Retryer retryer() {
        // period: initial interval (100ms)
        // maxPeriod: max interval between retries (1000ms)
        // maxAttempts: total attempts including initial (1 + 3 retries = 4)
        return new Retryer.Default(100L, 1000L, 4);
    }
}
