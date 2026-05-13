package com.dongato.inventory.domain.model;

/**
 * Type of stock movement.
 * <p>
 * McCall Factor: Integrity — ensures auditability of inventory changes.
 */
public enum MovementType {
    ENTRADA,
    SALIDA
}
