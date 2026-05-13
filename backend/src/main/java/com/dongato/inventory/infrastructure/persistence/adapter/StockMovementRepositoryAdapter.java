package com.dongato.inventory.infrastructure.persistence.adapter;

import com.dongato.inventory.application.ports.out.StockMovementRepositoryPort;
import com.dongato.inventory.domain.model.StockMovement;
import com.dongato.inventory.infrastructure.persistence.mapper.StockMovementPersistenceMapper;
import com.dongato.inventory.infrastructure.persistence.repository.JpaStockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter that implements StockMovementRepositoryPort using Spring Data JPA.
 * <p>
 * McCall Factor: Integrity — ensures audit data is persisted reliably.
 */
@Component
@RequiredArgsConstructor
public class StockMovementRepositoryAdapter implements StockMovementRepositoryPort {

    private final JpaStockMovementRepository jpaRepository;

    @Override
    public StockMovement save(StockMovement movement) {
        var entity = StockMovementPersistenceMapper.toEntity(movement);
        var saved = jpaRepository.save(entity);
        return StockMovementPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<StockMovement> findByProductId(Long productId) {
        return jpaRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(StockMovementPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovement> findAll() {
        return jpaRepository.findAll().stream()
                .map(StockMovementPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
