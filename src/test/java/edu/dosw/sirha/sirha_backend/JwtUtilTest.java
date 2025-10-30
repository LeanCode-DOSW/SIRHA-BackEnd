package edu.dosw.sirha.sirha_backend;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import edu.dosw.sirha.sirha_backend.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generateAndParse_shouldContainSubjectAndRole() {
        // Use a 32-char secret to create a 256-bit HMAC key
        String secret = "01234567890123456789012345678901";
        JwtUtil jwtUtil = new JwtUtil(secret, "test-issuer", 60);

        String token = jwtUtil.generate("alice", "ROLE_ADMIN");
        assertNotNull(token);

        Claims claims = jwtUtil.parse(token);
        assertEquals("alice", claims.getSubject());
        assertEquals("ROLE_ADMIN", claims.get("role"));
        assertEquals("test-issuer", claims.getIssuer());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }

}
