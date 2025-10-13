package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.CambioGrupo;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.*;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para los estados de solicitudes (State Pattern)
 */
@SpringBootTest
class RequestStateTest {

    private BaseRequest testRequest;

    @BeforeEach
    void setUp() {
        // Crear una instancia simple para las pruebas - solo necesitamos que exista
        testRequest = new CambioGrupo(null, null, null, null);
        testRequest.setId("TEST-001");
    }

    @Test
    void testEstadoPendienteCanOnlyReview() {
        EstadoPendiente estadoPendiente = new EstadoPendiente();
        
        assertEquals(RequestStateEnum.PENDIENTE, estadoPendiente.getState());
        
        // Solo debería permitir poner en revisión
        assertDoesNotThrow(() -> estadoPendiente.reviewRequest(testRequest, new ResponseRequest("Poniendo en revisión", RequestStateEnum.EN_REVISION)));
        
        // No debería permitir otras operaciones
        assertThrows(SirhaException.class, () -> estadoPendiente.approveRequest(testRequest, new ResponseRequest("Intentando aprobar", RequestStateEnum.APROBADA)));
        assertThrows(SirhaException.class, () -> estadoPendiente.rejectRequest(testRequest, new ResponseRequest("Intentando rechazar", RequestStateEnum.RECHAZADA)));
    }

    @Test
    void testEstadoEnRevisionCanApproveOrReject() {
        EstadoEnRevision estadoEnRevision = new EstadoEnRevision();
        
        assertEquals(RequestStateEnum.EN_REVISION, estadoEnRevision.getState());
        
        // Debería permitir aprobar y rechazar
        assertDoesNotThrow(() -> estadoEnRevision.approveRequest(testRequest, new ResponseRequest("Aprobando solicitud", RequestStateEnum.APROBADA)));
        assertDoesNotThrow(() -> estadoEnRevision.rejectRequest(testRequest, new ResponseRequest("Rechazando solicitud", RequestStateEnum.RECHAZADA)));

        // No debería permitir otras operaciones
        assertThrows(SirhaException.class, () -> estadoEnRevision.reviewRequest(testRequest, new ResponseRequest("Intentando revisar", RequestStateEnum.EN_REVISION)));
    }

    @Test
    void testEstadoAprobadaIsTerminal() {
        EstadoAprobada estadoAprobada = new EstadoAprobada();
        
        assertEquals(RequestStateEnum.APROBADA, estadoAprobada.getState());
        
        // No debería permitir ninguna operación (estado terminal)
        assertThrows(SirhaException.class, () -> estadoAprobada.approveRequest(testRequest, new ResponseRequest("Intentando aprobar una solicitud aprobada", RequestStateEnum.APROBADA)));
        assertThrows(SirhaException.class, () -> estadoAprobada.rejectRequest(testRequest, new ResponseRequest("Intentando rechazar una solicitud aprobada", RequestStateEnum.RECHAZADA)));
        assertThrows(SirhaException.class, () -> estadoAprobada.reviewRequest(testRequest, new ResponseRequest("Intentando revisar una solicitud aprobada", RequestStateEnum.EN_REVISION)));
    }

    @Test
    void testEstadoRechazadaIsTerminal() {
        EstadoRechazada estadoRechazada = new EstadoRechazada();
        
        assertEquals(RequestStateEnum.RECHAZADA, estadoRechazada.getState());
        
        // No debería permitir ninguna operación (estado terminal)
        assertThrows(SirhaException.class, () -> estadoRechazada.approveRequest(testRequest, new ResponseRequest("Intentando aprobar una solicitud rechazada", RequestStateEnum.APROBADA)));
        assertThrows(SirhaException.class, () -> estadoRechazada.rejectRequest(testRequest, new ResponseRequest("Intentando rechazar una solicitud rechazada", RequestStateEnum.RECHAZADA)));
        assertThrows(SirhaException.class, () -> estadoRechazada.reviewRequest(testRequest, new ResponseRequest("Intentando revisar una solicitud rechazada", RequestStateEnum.EN_REVISION)));
    }

    @Test
    void testStateTransitionFlow() {
        // Flujo normal: Pendiente -> En Revisión -> Aprobada
        EstadoPendiente pendiente = new EstadoPendiente();
        assertDoesNotThrow(() -> pendiente.reviewRequest(testRequest, new ResponseRequest("Revisando solicitud", RequestStateEnum.EN_REVISION)));
        
        EstadoEnRevision enRevision = new EstadoEnRevision();
        assertDoesNotThrow(() -> enRevision.approveRequest(testRequest, new ResponseRequest("Aprobando solicitud", RequestStateEnum.APROBADA)));

        EstadoAprobada aprobada = new EstadoAprobada();
        assertEquals(RequestStateEnum.APROBADA, aprobada.getState());
    }

    @Test
    void testAlternativeStateTransitionFlow() {
        // Flujo alternativo: Pendiente -> En Revisión -> Rechazada
        EstadoPendiente pendiente = new EstadoPendiente();
        assertDoesNotThrow(() -> pendiente.reviewRequest(testRequest, new ResponseRequest("Revisando solicitud", RequestStateEnum.EN_REVISION)));
        
        EstadoEnRevision enRevision = new EstadoEnRevision();
        assertDoesNotThrow(() -> enRevision.rejectRequest(testRequest, new ResponseRequest("Rechazando solicitud", RequestStateEnum.RECHAZADA)));

        EstadoRechazada rechazada = new EstadoRechazada();
        assertEquals(RequestStateEnum.RECHAZADA, rechazada.getState());
    }

    @Test
    void testInvalidStateTransitions() {
        // Verificar que las transiciones inválidas fallan
        EstadoPendiente pendiente = new EstadoPendiente();
        assertThrows(SirhaException.class, () -> pendiente.approveRequest(testRequest, new ResponseRequest("Intentando aprobar sin revisión", RequestStateEnum.APROBADA)));
        
        EstadoAprobada aprobada = new EstadoAprobada();
        assertThrows(SirhaException.class, () -> aprobada.rejectRequest(testRequest, new ResponseRequest("Intentando rechazar una solicitud aprobada", RequestStateEnum.RECHAZADA)));

        EstadoRechazada rechazada = new EstadoRechazada();
        assertThrows(SirhaException.class, () -> rechazada.approveRequest(testRequest, new ResponseRequest("Intentando aprobar una solicitud rechazada", RequestStateEnum.APROBADA)));
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