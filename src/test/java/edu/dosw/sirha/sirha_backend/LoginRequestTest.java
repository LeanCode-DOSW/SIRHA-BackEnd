package edu.dosw.sirha.sirha_backend;
import org.junit.jupiter.api.Test;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void constructorAndSettersGetters() {
        LoginRequest lr = new LoginRequest("user", "pass1234");
        assertEquals("user", lr.getUsername());
        assertEquals("pass1234", lr.getPassword());

        lr.setUsername("otro");
        lr.setPassword("newpass");
        assertEquals("otro", lr.getUsername());
        assertEquals("newpass", lr.getPassword());
    }
}