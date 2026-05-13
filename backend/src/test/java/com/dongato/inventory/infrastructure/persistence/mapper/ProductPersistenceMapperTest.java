package com.dongato.inventory.infrastructure.persistence.mapper;

import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import com.dongato.inventory.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductPersistenceMapperTest {

    @Test
    @DisplayName("Should map entity to domain")
    void shouldMapToDomain() {
        ProductEntity entity = ProductEntity.builder()
                .id(1L).name("Test").price(new BigDecimal("10.00")).stock(5)
                .status(ProductStatus.AVAILABLE).categoryId(2L)
                .createdAt(LocalDateTime.now()).build();

        Product domain = ProductPersistenceMapper.toDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(entity.getPrice(), domain.getPrice());
        assertEquals(entity.getStock(), domain.getStock());
        assertEquals(entity.getStatus(), domain.getStatus());
        assertEquals(entity.getCategoryId(), domain.getCategoryId());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle null entity")
    void shouldHandleNullEntity() {
        assertNull(ProductPersistenceMapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map domain to entity")
    void shouldMapToEntity() {
        Product domain = Product.builder()
                .id(1L).name("Test").price(new BigDecimal("10.00")).stock(5)
                .status(ProductStatus.AVAILABLE).categoryId(2L)
                .createdAt(LocalDateTime.now()).build();

        ProductEntity entity = ProductPersistenceMapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getName(), entity.getName());
        assertEquals(domain.getPrice(), entity.getPrice());
        assertEquals(domain.getStock(), entity.getStock());
        assertEquals(domain.getStatus(), entity.getStatus());
        assertEquals(domain.getCategoryId(), entity.getCategoryId());
        assertEquals(domain.getCreatedAt(), entity.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle null domain")
    void shouldHandleNullDomain() {
        assertNull(ProductPersistenceMapper.toEntity(null));
    }
}
