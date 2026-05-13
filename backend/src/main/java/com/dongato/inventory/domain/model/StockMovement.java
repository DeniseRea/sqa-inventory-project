package com.dongato.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain model for StockMovement — audit trail for inventory changes.
 * <p>
 * Records every stock change with type (ENTRADA/SALIDA) and reason.
 * McCall Factor: Integrity — enables full traceability of inventory operations.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovement {

    private Long id;
    private Long productId;
    private MovementType type;
    private Integer quantity;
    private MovementReason reason;
    private LocalDateTime createdAt;

    /**
     * Validates that the movement has coherent data.
     */
    public void validate() {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required for stock movement");
        }
        if (type == null) {
            throw new IllegalArgumentException("Movement type is required");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Movement quantity must be positive");
        }
        if (reason == null) {
            throw new IllegalArgumentException("Movement reason is required");
        }
    }
}
