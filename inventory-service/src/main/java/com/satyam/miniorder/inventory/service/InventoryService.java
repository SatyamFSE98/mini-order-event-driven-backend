package com.satyam.miniorder.inventory.service;

import com.satyam.miniorder.inventory.dto.InventoryReservedEvent;
import com.satyam.miniorder.inventory.dto.OrderCreatedEvent;
import com.satyam.miniorder.inventory.dto.ReserveInventoryRequest;
import com.satyam.miniorder.inventory.entity.InventoryReservation;
import com.satyam.miniorder.inventory.repository.InventoryReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InventoryService {

    private final InventoryReservationRepository inventoryReservationRepository;

    public InventoryService(InventoryReservationRepository inventoryReservationRepository) {
        this.inventoryReservationRepository = inventoryReservationRepository;
    }

    public InventoryReservedEvent reserveInventory(ReserveInventoryRequest request) {
        String status = request.quantity() <= 5 ? "RESERVED" : "FAILED";
        LocalDateTime now = LocalDateTime.now();

        InventoryReservation reservation = new InventoryReservation();
        reservation.setOrderId(request.orderId());
        reservation.setCustomerName(request.customerName());
        reservation.setEmail(request.email());
        reservation.setPhone(request.phone());
        reservation.setProductName(request.productName());
        reservation.setQuantity(request.quantity());
        reservation.setInventoryStatus(status);
        reservation.setCreatedAt(now);
        inventoryReservationRepository.save(reservation);

        InventoryReservedEvent event = new InventoryReservedEvent(
                request.orderId(),
                request.customerName(),
                request.email(),
                request.phone(),
                request.productName(),
                request.quantity(),
                status,
                "INVENTORY_RESERVED",
                now
        );

        System.out.println("Inventory processed for orderId=" + request.orderId() + " with status=" + status);

        return event;
    }

    public InventoryReservedEvent reserveInventoryFromOrderEvent(OrderCreatedEvent event) {
        ReserveInventoryRequest request = new ReserveInventoryRequest(
                event.orderId(),
                event.productName(),
                event.quantity(),
                event.customerName(),
                event.email(),
                event.phone()
        );
        return reserveInventory(request);
    }
}
