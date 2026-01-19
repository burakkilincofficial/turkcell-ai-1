package com.ecommerce.inventory.infrastructure.persistence.mapper;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.infrastructure.persistence.entity.InventoryEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between domain model (Inventory) and persistence entity (InventoryEntity).
 * Part of infrastructure layer.
 */
@Component
public class InventoryMapper {

    /**
     * Convert domain model to JPA entity.
     * @param domain the domain model
     * @return the JPA entity
     */
    public InventoryEntity toEntity(Inventory domain) {
        if (domain == null) {
            return null;
        }

        InventoryEntity entity = new InventoryEntity();
        entity.setId(domain.getId());
        entity.setProductId(domain.getProductId());
        entity.setProductName(domain.getProductName());
        entity.setQuantity(domain.getQuantity());
        entity.setMinStockLevel(domain.getMinStockLevel());
        entity.setMaxStockLevel(domain.getMaxStockLevel());
        entity.setLocation(domain.getLocation());
        entity.setDescription(domain.getDescription());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    /**
     * Convert JPA entity to domain model.
     * @param entity the JPA entity
     * @return the domain model
     */
    public Inventory toDomain(InventoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Inventory(
                entity.getId(),
                entity.getProductId(),
                entity.getProductName(),
                entity.getQuantity(),
                entity.getMinStockLevel(),
                entity.getMaxStockLevel(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
