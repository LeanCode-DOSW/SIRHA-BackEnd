package edu.dosw.sirha.sirha_backend;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void constructorsAndFields() {
        RegisterRequest r1 = new RegisterRequest("user1", "u1@universidad.edu.co", "password", "2025-000001");
        assertEquals("user1", r1.getUsername());
        assertEquals("u1@universidad.edu.co", r1.getEmail());
        assertEquals("password", r1.getPassword());
        assertEquals("2025-000001", r1.getCodigo());
        assertNull(r1.getCareer());

        RegisterRequest r2 = new RegisterRequest("user2", "u2@universidad.edu.co", "password2", Careers.INGENIERIA_AMBIENTAL);
        assertEquals("user2", r2.getUsername());
        assertEquals(Careers.INGENIERIA_AMBIENTAL, r2.getCareer());

        r1.setCareer(Careers.INGENIERIA_DE_SISTEMAS);
        assertEquals(Careers.INGENIERIA_DE_SISTEMAS, r1.getCareer());
    }
}