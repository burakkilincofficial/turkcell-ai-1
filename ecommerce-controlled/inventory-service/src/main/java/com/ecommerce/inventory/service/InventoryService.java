package com.ecommerce.inventory.service;

import com.ecommerce.inventory.domain.Inventory;
import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.PageResponse;
import com.ecommerce.inventory.exception.InventoryNotFoundException;
import com.ecommerce.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public PageResponse<InventoryResponse> getAllInventory(int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        Page<Inventory> inventoryPage = inventoryRepository.findAll(pageable);

        PageResponse<InventoryResponse> response = new PageResponse<>();
        response.setContent(inventoryPage.getContent().stream()
                .map(this::mapToResponse)
                .toList());
        response.setTotalElements((int) inventoryPage.getTotalElements());
        response.setTotalPages(inventoryPage.getTotalPages());
        response.setCurrentPage(inventoryPage.getNumber());

        return response;
    }

    public InventoryResponse getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));
        return mapToResponse(inventory);
    }

    public InventoryResponse getInventoryByProductId(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with productId: " + productId));
        return mapToResponse(inventory);
    }

    public InventoryResponse createInventory(InventoryRequest request) {
        Inventory inventory = new Inventory();
        inventory.setProductId(request.getProductId());
        inventory.setProductName(request.getProductName());
        inventory.setQuantity(request.getQuantity());
        inventory.setMinStockLevel(request.getMinStockLevel());
        inventory.setMaxStockLevel(request.getMaxStockLevel());
        inventory.setLocation(request.getLocation());
        inventory.setDescription(request.getDescription());

        Inventory saved = inventoryRepository.save(inventory);
        return mapToResponse(saved);
    }

    public InventoryResponse updateInventory(Long id, InventoryRequest request) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));

        inventory.setProductId(request.getProductId());
        inventory.setProductName(request.getProductName());
        inventory.setQuantity(request.getQuantity());
        inventory.setMinStockLevel(request.getMinStockLevel());
        inventory.setMaxStockLevel(request.getMaxStockLevel());
        inventory.setLocation(request.getLocation());
        inventory.setDescription(request.getDescription());

        Inventory updated = inventoryRepository.save(inventory);
        return mapToResponse(updated);
    }

    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new InventoryNotFoundException("Inventory not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    public InventoryResponse updateStock(Long id, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));

        inventory.setQuantity(inventory.getQuantity() + quantity);

        Inventory updated = inventoryRepository.save(inventory);
        return mapToResponse(updated);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setProductId(inventory.getProductId());
        response.setProductName(inventory.getProductName());
        response.setQuantity(inventory.getQuantity());
        response.setMinStockLevel(inventory.getMinStockLevel());
        response.setMaxStockLevel(inventory.getMaxStockLevel());
        response.setLocation(inventory.getLocation());
        response.setDescription(inventory.getDescription());
        response.setCreatedAt(inventory.getCreatedAt());
        response.setUpdatedAt(inventory.getUpdatedAt());
        return response;
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
