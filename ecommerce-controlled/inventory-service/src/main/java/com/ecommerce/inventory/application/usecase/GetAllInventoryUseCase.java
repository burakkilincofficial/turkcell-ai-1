package com.ecommerce.inventory.application.usecase;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for retrieving all inventories with pagination.
 * Application layer - orchestrates the retrieval flow.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllInventoryUseCase {

    private final InventoryRepository inventoryRepository;

    public Page<Inventory> execute(int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return inventoryRepository.findAll(pageable);
    }

    private Pageable createPageable(int page, int size, String sort) {
        if (sort == null || sort.isEmpty()) {
            return PageRequest.of(page, size);
        }

        String[] sortParams = sort.split(",");
        String property = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(direction, property));
    }
}
