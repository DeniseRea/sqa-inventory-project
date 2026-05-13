package com.dongato.inventory.application.usecases;

import com.dongato.inventory.application.ports.out.CategoryRepositoryPort;
import com.dongato.inventory.application.ports.out.ProductRepositoryPort;
import com.dongato.inventory.domain.exception.ResourceNotFoundException;
import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductUseCase.
 * McCall Factor: Testability — isolates business logic via Mockito mocks.
 */
@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private CategoryRepositoryPort categoryRepository;

    @InjectMocks
    private ProductUseCase productUseCase;

    // ─── helpers ─────────────────────────────────────────────────────────────

    private Product validProduct() {
        return Product.builder()
                .name("Americano")
                .price(new BigDecimal("2.50"))
                .stock(10)
                .categoryId(1L)
                .build();
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should create product successfully when category exists")
    void shouldCreateProductSuccessfully() {
        Product input = validProduct();
        Product saved = Product.builder()
                .id(1L).name("Americano")
                .price(new BigDecimal("2.50")).stock(10)
                .categoryId(1L).status(ProductStatus.AVAILABLE).build();

        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productUseCase.create(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Americano", result.getName());
        assertEquals(ProductStatus.AVAILABLE, result.getStatus());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should default stock to 0 when null on create")
    void shouldDefaultStockToZeroWhenNull() {
        Product input = Product.builder()
                .name("Cappuccino").price(new BigDecimal("3.00")).categoryId(1L).build();
        Product saved = Product.builder()
                .id(2L).name("Cappuccino").price(new BigDecimal("3.00"))
                .stock(0).status(ProductStatus.OUT_OF_STOCK).categoryId(1L).build();

        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productUseCase.create(input);

        assertEquals(0, input.getStock()); // defaulted before save
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category does not exist on create")
    void shouldThrowWhenCategoryNotFoundOnCreate() {
        Product input = validProduct();
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productUseCase.create(input));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when product name is blank")
    void shouldThrowWhenNameIsBlank() {
        Product input = Product.builder()
                .name("").price(new BigDecimal("2.00")).categoryId(1L).build();

        assertThrows(IllegalArgumentException.class, () -> productUseCase.create(input));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when price is negative")
    void shouldThrowWhenPriceIsNegative() {
        Product input = Product.builder()
                .name("Espresso").price(new BigDecimal("-1.00")).categoryId(1L).build();

        assertThrows(IllegalArgumentException.class, () -> productUseCase.create(input));
    }

    // ─── findById ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return product when found by ID")
    void shouldFindById() {
        Product expected = Product.builder().id(1L).name("Latte")
                .price(new BigDecimal("3.75")).stock(5)
                .status(ProductStatus.AVAILABLE).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(expected));

        Product result = productUseCase.findById(1L);

        assertEquals("Latte", result.getName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product not found by ID")
    void shouldThrowWhenProductNotFoundById() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productUseCase.findById(99L));
    }

    // ─── findAll ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return all products")
    void shouldFindAll() {
        when(productRepository.findAll()).thenReturn(List.of(validProduct(), validProduct()));

        List<Product> result = productUseCase.findAll();

        assertEquals(2, result.size());
    }

    // ─── findByCategoryId ────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return products by category ID when category exists")
    void shouldFindByCategoryId() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(validProduct()));

        List<Product> result = productUseCase.findByCategoryId(1L);

        assertEquals(1, result.size());
        verify(productRepository).findByCategoryId(1L);
    }

    @Test
    @DisplayName("Should throw when finding products for non-existent category")
    void shouldThrowWhenCategoryNotFoundForSearch() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productUseCase.findByCategoryId(99L));
    }

    // ─── findByStatus ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return products by AVAILABLE status")
    void shouldFindByStatus() {
        when(productRepository.findByStatus(ProductStatus.AVAILABLE))
                .thenReturn(List.of(validProduct()));

        List<Product> result = productUseCase.findByStatus(ProductStatus.AVAILABLE);

        assertEquals(1, result.size());
    }

    // ─── searchByName ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return products matching name search")
    void shouldSearchByName() {
        when(productRepository.findByNameContainingIgnoreCase("amer"))
                .thenReturn(List.of(validProduct()));

        List<Product> result = productUseCase.searchByName("amer");

        assertEquals(1, result.size());
        verify(productRepository).findByNameContainingIgnoreCase("amer");
    }

    // ─── update ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should update product name and price successfully")
    void shouldUpdateProductSuccessfully() {
        Product existing = Product.builder()
                .id(1L).name("Americano").price(new BigDecimal("2.50"))
                .stock(5).categoryId(1L).status(ProductStatus.AVAILABLE).build();
        Product updatePayload = Product.builder()
                .name("Americano XL").price(new BigDecimal("3.00")).categoryId(1L).build();
        Product saved = Product.builder()
                .id(1L).name("Americano XL").price(new BigDecimal("3.00"))
                .stock(5).categoryId(1L).status(ProductStatus.AVAILABLE).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productUseCase.update(1L, updatePayload);

        assertEquals("Americano XL", result.getName());
        assertEquals(new BigDecimal("3.00"), result.getPrice());
        verify(productRepository).save(existing);
    }

    @Test
    @DisplayName("Should throw when updating non-existent product")
    void shouldThrowWhenUpdatingNonExistentProduct() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        Product updatePayload = Product.builder()
                .name("X").price(BigDecimal.ONE).build();

        assertThrows(ResourceNotFoundException.class,
                () -> productUseCase.update(99L, updatePayload));
    }

    // ─── delete ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should delete product successfully when it exists")
    void shouldDeleteProductSuccessfully() {
        when(productRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> productUseCase.delete(1L));
        verify(productRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent product")
    void shouldThrowWhenDeletingNonExistentProduct() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productUseCase.delete(99L));
        verify(productRepository, never()).deleteById(any());
    }
}
