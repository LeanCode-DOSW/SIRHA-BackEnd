package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.*;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para todo el flujo de solicitudes académicas
 */
@SpringBootTest
class RequestIntegrationTest {

    private Student student;
    private AcademicPeriod academicPeriod;
    private StudyPlan studyPlan;
    private Subject mathematics;
    private Subject physics;
    private Subject chemistry;
    private Group mathGroup1;
    private Group mathGroup2;
    private Group physicsGroup;
    private Schedule mathSchedule1;
    private Schedule mathSchedule2;
    private Schedule physicsSchedule;

    @BeforeEach
    void setUp() {
        student = new Student("1", "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        
        mathematics = new Subject("101", "Matemáticas", 4);
        physics = new Subject("102", "Física", 3);
        chemistry = new Subject("103", "Química", 4);

        try {
            mathGroup1 = new Group(mathematics, 30, academicPeriod);
            mathGroup2 = new Group(mathematics, 10, academicPeriod);
            physicsGroup = new Group(physics, 20, academicPeriod);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }

        
        mathGroup1.setId("1001");
        mathGroup2.setId("1002");
        physicsGroup.setId("1003");

        mathSchedule1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        mathSchedule2 = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
        physicsSchedule = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0));
        try {
            mathGroup1.addSchedule(mathSchedule1);
            mathGroup2.addSchedule(mathSchedule2);
            physicsGroup.addSchedule(physicsSchedule);
        } catch (Exception e) {
            fail("No se esperaba una excepción al agregar horarios: " + e.getMessage());
        }
        
        studyPlan = new StudyPlan("Ingeniería de Sistemas", Careers.INGENIERIA_DE_SISTEMAS);
        studyPlan.addSubject(mathematics);
        studyPlan.addSubject(physics);
        studyPlan.addSubject(chemistry);
        
        Semaforo semaforo = new Semaforo(studyPlan);
        student.setAcademicProgress(semaforo);
        student.setCurrentPeriod(academicPeriod);
    }

    @Test
    void testCompleteGroupChangeWorkflow() {
        try {
            student.enrollSubject(mathematics, mathGroup1);
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertTrue(student.hasSubject(mathematics));
        assertTrue(mathGroup1.contieneEstudiante(student));
        
        CambioGrupo solicitudCambio = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        
        assertNotNull(solicitudCambio);
        assertEquals(student, solicitudCambio.getStudent());
        assertEquals(academicPeriod, solicitudCambio.getCurrentPeriod());
        assertNotNull(solicitudCambio.getCreadoEn());
        
        try{ 
            solicitudCambio.reviewRequest(new ResponseRequest("Revisando disponibilidad", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        assertEquals(2, solicitudCambio.getProcesos().size());
        
        assertTrue(mathGroup2.getCuposDisponibles() > 0);
        assertFalse(mathGroup2.isFull());
        assertTrue(mathGroup2.isOpen());
        
        assertFalse(mathSchedule1.seSolapaCon(mathSchedule2));
        
        try{ 
            solicitudCambio.approveRequest(new ResponseRequest("Cupos disponibles", RequestStateEnum.APROBADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getActualState());
        assertEquals(3, solicitudCambio.getProcesos().size());
        
        assertEquals(RequestStateEnum.PENDIENTE, solicitudCambio.getProcesos().get(0).getStatus());
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getProcesos().get(1).getStatus());
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getProcesos().get(2).getStatus());
    }

    @Test
    void testCompleteSubjectChangeWorkflow() {
        try{ 
            student.enrollSubject(mathematics, mathGroup1);
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertTrue(student.hasSubject(mathematics));

        CambioMateria solicitudCambio = new CambioMateria(student, mathematics, physics, mathGroup2, academicPeriod);

        assertNotNull(solicitudCambio);
        assertEquals(student, solicitudCambio.getStudent());
        assertEquals(academicPeriod, solicitudCambio.getCurrentPeriod());
        
        try{ 
            solicitudCambio.reviewRequest(new ResponseRequest("Revisando conflictos de horario", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        
        assertTrue(studyPlan.hasSubject(mathematics));
        assertTrue(studyPlan.hasSubject(physics));
        
        assertTrue(physicsGroup.getCuposDisponibles() > 0);
        assertTrue(physicsGroup.isOpen());
        
        assertFalse(mathSchedule1.seSolapaCon(physicsSchedule));

        try{ 
            solicitudCambio.approveRequest(new ResponseRequest("No hay conflictos - cambio aprobado", RequestStateEnum.APROBADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getActualState());
        
        assertEquals(3, solicitudCambio.getProcesos().size());
    }

    @Test
    void testRejectedRequestWorkflow() {
        try {
            for (int i = 0; i < 10; i++) {
                Student otroStudent = new Student(String.valueOf(i + 100), "student" + i, "student" + i + "@test.com", "pass", "202400" + i);
                mathGroup2.enrollStudent(otroStudent);
            }
        } catch (Exception e) {
            fail("No se esperaba una excepción al llenar el grupo: " + e.getMessage());
        }
        assertTrue(mathGroup2.isFull());
        assertEquals(0, mathGroup2.getCuposDisponibles());

        try {
            student.enrollSubject(mathematics, mathGroup1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir al estudiante en el grupo: " + e.getMessage());
        }

        CambioGrupo solicitudCambio = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        

        try{ 
            solicitudCambio.reviewRequest(new ResponseRequest("Revisando disponibilidad", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        
        try{ 
            solicitudCambio.rejectRequest(new ResponseRequest("No hay cupos disponibles", RequestStateEnum.RECHAZADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.RECHAZADA, solicitudCambio.getActualState());
        
        EstadoRechazada estadoFinal = new EstadoRechazada();
        assertThrows(SirhaException.class, () -> 
            estadoFinal.approveRequest( solicitudCambio, new ResponseRequest("Intentando aprobar una solicitud rechazada", RequestStateEnum.APROBADA)));
    }

    @Test
    void testSubjectWithPrerequisitesWorkflow() {
        Subject calculus = new Subject("201", "Cálculo", 4);
        studyPlan.addSubject(calculus);
        
        MustHaveApprovedSubject prerequisiteRule = new MustHaveApprovedSubject(mathematics);
        calculus.addPrerequisite(prerequisiteRule);
        
        assertTrue(calculus.hasPrerequisites());
        
        CambioMateria solicitudSinPrerequisito = new CambioMateria(student, physics, calculus, mathGroup2, academicPeriod);
        
        try{
            solicitudSinPrerequisito.reviewRequest(new ResponseRequest("Revisando sin prerequisitos", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

        try {
            solicitudSinPrerequisito.rejectRequest(new ResponseRequest("No cumple con los prerequisitos", RequestStateEnum.RECHAZADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.RECHAZADA, solicitudSinPrerequisito.getActualState());
        
        try { 
            student.enrollSubject(mathematics, mathGroup1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia requerida: " + e.getMessage());
        }
        CambioMateria solicitudConPrerequisito = new CambioMateria(student, physics, calculus, mathGroup2, academicPeriod);

        try{ 
            solicitudConPrerequisito.reviewRequest(new ResponseRequest("Revisando con prerequisitos", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

        try {
            solicitudConPrerequisito.approveRequest(new ResponseRequest("Cumple con los prerequisitos", RequestStateEnum.APROBADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

        assertEquals(RequestStateEnum.APROBADA, solicitudConPrerequisito.getActualState());
    }

    @Test
    void testScheduleConflictDetection() {
        Schedule conflictingSchedule = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));
        
        
        Subject conflictingSubject = new Subject("999", "Materia Conflictiva", 2);
        try {
            Group conflictingGroup = new Group(conflictingSubject, 15, academicPeriod);
            conflictingGroup.addSchedule(conflictingSchedule);
            studyPlan.addSubject(conflictingSubject);
            
            student.enrollSubject(mathematics, mathGroup1); // Lunes 8-10

            assertTrue(mathSchedule1.seSolapaCon(conflictingSchedule));

            CambioMateria solicitudConflicto = new CambioMateria(student, physics, conflictingSubject, conflictingGroup, academicPeriod);

            solicitudConflicto.reviewRequest(new ResponseRequest("Revisando conflictos de horario", RequestStateEnum.EN_REVISION));
            solicitudConflicto.rejectRequest(new ResponseRequest("Conflicto de horario detectado", RequestStateEnum.RECHAZADA));
            assertEquals(RequestStateEnum.RECHAZADA, solicitudConflicto.getActualState());
        } catch (Exception e) {
            fail("No se esperaba una excepción al preparar el escenario: " + e.getMessage());
        }
        
    
    }

    @Test
    void testMultipleRequestsForSameStudent() {
        CambioGrupo solicitud1 = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        try{
            solicitud1.reviewRequest(new ResponseRequest("Revisando primera solicitud", RequestStateEnum.EN_REVISION));
            solicitud1.approveRequest(new ResponseRequest("Primera solicitud aprobada", RequestStateEnum.APROBADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        CambioMateria solicitud2 = new CambioMateria(student, mathematics, physics, mathGroup2, academicPeriod);
        try {
            solicitud2.reviewRequest(new ResponseRequest("Revisando segunda solicitud", RequestStateEnum.EN_REVISION));
            solicitud2.approveRequest(new ResponseRequest("Segunda solicitud aprobada", RequestStateEnum.APROBADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }

        assertEquals(RequestStateEnum.APROBADA, solicitud1.getActualState());
        assertEquals(RequestStateEnum.APROBADA, solicitud2.getActualState());
        
        assertEquals(3, solicitud1.getProcesos().size());
        assertEquals(3, solicitud2.getProcesos().size());
    }

    @Test
    void testStateTransitionValidation() {
        CambioGrupo solicitud = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        
        assertEquals(RequestStateEnum.PENDIENTE, solicitud.getEnumState().getState());
        
        try{ 
            solicitud.reviewRequest(new ResponseRequest("Iniciando revisión", RequestStateEnum.EN_REVISION));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.EN_REVISION, solicitud.getActualState());

        try {
            solicitud.approveRequest(new ResponseRequest("Aprobada después de revisión", RequestStateEnum.APROBADA));
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        assertEquals(RequestStateEnum.APROBADA, solicitud.getActualState());
        
        EstadoAprobada estadoFinal = new EstadoAprobada();
        assertThrows(SirhaException.class, () -> 
            estadoFinal.rejectRequest(solicitud, new ResponseRequest("Intentando rechazar una solicitud aprobada", RequestStateEnum.RECHAZADA)));
    }

    @Test
    void testRequestValidationLogic() {

        try {
            student.enrollSubject(mathematics, mathGroup1);
        } catch (Exception e) {
            fail("No se esperaba una excepción aquí");
        }
        CambioGrupo solicitud = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        
        assertTrue(academicPeriod.isActive());
        assertTrue(mathGroup2.isOpen());
        assertTrue(mathGroup2.getCuposDisponibles() > 0);
        assertTrue(student.hasSubject(mathematics));
        assertFalse(mathSchedule1.seSolapaCon(mathSchedule2));
        
        assertTrue(solicitud.getCurrentPeriod().isActive());
    }
}