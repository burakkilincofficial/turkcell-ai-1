package com.ecommerce.order.dto;

public record Address(
    String street,
    String city,
    String postalCode,
    String country
) {
}
