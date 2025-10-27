package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;

import edu.dosw.sirha.sirha_backend.dto.ErrorResponse;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void defaultConstructorSetsTimestampss() {
        ErrorResponse er = new ErrorResponse();
        assertNotNull(er.getTimestamp());
        assertTrue(er.getTimestamp() instanceof LocalDateTime);
    }

    @Test
    void parameterizedConstructorAndAccessors() {
        ErrorResponse er = new ErrorResponse(404, "Not Found", "Recurso no existe");
        assertEquals(404, er.getStatus());
        assertEquals("Not Found", er.getError());
        assertEquals("Recurso no existe", er.getMessage());
        assertNotNull(er.getTimestamp());

        er.setStatus(500);
        er.setError("Server Error");
        er.setMessage("Falla interna");
        LocalDateTime now = LocalDateTime.now();
        er.setTimestamp(now);
        assertEquals(500, er.getStatus());
        assertEquals("Server Error", er.getError());
        assertEquals("Falla interna", er.getMessage());
        assertEquals(now, er.getTimestamp());
    }
}
