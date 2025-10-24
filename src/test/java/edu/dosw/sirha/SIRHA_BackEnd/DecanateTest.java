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
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
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
    
    private Subject matematicas;
    private Subject fisica;
    private Subject programacion;
    private Subject quimica;
    
    private Group grupoMatematicasA;
    private Group grupoMatematicasB;
    private Group grupoFisicaD;
    private Group grupoProgramacion2;
    private Group grupoQuimicaY;
    
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
        
        requestSistemas1 = new CambioGrupo(studentSistemas, matematicas, grupoMatematicasB, currentPeriod);

        requestSistemas2 = new CambioGrupo(studentSistemas, fisica, grupoFisicaD, currentPeriod);

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
        assertNotNull(currentPeriod);
        assertEquals("2024-1", currentPeriod.getPeriod());

        assertNotNull(studentSistemas);
        assertEquals("juan.perez", studentSistemas.getUsername());
        assertEquals("2024001", studentSistemas.getCodigo());

        assertNotNull(matematicas);
        assertEquals("MAT101", matematicas.getName());
        assertEquals(4, matematicas.getCredits());

        assertNotNull(grupoMatematicasA);
        assertEquals(30, grupoMatematicasA.getCapacity());
        assertEquals(0, grupoMatematicasA.getInscritos());
        assertEquals(currentPeriod, grupoMatematicasA.getCurrentPeriod());
        assertNotNull(grupoMatematicasA.getCode());

        assertNotNull(requestSistemas1);
        assertEquals(studentSistemas, requestSistemas1.getStudent());
        assertEquals(matematicas, requestSistemas1.getSubject());
        assertEquals(grupoMatematicasB, requestSistemas1.getNewGroup());
        assertEquals(currentPeriod, requestSistemas1.getCurrentPeriod());
    }

    @Test
    void testGroupInitialState() {
        assertEquals(0, grupoMatematicasA.getInscritos());
        assertEquals(30, grupoMatematicasA.getCapacity());
        assertNotNull(grupoMatematicasA.getGroupState());
        assertNotNull(grupoMatematicasA.getEstudiantes());
        assertTrue(grupoMatematicasA.getEstudiantes().isEmpty());
        assertNotNull(grupoMatematicasA.getSchedules());
        assertEquals(currentPeriod, grupoMatematicasA.getCurrentPeriod());
    }

    @Test
    void testReceiveValidRequest() throws SirhaException {
        
        decanateSistemas.receiveRequest(requestSistemas1);

        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        assertEquals(1, pendingRequests.size());
        assertEquals(requestSistemas1, pendingRequests.get(0));
        assertEquals(1, requestSistemas1.getPriority());
        assertEquals(1, decanateSistemas.getPendingRequests().size());

        assertEquals(RequestStateEnum.EN_REVISION, requestSistemas1.getActualState());
    }

    @Test
    void testRejectRequestFromDifferentCareer() {
        
        SirhaException exception = assertThrows(SirhaException.class, () -> {
            decanateSistemas.receiveRequest(requestIndustrial1);
        });
        
        assertTrue(exception.getMessage().contains("Esta decanatura no puede recibir solicitudes") ||
                  exception.getMessage().contains("carrera") ||
                  exception.getMessage().contains("INVALID_CAREER"));
        assertEquals(0, decanateSistemas.getPendingRequests().size());
        assertTrue(decanateSistemas.getPendingRequests().isEmpty());
    }

    @Test
    void testAutomaticPriorityAssignment() throws SirhaException {
        
        decanateSistemas.receiveRequest(requestSistemas1);
        decanateSistemas.receiveRequest(requestSistemas2);

        assertEquals(1, requestSistemas1.getPriority());
        assertEquals(2, requestSistemas2.getPriority());
        
        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        assertEquals(2, pendingRequests.size());
        assertEquals(2, decanateSistemas.getPendingRequests().size());

        assertEquals(requestSistemas1, pendingRequests.get(0));
        assertEquals(requestSistemas2, pendingRequests.get(1));

        assertEquals("MAT101", requestSistemas1.getSubject().getName());
        assertEquals("FIS101", requestSistemas2.getSubject().getName());
    }

    @Test
    void testApproveRequestAndReorderPriorities() throws SirhaException {
        
        CambioGrupo requestSistemas3 = new CambioGrupo(studentSistemas, programacion, grupoProgramacion2, currentPeriod);

        decanateSistemas.receiveRequest(requestSistemas1);
        decanateSistemas.receiveRequest(requestSistemas2);
        decanateSistemas.receiveRequest(requestSistemas3);

        assertEquals(3, decanateSistemas.getPendingRequests().size());

        decanateSistemas.approveRequest(requestSistemas1);

        assertEquals(RequestStateEnum.APROBADA, requestSistemas1.getActualState());
        
        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        assertEquals(2, pendingRequests.size()); // Solo quedan 2 pendientes
        
        assertEquals(1, requestSistemas2.getPriority()); // La segunda ahora es prioridad 1
        assertEquals(2, requestSistemas3.getPriority()); // La tercera ahora es prioridad 2

        assertEquals(requestSistemas2, pendingRequests.get(0));
        assertEquals(requestSistemas3, pendingRequests.get(1));
    }

    @Test
    void testRejectRequestAndReorderPriorities() throws SirhaException {

        decanateSistemas.receiveRequest(requestSistemas1);
        decanateSistemas.receiveRequest(requestSistemas2);

        assertEquals(2, decanateSistemas.getPendingRequests().size());

        decanateSistemas.rejectRequest(requestSistemas1);

        assertEquals(RequestStateEnum.RECHAZADA, requestSistemas1.getActualState());
        
        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        assertEquals(1, pendingRequests.size());
        assertEquals(1, requestSistemas2.getPriority()); // Ahora es prioridad 1
        assertEquals(1, decanateSistemas.getPendingRequests().size());

        assertEquals(requestSistemas2, pendingRequests.get(0));
        assertEquals("FIS101", requestSistemas2.getSubject().getName());
    }

    @Test
    void testCanReceiveRequest() {

        assertTrue(decanateSistemas.canReceiveRequest(requestSistemas1));
        assertTrue(decanateSistemas.canReceiveRequest(requestSistemas2));
        
        assertFalse(decanateSistemas.canReceiveRequest(requestIndustrial1));
        
        assertFalse(decanateSistemas.canReceiveRequest(null));

        assertTrue(decanateIndustrial.canReceiveRequest(requestIndustrial1));
        assertFalse(decanateIndustrial.canReceiveRequest(requestSistemas1));
    }

    @Test
    void testGroupChangeRequestDetails() throws SirhaException {

        decanateSistemas.receiveRequest(requestSistemas1);

        List<BaseRequest> pendingRequests = decanateSistemas.getPendingRequests();
        CambioGrupo cambioGrupo = (CambioGrupo) pendingRequests.get(0);

        assertEquals(studentSistemas, cambioGrupo.getStudent());
        assertEquals(Careers.INGENIERIA_DE_SISTEMAS, cambioGrupo.getStudent().getCareer());

        assertEquals("MAT101", cambioGrupo.getSubject().getName());
        assertEquals(4, cambioGrupo.getSubject().getCredits());

        assertEquals(grupoMatematicasB, cambioGrupo.getNewGroup());
        assertEquals(30, cambioGrupo.getNewGroup().getCapacity());
        assertEquals(currentPeriod, cambioGrupo.getNewGroup().getCurrentPeriod());

        assertEquals(currentPeriod, cambioGrupo.getCurrentPeriod());
    }

    @Test
    void testHandleNullRequest() {
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

        SirhaException exception2 = assertThrows(SirhaException.class, () -> {
            decanateSistemas.approveRequest(null);
        });
        assertTrue(exception2.getMessage().contains("La solicitud no puede ser nula") ||
                  exception2.getMessage().contains("VALIDATION_ERROR"));

        SirhaException exception3 = assertThrows(SirhaException.class, () -> {
            decanateSistemas.rejectRequest(null);
        });
        assertTrue(exception3.getMessage().contains("La solicitud no puede ser nula") ||
                  exception3.getMessage().contains("VALIDATION_ERROR"));
    }

    @Test
    void testGroupCapacityValidation() {
        assertThrows(SirhaException.class, () -> {
            new Group(matematicas, 0, currentPeriod);
        });
        
        assertThrows(SirhaException.class, () -> {
            new Group(fisica, -5, currentPeriod);
        });
        
        assertDoesNotThrow(() -> {
            new Group(programacion, 15, currentPeriod);
        });
    }

    @Test
    void testCompleteRequestProcessingFlowWithRealGroups() throws SirhaException {

        decanateSistemas.receiveRequest(requestSistemas1); 
        decanateSistemas.receiveRequest(requestSistemas2);
        assertEquals(2, decanateSistemas.getPendingRequests().size());
        
        assertEquals(1, requestSistemas1.getPriority());
        assertEquals(2, requestSistemas2.getPriority());
        
        CambioGrupo cambio1 = (CambioGrupo) requestSistemas1;
        assertEquals("MAT101", cambio1.getSubject().getName());
        assertEquals(grupoMatematicasB, cambio1.getNewGroup());

        CambioGrupo cambio2 = (CambioGrupo) requestSistemas2;
        assertEquals("FIS101", cambio2.getSubject().getName());
        assertEquals(grupoFisicaD, cambio2.getNewGroup());
        
        decanateSistemas.approveRequest(requestSistemas1);
        assertEquals(RequestStateEnum.APROBADA, requestSistemas1.getActualState());
        assertEquals(1, decanateSistemas.getPendingRequests().size());
        assertEquals(1, requestSistemas2.getPriority()); // Prioridad reajustada
        
        BaseRequest nextRequest = decanateSistemas.getPendingRequests().get(0);
        assertEquals(requestSistemas2, nextRequest);
        CambioGrupo nextCambio = (CambioGrupo) nextRequest;
        assertEquals("FIS101", nextCambio.getSubject().getName());
        assertEquals(grupoFisicaD, nextCambio.getNewGroup());
        
        decanateSistemas.rejectRequest(requestSistemas2);
        assertEquals(RequestStateEnum.RECHAZADA, requestSistemas2.getActualState());
        assertEquals(0, decanateSistemas.getPendingRequests().size());
        
        assertEquals(0, decanateSistemas.getPendingRequests().size());
    }

    @Test
    void testMultipleDecanatesIndependentProcessing() throws SirhaException {

        decanateSistemas.receiveRequest(requestSistemas1);  
        decanateSistemas.receiveRequest(requestSistemas2);   
        decanateIndustrial.receiveRequest(requestIndustrial1);

        assertEquals(2, decanateSistemas.getPendingRequests().size());
        assertEquals(1, decanateIndustrial.getPendingRequests().size());

        List<BaseRequest> sistemasPending = decanateSistemas.getPendingRequests();
        assertEquals("MAT101", ((CambioGrupo) sistemasPending.get(0)).getSubject().getName());
        assertEquals("FIS101", ((CambioGrupo) sistemasPending.get(1)).getSubject().getName());

        List<BaseRequest> industrialPending = decanateIndustrial.getPendingRequests();
        assertEquals("QUI101", ((CambioGrupo) industrialPending.get(0)).getSubject().getName());

        decanateSistemas.approveRequest(requestSistemas1);
        assertEquals(1, decanateSistemas.getPendingRequests().size());
        assertEquals(1, decanateIndustrial.getPendingRequests().size());
    }
}
