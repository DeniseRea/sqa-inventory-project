package com.dongato.inventory.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Category domain model.
 * McCall Factor: Testability — validates domain invariants.
 */
class CategoryTest {

    @Test
    @DisplayName("Should validate successfully with valid data")
    void shouldValidateSuccessfully() {
        Category category = Category.builder()
                .name("CAFE")
                .description("Bebidas de café")
                .build();

        assertDoesNotThrow(category::validate);
    }

    @Test
    @DisplayName("Should throw on null name")
    void shouldThrowOnNullName() {
        Category category = Category.builder()
                .name(null)
                .build();

        assertThrows(IllegalArgumentException.class, category::validate);
    }

    @Test
    @DisplayName("Should throw on blank name")
    void shouldThrowOnBlankName() {
        Category category = Category.builder()
                .name("   ")
                .build();

        assertThrows(IllegalArgumentException.class, category::validate);
    }
}
