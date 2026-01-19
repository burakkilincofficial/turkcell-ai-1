package com.ecommerce.inventory.application.usecase;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.exception.InventoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for retrieving inventory by product ID.
 * Application layer - orchestrates the retrieval flow.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetInventoryByProductIdUseCase {

    private final InventoryRepository inventoryRepository;

    public Inventory execute(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with productId: " + productId));
    }
}
