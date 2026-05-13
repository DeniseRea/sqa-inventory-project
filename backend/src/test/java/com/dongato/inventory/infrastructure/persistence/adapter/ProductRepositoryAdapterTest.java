package com.dongato.inventory.infrastructure.persistence.adapter;

import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import com.dongato.inventory.infrastructure.persistence.entity.ProductEntity;
import com.dongato.inventory.infrastructure.persistence.repository.JpaProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private JpaProductRepository repository;

    @InjectMocks
    private ProductRepositoryAdapter adapter;

    private Product mockProduct() {
        return Product.builder()
                .id(1L).name("Test").price(new BigDecimal("10.00")).stock(5)
                .status(ProductStatus.AVAILABLE).categoryId(2L)
                .createdAt(LocalDateTime.now()).build();
    }

    private ProductEntity mockEntity() {
        return ProductEntity.builder()
                .id(1L).name("Test").price(new BigDecimal("10.00")).stock(5)
                .status(ProductStatus.AVAILABLE).categoryId(2L)
                .createdAt(LocalDateTime.now()).build();
    }

    @Test
    @DisplayName("Should save product")
    void shouldSaveProduct() {
        Product domain = mockProduct();
        ProductEntity entity = mockEntity();

        when(repository.save(any(ProductEntity.class))).thenReturn(entity);

        Product saved = adapter.save(domain);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(repository).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should find product by id")
    void shouldFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity()));

        Optional<Product> found = adapter.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    @DisplayName("Should find all products")
    void shouldFindAll() {
        when(repository.findAll()).thenReturn(List.of(mockEntity()));

        List<Product> products = adapter.findAll();

        assertEquals(1, products.size());
    }

    @Test
    @DisplayName("Should return true if product exists by id")
    void shouldReturnTrueIfExistsById() {
        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(adapter.existsById(1L));
    }

    @Test
    @DisplayName("Should delete product by id")
    void shouldDeleteById() {
        doNothing().when(repository).deleteById(1L);

        adapter.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Should find products by category id")
    void shouldFindByCategoryId() {
        when(repository.findByCategoryId(2L)).thenReturn(List.of(mockEntity()));

        List<Product> products = adapter.findByCategoryId(2L);

        assertEquals(1, products.size());
    }

    @Test
    @DisplayName("Should find products by status")
    void shouldFindByStatus() {
        when(repository.findByStatus(ProductStatus.AVAILABLE)).thenReturn(List.of(mockEntity()));

        List<Product> products = adapter.findByStatus(ProductStatus.AVAILABLE);

        assertEquals(1, products.size());
    }

    @Test
    @DisplayName("Should find products by name containing ignore case")
    void shouldFindByNameContainingIgnoreCase() {
        when(repository.findByNameContainingIgnoreCase("Test")).thenReturn(List.of(mockEntity()));

        List<Product> products = adapter.findByNameContainingIgnoreCase("Test");

        assertEquals(1, products.size());
    }
}
