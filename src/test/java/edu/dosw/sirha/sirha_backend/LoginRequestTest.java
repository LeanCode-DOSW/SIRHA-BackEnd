package edu.dosw.sirha.sirha_backend;
import org.junit.jupiter.api.Test;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void constructorAndSettersGetters() {
        LoginRequest lr = new LoginRequest("user", "user@universidad.edu.co", "pass1234");
        assertEquals("user", lr.getUsername());
        assertEquals("user@universidad.edu.co", lr.getEmail());
        assertEquals("pass1234", lr.getPassword());

        lr.setUsername("otro");
        lr.setEmail("otro@universidad.edu.co");
        lr.setPassword("newpass");
        assertEquals("otro", lr.getUsername());
        assertEquals("otro@universidad.edu.co", lr.getEmail());
        assertEquals("newpass", lr.getPassword());
    }
}