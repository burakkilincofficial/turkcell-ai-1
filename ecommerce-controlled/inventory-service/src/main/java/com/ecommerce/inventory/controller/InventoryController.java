package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.PageResponse;
import com.ecommerce.inventory.dto.StockUpdateRequest;
import com.ecommerce.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory CRUD işlemleri")
public class InventoryController {

    private final InventoryService inventoryService;

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
        PageResponse<InventoryResponse> response = inventoryService.getAllInventory(page, size, sort);
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
        InventoryResponse response = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(response);
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
        InventoryResponse response = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(response);
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
        InventoryResponse response = inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
        InventoryResponse response = inventoryService.updateInventory(id, request);
        return ResponseEntity.ok(response);
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
        inventoryService.deleteInventory(id);
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
        InventoryResponse response = inventoryService.updateStock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }
}
