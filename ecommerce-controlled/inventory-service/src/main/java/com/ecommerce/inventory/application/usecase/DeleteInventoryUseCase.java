package com.ecommerce.inventory.application.usecase;

import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.exception.InventoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for deleting an inventory record.
 * Application layer - orchestrates the deletion flow.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteInventoryUseCase {

    private final InventoryRepository inventoryRepository;

    public void execute(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new InventoryNotFoundException("Inventory not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }
}
