package com.dongato.inventory.application.usecases;

import com.dongato.inventory.application.ports.out.ProductRepositoryPort;
import com.dongato.inventory.application.ports.out.StockMovementRepositoryPort;
import com.dongato.inventory.domain.exception.InsufficientStockException;
import com.dongato.inventory.domain.exception.ResourceNotFoundException;
import com.dongato.inventory.domain.model.MovementType;
import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.StockMovement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case for StockMovement management — the audit trail for all inventory changes.
 * <p>
 * Every stock modification MUST go through this use case to ensure traceability.
 * McCall Factors: Integrity (audit trail), Correctness (stock consistency).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StockMovementUseCase {

    private final StockMovementRepositoryPort movementRepository;
    private final ProductRepositoryPort productRepository;

    /**
     * Registers a stock movement and updates the product stock accordingly.
     * ENTRADA adds stock; SALIDA removes stock.
     *
     * @param movement the stock movement to register
     * @return the persisted stock movement
     */
    @Transactional
    public StockMovement registerMovement(StockMovement movement) {
        movement.validate();

        Product product = productRepository.findById(movement.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", movement.getProductId()));

        if (movement.getType() == MovementType.ENTRADA) {
            product.addStock(movement.getQuantity());
            log.info("Stock ENTRY: product='{}', qty={}, reason={}",
                    product.getName(), movement.getQuantity(), movement.getReason());
        } else {
            try {
                product.removeStock(movement.getQuantity());
            } catch (IllegalStateException e) {
                throw new InsufficientStockException(
                        product.getName(), product.getStock(), movement.getQuantity());
            }
            log.info("Stock EXIT: product='{}', qty={}, reason={}",
                    product.getName(), movement.getQuantity(), movement.getReason());
        }

        productRepository.save(product);

        movement.setCreatedAt(LocalDateTime.now());
        return movementRepository.save(movement);
    }

    @Transactional
    public StockMovement registerBulkMovement(List<StockMovement> movements) {
        StockMovement last = null;
        for (StockMovement m : movements) {
            last = registerMovement(m);
        }
        return last;
    }

    public List<StockMovement> findByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", productId);
        }
        return movementRepository.findByProductId(productId);
    }

    public List<StockMovement> findAll() {
        return movementRepository.findAll();
    }
}
