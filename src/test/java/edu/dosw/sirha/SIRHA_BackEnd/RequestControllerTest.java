package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.controller.RequestController;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para RequestController
 */
@SpringBootTest
class RequestControllerTest {

    private RequestController requestController;
    private RequestService requestService;
    private Student student;
    private AcademicPeriod academicPeriod;
    private Subject subject;
    private Group group;

    @BeforeEach
    void setUp() {
        requestService = mock(RequestService.class);
        requestController = new RequestController(requestService);
        
        student = new Student(1, "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        subject = new Subject(101, "Matemáticas", 4);
        group = new Group(30, academicPeriod);
    }

    @Test
    void testGetAll() {
        // Arrange
        CambioGrupo cambioGrupo1 = new CambioGrupo(student, subject, group, academicPeriod);
        CambioGrupo cambioGrupo2 = new CambioGrupo(student, subject, group, academicPeriod);
        List<BaseRequest> expectedRequests = Arrays.asList(cambioGrupo1, cambioGrupo2);
        
        when(requestService.findAll()).thenReturn(expectedRequests);
        
        // Act
        List<BaseRequest> actualRequests = requestController.getAll();
        
        // Assert
        assertEquals(2, actualRequests.size());
        assertEquals(expectedRequests, actualRequests);
        verify(requestService).findAll();
    }

    @Test
    void testGetAllEmptyList() {
        // Arrange
        when(requestService.findAll()).thenReturn(Arrays.asList());
        
        // Act
        List<BaseRequest> actualRequests = requestController.getAll();
        
        // Assert
        assertTrue(actualRequests.isEmpty());
        verify(requestService).findAll();
    }

    @Test
    void testCreate() {
        // Arrange
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, group, academicPeriod);
        
        when(requestService.save(cambioGrupo)).thenReturn(cambioGrupo);
        
        // Act
        BaseRequest createdRequest = requestController.create(cambioGrupo);
        
        // Assert
        assertEquals(cambioGrupo, createdRequest);
        verify(requestService).save(cambioGrupo);
    }

    @Test
    void testCreateCambioMateria() {
        // Arrange
        Subject oldSubject = new Subject(100, "Álgebra", 3);
        Subject newSubject = new Subject(101, "Cálculo", 4);
        CambioMateria cambioMateria = new CambioMateria(student, oldSubject, newSubject, academicPeriod);
        
        when(requestService.save(cambioMateria)).thenReturn(cambioMateria);
        
        // Act
        BaseRequest createdRequest = requestController.create(cambioMateria);
        
        // Assert
        assertNotNull(createdRequest);
        assertTrue(createdRequest instanceof CambioMateria);
        verify(requestService).save(cambioMateria);
    }

    @Test
    void testApprove() {
        // Arrange
        String requestId = "req123";
        
        doNothing().when(requestService).aprobarSolicitud(requestId);
        
        // Act
        assertDoesNotThrow(() -> requestController.approve(requestId));
        
        // Assert
        verify(requestService).aprobarSolicitud(requestId);
    }

    @Test
    void testApproveWithException() {
        // Arrange
        String requestId = "nonexistent";
        
        doThrow(new RuntimeException("Solicitud no encontrada"))
            .when(requestService).aprobarSolicitud(requestId);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> requestController.approve(requestId));
        
        assertEquals("Solicitud no encontrada", exception.getMessage());
        verify(requestService).aprobarSolicitud(requestId);
    }

    @Test
    void testReject() {
        // Arrange
        String requestId = "req123";
        
        doNothing().when(requestService).rechazarSolicitud(requestId);
        
        // Act
        assertDoesNotThrow(() -> requestController.reject(requestId));
        
        // Assert
        verify(requestService).rechazarSolicitud(requestId);
    }

    @Test
    void testRejectWithException() {
        // Arrange
        String requestId = "nonexistent";
        
        doThrow(new RuntimeException("Solicitud no encontrada"))
            .when(requestService).rechazarSolicitud(requestId);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> requestController.reject(requestId));
        
        assertEquals("Solicitud no encontrada", exception.getMessage());
        verify(requestService).rechazarSolicitud(requestId);
    }

    @Test
    void testControllerConstructor() {
        // Arrange
        RequestService mockService = mock(RequestService.class);
        
        // Act
        RequestController controller = new RequestController(mockService);
        
        // Assert
        assertNotNull(controller);
    }

    @Test
    void testMultipleOperations() {
        // Arrange
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, group, academicPeriod);
        String requestId = "req123";
        
        when(requestService.save(cambioGrupo)).thenReturn(cambioGrupo);
        when(requestService.findAll()).thenReturn(Arrays.asList(cambioGrupo));
        doNothing().when(requestService).aprobarSolicitud(requestId);
        
        // Act
        BaseRequest created = requestController.create(cambioGrupo);
        List<BaseRequest> all = requestController.getAll();
        requestController.approve(requestId);
        
        // Assert
        assertEquals(cambioGrupo, created);
        assertEquals(1, all.size());
        verify(requestService).save(cambioGrupo);
        verify(requestService).findAll();
        verify(requestService).aprobarSolicitud(requestId);
    }

    @Test
    void testWorkflowScenario() {
        // Arrange - Escenario completo de workflow
        String requestId = "workflow123";
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, group, academicPeriod);
        
        when(requestService.save(cambioGrupo)).thenReturn(cambioGrupo);
        when(requestService.findAll()).thenReturn(Arrays.asList(cambioGrupo));
        doNothing().when(requestService).aprobarSolicitud(requestId);
        
        // Act - Simular workflow completo
        // 1. Crear solicitud
        BaseRequest created = requestController.create(cambioGrupo);
        
        // 2. Listar todas las solicitudes
        List<BaseRequest> allRequests = requestController.getAll();
        
        // 3. Aprobar la solicitud
        requestController.approve(requestId);
        
        // Assert
        assertNotNull(created);
        assertEquals(1, allRequests.size());
        verify(requestService).save(cambioGrupo);
        verify(requestService).findAll();
        verify(requestService).aprobarSolicitud(requestId);
    }

    @Test
    void testCreateMultipleRequestTypes() {
        // Arrange
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, group, academicPeriod);
        Subject oldSubject = new Subject(100, "Álgebra", 3);
        Subject newSubject = new Subject(102, "Física", 3);
        CambioMateria cambioMateria = new CambioMateria(student, oldSubject, newSubject, academicPeriod);
        
        when(requestService.save(cambioGrupo)).thenReturn(cambioGrupo);
        when(requestService.save(cambioMateria)).thenReturn(cambioMateria);
        
        // Act
        BaseRequest createdGrupo = requestController.create(cambioGrupo);
        BaseRequest createdMateria = requestController.create(cambioMateria);
        
        // Assert
        assertTrue(createdGrupo instanceof CambioGrupo);
        assertTrue(createdMateria instanceof CambioMateria);
        verify(requestService).save(cambioGrupo);
        verify(requestService).save(cambioMateria);
    }

    @Test
    void testApproveAndRejectDifferentRequests() {
        // Arrange
        String approveId = "approve123";
        String rejectId = "reject456";
        
        doNothing().when(requestService).aprobarSolicitud(approveId);
        doNothing().when(requestService).rechazarSolicitud(rejectId);
        
        // Act
        assertDoesNotThrow(() -> requestController.approve(approveId));
        assertDoesNotThrow(() -> requestController.reject(rejectId));
        
        // Assert
        verify(requestService).aprobarSolicitud(approveId);
        verify(requestService).rechazarSolicitud(rejectId);
    }

    @Test
    void testServiceInteractionCount() {
        // Arrange
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, group, academicPeriod);
        
        when(requestService.save(cambioGrupo)).thenReturn(cambioGrupo);
        when(requestService.findAll()).thenReturn(Arrays.asList(cambioGrupo));
        
        // Act
        requestController.create(cambioGrupo);
        requestController.getAll();
        requestController.getAll(); // Segunda llamada
        
        // Assert
        verify(requestService, times(1)).save(cambioGrupo);
        verify(requestService, times(2)).findAll();
    }

    @Test
    void testNullInputHandling() {
        // Arrange
        when(requestService.save(null)).thenThrow(new IllegalArgumentException("Request cannot be null"));
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> requestController.create(null));
        
        assertEquals("Request cannot be null", exception.getMessage());
        verify(requestService).save(null);
    }
}