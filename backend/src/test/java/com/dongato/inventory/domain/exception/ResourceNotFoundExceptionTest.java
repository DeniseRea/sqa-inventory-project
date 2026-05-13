package com.dongato.inventory.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Debe construir mensaje con recurso e identificador")
    void shouldBuildMessageFromResourceAndId() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", 10L);

        assertEquals("Product not found with id: 10", ex.getMessage());
    }

    @Test
    @DisplayName("Debe conservar mensaje personalizado")
    void shouldKeepCustomMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Category not found");

        assertEquals("Category not found", ex.getMessage());
    }
}
