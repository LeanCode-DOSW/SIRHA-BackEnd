package edu.dosw.sirha.sirha_backend;
import org.junit.jupiter.api.Test;

import edu.dosw.sirha.sirha_backend.dto.AuthResponse;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void constructorAndAccessorsss() {
        AuthResponse ar = new AuthResponse("juan", "juan@uni.edu.co", "tok-123");
        assertEquals("juan", ar.getUsername());
        assertEquals("juan@uni.edu.co", ar.getEmail());
        assertEquals("tok-123", ar.getToken());
        assertTrue(ar.getTimestamp() > 0);

        ar.setUsername("maria");
        ar.setEmail("maria@uni.edu.co");
        ar.setToken("tok-456");
        ar.setTimestamp(12345L);
        assertEquals("maria", ar.getUsername());
        assertEquals("maria@uni.edu.co", ar.getEmail());
        assertEquals("tok-456", ar.getToken());
        assertEquals(12345L, ar.getTimestamp());
    }
}