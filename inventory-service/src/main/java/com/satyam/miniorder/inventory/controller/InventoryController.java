package com.satyam.miniorder.inventory.controller;

import com.satyam.miniorder.inventory.dto.ApiResponse;
import com.satyam.miniorder.inventory.dto.InventoryReservedEvent;
import com.satyam.miniorder.inventory.dto.ReserveInventoryRequest;
import com.satyam.miniorder.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<InventoryReservedEvent>> reserveInventoryManually(
            @Valid @RequestBody ReserveInventoryRequest request) {
        InventoryReservedEvent response = inventoryService.reserveInventory(request);
        return ResponseEntity.ok(ApiResponse.success("Inventory reserved manually. Kafka flow is also enabled through consumer/producer.", response));
    }
}
