package com.dongato.inventory.domain.exception;

/**
 * Thrown when a business rule is violated.
 * McCall Factor: Correctness — enforces business invariants at domain level.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
