package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;

import edu.dosw.sirha.sirha_backend.dto.MeResponse;

import static org.junit.jupiter.api.Assertions.*;

class MeResponseTest {

    @Test
    void constructorsAndAccessors() {
        MeResponse m = new MeResponse();
        m.setUsername("ana");
        m.setRole("ROLE_STUDENT");
        assertEquals("ana", m.getUsername());
        assertEquals("ROLE_STUDENT", m.getRole());

        MeResponse m2 = new MeResponse("pepe", "ROLE_ADMIN");
        assertEquals("pepe", m2.getUsername());
        assertEquals("ROLE_ADMIN", m2.getRole());
    }
}