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
import java.util.List;
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

    // ─── helpers ─────────────────────────────────────────────────────────────

    private Product productWithStock(int stock) {
        return Product.builder()
                .id(1L).name("Americano")
                .price(new BigDecimal("2.50"))
                .stock(stock)
                .status(stock > 0 ? ProductStatus.AVAILABLE : ProductStatus.OUT_OF_STOCK)
                .build();
    }

    private StockMovement entradaMovement(int qty) {
        return StockMovement.builder()
                .productId(1L).type(MovementType.ENTRADA)
                .quantity(qty).reason(MovementReason.REPOSICION).build();
    }

    private StockMovement salidaMovement(int qty) {
        return StockMovement.builder()
                .productId(1L).type(MovementType.SALIDA)
                .quantity(qty).reason(MovementReason.VENTA).build();
    }

    // ─── registerMovement ENTRADA ────────────────────────────────────────────

    @Test
    @DisplayName("Should register ENTRADA and increase product stock")
    void shouldRegisterEntradaSuccessfully() {
        Product product = productWithStock(10);
        StockMovement movement = entradaMovement(5);
        StockMovement saved = StockMovement.builder().id(1L).productId(1L)
                .type(MovementType.ENTRADA).quantity(5).reason(MovementReason.REPOSICION).build();

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
    @DisplayName("Should register ENTRADA on a product with zero stock and make it AVAILABLE")
    void shouldRegisterEntradaOnZeroStock() {
        Product product = productWithStock(0);
        StockMovement movement = entradaMovement(10);
        StockMovement saved = StockMovement.builder().id(2L).productId(1L)
                .type(MovementType.ENTRADA).quantity(10).reason(MovementReason.REPOSICION).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(StockMovement.class))).thenReturn(saved);

        stockMovementUseCase.registerMovement(movement);

        assertEquals(10, product.getStock());
        assertEquals(ProductStatus.AVAILABLE, product.getStatus());
    }

    // ─── registerMovement SALIDA ─────────────────────────────────────────────

    @Test
    @DisplayName("Should register SALIDA and decrease product stock")
    void shouldRegisterSalidaSuccessfully() {
        Product product = productWithStock(10);
        StockMovement movement = salidaMovement(3);
        StockMovement saved = StockMovement.builder().id(3L).productId(1L)
                .type(MovementType.SALIDA).quantity(3).reason(MovementReason.VENTA).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(StockMovement.class))).thenReturn(saved);

        StockMovement result = stockMovementUseCase.registerMovement(movement);

        assertNotNull(result);
        assertEquals(7, product.getStock());
        assertEquals(ProductStatus.AVAILABLE, product.getStatus());
    }

    @Test
    @DisplayName("Should set status to OUT_OF_STOCK when SALIDA drains all stock")
    void shouldSetOutOfStockWhenStockReachesZero() {
        Product product = productWithStock(5);
        StockMovement movement = salidaMovement(5);
        StockMovement saved = StockMovement.builder().id(4L).productId(1L)
                .type(MovementType.SALIDA).quantity(5).reason(MovementReason.VENTA).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(StockMovement.class))).thenReturn(saved);

        stockMovementUseCase.registerMovement(movement);

        assertEquals(0, product.getStock());
        assertEquals(ProductStatus.OUT_OF_STOCK, product.getStatus());
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when SALIDA exceeds available stock")
    void shouldThrowOnInsufficientStock() {
        Product product = productWithStock(2);
        StockMovement movement = salidaMovement(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class,
                () -> stockMovementUseCase.registerMovement(movement));
        verify(productRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    // ─── validation ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product not found on movement")
    void shouldThrowWhenProductNotFound() {
        StockMovement movement = entradaMovement(5);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> stockMovementUseCase.registerMovement(movement));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when movement quantity is zero")
    void shouldThrowWhenQuantityIsZero() {
        StockMovement movement = StockMovement.builder()
                .productId(1L).type(MovementType.ENTRADA)
                .quantity(0).reason(MovementReason.REPOSICION).build();

        assertThrows(IllegalArgumentException.class,
                () -> stockMovementUseCase.registerMovement(movement));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when movement type is null")
    void shouldThrowWhenMovementTypeIsNull() {
        StockMovement movement = StockMovement.builder()
                .productId(1L).type(null)
                .quantity(5).reason(MovementReason.REPOSICION).build();

        assertThrows(IllegalArgumentException.class,
                () -> stockMovementUseCase.registerMovement(movement));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when reason is null")
    void shouldThrowWhenReasonIsNull() {
        StockMovement movement = StockMovement.builder()
                .productId(1L).type(MovementType.ENTRADA)
                .quantity(5).reason(null).build();

        assertThrows(IllegalArgumentException.class,
                () -> stockMovementUseCase.registerMovement(movement));
    }

    // ─── findByProductId ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return movements for an existing product")
    void shouldFindMovementsByProductId() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(movementRepository.findByProductId(1L))
                .thenReturn(List.of(entradaMovement(5), salidaMovement(2)));

        List<StockMovement> result = stockMovementUseCase.findByProductId(1L);

        assertEquals(2, result.size());
        verify(movementRepository).findByProductId(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when finding movements for non-existent product")
    void shouldThrowWhenFindingMovementsForNonExistentProduct() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> stockMovementUseCase.findByProductId(99L));
        verify(movementRepository, never()).findByProductId(any());
    }

    // ─── findAll ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return all stock movements")
    void shouldFindAllMovements() {
        when(movementRepository.findAll())
                .thenReturn(List.of(entradaMovement(5), salidaMovement(3)));

        List<StockMovement> result = stockMovementUseCase.findAll();

        assertEquals(2, result.size());
    }
}
