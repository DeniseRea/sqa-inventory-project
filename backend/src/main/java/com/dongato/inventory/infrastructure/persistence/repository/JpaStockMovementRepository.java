package com.dongato.inventory.infrastructure.persistence.repository;

import com.dongato.inventory.infrastructure.persistence.entity.StockMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for StockMovement.
 */
@Repository
public interface JpaStockMovementRepository extends JpaRepository<StockMovementEntity, Long> {

    List<StockMovementEntity> findByProductIdOrderByCreatedAtDesc(Long productId);
}
