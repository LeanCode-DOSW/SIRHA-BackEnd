package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import edu.dosw.sirha.sirha_backend.dto.ApiErrorResponse;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorResponseTest {

    @Test
    void constructorAndGettersAndSuggestion() {
        ApiErrorResponse resp = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                "ERR_01",
                "Invalid input",
                "Detalles de error",
                "/api/test"
        );

        assertEquals("error", resp.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), resp.getStatusCode());
        assertEquals("ERR_01", resp.getCode());
        assertEquals("Invalid input", resp.getMessage());
        assertEquals("Detalles de error", resp.getDetails());
        assertEquals("/api/test", resp.getPath());
        assertNotNull(resp.getRequestId());
        assertNotNull(resp.getTimestamp());

        resp.setSuggestion("Revise los datos e intente de nuevo");
        assertEquals("Revise los datos e intente de nuevo", resp.getSuggestion());
    }
}