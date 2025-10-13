package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.*;
import edu.dosw.sirha.sirha_backend.domain.port.RequestProcess;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;

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
        student = new Student("1", "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

        subject1 = new Subject("101", "Matemáticas", 4);
        subject2 = new Subject("102", "Física", 3);
        try {
            group1 = new Group(subject1, 30, academicPeriod);
            group2 = new Group(subject2, 25, academicPeriod);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }

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
    void testCambioMateriaCreation() {
        CambioMateria cambioMateria = new CambioMateria(student, subject1, subject2, group1, academicPeriod);
        cambioMateria.setId("12345");
        
        assertNotNull(cambioMateria);
        assertEquals(student, cambioMateria.getStudent());
        assertEquals(academicPeriod, cambioMateria.getCurrentPeriod());
        assertNotNull(cambioMateria.getCreadoEn());
        assertNotNull(cambioMateria.getId());
        }

    @Test
    void testRequestStateManagement() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);
        assertEquals(RequestStateEnum.PENDIENTE, cambioGrupo.getActualState());
    }


    @Test
    void testApprovalProcess() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);
        try {
            cambioGrupo.reviewRequest(new ResponseRequest("Iniciando revisión", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        
        
        assertEquals(RequestStateEnum.EN_REVISION, cambioGrupo.getActualState());
        assertEquals(EstadoEnRevision.class, cambioGrupo.getEnumState().getClass());
        assertEquals(2, cambioGrupo.getProcesos().size());

        try {
            cambioGrupo.approveRequest(new ResponseRequest("Solicitud aprobada por disponibilidad de cupos", RequestStateEnum.APROBADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

        assertEquals(RequestStateEnum.APROBADA, cambioGrupo.getActualState());
        assertEquals(3, cambioGrupo.getProcesos().size());
        
        RequestProcess proceso = cambioGrupo.getActualProcess();
        assertEquals(RequestStateEnum.APROBADA, proceso.getStatus());
        assertEquals("Solicitud aprobada por disponibilidad de cupos", proceso.getComment());
    }

    @Test
    void testRejectionProcess() {
        CambioMateria cambioMateria = new CambioMateria(student, subject1, subject2, group1, academicPeriod);
        
        try{
            cambioMateria.reviewRequest(new ResponseRequest("Iniciando revisión", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        try {
            cambioMateria.rejectRequest(new ResponseRequest("No cumple con los prerequisitos", RequestStateEnum.RECHAZADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

        assertEquals(RequestStateEnum.RECHAZADA, cambioMateria.getActualState());
        assertEquals(3, cambioMateria.getProcesos().size());
        
        RequestProcess proceso = cambioMateria.getActualProcess();
        assertEquals(RequestStateEnum.RECHAZADA, proceso.getStatus());
        assertEquals("No cumple con los prerequisitos", proceso.getComment());
    }

    @Test
    void testReviewProcess() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);

        try {
            cambioGrupo.reviewRequest(new ResponseRequest("Iniciando revisión", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

        assertEquals(RequestStateEnum.EN_REVISION, cambioGrupo.getActualState());
        assertEquals(2, cambioGrupo.getProcesos().size());
        
        RequestProcess proceso = cambioGrupo.getActualProcess();
        assertEquals(RequestStateEnum.EN_REVISION, proceso.getStatus());
        assertEquals("Iniciando revisión", proceso.getComment());
    }

    @Test
    void testPendingProcess() {
        CambioMateria cambioMateria = new CambioMateria(student, subject1, subject2, group1, academicPeriod);

        try {
            cambioMateria.reviewRequest(new ResponseRequest("Iniciando revisión", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

    }

    @Test
    void testMultipleProcesses() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject1, group1, academicPeriod);

        assertEquals(EstadoPendiente.class, cambioGrupo.getEnumState().getClass());

        try{ 
            cambioGrupo.reviewRequest(new ResponseRequest("Iniciando revisión", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

        assertEquals(RequestStateEnum.EN_REVISION, cambioGrupo.getActualState());
        assertEquals(EstadoEnRevision.class, cambioGrupo.getEnumState().getClass());
        assertEquals(2, cambioGrupo.getProcesos().size());
        

        try{ 
            cambioGrupo.approveRequest(new ResponseRequest("Cupos disponibles", RequestStateEnum.APROBADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.APROBADA, cambioGrupo.getActualState());
        assertEquals(3, cambioGrupo.getProcesos().size());
        
        assertEquals(RequestStateEnum.PENDIENTE, cambioGrupo.getProcesos().get(0).getStatus());
        assertEquals(RequestStateEnum.EN_REVISION, cambioGrupo.getProcesos().get(1).getStatus());
        assertEquals(RequestStateEnum.APROBADA, cambioGrupo.getProcesos().get(2).getStatus());
    }

    @Test
    void testResponseProcessCreation() {
        ResponseProcess proceso = new ResponseProcess(RequestStateEnum.APROBADA, "Solicitud aprobada");
        
        assertNotNull(proceso);
        assertEquals(RequestStateEnum.APROBADA, proceso.getStatus());
        assertEquals("Solicitud aprobada", proceso.getComment());
        assertNotNull(proceso.getCreatedAt());
    }

    @Test
    void testResponseProcessDefaultConstructor() {
        ResponseProcess proceso = new ResponseProcess();
        
        assertNotNull(proceso);
        assertNotNull(proceso.getCreatedAt());
        assertNull(proceso.getStatus());
        assertNull(proceso.getComment());
    }

    @Test
    void testResponseProcessStateUpdate() {
        ResponseProcess proceso = new ResponseProcess();
        
        proceso.setStatus(RequestStateEnum.EN_REVISION, "Iniciando revisión");
        
        assertEquals(RequestStateEnum.EN_REVISION, proceso.getStatus());
        assertEquals("Iniciando revisión", proceso.getComment());
    }

}