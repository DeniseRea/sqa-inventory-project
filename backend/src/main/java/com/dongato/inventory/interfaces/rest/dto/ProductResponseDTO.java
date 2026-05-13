package com.dongato.inventory.interfaces.rest.dto;

import com.dongato.inventory.domain.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for product responses.
 * McCall Factor: Interoperability — standardized API contract.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private ProductStatus status;
    private Long categoryId;
    private LocalDateTime createdAt;
}
