package com.dongato.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain model for Category.
 * <p>
 * Represents a product grouping (e.g., CAFE, BEBIDA, SNACK).
 * McCall Factor: Correctness — pure business rules without framework coupling.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    /**
     * Validates that the category has a non-blank name.
     *
     * @throws IllegalArgumentException if the name is blank
     */
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
    }
}
