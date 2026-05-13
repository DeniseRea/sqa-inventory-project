package com.dongato.inventory.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for category responses.
 * McCall Factor: Interoperability — standardized API contract.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
