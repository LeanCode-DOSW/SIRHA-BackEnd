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
        student.setCurrentPeriod(academicPeriod);

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
    void testCambioGrupoCapacityValidation() {
        for (int i = 0; i < 25; i++) {
            Student otroStudent = new Student(i + 100, "student" + i, "student" + i + "@test.com", "pass", "202400" + i);
            grupoNuevo.enrollStudent(otroStudent);
        }
        
        assertTrue(grupoNuevo.isFull());
        assertEquals(0, grupoNuevo.getCuposDisponibles());
        student.enrollSubject(subject, grupoActual);
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            cambioGrupo.validateRequest();
        });

        assertNotNull(exception);
        assertEquals("El nuevo grupo está cerrado", exception.getMessage());
    }

    @Test
    void testCambioGrupoWithAvailableCapacity() {
        assertEquals(25, grupoNuevo.getCuposDisponibles());
        assertFalse(grupoNuevo.isFull());
        
        student.enrollSubject(subject, grupoActual);
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertTrue(grupoNuevo.getCuposDisponibles() > 0);
        assertTrue(cambioGrupo.validateRequest()); 
    }

    @Test
    void testCambioGrupoSameSubject() {
        student.enrollSubject(subject, grupoActual);
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertTrue(subject.getGroups().contains(grupoActual));
        assertTrue(subject.getGroups().contains(grupoNuevo));
        
        assertTrue(cambioGrupo.validateRequest());
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
        AcademicPeriod periodoInactivo = new AcademicPeriod("2023-1", 
            LocalDate.now().minusMonths(6), 
            LocalDate.now().minusMonths(2));

        student.enrollSubject(subject, grupoActual);
        
        assertFalse(periodoInactivo.isActive());
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, periodoInactivo);
        
        assertTrue(cambioGrupo.validateRequest());
    }



    @Test
    void testCambioGrupoStudentEnrollment() {
        student.enrollSubject(subject, grupoActual);
        assertTrue(student.hasSubject(subject));
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertTrue(student.hasSubject(subject));
        assertTrue(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoIntegrationScenario() {
        
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
        assertTrue(cambioGrupo.validateRequest());
    }
}