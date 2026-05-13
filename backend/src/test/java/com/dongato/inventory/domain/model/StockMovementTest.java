package com.dongato.inventory.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StockMovement domain model.
 * McCall Factor: Testability — validates audit data integrity.
 */
class StockMovementTest {

    @Test
    @DisplayName("Should validate successfully with valid data")
    void shouldValidateSuccessfully() {
        StockMovement movement = StockMovement.builder()
                .productId(1L)
                .type(MovementType.ENTRADA)
                .quantity(10)
                .reason(MovementReason.REPOSICION)
                .build();

        assertDoesNotThrow(movement::validate);
    }

    @Test
    @DisplayName("Should throw on null product ID")
    void shouldThrowOnNullProductId() {
        StockMovement movement = StockMovement.builder()
                .productId(null)
                .type(MovementType.ENTRADA)
                .quantity(10)
                .reason(MovementReason.REPOSICION)
                .build();

        assertThrows(IllegalArgumentException.class, movement::validate);
    }

    @Test
    @DisplayName("Should throw on null type")
    void shouldThrowOnNullType() {
        StockMovement movement = StockMovement.builder()
                .productId(1L)
                .type(null)
                .quantity(10)
                .reason(MovementReason.VENTA)
                .build();

        assertThrows(IllegalArgumentException.class, movement::validate);
    }

    @Test
    @DisplayName("Should throw on zero quantity")
    void shouldThrowOnZeroQuantity() {
        StockMovement movement = StockMovement.builder()
                .productId(1L)
                .type(MovementType.SALIDA)
                .quantity(0)
                .reason(MovementReason.VENTA)
                .build();

        assertThrows(IllegalArgumentException.class, movement::validate);
    }

    @Test
    @DisplayName("Should throw on null quantity")
    void shouldThrowOnNullQuantity() {
        StockMovement movement = StockMovement.builder()
                .productId(1L)
                .type(MovementType.SALIDA)
                .quantity(null)
                .reason(MovementReason.VENTA)
                .build();

        assertThrows(IllegalArgumentException.class, movement::validate);
    }

    @Test
    @DisplayName("Should throw on negative quantity")
    void shouldThrowOnNegativeQuantity() {
        StockMovement movement = StockMovement.builder()
                .productId(1L)
                .type(MovementType.SALIDA)
                .quantity(-2)
                .reason(MovementReason.VENTA)
                .build();

        assertThrows(IllegalArgumentException.class, movement::validate);
    }

    @Test
    @DisplayName("Should throw on null reason")
    void shouldThrowOnNullReason() {
        StockMovement movement = StockMovement.builder()
                .productId(1L)
                .type(MovementType.ENTRADA)
                .quantity(10)
                .reason(null)
                .build();

        assertThrows(IllegalArgumentException.class, movement::validate);
    }
}
