package com.dongato.inventory.infrastructure.persistence.adapter;

import com.dongato.inventory.domain.model.MovementReason;
import com.dongato.inventory.domain.model.MovementType;
import com.dongato.inventory.domain.model.StockMovement;
import com.dongato.inventory.infrastructure.persistence.entity.StockMovementEntity;
import com.dongato.inventory.infrastructure.persistence.repository.JpaStockMovementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockMovementRepositoryAdapterTest {

    @Mock
    private JpaStockMovementRepository repository;

    @InjectMocks
    private StockMovementRepositoryAdapter adapter;

    private StockMovement mockMovement() {
        return StockMovement.builder()
                .id(1L).productId(2L).type(MovementType.ENTRADA)
                .quantity(10).reason(MovementReason.REPOSICION)
                .createdAt(LocalDateTime.now()).build();
    }

    private StockMovementEntity mockEntity() {
        return StockMovementEntity.builder()
                .id(1L).productId(2L).type(MovementType.ENTRADA)
                .quantity(10).reason(MovementReason.REPOSICION)
                .createdAt(LocalDateTime.now()).build();
    }

    @Test
    @DisplayName("Should save stock movement")
    void shouldSaveStockMovement() {
        when(repository.save(any(StockMovementEntity.class))).thenReturn(mockEntity());

        StockMovement saved = adapter.save(mockMovement());

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(repository).save(any(StockMovementEntity.class));
    }

    @Test
    @DisplayName("Should find all stock movements")
    void shouldFindAll() {
        when(repository.findAll()).thenReturn(List.of(mockEntity()));

        List<StockMovement> movements = adapter.findAll();

        assertEquals(1, movements.size());
    }

    @Test
    @DisplayName("Should find stock movements by product id")
    void shouldFindByProductId() {
        when(repository.findByProductIdOrderByCreatedAtDesc(2L)).thenReturn(List.of(mockEntity()));

        List<StockMovement> movements = adapter.findByProductId(2L);

        assertEquals(1, movements.size());
    }
}
