package com.dongato.inventory.interfaces.rest.dto;

import com.dongato.inventory.domain.model.MovementReason;
import com.dongato.inventory.domain.model.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for stock movement responses.
 * McCall Factor: Interoperability — consistent API response format.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementResponseDTO {

    private Long id;
    private Long productId;
    private MovementType type;
    private Integer quantity;
    private MovementReason reason;
    private LocalDateTime createdAt;
}
