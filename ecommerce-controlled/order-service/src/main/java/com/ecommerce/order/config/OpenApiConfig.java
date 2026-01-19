package com.ecommerce.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8082");
        localServer.setDescription("Local development server");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.ecommerce.com/orders");
        prodServer.setDescription("Production server");

        Contact contact = new Contact();
        contact.setName("API Support");
        contact.setEmail("support@ecommerce.com");

        Info info = new Info()
                .title("Order Service API")
                .version("1.0.0")
                .description("Sipariş yönetimi için CRUD işlemlerini sağlayan API")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, prodServer));
    }
}
