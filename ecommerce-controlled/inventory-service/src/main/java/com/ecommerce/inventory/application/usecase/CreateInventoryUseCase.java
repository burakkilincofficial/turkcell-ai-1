package com.ecommerce.inventory.application.usecase;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for creating a new inventory record.
 * Application layer - orchestrates the creation flow.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CreateInventoryUseCase {

    private final InventoryRepository inventoryRepository;

    public Inventory execute(String productId, String productName, Integer quantity,
                             Integer minStockLevel, Integer maxStockLevel,
                             String location, String description) {
        
        // Create domain model (validates invariants in constructor)
        Inventory inventory = new Inventory(
                productId,
                productName,
                quantity,
                minStockLevel,
                maxStockLevel,
                location,
                description
        );

        // Persist and return
        return inventoryRepository.save(inventory);
    }
}
