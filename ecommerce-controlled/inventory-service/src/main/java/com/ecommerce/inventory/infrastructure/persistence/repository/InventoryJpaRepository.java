package com.ecommerce.inventory.infrastructure.persistence.repository;

import com.ecommerce.inventory.infrastructure.persistence.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository for InventoryEntity.
 * Infrastructure layer - extends Spring Data JPA.
 */
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {
    
    Optional<InventoryEntity> findByProductId(String productId);
}
