package com.dongato.inventory.interfaces.rest.mapper;

import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import com.dongato.inventory.interfaces.rest.dto.ProductCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.ProductResponseDTO;
import com.dongato.inventory.interfaces.rest.dto.ProductUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductRestMapper.
 * McCall Factor: Maintainability — ensures correct field mapping between layers.
 */
class ProductRestMapperTest {

    @Test
    @DisplayName("Should map ProductCreateDTO to domain with all fields")
    void shouldMapCreateDtoToDomain() {
        ProductCreateDTO dto = ProductCreateDTO.builder()
                .name("Americano")
                .price(new BigDecimal("2.50"))
                .stock(10)
                .categoryId(1L)
                .build();

        Product product = ProductRestMapper.toDomain(dto);

        assertEquals("Americano", product.getName());
        assertEquals(new BigDecimal("2.50"), product.getPrice());
        assertEquals(10, product.getStock());
        assertEquals(1L, product.getCategoryId());
    }

    @Test
    @DisplayName("Should default stock to 0 when null in ProductCreateDTO")
    void shouldDefaultStockToZeroWhenNullInCreateDto() {
        ProductCreateDTO dto = ProductCreateDTO.builder()
                .name("Espresso")
                .price(new BigDecimal("1.50"))
                .stock(null)
                .categoryId(2L)
                .build();

        Product product = ProductRestMapper.toDomain(dto);

        assertEquals(0, product.getStock());
    }

    @Test
    @DisplayName("Should map ProductUpdateDTO to domain")
    void shouldMapUpdateDtoToDomain() {
        ProductUpdateDTO dto = ProductUpdateDTO.builder()
                .name("Latte XL")
                .price(new BigDecimal("4.00"))
                .categoryId(3L)
                .build();

        Product product = ProductRestMapper.toDomain(dto);

        assertEquals("Latte XL", product.getName());
        assertEquals(new BigDecimal("4.00"), product.getPrice());
        assertEquals(3L, product.getCategoryId());
    }

    @Test
    @DisplayName("Should map Product domain to ProductResponseDTO with all fields")
    void shouldMapDomainToResponseDto() {
        LocalDateTime now = LocalDateTime.now();
        Product domain = Product.builder()
                .id(1L).name("Mocha")
                .price(new BigDecimal("3.75")).stock(5)
                .status(ProductStatus.AVAILABLE).categoryId(2L)
                .createdAt(now).build();

        ProductResponseDTO dto = ProductRestMapper.toResponseDto(domain);

        assertEquals(1L, dto.getId());
        assertEquals("Mocha", dto.getName());
        assertEquals(new BigDecimal("3.75"), dto.getPrice());
        assertEquals(5, dto.getStock());
        assertEquals(ProductStatus.AVAILABLE, dto.getStatus());
        assertEquals(2L, dto.getCategoryId());
        assertEquals(now, dto.getCreatedAt());
    }
}
