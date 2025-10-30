package edu.dosw.sirha.sirha_backend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.dosw.sirha.sirha_backend.dto.AuthResponse;

class AuthResponseTest {

    @Test
    void defaultConstructor_initializesTimestamp() {
        long before = System.currentTimeMillis();
        AuthResponse ar = new AuthResponse();
        long after = System.currentTimeMillis();

        assertTrue(ar.getTimestamp() >= before && ar.getTimestamp() <= after,
            "timestamp debe establecerse en el constructor por defecto");
        assertNull(ar.getUsername());
        assertNull(ar.getEmail());
        assertNull(ar.getToken());
    }

    @Test
    void twoArgConstructor_setsUsernameAndToken_and_keepsEmailNull() {
        long before = System.currentTimeMillis();
        AuthResponse ar = new AuthResponse("juan", "tok-123");
        long after = System.currentTimeMillis();

        assertEquals("juan", ar.getUsername());
        assertEquals("tok-123", ar.getToken());
        assertNull(ar.getEmail(), "email debe ser null cuando se usa el constructor de dos argumentos");
        assertTrue(ar.getTimestamp() >= before && ar.getTimestamp() <= after);
    }

    @Test
    void threeArgConstructor_setsAllFields() {
        AuthResponse ar = new AuthResponse("ana", "ana@uni.edu", "tok-999");

        assertEquals("ana", ar.getUsername());
        assertEquals("ana@uni.edu", ar.getEmail());
        assertEquals("tok-999", ar.getToken());
        assertTrue(ar.getTimestamp() > 0);
    }

    @Test
    void settersAndGetters_workAndTimestampCanBeOverwritten() {
        AuthResponse ar = new AuthResponse();

        ar.setUsername("maria");
        ar.setEmail("maria@uni.edu.co");
        ar.setToken("tok-456");

        assertEquals("maria", ar.getUsername());
        assertEquals("maria@uni.edu.co", ar.getEmail());
        assertEquals("tok-456", ar.getToken());

        ar.setTimestamp(12345L);
        assertEquals(12345L, ar.getTimestamp());
    }
}