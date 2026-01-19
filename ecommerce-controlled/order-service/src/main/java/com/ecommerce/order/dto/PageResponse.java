package com.ecommerce.order.dto;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    Integer totalElements,
    Integer totalPages,
    Integer currentPage
) {
}
