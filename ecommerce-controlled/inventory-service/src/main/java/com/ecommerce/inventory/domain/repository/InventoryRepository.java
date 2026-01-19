package com.ecommerce.inventory.domain.repository;

import com.ecommerce.inventory.domain.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Domain repository interface (Port in Hexagonal Architecture).
 * Defines the contract for inventory persistence without infrastructure concerns.
 */
public interface InventoryRepository {

    /**
     * Save a new or existing inventory.
     * @param inventory the inventory to save
     * @return the saved inventory with generated ID if new
     */
    Inventory save(Inventory inventory);

    /**
     * Find inventory by ID.
     * @param id the inventory ID
     * @return Optional containing the inventory if found
     */
    Optional<Inventory> findById(Long id);

    /**
     * Find inventory by product ID.
     * @param productId the product ID
     * @return Optional containing the inventory if found
     */
    Optional<Inventory> findByProductId(String productId);

    /**
     * Find all inventories with pagination.
     * @param pageable pagination information
     * @return page of inventories
     */
    Page<Inventory> findAll(Pageable pageable);

    /**
     * Check if inventory exists by ID.
     * @param id the inventory ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Delete inventory by ID.
     * @param id the inventory ID
     */
    void deleteById(Long id);
}
