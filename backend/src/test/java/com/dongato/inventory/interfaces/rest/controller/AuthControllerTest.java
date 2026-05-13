package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.infrastructure.security.JwtTokenProvider;
import com.dongato.inventory.interfaces.rest.dto.AuthRequestDTO;
import com.dongato.inventory.interfaces.rest.dto.AuthResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Should authenticate user and return token")
    void shouldLoginSuccessfully() {
        AuthRequestDTO request = new AuthRequestDTO("admin", "password");
        Authentication authentication = mock(Authentication.class);
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin");
        when(jwtTokenProvider.generateToken("admin")).thenReturn("mock-jwt-token");
        when(jwtTokenProvider.getExpirationMs()).thenReturn(3600000L);

        ResponseEntity<AuthResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mock-jwt-token", response.getBody().getToken());
        assertEquals("admin", response.getBody().getUsername());
        assertEquals(3600L, response.getBody().getExpiresIn());
    }

    @Test
    @DisplayName("Should throw BadCredentialsException on invalid credentials")
    void shouldThrowBadCredentials() {
        AuthRequestDTO request = new AuthRequestDTO("admin", "wrong");
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authController.login(request));
    }
}
