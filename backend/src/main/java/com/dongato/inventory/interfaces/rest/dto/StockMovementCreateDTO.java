package com.dongato.inventory.interfaces.rest.dto;

import com.dongato.inventory.domain.model.MovementReason;
import com.dongato.inventory.domain.model.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a stock movement.
 * McCall Factor: Integrity — validates audit data at boundary.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementCreateDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Movement type is required (ENTRADA or SALIDA)")
    private MovementType type;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Movement reason is required")
    private MovementReason reason;
}
