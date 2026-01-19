package com.ecommerce.inventory.application.usecase;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.exception.InventoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for updating an existing inventory record.
 * Application layer - orchestrates the update flow.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateInventoryUseCase {

    private final InventoryRepository inventoryRepository;

    public Inventory execute(Long id, String productId, String productName, Integer quantity,
                             Integer minStockLevel, Integer maxStockLevel,
                             String location, String description) {
        
        // Find existing inventory
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));

        // Update using domain method (validates invariants)
        inventory.updateDetails(productId, productName, quantity, minStockLevel, 
                               maxStockLevel, location, description);

        // Persist and return
        return inventoryRepository.save(inventory);
    }
}
