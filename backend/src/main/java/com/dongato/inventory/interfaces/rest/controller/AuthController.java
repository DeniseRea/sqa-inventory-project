package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.infrastructure.security.JwtTokenProvider;
import com.dongato.inventory.interfaces.rest.dto.AuthRequestDTO;
import com.dongato.inventory.interfaces.rest.dto.AuthResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller for JWT token generation.
 * <p>
 * McCall Factor: Integrity — secure authentication endpoint.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            String token = jwtTokenProvider.generateToken(authentication.getName());

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .token(token)
                    .type("Bearer")
                    .username(authentication.getName())
                    .expiresIn(jwtTokenProvider.getExpirationMs() / 1000)
                    .build();

            log.info("User '{}' authenticated successfully", authentication.getName());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.warn("Failed authentication attempt for user: {}", request.getUsername());
            throw e;
        }
    }
}
