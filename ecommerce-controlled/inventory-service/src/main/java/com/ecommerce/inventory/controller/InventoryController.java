package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.application.usecase.*;
import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.PageResponse;
import com.ecommerce.inventory.dto.StockUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for inventory operations.
 * Web layer - handles HTTP requests/responses, delegates to application use-cases.
 */
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory CRUD işlemleri")
public class InventoryController {

    private final GetAllInventoryUseCase getAllInventoryUseCase;
    private final GetInventoryByIdUseCase getInventoryByIdUseCase;
    private final GetInventoryByProductIdUseCase getInventoryByProductIdUseCase;
    private final CreateInventoryUseCase createInventoryUseCase;
    private final UpdateInventoryUseCase updateInventoryUseCase;
    private final DeleteInventoryUseCase deleteInventoryUseCase;
    private final UpdateStockUseCase updateStockUseCase;

    @GetMapping
    @Operation(summary = "Tüm inventory kayıtlarını listele", description = "Sistemdeki tüm inventory kayıtlarını getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarılı işlem"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<PageResponse<InventoryResponse>> getAllInventory(
            @Parameter(description = "Sayfa numarası") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sayfa başına kayıt sayısı") 
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sıralama kriteri (örn. productName,asc)") 
            @RequestParam(required = false) String sort) {
        
        Page<Inventory> inventoryPage = getAllInventoryUseCase.execute(page, size, sort);
        
        PageResponse<InventoryResponse> response = new PageResponse<>();
        response.setContent(inventoryPage.getContent().stream()
                .map(this::mapToResponse)
                .toList());
        response.setTotalElements((int) inventoryPage.getTotalElements());
        response.setTotalPages(inventoryPage.getTotalPages());
        response.setCurrentPage(inventoryPage.getNumber());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre inventory kaydını getir", description = "Belirtilen ID'ye sahip inventory kaydını getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarılı işlem"),
            @ApiResponse(responseCode = "404", description = "Inventory bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<InventoryResponse> getInventoryById(
            @Parameter(description = "Inventory ID") 
            @PathVariable Long id) {
        Inventory inventory = getInventoryByIdUseCase.execute(id);
        return ResponseEntity.ok(mapToResponse(inventory));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Ürün ID'sine göre inventory bilgisini getir", description = "Belirtilen ürün ID'sine sahip inventory bilgisini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarılı işlem"),
            @ApiResponse(responseCode = "404", description = "Inventory bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<InventoryResponse> getInventoryByProductId(
            @Parameter(description = "Ürün ID") 
            @PathVariable String productId) {
        Inventory inventory = getInventoryByProductIdUseCase.execute(productId);
        return ResponseEntity.ok(mapToResponse(inventory));
    }

    @PostMapping
    @Operation(summary = "Yeni inventory kaydı oluştur", description = "Yeni bir inventory kaydı ekler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody InventoryRequest request) {
        
        Inventory inventory = createInventoryUseCase.execute(
                request.getProductId(),
                request.getProductName(),
                request.getQuantity(),
                request.getMinStockLevel(),
                request.getMaxStockLevel(),
                request.getLocation(),
                request.getDescription()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(inventory));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Inventory kaydını güncelle", description = "Belirtilen ID'ye sahip inventory kaydını günceller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory başarıyla güncellendi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "404", description = "Inventory bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<InventoryResponse> updateInventory(
            @Parameter(description = "Inventory ID") 
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequest request) {
        
        Inventory inventory = updateInventoryUseCase.execute(
                id,
                request.getProductId(),
                request.getProductName(),
                request.getQuantity(),
                request.getMinStockLevel(),
                request.getMaxStockLevel(),
                request.getLocation(),
                request.getDescription()
        );
        
        return ResponseEntity.ok(mapToResponse(inventory));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inventory kaydını sil", description = "Belirtilen ID'ye sahip inventory kaydını siler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventory başarıyla silindi"),
            @ApiResponse(responseCode = "404", description = "Inventory bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<Void> deleteInventory(
            @Parameter(description = "Inventory ID") 
            @PathVariable Long id) {
        deleteInventoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Stok miktarını güncelle", description = "Belirtilen inventory kaydının stok miktarını günceller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stok başarıyla güncellendi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "404", description = "Inventory bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<InventoryResponse> updateStock(
            @Parameter(description = "Inventory ID") 
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        Inventory inventory = updateStockUseCase.execute(id, request.getQuantity());
        return ResponseEntity.ok(mapToResponse(inventory));
    }

    // Mapping helper method
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
}
