package com.dongato.inventory.application.ports.out;

import com.dongato.inventory.domain.model.StockMovement;

import java.util.List;

/**
 * Output port for StockMovement persistence.
 * <p>
 * McCall Factor: Integrity — provides the contract for audit trail storage.
 */
public interface StockMovementRepositoryPort {

    StockMovement save(StockMovement movement);

    List<StockMovement> findByProductId(Long productId);

    List<StockMovement> findAll();
}
