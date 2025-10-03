package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para BaseRequest y sus implementaciones concretas
 */
@SpringBootTest
class BaseRequestTest {

    private Student student;
    private AcademicPeriod academicPeriod;
    private Subject subject1;
    private Subject subject2;
    private Group group1;
    private Group group2;

    @BeforeEach
    void setUp() {
        student = new Student(1, "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        
        subject1 = new Subject(101, "Matemáticas", 4);
        subject2 = new Subject(102, "Física", 3);
        
        group1 = new Group(30, academicPeriod);
        group2 = new Group(25, academicPeriod);
        
        subject1.addGroup(group1);
        subject2.addGroup(group2);
    }

    @Test
    void testBaseRequestCreation() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);
        
        assertNotNull(cambioGrupo);
        assertNotNull(cambioGrupo.getCreadoEn());
        assertEquals(student, cambioGrupo.getStudent());
        assertEquals(academicPeriod, cambioGrupo.getCurrentPeriod());
        assertNotNull(cambioGrupo.getProcesos());
        assertFalse(cambioGrupo.getProcesos().isEmpty());
        assertEquals(1, cambioGrupo.getProcesos().size());
    }

    @Test
    void testCambioGrupoCreation() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);
        
        assertNotNull(cambioGrupo);
        assertEquals(student, cambioGrupo.getStudent());
        assertEquals(academicPeriod, cambioGrupo.getCurrentPeriod());
        
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioMateriaCreation() {
        CambioMateria cambioMateria = new CambioMateria(student, subject1, subject2, academicPeriod);
        
        assertNotNull(cambioMateria);
        assertEquals(student, cambioMateria.getStudent());
        assertEquals(academicPeriod, cambioMateria.getCurrentPeriod());
        
        assertFalse(cambioMateria.validateRequest());
    }

    @Test
    void testRequestStateManagement() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);
        assertEquals(RequestStateEnum.PENDIENTE, cambioGrupo.getActualState());
    }


    @Test
    void testApprovalProcess() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);

        cambioGrupo.reviewRequest("Iniciando revisión");
        
        assertEquals(RequestStateEnum.EN_REVISION, cambioGrupo.getActualState());
        assertEquals(EstadoEnRevision.class, cambioGrupo.getEstado().getClass());
        assertEquals(2, cambioGrupo.getProcesos().size());

        cambioGrupo.approveRequest("Solicitud aprobada por disponibilidad de cupos");
        
        assertEquals(RequestStateEnum.APROBADA, cambioGrupo.getActualState());
        assertEquals(3, cambioGrupo.getProcesos().size());
        
        ResponseProcess proceso = cambioGrupo.getActualProcess();
        assertEquals(RequestStateEnum.APROBADA, proceso.getEstado());
        assertEquals("Solicitud aprobada por disponibilidad de cupos", proceso.getComentario());
    }

    @Test
    void testRejectionProcess() {
        CambioMateria cambioMateria = new CambioMateria(student, subject1, subject2, academicPeriod);
        
        cambioMateria.reviewRequest("Iniciando revisión");

        cambioMateria.rejectRequest("No cumple con los prerequisitos");
        
        assertEquals(RequestStateEnum.RECHAZADA, cambioMateria.getActualState());
        assertEquals(3, cambioMateria.getProcesos().size());
        
        ResponseProcess proceso = cambioMateria.getActualProcess();
        assertEquals(RequestStateEnum.RECHAZADA, proceso.getEstado());
        assertEquals("No cumple con los prerequisitos", proceso.getComentario());
    }

    @Test
    void testReviewProcess() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);
        
        cambioGrupo.reviewRequest("Iniciando revisión");
                
        assertEquals(RequestStateEnum.EN_REVISION, cambioGrupo.getActualState());
        assertEquals(2, cambioGrupo.getProcesos().size());
        
        ResponseProcess proceso = cambioGrupo.getActualProcess();
        assertEquals(RequestStateEnum.EN_REVISION, proceso.getEstado());
        assertEquals("Iniciando revisión", proceso.getComentario());
    }

    @Test
    void testPendingProcess() {
        CambioMateria cambioMateria = new CambioMateria(student, subject1, subject2, academicPeriod);

        cambioMateria.reviewRequest("Iniciando revisión");

        assertThrows(IllegalStateException.class, () -> {
            cambioMateria.pendingRequest("Falta documentación");
        });
    }

    @Test
    void testMultipleProcesses() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);

        assertEquals(EstadoPendiente.class, cambioGrupo.getEstado().getClass());

        cambioGrupo.reviewRequest("Iniciando revisión");
        assertEquals(RequestStateEnum.EN_REVISION, cambioGrupo.getActualState());
        assertEquals(EstadoEnRevision.class, cambioGrupo.getEstado().getClass());
        assertEquals(2, cambioGrupo.getProcesos().size());
        

        cambioGrupo.approveRequest("Cupos disponibles");
        assertEquals(RequestStateEnum.APROBADA, cambioGrupo.getActualState());
        assertEquals(3, cambioGrupo.getProcesos().size());
        
        assertEquals(RequestStateEnum.PENDIENTE, cambioGrupo.getProcesos().get(0).getEstado());
        assertEquals(RequestStateEnum.EN_REVISION, cambioGrupo.getProcesos().get(1).getEstado());
        assertEquals(RequestStateEnum.APROBADA, cambioGrupo.getProcesos().get(2).getEstado());
    }

    @Test
    void testResponseProcessCreation() {
        ResponseProcess proceso = new ResponseProcess(RequestStateEnum.APROBADA, "Solicitud aprobada");
        
        assertNotNull(proceso);
        assertEquals(RequestStateEnum.APROBADA, proceso.getEstado());
        assertEquals("Solicitud aprobada", proceso.getComentario());
        assertNotNull(proceso.getCreadoEn());
    }

    @Test
    void testResponseProcessDefaultConstructor() {
        ResponseProcess proceso = new ResponseProcess();
        
        assertNotNull(proceso);
        assertNotNull(proceso.getCreadoEn());
        assertNull(proceso.getEstado());
        assertNull(proceso.getComentario());
    }

    @Test
    void testResponseProcessStateUpdate() {
        ResponseProcess proceso = new ResponseProcess();
        
        proceso.setEstado(RequestStateEnum.EN_REVISION, "Iniciando revisión");
        
        assertEquals(RequestStateEnum.EN_REVISION, proceso.getEstado());
        assertEquals("Iniciando revisión", proceso.getComentario());
    }

}