package com.satyam.miniorder.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReserveInventoryRequest(
        @NotNull(message = "Order id is required")
        Long orderId,

        @NotBlank(message = "Product name is required")
        String productName,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        String customerName,

        @Email(message = "Email should be valid")
        String email,

        String phone
) {
}
