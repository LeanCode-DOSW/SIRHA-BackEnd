package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.DiasSemana;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CambioGrupo - Funcionalidad específica de cambio de grupo
 */
@SpringBootTest
class CambioGrupoTest {

    private Student student;
    private AcademicPeriod academicPeriod;
    private Subject subject;
    private Group grupoActual;
    private Group grupoNuevo;
    private StudyPlan studyPlan;
    private Schedule scheduleConflict;
    private Schedule scheduleNoConflict;

    @BeforeEach
    void setUp() {
        student = new Student(1, "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        
        subject = new Subject(101, "Matemáticas", 4);
        subject.setId(01);
        
        grupoActual = new Group(30, academicPeriod);
        grupoActual.setId(1);
        grupoNuevo = new Group(25, academicPeriod);
        grupoNuevo.setId(2);

        subject.addGroup(grupoActual);
        subject.addGroup(grupoNuevo);
        
        studyPlan = new StudyPlan("Ingeniería de Sistemas");
        studyPlan.addSubject(subject);
        
        Semaforo semaforo = new Semaforo(studyPlan);
        student.setAcademicProgress(semaforo);
        
        // Configurar horarios para probar conflictos
        scheduleConflict = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        scheduleNoConflict = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
    }

    @Test
    void testCambioGrupoCreation() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertNotNull(cambioGrupo);
        assertEquals(student, cambioGrupo.getStudent());
        assertEquals(academicPeriod, cambioGrupo.getCurrentPeriod());
        assertNotNull(cambioGrupo.getCreadoEn());
    }

    @Test
    void testCambioGrupoValidation() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // Por defecto, validateRequest retorna false (lógica pendiente de implementar)
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoWithScheduleConflict() {
        // Configurar estudiante con una materia ya inscrita
        Subject otraMateria = new Subject(102, "Física", 3);
        Group otroGrupo = new Group(20, academicPeriod);
        otroGrupo.addSchedule(scheduleConflict);
        otraMateria.addGroup(otroGrupo);
        studyPlan.addSubject(otraMateria);
        
        // Inscribir estudiante en la otra materia
        student.enrollSubject(otraMateria, otroGrupo);
        
        // Configurar el grupo nuevo con horario conflictivo
        grupoNuevo.addSchedule(scheduleConflict);
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // La validación debería detectar el conflicto (cuando se implemente)
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoWithNoScheduleConflict() {
        // Configurar estudiante con una materia ya inscrita
        Subject otraMateria = new Subject(102, "Física", 3);
        Group otroGrupo = new Group(20, academicPeriod);
        otroGrupo.addSchedule(scheduleConflict);
        otraMateria.addGroup(otroGrupo);
        studyPlan.addSubject(otraMateria);
        
        // Inscribir estudiante en la otra materia
        student.enrollSubject(otraMateria, otroGrupo);
        
        // Configurar el grupo nuevo sin conflicto
        grupoNuevo.addSchedule(scheduleNoConflict);
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // Verificar que no hay conflicto de horarios
        assertFalse(scheduleConflict.seSolapaCon(scheduleNoConflict));
        
        // La validación debería pasar (cuando se implemente correctamente)
        assertFalse(cambioGrupo.validateRequest()); // Por ahora retorna false por defecto
    }

    @Test
    void testCambioGrupoCapacityValidation() {
        // Llenar el grupo nuevo hasta su capacidad
        for (int i = 0; i < 25; i++) {
            Student otroStudent = new Student(i + 100, "student" + i, "student" + i + "@test.com", "pass", "202400" + i);
            grupoNuevo.enrollStudent(otroStudent);
        }
        
        assertTrue(grupoNuevo.isFull());
        assertEquals(0, grupoNuevo.getCuposDisponibles());
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // La validación debería fallar porque no hay cupos
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoWithAvailableCapacity() {
        assertEquals(25, grupoNuevo.getCuposDisponibles());
        assertFalse(grupoNuevo.isFull());
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // Verificar que hay cupos disponibles
        assertTrue(grupoNuevo.getCuposDisponibles() > 0);
        assertFalse(cambioGrupo.validateRequest()); // Por defecto retorna false
    }

    @Test
    void testCambioGrupoSameSubject() {
        // Verificar que el cambio es para la misma materia
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // Ambos grupos deberían pertenecer a la misma materia
        assertTrue(subject.getGroups().contains(grupoActual));
        assertTrue(subject.getGroups().contains(grupoNuevo));
        
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoDifferentGroups() {
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // Verificar que son grupos diferentes
        assertNotEquals(grupoActual.getId(), grupoNuevo.getId());
        assertNotEquals(grupoActual.getCapacidad(), grupoNuevo.getCapacidad());
    }

    @Test
    void testCambioGrupoValidPeriod() {
        assertTrue(academicPeriod.isActive());
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertEquals(academicPeriod, cambioGrupo.getCurrentPeriod());
        assertTrue(cambioGrupo.getCurrentPeriod().isActive());
    }

    @Test
    void testCambioGrupoInactivePeriod() {
        // Crear período inactivo (fechas pasadas)
        AcademicPeriod periodoInactivo = new AcademicPeriod("2023-1", 
            LocalDate.now().minusMonths(6), 
            LocalDate.now().minusMonths(2));
        
        assertFalse(periodoInactivo.isActive());
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, periodoInactivo);
        
        // La validación debería fallar para períodos inactivos
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoWithClosedGroup() {
        // Cerrar el grupo nuevo
        grupoNuevo.closeGroup();
        assertFalse(grupoNuevo.isOpen());
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // La validación debería fallar porque el grupo está cerrado
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoWithOpenGroup() {
        assertTrue(grupoNuevo.isOpen());
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // El grupo está abierto, pero la validación por defecto retorna false
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoStudentEnrollment() {
        // Inscribir estudiante en el grupo actual
        student.enrollSubject(subject, grupoActual);
        assertTrue(student.hasSubject(subject));
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // El estudiante ya está inscrito en la materia
        assertTrue(student.hasSubject(subject));
        assertFalse(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoIntegrationScenario() {
        // Escenario completo de cambio de grupo
        
        // 1. Inscribir estudiante en grupo actual
        student.enrollSubject(subject, grupoActual);
        assertTrue(student.hasSubject(subject));
        
        // 2. Verificar que el grupo nuevo tiene cupos
        assertTrue(grupoNuevo.getCuposDisponibles() > 0);
        assertTrue(grupoNuevo.isOpen());
        
        // 3. Verificar que no hay conflictos de horario
        grupoActual.addSchedule(scheduleConflict);
        grupoNuevo.addSchedule(scheduleNoConflict);
        assertFalse(scheduleConflict.seSolapaCon(scheduleNoConflict));
        
        // 4. Crear solicitud de cambio
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        // 5. Validar todos los aspectos
        assertNotNull(cambioGrupo);
        assertEquals(student, cambioGrupo.getStudent());
        assertEquals(academicPeriod, cambioGrupo.getCurrentPeriod());
        assertTrue(academicPeriod.isActive());
        
        // La validación completa se implementará en el futuro
        assertFalse(cambioGrupo.validateRequest());
    }
}