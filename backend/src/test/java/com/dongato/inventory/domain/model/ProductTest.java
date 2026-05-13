package com.dongato.inventory.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Product domain model.
 * McCall Factor: Testability — validates business rules in isolation.
 */
class ProductTest {

    @Nested
    @DisplayName("Stock Management")
    class StockManagement {

        @Test
        @DisplayName("Should add stock and recalculate status to AVAILABLE")
        void shouldAddStockSuccessfully() {
            Product product = Product.builder()
                    .name("Americano")
                    .price(new BigDecimal("2.50"))
                    .stock(0)
                    .status(ProductStatus.OUT_OF_STOCK)
                    .build();

            product.addStock(10);

            assertEquals(10, product.getStock());
            assertEquals(ProductStatus.AVAILABLE, product.getStatus());
        }

        @Test
        @DisplayName("Should add stock from null stock value")
        void shouldAddStockFromNullStockValue() {
            Product product = Product.builder()
                    .name("Americano")
                    .price(new BigDecimal("2.50"))
                    .stock(null)
                    .status(ProductStatus.OUT_OF_STOCK)
                    .build();

            product.addStock(4);

            assertEquals(4, product.getStock());
            assertEquals(ProductStatus.AVAILABLE, product.getStatus());
        }

        @Test
        @DisplayName("Should remove stock and recalculate status")
        void shouldRemoveStockSuccessfully() {
            Product product = Product.builder()
                    .name("Cappuccino")
                    .price(new BigDecimal("3.50"))
                    .stock(5)
                    .status(ProductStatus.AVAILABLE)
                    .build();

            product.removeStock(5);

            assertEquals(0, product.getStock());
            assertEquals(ProductStatus.OUT_OF_STOCK, product.getStatus());
        }

        @Test
        @DisplayName("Should throw exception for insufficient stock")
        void shouldThrowOnInsufficientStock() {
            Product product = Product.builder()
                    .name("Latte")
                    .price(new BigDecimal("3.75"))
                    .stock(3)
                    .build();

            IllegalStateException ex = assertThrows(
                    IllegalStateException.class,
                    () -> product.removeStock(5));

            assertTrue(ex.getMessage().contains("Insufficient stock"));
        }

        @Test
        @DisplayName("Should throw exception for negative entry quantity")
        void shouldThrowOnNegativeEntryQuantity() {
            Product product = Product.builder()
                    .name("Espresso")
                    .price(new BigDecimal("2.00"))
                    .stock(10)
                    .build();

            assertThrows(IllegalArgumentException.class,
                    () -> product.addStock(-5));
        }

        @Test
        @DisplayName("Should throw exception for zero entry quantity")
        void shouldThrowOnZeroEntryQuantity() {
            Product product = Product.builder()
                    .name("Espresso")
                    .price(new BigDecimal("2.00"))
                    .stock(10)
                    .build();

            assertThrows(IllegalArgumentException.class,
                    () -> product.addStock(0));
        }

        @Test
        @DisplayName("Should throw exception for zero exit quantity")
        void shouldThrowOnZeroExitQuantity() {
            Product product = Product.builder()
                    .name("Mocha")
                    .price(new BigDecimal("4.00"))
                    .stock(10)
                    .build();

            assertThrows(IllegalArgumentException.class,
                    () -> product.removeStock(0));
        }

        @Test
        @DisplayName("Should throw exception for negative exit quantity")
        void shouldThrowOnNegativeExitQuantity() {
            Product product = Product.builder()
                    .name("Mocha")
                    .price(new BigDecimal("4.00"))
                    .stock(10)
                    .build();

            assertThrows(IllegalArgumentException.class,
                    () -> product.removeStock(-1));
        }

        @Test
        @DisplayName("Should treat null stock as zero when removing")
        void shouldTreatNullStockAsZeroWhenRemoving() {
            Product product = Product.builder()
                    .name("Mocha")
                    .price(new BigDecimal("4.00"))
                    .stock(null)
                    .build();

            IllegalStateException ex = assertThrows(
                    IllegalStateException.class,
                    () -> product.removeStock(1));

            assertTrue(ex.getMessage().contains("available=0"));
        }
    }

    @Nested
    @DisplayName("Status Recalculation")
    class StatusRecalculation {

        @Test
        @DisplayName("Should set AVAILABLE when stock > 0")
        void shouldBeAvailableWithPositiveStock() {
            Product product = Product.builder().stock(1).build();
            product.recalculateStatus();
            assertEquals(ProductStatus.AVAILABLE, product.getStatus());
        }

        @Test
        @DisplayName("Should set OUT_OF_STOCK when stock = 0")
        void shouldBeOutOfStockWhenZero() {
            Product product = Product.builder().stock(0).build();
            product.recalculateStatus();
            assertEquals(ProductStatus.OUT_OF_STOCK, product.getStatus());
        }

        @Test
        @DisplayName("Should set OUT_OF_STOCK when stock is null")
        void shouldBeOutOfStockWhenNull() {
            Product product = Product.builder().stock(null).build();
            product.recalculateStatus();
            assertEquals(ProductStatus.OUT_OF_STOCK, product.getStatus());
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should throw on blank name")
        void shouldThrowOnBlankName() {
            Product product = Product.builder()
                    .name("")
                    .price(new BigDecimal("2.50"))
                    .stock(0)
                    .build();

            assertThrows(IllegalArgumentException.class, product::validate);
        }

        @Test
        @DisplayName("Should throw on null name")
        void shouldThrowOnNullName() {
            Product product = Product.builder()
                    .name(null)
                    .price(new BigDecimal("2.50"))
                    .stock(0)
                    .build();

            assertThrows(IllegalArgumentException.class, product::validate);
        }

        @Test
        @DisplayName("Should throw on negative price")
        void shouldThrowOnNegativePrice() {
            Product product = Product.builder()
                    .name("Test")
                    .price(new BigDecimal("-1.00"))
                    .stock(0)
                    .build();

            assertThrows(IllegalArgumentException.class, product::validate);
        }

        @Test
        @DisplayName("Should throw on null price")
        void shouldThrowOnNullPrice() {
            Product product = Product.builder()
                    .name("Test")
                    .price(null)
                    .stock(0)
                    .build();

            assertThrows(IllegalArgumentException.class, product::validate);
        }

        @Test
        @DisplayName("Should throw on negative stock")
        void shouldThrowOnNegativeStock() {
            Product product = Product.builder()
                    .name("Test")
                    .price(new BigDecimal("2.50"))
                    .stock(-5)
                    .build();

            assertThrows(IllegalArgumentException.class, product::validate);
        }

        @Test
        @DisplayName("Should pass validation for valid product")
        void shouldPassValidation() {
            Product product = Product.builder()
                    .name("Americano")
                    .price(new BigDecimal("2.50"))
                    .stock(10)
                    .build();

            assertDoesNotThrow(product::validate);
        }

        @Test
        @DisplayName("Should pass validation when stock is null")
        void shouldPassValidationWhenStockIsNull() {
            Product product = Product.builder()
                    .name("Americano")
                    .price(new BigDecimal("2.50"))
                    .stock(null)
                    .build();

            assertDoesNotThrow(product::validate);
        }
    }
}
