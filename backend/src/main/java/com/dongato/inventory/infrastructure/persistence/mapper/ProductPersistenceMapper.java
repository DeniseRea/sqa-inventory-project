package com.dongato.inventory.infrastructure.persistence.mapper;

import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.infrastructure.persistence.entity.ProductEntity;

/**
 * Maps between Product domain model and ProductEntity JPA entity.
 * <p>
 * McCall Factor: Maintainability — decouples domain representation from ORM.
 */
public final class ProductPersistenceMapper {

    private ProductPersistenceMapper() {
        // Utility class — prevent instantiation
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .status(entity.getStatus())
                .categoryId(entity.getCategoryId())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static ProductEntity toEntity(Product domain) {
        if (domain == null) return null;
        return ProductEntity.builder()
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
