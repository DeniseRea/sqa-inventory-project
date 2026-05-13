package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.application.usecases.StockMovementUseCase;
import com.dongato.inventory.domain.model.MovementReason;
import com.dongato.inventory.domain.model.MovementType;
import com.dongato.inventory.domain.model.StockMovement;
import com.dongato.inventory.interfaces.rest.dto.StockMovementCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.StockMovementResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockMovementControllerTest {

    @Mock
    private StockMovementUseCase stockMovementUseCase;

    @InjectMocks
    private StockMovementController stockMovementController;

    private StockMovement mockMovement() {
        return StockMovement.builder()
                .id(1L).productId(1L).type(MovementType.ENTRADA)
                .quantity(10).reason(MovementReason.REPOSICION).build();
    }

    @Test
    @DisplayName("Should register stock movement and return 201")
    void shouldRegisterMovement() {
        StockMovementCreateDTO dto = new StockMovementCreateDTO(1L, MovementType.ENTRADA, 10, MovementReason.REPOSICION);
        when(stockMovementUseCase.registerMovement(any(StockMovement.class))).thenReturn(mockMovement());

        ResponseEntity<StockMovementResponseDTO> response = stockMovementController.register(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getProductId());
    }

    @Test
    @DisplayName("Should find all stock movements and return 200")
    void shouldFindAll() {
        when(stockMovementUseCase.findAll()).thenReturn(List.of(mockMovement()));

        ResponseEntity<List<StockMovementResponseDTO>> response = stockMovementController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Should find stock movements by product id and return 200")
    void shouldFindByProduct() {
        when(stockMovementUseCase.findByProductId(1L)).thenReturn(List.of(mockMovement()));

        ResponseEntity<List<StockMovementResponseDTO>> response = stockMovementController.findByProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
