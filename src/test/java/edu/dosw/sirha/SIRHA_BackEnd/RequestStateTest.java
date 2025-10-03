package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para los estados de solicitudes (State Pattern)
 */
@SpringBootTest
class RequestStateTest {

    @Test
    void testEstadoPendienteCanOnlyReview() {
        EstadoPendiente estadoPendiente = new EstadoPendiente();
        BaseRequest mockRequest = mock(BaseRequest.class);
        
        assertEquals(RequestStateEnum.PENDIENTE, estadoPendiente.getState());
        
        // Solo debería permitir poner en revisión
        assertDoesNotThrow(() -> estadoPendiente.reviewRequest(mockRequest));
        
        // No debería permitir otras operaciones
        assertThrows(IllegalStateException.class, () -> estadoPendiente.approveRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoPendiente.rejectRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoPendiente.pendingRequest(mockRequest));
    }

    @Test
    void testEstadoEnRevisionCanApproveOrReject() {
        EstadoEnRevision estadoEnRevision = new EstadoEnRevision();
        BaseRequest mockRequest = mock(BaseRequest.class);
        
        assertEquals(RequestStateEnum.EN_REVISION, estadoEnRevision.getState());
        
        // Debería permitir aprobar y rechazar
        assertDoesNotThrow(() -> estadoEnRevision.approveRequest(mockRequest));
        assertDoesNotThrow(() -> estadoEnRevision.rejectRequest(mockRequest));
        
        // No debería permitir otras operaciones
        assertThrows(IllegalStateException.class, () -> estadoEnRevision.pendingRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoEnRevision.reviewRequest(mockRequest));
    }

    @Test
    void testEstadoAprobadaIsTerminal() {
        EstadoAprobada estadoAprobada = new EstadoAprobada();
        BaseRequest mockRequest = mock(BaseRequest.class);
        
        assertEquals(RequestStateEnum.APROBADA, estadoAprobada.getState());
        
        // No debería permitir ninguna operación (estado terminal)
        assertThrows(IllegalStateException.class, () -> estadoAprobada.approveRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoAprobada.rejectRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoAprobada.pendingRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoAprobada.reviewRequest(mockRequest));
    }

    @Test
    void testEstadoRechazadaIsTerminal() {
        EstadoRechazada estadoRechazada = new EstadoRechazada();
        BaseRequest mockRequest = mock(BaseRequest.class);
        
        assertEquals(RequestStateEnum.RECHAZADA, estadoRechazada.getState());
        
        // No debería permitir ninguna operación (estado terminal)
        assertThrows(IllegalStateException.class, () -> estadoRechazada.approveRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoRechazada.rejectRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoRechazada.pendingRequest(mockRequest));
        assertThrows(IllegalStateException.class, () -> estadoRechazada.reviewRequest(mockRequest));
    }

    @Test
    void testStateTransitionFlow() {
        BaseRequest mockRequest = mock(BaseRequest.class);
        
        // Flujo normal: Pendiente -> En Revisión -> Aprobada
        EstadoPendiente pendiente = new EstadoPendiente();
        assertDoesNotThrow(() -> pendiente.reviewRequest(mockRequest));
        
        EstadoEnRevision enRevision = new EstadoEnRevision();
        assertDoesNotThrow(() -> enRevision.approveRequest(mockRequest));
        
        EstadoAprobada aprobada = new EstadoAprobada();
        assertEquals(RequestStateEnum.APROBADA, aprobada.getState());
    }

    @Test
    void testAlternativeStateTransitionFlow() {
        BaseRequest mockRequest = mock(BaseRequest.class);
        
        // Flujo alternativo: Pendiente -> En Revisión -> Rechazada
        EstadoPendiente pendiente = new EstadoPendiente();
        assertDoesNotThrow(() -> pendiente.reviewRequest(mockRequest));
        
        EstadoEnRevision enRevision = new EstadoEnRevision();
        assertDoesNotThrow(() -> enRevision.rejectRequest(mockRequest));
        
        EstadoRechazada rechazada = new EstadoRechazada();
        assertEquals(RequestStateEnum.RECHAZADA, rechazada.getState());
    }

    @Test
    void testInvalidStateTransitions() {
        BaseRequest mockRequest = mock(BaseRequest.class);
        
        // Verificar que las transiciones inválidas fallan
        EstadoPendiente pendiente = new EstadoPendiente();
        assertThrows(IllegalStateException.class, () -> pendiente.approveRequest(mockRequest));
        
        EstadoEnRevision enRevision = new EstadoEnRevision();
        assertThrows(IllegalStateException.class, () -> enRevision.pendingRequest(mockRequest));
        
        EstadoAprobada aprobada = new EstadoAprobada();
        assertThrows(IllegalStateException.class, () -> aprobada.rejectRequest(mockRequest));
        
        EstadoRechazada rechazada = new EstadoRechazada();
        assertThrows(IllegalStateException.class, () -> rechazada.approveRequest(mockRequest));
    }

    @Test
    void testAllStateEnums() {
        // Verificar que todos los estados tienen su enum correspondiente
        assertEquals(RequestStateEnum.PENDIENTE, new EstadoPendiente().getState());
        assertEquals(RequestStateEnum.EN_REVISION, new EstadoEnRevision().getState());
        assertEquals(RequestStateEnum.APROBADA, new EstadoAprobada().getState());
        assertEquals(RequestStateEnum.RECHAZADA, new EstadoRechazada().getState());
    }

    @Test
    void testStateEnumValues() {
        // Verificar todos los valores del enum
        RequestStateEnum[] expectedStates = {
            RequestStateEnum.PENDIENTE,
            RequestStateEnum.APROBADA,
            RequestStateEnum.RECHAZADA,
            RequestStateEnum.EN_REVISION
        };
        
        RequestStateEnum[] actualStates = RequestStateEnum.values();
        assertEquals(expectedStates.length, actualStates.length);
        
        for (RequestStateEnum expectedState : expectedStates) {
            boolean found = false;
            for (RequestStateEnum actualState : actualStates) {
                if (expectedState == actualState) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Estado " + expectedState + " no encontrado en los valores del enum");
        }
    }

    @Test
    void testStateEnumStringRepresentation() {
        assertEquals("PENDIENTE", RequestStateEnum.PENDIENTE.toString());
        assertEquals("APROBADA", RequestStateEnum.APROBADA.toString());
        assertEquals("RECHAZADA", RequestStateEnum.RECHAZADA.toString());
        assertEquals("EN_REVISION", RequestStateEnum.EN_REVISION.toString());
    }
}