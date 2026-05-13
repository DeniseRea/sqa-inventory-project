package com.dongato.inventory.application.usecases;

import com.dongato.inventory.application.ports.out.ProductRepositoryPort;
import com.dongato.inventory.application.ports.out.StockMovementRepositoryPort;
import com.dongato.inventory.domain.exception.InsufficientStockException;
import com.dongato.inventory.domain.exception.ResourceNotFoundException;
import com.dongato.inventory.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StockMovementUseCase.
 * McCall Factor: Testability — validates stock movement logic in isolation.
 */
@ExtendWith(MockitoExtension.class)
class StockMovementUseCaseTest {

    @Mock
    private StockMovementRepositoryPort movementRepository;

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private StockMovementUseCase stockMovementUseCase;

    @Test
    @DisplayName("Should register ENTRADA and increase product stock")
    void shouldRegisterEntradaSuccessfully() {
        Product product = Product.builder()
                .id(1L).name("Americano").price(new BigDecimal("2.50"))
                .stock(10).status(ProductStatus.AVAILABLE).build();
        StockMovement movement = StockMovement.builder()
                .productId(1L).type(MovementType.ENTRADA)
                .quantity(5).reason(MovementReason.REPOSICION).build();
        StockMovement saved = StockMovement.builder()
                .id(1L).productId(1L).type(MovementType.ENTRADA)
                .quantity(5).reason(MovementReason.REPOSICION).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(StockMovement.class))).thenReturn(saved);

        StockMovement result = stockMovementUseCase.registerMovement(movement);

        assertNotNull(result);
        assertEquals(15, product.getStock());
        assertEquals(ProductStatus.AVAILABLE, product.getStatus());
        verify(productRepository).save(product);
        verify(movementRepository).save(any(StockMovement.class));
    }

    @Test
    @DisplayName("Should register SALIDA and decrease product stock")
    void shouldRegisterSalidaSuccessfully() {
        Product product = Product.builder()
                .id(1L).name("Latte").price(new BigDecimal("3.75"))
                .stock(10).status(ProductStatus.AVAILABLE).build();
        StockMovement movement = StockMovement.builder()
                .productId(1L).type(MovementType.SALIDA)
                .quantity(3).reason(MovementReason.VENTA).build();
        StockMovement saved = StockMovement.builder()
                .id(2L).productId(1L).type(MovementType.SALIDA)
                .quantity(3).reason(MovementReason.VENTA).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(StockMovement.class))).thenReturn(saved);

        StockMovement result = stockMovementUseCase.registerMovement(movement);

        assertNotNull(result);
        assertEquals(7, product.getStock());
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Should throw InsufficientStockException on SALIDA with insufficient stock")
    void shouldThrowOnInsufficientStock() {
        Product product = Product.builder()
                .id(1L).name("Espresso").price(new BigDecimal("2.00"))
                .stock(2).status(ProductStatus.AVAILABLE).build();
        StockMovement movement = StockMovement.builder()
                .productId(1L).type(MovementType.SALIDA)
                .quantity(5).reason(MovementReason.VENTA).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class,
                () -> stockMovementUseCase.registerMovement(movement));
        verify(productRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when product not found")
    void shouldThrowWhenProductNotFound() {
        StockMovement movement = StockMovement.builder()
                .productId(99L).type(MovementType.ENTRADA)
                .quantity(5).reason(MovementReason.REPOSICION).build();

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> stockMovementUseCase.registerMovement(movement));
    }

    @Test
    @DisplayName("Should set status to OUT_OF_STOCK when stock reaches zero")
    void shouldSetOutOfStockWhenZero() {
        Product product = Product.builder()
                .id(1L).name("Mocha").price(new BigDecimal("4.00"))
                .stock(5).status(ProductStatus.AVAILABLE).build();
        StockMovement movement = StockMovement.builder()
                .productId(1L).type(MovementType.SALIDA)
                .quantity(5).reason(MovementReason.VENTA).build();
        StockMovement saved = StockMovement.builder()
                .id(3L).productId(1L).type(MovementType.SALIDA)
                .quantity(5).reason(MovementReason.VENTA).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(StockMovement.class))).thenReturn(saved);

        stockMovementUseCase.registerMovement(movement);

        assertEquals(0, product.getStock());
        assertEquals(ProductStatus.OUT_OF_STOCK, product.getStatus());
    }
}
