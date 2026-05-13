package com.dongato.inventory.interfaces.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a product.
 * McCall Factor: Correctness — strict input validation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price must be non-negative")
    private BigDecimal price;

    @Min(value = 0, message = "Initial stock must be non-negative")
    private Integer stock;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
