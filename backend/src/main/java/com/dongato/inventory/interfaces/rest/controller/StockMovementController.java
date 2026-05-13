package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.application.usecases.StockMovementUseCase;
import com.dongato.inventory.domain.model.StockMovement;
import com.dongato.inventory.interfaces.rest.dto.StockMovementCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.StockMovementResponseDTO;
import com.dongato.inventory.interfaces.rest.mapper.StockMovementRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Stock Movement management.
 * <p>
 * This is the ONLY way to modify product stock — ensuring full audit trail.
 * McCall Factors: Integrity (traceability), Interoperability (REST standard).
 */
@RestController
@RequestMapping("/api/v1/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementUseCase stockMovementUseCase;

    @PostMapping
    public ResponseEntity<StockMovementResponseDTO> register(
            @Valid @RequestBody StockMovementCreateDTO dto) {
        StockMovement movement = StockMovementRestMapper.toDomain(dto);
        StockMovement registered = stockMovementUseCase.registerMovement(movement);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(StockMovementRestMapper.toResponseDto(registered));
    }

    @GetMapping
    public ResponseEntity<List<StockMovementResponseDTO>> findAll() {
        List<StockMovementResponseDTO> movements = stockMovementUseCase.findAll().stream()
                .map(StockMovementRestMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovementResponseDTO>> findByProduct(
            @PathVariable Long productId) {
        List<StockMovementResponseDTO> movements = stockMovementUseCase.findByProductId(productId)
                .stream()
                .map(StockMovementRestMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movements);
    }
}
