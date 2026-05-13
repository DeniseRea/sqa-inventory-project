package com.dongato.inventory.domain.exception;

/**
 * Thrown when there is insufficient stock for an operation.
 * McCall Factor: Correctness — prevents inventory going negative.
 */
public class InsufficientStockException extends BusinessRuleException {

    public InsufficientStockException(String productName, int available, int requested) {
        super(String.format("Insufficient stock for '%s': available=%d, requested=%d",
                productName, available, requested));
    }
}
