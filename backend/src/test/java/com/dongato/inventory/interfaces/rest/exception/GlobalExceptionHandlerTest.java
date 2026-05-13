package com.dongato.inventory.interfaces.rest.exception;

import com.dongato.inventory.domain.exception.BusinessRuleException;
import com.dongato.inventory.domain.exception.ResourceNotFoundException;
import com.dongato.inventory.interfaces.rest.dto.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GlobalExceptionHandler.
 * McCall Factor: Usability & Integrity — verifies consistent and safe error responses.
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/api/v1/test");
    }

    @Test
    @DisplayName("Should return 404 for ResourceNotFoundException")
    void shouldReturn404ForResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", 1L);

        ResponseEntity<ApiErrorDTO> response = handler.handleNotFound(ex, mockRequest);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("/api/v1/test", response.getBody().getPath());
    }

    @Test
    @DisplayName("Should return 409 for BusinessRuleException")
    void shouldReturn409ForBusinessRuleException() {
        BusinessRuleException ex = new BusinessRuleException("Category already exists");

        ResponseEntity<ApiErrorDTO> response = handler.handleBusinessRule(ex, mockRequest);

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode().value());
        assertEquals("Category already exists", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should return 400 for IllegalArgumentException")
    void shouldReturn400ForIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");

        ResponseEntity<ApiErrorDTO> response = handler.handleIllegalArgument(ex, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals("Invalid input", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should return 401 for BadCredentialsException")
    void shouldReturn401ForBadCredentials() {
        BadCredentialsException ex = new BadCredentialsException("Wrong password");

        ResponseEntity<ApiErrorDTO> response = handler.handleBadCredentials(ex, mockRequest);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode().value());
        assertEquals("Invalid username or password", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should return 500 for generic unexpected exceptions")
    void shouldReturn500ForGenericException() {
        Exception ex = new RuntimeException("Something broke");

        ResponseEntity<ApiErrorDTO> response = handler.handleGeneric(ex, mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should include timestamp in error response")
    void shouldIncludeTimestampInResponse() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Category", 5L);

        ResponseEntity<ApiErrorDTO> response = handler.handleNotFound(ex, mockRequest);

        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should not expose internal exception details for generic errors (Integrity)")
    void shouldNotExposeInternalDetails() {
        Exception ex = new RuntimeException("NullPointerException: secret internal detail");

        ResponseEntity<ApiErrorDTO> response = handler.handleGeneric(ex, mockRequest);

        assertFalse(response.getBody().getMessage().contains("NullPointerException"));
        assertFalse(response.getBody().getMessage().contains("secret internal detail"));
    }
}
