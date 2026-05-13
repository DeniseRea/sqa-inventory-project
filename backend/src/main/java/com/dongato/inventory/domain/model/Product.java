package com.dongato.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model for Product — the heart of the inventory.
 * <p>
 * Contains business logic for stock management and automatic status calculation.
 * McCall Factor: Correctness — encapsulates invariants that prevent invalid states.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private ProductStatus status;
    private Long categoryId;
    private LocalDateTime createdAt;

    /**
     * Automatically recalculates the product status based on current stock level.
     * Stock <= 0 results in OUT_OF_STOCK, otherwise AVAILABLE.
     */
    public void recalculateStatus() {
        this.status = (this.stock != null && this.stock > 0)
                ? ProductStatus.AVAILABLE
                : ProductStatus.OUT_OF_STOCK;
    }

    /**
     * Applies a stock entry (adds quantity).
     *
     * @param quantity the quantity to add (must be positive)
     * @throws IllegalArgumentException if quantity is not positive
     */
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Entry quantity must be positive");
        }
        this.stock = (this.stock == null ? 0 : this.stock) + quantity;
        recalculateStatus();
    }

    /**
     * Applies a stock exit (subtracts quantity).
     *
     * @param quantity the quantity to subtract (must be positive)
     * @throws IllegalArgumentException if quantity is not positive
     * @throws IllegalStateException    if insufficient stock
     */
    public void removeStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Exit quantity must be positive");
        }
        int currentStock = (this.stock == null) ? 0 : this.stock;
        if (currentStock < quantity) {
            throw new IllegalStateException(
                    String.format("Insufficient stock: available=%d, requested=%d", currentStock, quantity));
        }
        this.stock = currentStock - quantity;
        recalculateStatus();
    }

    /**
     * Validates basic product invariants.
     */
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price must be non-negative");
        }
        if (stock != null && stock < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
    }
}
