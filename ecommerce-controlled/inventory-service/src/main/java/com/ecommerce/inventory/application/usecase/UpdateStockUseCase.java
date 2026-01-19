package com.ecommerce.inventory.application.usecase;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.exception.InventoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for updating stock quantity.
 * Application layer - orchestrates the stock update flow.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateStockUseCase {

    private final InventoryRepository inventoryRepository;

    public Inventory execute(Long id, Integer quantityChange) {
        // Find existing inventory
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));

        // Update stock using domain method
        inventory.updateStock(quantityChange);

        // Persist and return
        return inventoryRepository.save(inventory);
    }
}
