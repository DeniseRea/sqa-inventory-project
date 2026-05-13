package com.dongato.inventory.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Should format message with resource name and id")
    void shouldFormatMessageWithResourceNameAndId() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", 42L);

        assertEquals("Product not found with id: 42", ex.getMessage());
    }

    @Test
    @DisplayName("Should accept custom message")
    void shouldAcceptCustomMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Custom message");

        assertEquals("Custom message", ex.getMessage());
    }
}
