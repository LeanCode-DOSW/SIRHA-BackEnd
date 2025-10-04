package edu.dosw.sirha.SIRHA_BackEnd.config;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.BaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestState;

class BaseRequestTest {

    private BaseRequest request;

    // Clase dummy para pruebas
    static class TestRequest extends BaseRequest {
        public TestRequest(int priority) {
            super(priority);
        }

        @Override
        public void approve() {
            setState(RequestState.APPROVED);
        }

        @Override
        public void reject() {
            setState(RequestState.REJECTED);
        }
    }

    @BeforeEach
    void setUp() {
        request = new TestRequest(3);
        request.setId("REQ-123");
    }

    @Test
    void constructor_ShouldInitializePendingAndCreatedAt() {
        assertEquals(RequestState.PENDING, request.getState());
        assertNotNull(request.getCreatedAt());
    }

    @Test
    void approve_ShouldChangeStateToApproved() {
        request.approve();
        assertEquals(RequestState.APPROVED, request.getState());
    }

    @Test
    void reject_ShouldChangeStateToRejected() {
        request.reject();
        assertEquals(RequestState.REJECTED, request.getState());
    }

    @Test
    void setPriority_ValidValue_ShouldUpdatePriority() {
        request.setPriority(5);
        assertEquals(5, request.getPriority());
    }

    @Test
    void setPriority_InvalidValue_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> request.setPriority(0));
        assertThrows(IllegalArgumentException.class, () -> request.setPriority(6));
    }

    @Test
    void setCreatedAt_ValidDate_ShouldUpdate() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        request.setCreatedAt(past);
        assertEquals(past, request.getCreatedAt());
    }

    @Test
    void setCreatedAt_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> request.setCreatedAt(null));
    }

    @Test
    void setCreatedAt_FutureDate_ShouldThrowException() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> request.setCreatedAt(future));
    }

    @Test
    void toString_ShouldReturnFormattedString() {
        String result = request.toString();
        assertTrue(result.contains("REQ-123"));
        assertTrue(result.contains("priority=3"));
        assertTrue(result.contains("PENDING"));
    }
}
