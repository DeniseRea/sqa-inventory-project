package com.dongato.inventory.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication responses (JWT token).
 * McCall Factor: Integrity — secure token delivery.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String type;
    private String username;
    private long expiresIn;
}
