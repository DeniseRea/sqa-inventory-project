package com.dongato.inventory.interfaces.rest.mapper;

import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.interfaces.rest.dto.ProductCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.ProductResponseDTO;
import com.dongato.inventory.interfaces.rest.dto.ProductUpdateDTO;

/**
 * Maps between Product domain model and REST DTOs.
 * McCall Factor: Maintainability — separates API layer from domain.
 */
public final class ProductRestMapper {

    private ProductRestMapper() {
        // Utility class
    }

    public static Product toDomain(ProductCreateDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock() != null ? dto.getStock() : 0)
                .categoryId(dto.getCategoryId())
                .build();
    }

    public static Product toDomain(ProductUpdateDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .categoryId(dto.getCategoryId())
                .build();
    }

    public static ProductResponseDTO toResponseDto(Product domain) {
        return ProductResponseDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .price(domain.getPrice())
                .stock(domain.getStock())
                .status(domain.getStatus())
                .categoryId(domain.getCategoryId())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
