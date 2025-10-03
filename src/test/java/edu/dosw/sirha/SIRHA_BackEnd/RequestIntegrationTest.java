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
        // Configurar estudiante y período académico
        student = new Student(1, "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        
        // Configurar materias
        mathematics = new Subject(101, "Matemáticas", 4);
        physics = new Subject(102, "Física", 3);
        chemistry = new Subject(103, "Química", 4);
        
        // Configurar grupos
        mathGroup1 = new Group(30, academicPeriod);
        mathGroup2 = new Group(25, academicPeriod);
        physicsGroup = new Group(20, academicPeriod);
        
        mathGroup1.setId(1001);
        mathGroup2.setId(1002);
        physicsGroup.setId(1003);
        
        // Configurar horarios
        mathSchedule1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        mathSchedule2 = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
        physicsSchedule = new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0));
        
        mathGroup1.addSchedule(mathSchedule1);
        mathGroup2.addSchedule(mathSchedule2);
        physicsGroup.addSchedule(physicsSchedule);
        
        // Asociar grupos a materias
        mathematics.addGroup(mathGroup1);
        mathematics.addGroup(mathGroup2);
        physics.addGroup(physicsGroup);
        
        // Configurar plan de estudios
        studyPlan = new StudyPlan("Ingeniería de Sistemas");
        studyPlan.addSubject(mathematics);
        studyPlan.addSubject(physics);
        studyPlan.addSubject(chemistry);
        
        // Configurar progreso académico del estudiante
        Semaforo semaforo = new Semaforo(studyPlan);
        student.setAcademicProgress(semaforo);
        student.setCurrentPeriod(academicPeriod);
    }

    @Test
    void testCompleteGroupChangeWorkflow() {
        // FASE 1: Inscripción inicial
        // Inscribir estudiante en grupo de matemáticas 1
        student.enrollSubject(mathematics, mathGroup1);
        assertTrue(student.hasSubject(mathematics));
        assertTrue(mathGroup1.contieneEstudiante(student));
        
        // FASE 2: Crear solicitud de cambio de grupo
        CambioGrupo solicitudCambio = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        
        // Verificar datos de la solicitud
        assertNotNull(solicitudCambio);
        assertEquals(student, solicitudCambio.getStudent());
        assertEquals(academicPeriod, solicitudCambio.getCurrentPeriod());
        assertNotNull(solicitudCambio.getCreadoEn());
        
        // FASE 3: Procesar solicitud - Poner en revisión
        solicitudCambio.reviewRequest("Iniciando revisión de cambio de grupo");
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        assertEquals(2, solicitudCambio.getProcesos().size());
        
        // FASE 4: Validar condiciones para el cambio
        // Verificar que hay cupos en el grupo nuevo
        assertTrue(mathGroup2.getCuposDisponibles() > 0);
        assertFalse(mathGroup2.isFull());
        assertTrue(mathGroup2.isOpen());
        
        // Verificar que no hay conflictos de horario
        assertFalse(mathSchedule1.seSolapaCon(mathSchedule2));
        
        // FASE 5: Aprobar solicitud
        solicitudCambio.approveRequest("Cambio aprobado - cupos disponibles y sin conflictos");
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getActualState());
        assertEquals(3, solicitudCambio.getProcesos().size());
        
        // Verificar historial de procesos
        assertEquals(RequestStateEnum.PENDIENTE, solicitudCambio.getProcesos().get(0).getEstado());
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getProcesos().get(1).getEstado());
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getProcesos().get(2).getEstado());
    }

    @Test
    void testCompleteSubjectChangeWorkflow() {
        // FASE 1: Inscripción inicial en matemáticas
        student.enrollSubject(mathematics, mathGroup1);
        assertTrue(student.hasSubject(mathematics));
        
        // FASE 2: Crear solicitud de cambio de materia
        CambioMateria solicitudCambio = new CambioMateria(student, mathematics, physics, academicPeriod);
        
        // Verificar datos de la solicitud
        assertNotNull(solicitudCambio);
        assertEquals(student, solicitudCambio.getStudent());
        assertEquals(academicPeriod, solicitudCambio.getCurrentPeriod());
        
        // Poner en revisión
        solicitudCambio.reviewRequest("Revisando cambio de materia");
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        
        // FASE 4: Validaciones
        // Verificar que ambas materias están en el plan de estudios
        assertTrue(studyPlan.hasSubject(mathematics));
        assertTrue(studyPlan.hasSubject(physics));
        
        // Verificar que hay cupos en la materia nueva
        assertTrue(physicsGroup.getCuposDisponibles() > 0);
        assertTrue(physicsGroup.isOpen());
        
        // Verificar que no hay conflictos de horario
        assertFalse(mathSchedule1.seSolapaCon(physicsSchedule));
        
        // FASE 5: Aprobar solicitud
        solicitudCambio.approveRequest("Cambio de materia aprobado");
        assertEquals(RequestStateEnum.APROBADA, solicitudCambio.getActualState());
        
        // Verificar que se registró el proceso completo
        assertEquals(3, solicitudCambio.getProcesos().size());
    }

    @Test
    void testRejectedRequestWorkflow() {
        // FASE 1: Llenar completamente el grupo objetivo
        for (int i = 0; i < 25; i++) {
            Student otroStudent = new Student(i + 100, "student" + i, "student" + i + "@test.com", "pass", "202400" + i);
            mathGroup2.enrollStudent(otroStudent);
        }
        
        assertTrue(mathGroup2.isFull());
        assertEquals(0, mathGroup2.getCuposDisponibles());
        
        // FASE 2: Inscribir estudiante en grupo 1
        student.enrollSubject(mathematics, mathGroup1);
        
        // FASE 3: Crear solicitud de cambio al grupo lleno
        CambioGrupo solicitudCambio = new CambioGrupo(student, mathematics, mathGroup2, academicPeriod);
        
        // FASE 4: Procesar solicitud

        solicitudCambio.reviewRequest("Revisando disponibilidad");
        assertEquals(RequestStateEnum.EN_REVISION, solicitudCambio.getActualState());
        
        // FASE 5: Rechazar por falta de cupos
        solicitudCambio.rejectRequest("Grupo lleno - no hay cupos disponibles");
        assertEquals(RequestStateEnum.RECHAZADA, solicitudCambio.getActualState());
        
        // Verificar que no se puede modificar después del rechazo
        EstadoRechazada estadoFinal = new EstadoRechazada();
        assertThrows(IllegalStateException.class, () -> 
            estadoFinal.approveRequest(solicitudCambio));
    }

    @Test
    void testSubjectWithPrerequisitesWorkflow() {
        // FASE 1: Configurar prerequisitos
        Subject calculus = new Subject(201, "Cálculo", 4);
        Group calculusGroup = new Group(20, academicPeriod);
        calculus.addGroup(calculusGroup);
        studyPlan.addSubject(calculus);
        
        // Cálculo requiere Matemáticas
        MustHaveApprovedSubject prerequisiteRule = new MustHaveApprovedSubject(mathematics);
        calculus.addPrerequisite(prerequisiteRule);
        
        assertTrue(calculus.hasPrerequisites());
        
        // FASE 2: Intentar cambio sin prerequisito cumplido
        CambioMateria solicitudSinPrerequisito = new CambioMateria(student, physics, calculus, academicPeriod);
        
        solicitudSinPrerequisito.reviewRequest("Revisando prerequisitos");
        
        // FASE 3: Rechazar por prerequisitos no cumplidos
        solicitudSinPrerequisito.rejectRequest("No cumple prerequisitos: Matemáticas requerida");
        assertEquals(RequestStateEnum.RECHAZADA, solicitudSinPrerequisito.getActualState());
        
        // FASE 4: Cumplir prerequisito e intentar de nuevo
        student.enrollSubject(mathematics, mathGroup1);
        
        CambioMateria solicitudConPrerequisito = new CambioMateria(student, physics, calculus, academicPeriod);

        solicitudConPrerequisito.reviewRequest("Revisando con prerequisitos cumplidos");
        solicitudConPrerequisito.approveRequest("Prerequisitos cumplidos - cambio aprobado");
        
        assertEquals(RequestStateEnum.APROBADA, solicitudConPrerequisito.getActualState());
    }

    @Test
    void testScheduleConflictDetection() {
        // FASE 1: Crear horario con conflicto
        Schedule conflictingSchedule = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));
        Group conflictingGroup = new Group(15, academicPeriod);
        conflictingGroup.addSchedule(conflictingSchedule);
        
        Subject conflictingSubject = new Subject(999, "Materia Conflictiva", 2);
        conflictingSubject.addGroup(conflictingGroup);
        studyPlan.addSubject(conflictingSubject);
        
        // FASE 2: Inscribir estudiante en materia con horario base
        student.enrollSubject(mathematics, mathGroup1); // Lunes 8-10
        
        // FASE 3: Intentar inscripción en materia con conflicto
        // El horario conflictivo es Lunes 9-11, se solapa con Lunes 8-10
        assertTrue(mathSchedule1.seSolapaCon(conflictingSchedule));
        
        // FASE 4: Crear solicitud que generaría conflicto
        CambioMateria solicitudConflicto = new CambioMateria(student, physics, conflictingSubject, academicPeriod);
        
        solicitudConflicto.reviewRequest("Revisando conflictos de horario");
        
        // FASE 5: Rechazar por conflicto de horario
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
        
        // FASE 1: Estado inicial pendiente
        assertEquals(RequestStateEnum.PENDIENTE, solicitud.getEstado().getState());
        
        // FASE 2: Transición válida a revisión
        solicitud.reviewRequest("Iniciando revisión");
        assertEquals(RequestStateEnum.EN_REVISION, solicitud.getActualState());
        
        // FASE 3: Transición válida a aprobada
        solicitud.approveRequest("Aprobada después de revisión");
        assertEquals(RequestStateEnum.APROBADA, solicitud.getActualState());
        
        // FASE 4: Verificar que no se puede cambiar desde estado terminal
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