package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.DiasSemana;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

        mathGroup1 = new Group(30, academicPeriod);
        mathGroup2 = new Group(25, academicPeriod);
        physicsGroup = new Group(20, academicPeriod);
        
        mathGroup1.setId(1001);
        mathGroup2.setId(1002);
        physicsGroup.setId(1003);
        
        mathSchedule1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        mathSchedule2 = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
        physicsSchedule = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0));
        
        mathGroup1.addSchedule(mathSchedule1);
        mathGroup2.addSchedule(mathSchedule2);
        physicsGroup.addSchedule(physicsSchedule);
        
        mathematics.addGroup(mathGroup1);
        mathematics.addGroup(mathGroup2);
        physics.addGroup(physicsGroup);
        
        studyPlan = new StudyPlan("Ingeniería de Sistemas");
        studyPlan.addSubject(mathematics);
        studyPlan.addSubject(physics);
        studyPlan.addSubject(chemistry);
        
        Semaforo semaforo = new Semaforo(studyPlan);
        student.setAcademicProgress(semaforo);
        student.setCurrentPeriod(academicPeriod);
    }

    @Test
    void testCompleteGroupChangeWorkflow() {
        student.enrollSubject(mathematics, mathGroup1);
        assertTrue(student.hasSubject(mathematics));
        assertTrue(mathGroup1.contieneEstudiante(student));
        
        CambioGrupo solicitudCambio = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        
        assertNotNull(solicitudCambio);
        assertEquals(student, solicitudCambio.getStudent());
        assertEquals(academicPeriod, solicitudCambio.getCurrentPeriod());
        assertNotNull(solicitudCambio.getCreadoEn());
        
        solicitudCambio.reviewRequest("Iniciando revisión de cambio de grupo");
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        assertEquals(2, solicitudCambio.getProcesos().size());
        
        assertTrue(mathGroup2.getCuposDisponibles() > 0);
        assertFalse(mathGroup2.isFull());
        assertTrue(mathGroup2.isOpen());
        
        assertFalse(mathSchedule1.seSolapaCon(mathSchedule2));
        
        solicitudCambio.approveRequest("Cambio aprobado - cupos disponibles y sin conflictos");
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getActualState());
        assertEquals(3, solicitudCambio.getProcesos().size());
        
        assertEquals(RequestStateEnum.PENDIENTE, solicitudCambio.getProcesos().get(0).getEstado());
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getProcesos().get(1).getEstado());
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getProcesos().get(2).getEstado());
    }

    @Test
    void testCompleteSubjectChangeWorkflow() {
        student.enrollSubject(mathematics, mathGroup1);
        assertTrue(student.hasSubject(mathematics));
        
        CambioMateria solicitudCambio = new CambioMateria(student, mathematics, physics, academicPeriod);
        
        assertNotNull(solicitudCambio);
        assertEquals(student, solicitudCambio.getStudent());
        assertEquals(academicPeriod, solicitudCambio.getCurrentPeriod());
        
        solicitudCambio.reviewRequest("Revisando cambio de materia");
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        
        assertTrue(studyPlan.hasSubject(mathematics));
        assertTrue(studyPlan.hasSubject(physics));
        
        assertTrue(physicsGroup.getCuposDisponibles() > 0);
        assertTrue(physicsGroup.isOpen());
        
        assertFalse(mathSchedule1.seSolapaCon(physicsSchedule));
        
        solicitudCambio.approveRequest("Cambio de materia aprobado");
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getActualState());
        
        assertEquals(3, solicitudCambio.getProcesos().size());
    }

    @Test
    void testRejectedRequestWorkflow() {
        for (int i = 0; i < 25; i++) {
            Student otroStudent = new Student(String.valueOf(i + 100), "student" + i, "student" + i + "@test.com", "pass", "202400" + i);
            mathGroup2.enrollStudent(otroStudent);
        }
        
        assertTrue(mathGroup2.isFull());
        assertEquals(0, mathGroup2.getCuposDisponibles());
        
        student.enrollSubject(mathematics, mathGroup1);
        
        CambioGrupo solicitudCambio = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        

        solicitudCambio.reviewRequest("Revisando disponibilidad");
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        
        solicitudCambio.rejectRequest("Grupo lleno - no hay cupos disponibles");
        assertEquals(RequestStateEnum.RECHAZADA, solicitudCambio.getActualState());
        
        EstadoRechazada estadoFinal = new EstadoRechazada();
        assertThrows(IllegalStateException.class, () -> 
            estadoFinal.approveRequest(solicitudCambio));
    }

    @Test
    void testSubjectWithPrerequisitesWorkflow() {
        Subject calculus = new Subject("201", "Cálculo", 4);
        Group calculusGroup = new Group(20, academicPeriod);
        calculus.addGroup(calculusGroup);
        studyPlan.addSubject(calculus);
        
        MustHaveApprovedSubject prerequisiteRule = new MustHaveApprovedSubject(mathematics);
        calculus.addPrerequisite(prerequisiteRule);
        
        assertTrue(calculus.hasPrerequisites());
        
        CambioMateria solicitudSinPrerequisito = new CambioMateria(student, physics, calculus, academicPeriod);
        
        solicitudSinPrerequisito.reviewRequest("Revisando prerequisitos");
        
        solicitudSinPrerequisito.rejectRequest("No cumple prerequisitos: Matemáticas requerida");
        assertEquals(RequestStateEnum.RECHAZADA, solicitudSinPrerequisito.getActualState());
        
        student.enrollSubject(mathematics, mathGroup1);
        
        CambioMateria solicitudConPrerequisito = new CambioMateria(student, physics, calculus, academicPeriod);

        solicitudConPrerequisito.reviewRequest("Revisando con prerequisitos cumplidos");
        solicitudConPrerequisito.approveRequest("Prerequisitos cumplidos - cambio aprobado");
        
        assertEquals(RequestStateEnum.APROBADA, solicitudConPrerequisito.getActualState());
    }

    @Test
    void testScheduleConflictDetection() {
        Schedule conflictingSchedule = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));
        Group conflictingGroup = new Group(15, academicPeriod);
        conflictingGroup.addSchedule(conflictingSchedule);
        
        Subject conflictingSubject = new Subject("999", "Materia Conflictiva", 2);
        conflictingSubject.addGroup(conflictingGroup);
        studyPlan.addSubject(conflictingSubject);
        
        student.enrollSubject(mathematics, mathGroup1); // Lunes 8-10
        
        assertTrue(mathSchedule1.seSolapaCon(conflictingSchedule));
        
        CambioMateria solicitudConflicto = new CambioMateria(student, physics, conflictingSubject, academicPeriod);
        
        solicitudConflicto.reviewRequest("Revisando conflictos de horario");
        
        solicitudConflicto.rejectRequest("Conflicto de horario detectado");
        assertEquals(RequestStateEnum.RECHAZADA, solicitudConflicto.getActualState());
    }

    @Test
    void testMultipleRequestsForSameStudent() {
        CambioGrupo solicitud1 = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        solicitud1.reviewRequest("Revisando primera solicitud");
        solicitud1.approveRequest("Primera solicitud aprobada");
        CambioMateria solicitud2 = new CambioMateria(student, mathematics, physics, academicPeriod);    
        solicitud2.reviewRequest("Revisando segunda solicitud");
        solicitud2.approveRequest("Segunda solicitud aprobada");
        
        assertEquals(RequestStateEnum.APROBADA, solicitud1.getActualState());
        assertEquals(RequestStateEnum.APROBADA, solicitud2.getActualState());
        
        assertEquals(3, solicitud1.getProcesos().size());
        assertEquals(3, solicitud2.getProcesos().size());
    }

    @Test
    void testStateTransitionValidation() {
        CambioGrupo solicitud = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        
        assertEquals(RequestStateEnum.PENDIENTE, solicitud.getEstado().getState());
        
        solicitud.reviewRequest("Iniciando revisión");
        assertEquals(RequestStateEnum.EN_REVISION, solicitud.getActualState());
        
        solicitud.approveRequest("Aprobada después de revisión");
        assertEquals(RequestStateEnum.APROBADA, solicitud.getActualState());
        
        EstadoAprobada estadoFinal = new EstadoAprobada();
        assertThrows(IllegalStateException.class, () -> 
            estadoFinal.rejectRequest(solicitud));
        assertThrows(IllegalStateException.class, () -> 
            estadoFinal.pendingRequest(solicitud));
    }

    @Test
    void testRequestValidationLogic() {

        student.enrollSubject(mathematics, mathGroup1);
        CambioGrupo solicitud = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        
        assertTrue(academicPeriod.isActive());
        assertTrue(mathGroup2.isOpen());
        assertTrue(mathGroup2.getCuposDisponibles() > 0);
        assertTrue(student.hasSubject(mathematics));
        assertFalse(mathSchedule1.seSolapaCon(mathSchedule2));
        
        assertTrue(solicitud.getCurrentPeriod().isActive());
    }
}