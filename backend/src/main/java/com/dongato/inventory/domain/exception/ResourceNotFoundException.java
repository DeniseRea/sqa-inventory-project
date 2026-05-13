package com.dongato.inventory.domain.exception;

/**
 * Thrown when a requested resource is not found.
 * McCall Factor: Correctness — clear error semantics.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
