package com.dongato.inventory.domain.model;

/**
 * Represents the status of a product in the inventory.
 * <p>
 * McCall Factor: Correctness — ensures valid product states.
 */
public enum ProductStatus {
    AVAILABLE,
    OUT_OF_STOCK
}
