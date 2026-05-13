package com.dongato.inventory.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtTokenProvider.
 * McCall Factor: Integrity — verifies token generation and validation are correct and secure.
 */
class JwtTokenProviderTest {

    private static final String SECRET =
            "TestSecretKeyForJWTTokenMustBeAtLeast256BitsLongForHMACSHA";
    private static final long EXPIRATION_MS = 3_600_000L; // 1 hour

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(SECRET, EXPIRATION_MS);
    }

    @Test
    @DisplayName("Should generate a non-blank JWT token")
    void shouldGenerateToken() {
        String token = jwtTokenProvider.generateToken("admin");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("Should extract correct username from token")
    void shouldExtractUsernameFromToken() {
        String token = jwtTokenProvider.generateToken("admin");

        String username = jwtTokenProvider.getUsernameFromToken(token);

        assertEquals("admin", username);
    }

    @Test
    @DisplayName("Should validate a freshly generated token as true")
    void shouldValidateFreshToken() {
        String token = jwtTokenProvider.generateToken("cashier");

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("Should return false for a tampered token")
    void shouldReturnFalseForTamperedToken() {
        String token = jwtTokenProvider.generateToken("admin");
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";

        assertFalse(jwtTokenProvider.validateToken(tampered));
    }

    @Test
    @DisplayName("Should return false for a completely invalid token string")
    void shouldReturnFalseForGarbageToken() {
        assertFalse(jwtTokenProvider.validateToken("not.a.real.token"));
    }

    @Test
    @DisplayName("Should return false for empty string token")
    void shouldReturnFalseForEmptyToken() {
        assertFalse(jwtTokenProvider.validateToken(""));
    }

    @Test
    @DisplayName("Should return configured expiration time")
    void shouldReturnExpirationMs() {
        assertEquals(EXPIRATION_MS, jwtTokenProvider.getExpirationMs());
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        String token1 = jwtTokenProvider.generateToken("admin");
        String token2 = jwtTokenProvider.generateToken("cashier");

        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Should expire token generated with 1ms expiration")
    void shouldExpireTokenWithShortExpiration() throws InterruptedException {
        JwtTokenProvider shortLived = new JwtTokenProvider(SECRET, 1L);
        String token = shortLived.generateToken("admin");

        Thread.sleep(10);

        assertFalse(shortLived.validateToken(token));
    }
}
