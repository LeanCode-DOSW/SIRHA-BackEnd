package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.CambioGrupo;
import edu.dosw.sirha.sirha_backend.domain.model.Decanate;
import edu.dosw.sirha.sirha_backend.domain.model.Semaforo;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DecanateTest {

    private Decanate decanateSistemas;
    private Decanate decanateIndustrial;
    private Student studentSistemas;
    private Student studentIndustrial;
    private AcademicPeriod currentPeriod;
    
    // Materias
    private Subject matematicas;
    private Subject fisica;
    private Subject programacion;
    private Subject quimica;
    
    // Grupos
    private Group grupoMatematicasA;
    private Group grupoMatematicasB;
    private Group grupoFisicaD;
    private Group grupoProgramacion2;
    private Group grupoQuimicaY;
    
    // Solicitudes
    private CambioGrupo requestSistemas1;
    private CambioGrupo requestSistemas2;
    private CambioGrupo requestIndustrial1;

    @BeforeEach
    void setUp() throws SirhaException {
        currentPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(5));

        decanateSistemas = new Decanate(Careers.INGENIERIA_DE_SISTEMAS);
        decanateIndustrial = new Decanate(Careers.INGENIERIA_INDUSTRIAL);

        matematicas = new Subject("Matemáticas I", "MAT101", 4);
        fisica = new Subject("Física I", "FIS101", 4);
        programacion = new Subject("Programación I", "PROG101", 3);
        quimica = new Subject("Química I", "QUI101", 4);

        grupoMatematicasA = new Group(matematicas, 30, currentPeriod);
        grupoMatematicasB = new Group(matematicas, 30, currentPeriod);
        
        grupoFisicaD = new Group(fisica, 25, currentPeriod);
        
        grupoProgramacion2 = new Group(programacion, 20, currentPeriod);
        
        grupoQuimicaY = new Group(quimica, 30, currentPeriod);

        StudyPlan studyPlan1 = new StudyPlan("Ingeniería de Sistemas", Careers.INGENIERIA_DE_SISTEMAS);
        StudyPlan studyPlan2 = new StudyPlan("Ingeniería Industrial", Careers.INGENIERIA_INDUSTRIAL);

        studentSistemas = new Student(
            "juan.perez",
            "juan@universidad.edu",
            "password123",
            "2024001"
        );
        studentSistemas.setAcademicProgress(new Semaforo(studyPlan1));
        studentIndustrial = new Student(
            "maria.garcia",
            "maria@universidad.edu", 
            "password456",
            "2024002"
        );
        studentIndustrial.setAcademicProgress(new Semaforo(studyPlan2));
        
        // Solicitud 1: Estudiante de Sistemas - Cambio en Matemáticas (A → B)
        requestSistemas1 = new CambioGrupo(studentSistemas, matematicas, grupoMatematicasB, currentPeriod);

        // Solicitud 2: Estudiante de Sistemas - Cambio en Física (C → D)
        requestSistemas2 = new CambioGrupo(studentSistemas, fisica, grupoFisicaD, currentPeriod);

        // Solicitud 3: Estudiante de Industrial - Cambio en Química (X → Y)
        requestIndustrial1 = new CambioGrupo(studentIndustrial, quimica, grupoQuimicaY, currentPeriod);
    }

    @Test
    void testDecanateCreation() {
        assertEquals(Careers.INGENIERIA_DE_SISTEMAS.getDisplayName(), decanateSistemas.getName());
        assertNotNull(decanateSistemas.getPendingRequests());
        assertTrue(decanateSistemas.getPendingRequests().isEmpty());
        assertEquals(0, decanateSistemas.getPendingRequests().size());
    }

    @Test
    void testSetupCreation() {
        // Assert - Verificar período académico
        assertNotNull(currentPeriod);
        assertEquals("2024-1", currentPeriod.getPeriod());

        // Assert - Verificar estudiantes
        assertNotNull(studentSistemas);
        assertEquals("juan.perez", studentSistemas.getUsername());
        assertEquals("2024001", studentSistemas.getCodigo());

        // Assert - Verificar materias
        assertNotNull(matematicas);
        assertEquals("MAT101", matematicas.getName());
        assertEquals(4, matematicas.getCredits());

        // Assert - Verificar grupos (creados con constructor real)
        assertNotNull(grupoMatematicasA);
        assertEquals(matematicas, grupoMatematicasA.getCurso()); // Verificar subject asignado
        assertEquals(30, grupoMatematicasA.getCapacidad()); // Verificar capacidad
        assertEquals(0, grupoMatematicasA.getInscritos()); // Inicialmente 0 inscritos
        assertEquals(currentPeriod, grupoMatematicasA.getCurrentPeriod());
        assertNotNull(grupoMatematicasA.getCode()); // Code generado automáticamente

        // Assert - Verificar solicitudes (creadas con constructor real)
        assertNotNull(requestSistemas1);
        assertEquals(studentSistemas, requestSistemas1.getStudent());
        assertEquals(matematicas, requestSistemas1.getSubject());
        assertEquals(grupoMatematicasB, requestSistemas1.getNewGroup());
        assertEquals(currentPeriod, requestSistemas1.getCurrentPeriod());
    }

    @Test
    void testGroupInitialState() {
        assertEquals(0, grupoMatematicasA.getInscritos());
        assertEquals(30, grupoMatematicasA.getCapacidad());
        assertNotNull(grupoMatematicasA.getGroupState());
        assertNotNull(grupoMatematicasA.getEstudiantes());
        assertTrue(grupoMatematicasA.getEstudiantes().isEmpty());
        assertNotNull(grupoMatematicasA.getSchedules());

        assertEquals(matematicas, grupoMatematicasA.getCurso());
        assertEquals(currentPeriod, grupoMatematicasA.getCurrentPeriod());
    }

    @Test
    void testReceiveValidRequest() throws SirhaException {
        
        // Act
        decanateSistemas.receiveRequest(requestSistemas1);

        // Assert
        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        assertEquals(1, pendingRequests.size());
        assertEquals(requestSistemas1, pendingRequests.get(0));
        assertEquals(1, requestSistemas1.getPriority());
        assertEquals(1, decanateSistemas.getPendingRequests().size());

        // Verificar que la solicitud cambió a EN_REVISION
        assertEquals(RequestStateEnum.EN_REVISION, requestSistemas1.getActualState());
    }

    @Test
    void testRejectRequestFromDifferentCareer() {
        
        // Act & Assert
        SirhaException exception = assertThrows(SirhaException.class, () -> {
            decanateSistemas.receiveRequest(requestIndustrial1);
        });
        
        // Verificar mensaje de error específico
        assertTrue(exception.getMessage().contains("Esta decanatura no puede recibir solicitudes") ||
                  exception.getMessage().contains("carrera") ||
                  exception.getMessage().contains("INVALID_CAREER"));
        assertEquals(0, decanateSistemas.getPendingRequests().size());
        assertTrue(decanateSistemas.getPendingRequests().isEmpty());
    }

    @Test
    void testAutomaticPriorityAssignment() throws SirhaException {
        
        // Act - Recibir múltiples solicitudes
        decanateSistemas.receiveRequest(requestSistemas1);
        decanateSistemas.receiveRequest(requestSistemas2);

        // Assert - Verificar prioridades FIFO
        assertEquals(1, requestSistemas1.getPriority());
        assertEquals(2, requestSistemas2.getPriority());
        
        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        assertEquals(2, pendingRequests.size());
        assertEquals(2, decanateSistemas.getPendingRequests().size());

        // Verificar orden FIFO (primera en llegar, primera en la lista)
        assertEquals(requestSistemas1, pendingRequests.get(0));
        assertEquals(requestSistemas2, pendingRequests.get(1));

        // Verificar detalles de las solicitudes
        assertEquals("MAT101", requestSistemas1.getSubject().getName());
        assertEquals("FIS101", requestSistemas2.getSubject().getName());
    }

    @Test
    void testApproveRequestAndReorderPriorities() throws SirhaException {
        
        // Arrange - Crear tercera solicitud
        CambioGrupo requestSistemas3 = new CambioGrupo(studentSistemas, programacion, grupoProgramacion2, currentPeriod);

        // Recibir múltiples solicitudes
        decanateSistemas.receiveRequest(requestSistemas1);
        decanateSistemas.receiveRequest(requestSistemas2);
        decanateSistemas.receiveRequest(requestSistemas3);

        // Verificar estado inicial
        assertEquals(3, decanateSistemas.getPendingRequests().size());

        // Act - Aprobar la primera solicitud
        decanateSistemas.approveRequest(requestSistemas1);

        // Assert - Verificar estado de la solicitud aprobada
        assertEquals(RequestStateEnum.APROBADA, requestSistemas1.getActualState());
        
        // Verificar que las solicitudes pendientes se reordenaron
        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        assertEquals(2, pendingRequests.size()); // Solo quedan 2 pendientes
        
        // Verificar nuevo orden de prioridades después del reordenamiento
        assertEquals(1, requestSistemas2.getPriority()); // La segunda ahora es prioridad 1
        assertEquals(2, requestSistemas3.getPriority()); // La tercera ahora es prioridad 2

        // Verificar que las solicitudes pendientes mantienen su orden
        assertEquals(requestSistemas2, pendingRequests.get(0));
        assertEquals(requestSistemas3, pendingRequests.get(1));
    }

    @Test
    void testRejectRequestAndReorderPriorities() throws SirhaException {

        // Arrange - Recibir múltiples solicitudes
        decanateSistemas.receiveRequest(requestSistemas1);
        decanateSistemas.receiveRequest(requestSistemas2);

        assertEquals(2, decanateSistemas.getPendingRequests().size());

        // Act - Rechazar la primera solicitud
        decanateSistemas.rejectRequest(requestSistemas1);

        // Assert - Verificar estado de la solicitud rechazada
        assertEquals(RequestStateEnum.RECHAZADA, requestSistemas1.getActualState());
        
        // Verificar reordenamiento
        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        assertEquals(1, pendingRequests.size());
        assertEquals(1, requestSistemas2.getPriority()); // Ahora es prioridad 1
        assertEquals(1, decanateSistemas.getPendingRequests().size());

        // Verificar que la solicitud restante es correcta
        assertEquals(requestSistemas2, pendingRequests.get(0));
        assertEquals("FIS101", requestSistemas2.getSubject().getName());
    }

    @Test
    void testCanReceiveRequest() {

        // Assert - Solicitud de la misma carrera (Sistemas)
        assertTrue(decanateSistemas.canReceiveRequest(requestSistemas1));
        assertTrue(decanateSistemas.canReceiveRequest(requestSistemas2));
        
        // Assert - Solicitud de carrera diferente (Industrial)
        assertFalse(decanateSistemas.canReceiveRequest(requestIndustrial1));
        
        // Assert - Solicitud nula
        assertFalse(decanateSistemas.canReceiveRequest(null));

        // Assert - Verificar también la decanatura industrial
        assertTrue(decanateIndustrial.canReceiveRequest(requestIndustrial1));
        assertFalse(decanateIndustrial.canReceiveRequest(requestSistemas1));
    }

    @Test
    void testGroupChangeRequestDetails() throws SirhaException {

        // Act - Recibir solicitud
        decanateSistemas.receiveRequest(requestSistemas1);

        // Assert - Verificar detalles completos de la solicitud
        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        CambioGrupo cambioGrupo = (CambioGrupo) pendingRequests.get(0);

        // Verificar estudiante
        assertEquals(studentSistemas, cambioGrupo.getStudent());
        assertEquals(Careers.INGENIERIA_DE_SISTEMAS, cambioGrupo.getStudent().getCareer());

        // Verificar materia
        assertEquals("MAT101", cambioGrupo.getSubject().getName());
        assertEquals(4, cambioGrupo.getSubject().getCredits());

        // Verificar grupo destino
        assertEquals(grupoMatematicasB, cambioGrupo.getNewGroup());
        assertEquals(matematicas, cambioGrupo.getNewGroup().getCurso());
        assertEquals(30, cambioGrupo.getNewGroup().getCapacidad());
        assertEquals(currentPeriod, cambioGrupo.getNewGroup().getCurrentPeriod());

        // Verificar metadatos de la solicitud
        assertEquals(currentPeriod, cambioGrupo.getCurrentPeriod());
    }

    @Test
    void testHandleNullRequest() {
        // Act & Assert - Recibir solicitud nula
        assertThrows(SirhaException.class, () -> {
            decanateSistemas.approveRequest(null);
        });
        assertThrows(SirhaException.class, () -> {
            decanateSistemas.rejectRequest(requestIndustrial1);
        });
        SirhaException exception1 = assertThrows(SirhaException.class, () -> {
            decanateSistemas.receiveRequest(null);
        });
        assertTrue(exception1.getMessage().contains("Error de validación de datos"));

        // Act & Assert - Aprobar solicitud nula
        SirhaException exception2 = assertThrows(SirhaException.class, () -> {
            decanateSistemas.approveRequest(null);
        });
        assertTrue(exception2.getMessage().contains("La solicitud no puede ser nula") ||
                  exception2.getMessage().contains("VALIDATION_ERROR"));

        // Act & Assert - Rechazar solicitud nula
        SirhaException exception3 = assertThrows(SirhaException.class, () -> {
            decanateSistemas.rejectRequest(null);
        });
        assertTrue(exception3.getMessage().contains("La solicitud no puede ser nula") ||
                  exception3.getMessage().contains("VALIDATION_ERROR"));
    }

    @Test
    void testGroupCapacityValidation() {
        // Assert - Verificar que no se puede crear grupo con capacidad <= 0
        assertThrows(SirhaException.class, () -> {
            new Group(matematicas, 0, currentPeriod);
        });
        
        assertThrows(SirhaException.class, () -> {
            new Group(fisica, -5, currentPeriod);
        });
        
        // Assert - Verificar que sí se puede crear grupo con capacidad > 0
        assertDoesNotThrow(() -> {
            new Group(programacion, 15, currentPeriod);
        });
    }

    @Test
    void testCompleteRequestProcessingFlowWithRealGroups() throws SirhaException {

        // 1. Recibir solicitudes
        decanateSistemas.receiveRequest(requestSistemas1); 
        decanateSistemas.receiveRequest(requestSistemas2);
        assertEquals(2, decanateSistemas.getPendingRequests().size());
        
        // 2. Verificar prioridades iniciales
        assertEquals(1, requestSistemas1.getPriority());
        assertEquals(2, requestSistemas2.getPriority());
        
        // 3. Verificar detalles de las solicitudes y grupos
        CambioGrupo cambio1 = (CambioGrupo) requestSistemas1;
        assertEquals("MAT101", cambio1.getSubject().getName());
        assertEquals(grupoMatematicasB, cambio1.getNewGroup());
        assertEquals(matematicas, cambio1.getNewGroup().getCurso());

        CambioGrupo cambio2 = (CambioGrupo) requestSistemas2;
        assertEquals("FIS101", cambio2.getSubject().getName());
        assertEquals(grupoFisicaD, cambio2.getNewGroup());
        assertEquals(fisica, cambio2.getNewGroup().getCurso());
        
        // 4. Aprobar primera solicitud (Matemáticas)
        decanateSistemas.approveRequest(requestSistemas1);
        assertEquals(RequestStateEnum.APROBADA, requestSistemas1.getActualState());
        assertEquals(1, decanateSistemas.getPendingRequests().size());
        assertEquals(1, requestSistemas2.getPriority()); // Prioridad reajustada
        
        // 5. Verificar que la siguiente solicitud es Física
        BaseRequest nextRequest = decanateSistemas.getPendingRequests().get(0);
        assertEquals(requestSistemas2, nextRequest);
        CambioGrupo nextCambio = (CambioGrupo) nextRequest;
        assertEquals("FIS101", nextCambio.getSubject().getName());
        assertEquals(grupoFisicaD, nextCambio.getNewGroup());
        
        // 6. Rechazar segunda solicitud (Física)
        decanateSistemas.rejectRequest(requestSistemas2);
        assertEquals(RequestStateEnum.RECHAZADA, requestSistemas2.getActualState());
        assertEquals(0, decanateSistemas.getPendingRequests().size());
        
        // 7. Verificar que no hay más solicitudes pendientes
        assertEquals(0, decanateSistemas.getPendingRequests().size());
    }

    @Test
    void testMultipleDecanatesIndependentProcessing() throws SirhaException {

        // Act - Cada decanatura recibe solicitudes de su carrera
        decanateSistemas.receiveRequest(requestSistemas1);   // Sistemas: Matemáticas
        decanateSistemas.receiveRequest(requestSistemas2);   // Sistemas: Física
        decanateIndustrial.receiveRequest(requestIndustrial1); // Industrial: Química

        // Assert - Verificar estado independiente
        assertEquals(2, decanateSistemas.getPendingRequests().size());
        assertEquals(1, decanateIndustrial.getPendingRequests().size());

        // Verificar que cada decanatura tiene las solicitudes correctas
        List<BaseRequest> sistemasPending = decanateSistemas.getPendingRequests();
        assertEquals("MAT101", ((CambioGrupo) sistemasPending.get(0)).getSubject().getName());
        assertEquals("FIS101", ((CambioGrupo) sistemasPending.get(1)).getSubject().getName());

        List<BaseRequest> industrialPending = decanateIndustrial.getPendingRequests();
        assertEquals("QUI101", ((CambioGrupo) industrialPending.get(0)).getSubject().getName());

        // Procesar en una decanatura no debe afectar la otra
        decanateSistemas.approveRequest(requestSistemas1);
        assertEquals(1, decanateSistemas.getPendingRequests().size()); // Sistemas: 1 menos
        assertEquals(1, decanateIndustrial.getPendingRequests().size()); // Industrial: sin cambios
    }
}
