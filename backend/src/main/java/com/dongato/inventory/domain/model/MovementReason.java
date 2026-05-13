package com.dongato.inventory.domain.model;

/**
 * Reason for a stock movement.
 * <p>
 * McCall Factor: Integrity — provides context for audit trail.
 */
public enum MovementReason {
    VENTA,
    REPOSICION,
    AJUSTE,
    DEVOLUCION,
    MERMA
}
