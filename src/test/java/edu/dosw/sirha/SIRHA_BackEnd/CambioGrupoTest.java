package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;

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
        student = new Student("1", "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        subject = new Subject("101", "Matemáticas", 4);
        subject.setId("01");
        try {
            grupoActual = new Group(subject, 30, academicPeriod);
            grupoActual.setId("1");
            grupoNuevo = new Group(subject, 25, academicPeriod);
            grupoNuevo.setId("2");
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
        
        studyPlan = new StudyPlan("Ingeniería de Sistemas", Careers.INGENIERIA_DE_SISTEMAS);
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
        student.getAcademicSummary();
        student.getAcademicPensum();
    }

    @Test
    void testCambioGrupoCapacityValidation() {
        try {
            for (int i = 0; i < 25; i++) {
                Student otroStudent = new Student(String.valueOf(i + 100), "student" + i, "student" + i + "@test.com", "pass", "202400" + i);
                grupoNuevo.enrollStudent(otroStudent);
            }
            assertTrue(grupoNuevo.isFull());
            assertEquals(0, grupoNuevo.getCuposDisponibles());
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiantes en el grupo nuevo: " + e.getMessage());
        }
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        

        IllegalStateException exception = assertThrows(IllegalStateException.class, cambioGrupo::validateRequest);

        assertNotNull(exception);
        assertEquals("El nuevo grupo está cerrado", exception.getMessage());
    }

    @Test
    void testCambioGrupoWithAvailableCapacity() {
        assertEquals(25, grupoNuevo.getCuposDisponibles());
        assertFalse(grupoNuevo.isFull());
        try {
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertTrue(grupoNuevo.getCuposDisponibles() > 0);
        assertTrue(cambioGrupo.validateRequest()); 
    }

    @Test
    void testCambioGrupoSameSubject() {
        try {
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertTrue(subject.getGroups().contains(grupoActual));
        assertTrue(subject.getGroups().contains(grupoNuevo));
        
        assertTrue(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoDifferentGroups() {
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
        try {
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }

        assertFalse(periodoInactivo.isActive());
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, periodoInactivo);
        
        assertTrue(cambioGrupo.validateRequest());
    }



    @Test
    void testCambioGrupoStudentEnrollment() {
        try {
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }
        assertTrue(student.hasSubject(subject));
        
        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertTrue(student.hasSubject(subject));
        assertTrue(cambioGrupo.validateRequest());
    }

    @Test
    void testCambioGrupoIntegrationScenario() {
        try {
            grupoActual.addSchedule(scheduleConflict);
            grupoNuevo.addSchedule(scheduleNoConflict);
            assertFalse(scheduleConflict.seSolapaCon(scheduleNoConflict));
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }
        assertTrue(student.hasSubject(subject));
        
        assertTrue(grupoNuevo.getCuposDisponibles() > 0);
        assertTrue(grupoNuevo.isOpen());

        CambioGrupo cambioGrupo = new CambioGrupo(student, subject, grupoNuevo, academicPeriod);
        
        assertNotNull(cambioGrupo);
        assertEquals(student, cambioGrupo.getStudent());
        assertEquals(academicPeriod, cambioGrupo.getCurrentPeriod());
        assertTrue(academicPeriod.isActive());
        
        assertTrue(cambioGrupo.validateRequest());
    }
    @Test
    void testValidateChangeGroup_Error1_EstudianteNoTieneMateria() {
        Subject materiaNoInscrita = new Subject("999", "Materia No Inscrita", 3);
        try {
            Group grupoNoInscrito = new Group(materiaNoInscrita, 20, academicPeriod);
            studyPlan.addSubject(materiaNoInscrita);
        
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                student.validateChangeGroup(materiaNoInscrita, grupoNoInscrito);
            });
            assertEquals("El estudiante no tiene la materia especificada", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo no inscrito: " + e.getMessage());
        } 
        
    }

    @Test
    void testValidateChangeGroup_Error3_GrupoNuevoEsMismoQueActual() {
        try {
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeGroup(subject, grupoActual); // Mismo grupo
        });
        assertEquals("El nuevo grupo es el mismo que el actual", exception.getMessage());
    }

    @Test
    void testValidateChangeGroup_Error4_GrupoNoPerteneceMateriaEspecificada() {
        try {
            student.enrollSubject(subject, grupoActual);
        
            Subject otraMateria = new Subject("102", "Física", 3);
            Group grupoExterno = new Group(otraMateria, 20, academicPeriod);
            grupoExterno.setId("999");
            
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                student.validateChangeGroup(subject, grupoExterno);
            });
            assertEquals("El grupo no pertenece a la materia especificada", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo externo: " + e.getMessage());
        }
    }

    @Test
    void testValidateChangeGroup_Error5_GrupoNuevoEstaCerrado() {
        try {
            student.enrollSubject(subject, grupoActual);
            grupoNuevo.closeGroup();
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }

        
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeGroup(subject, grupoNuevo);
        });
        assertEquals("El nuevo grupo está cerrado", exception.getMessage());
    }

    @Test
    void testValidateChangeGroup_Error6_ConflictoDeHorarios() {
        // Error: Conflicto de horarios con el nuevo grupo
        try { 
            grupoActual.addSchedule(scheduleConflict);
            grupoNuevo.addSchedule(scheduleConflict); // Mismo horario = conflicto
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeGroup(subject, grupoNuevo);
        });
        assertEquals("Conflicto de horarios con el nuevo grupo", exception.getMessage());
    }



    @Test
    void testValidateChangeGroup_Error9_GrupoNoDifferentePeriodo() {
        try {
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }
        AcademicPeriod otroPeriodo = new AcademicPeriod("2024-2", 
            LocalDate.now().plusMonths(6), 
            LocalDate.now().plusMonths(10));
        try {
            Group grupoOtroPeriodo = new Group(subject, 20, otroPeriodo);
            grupoOtroPeriodo.setId("888");
            
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                student.validateChangeGroup(subject, grupoOtroPeriodo);
            });
            assertEquals("El período académico no es válido para el nuevo grupo", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo con otro período: " + e.getMessage());
        }
        
    }

    @Test
    void testValidateChangeGroup_CasoExitoso() {
        try {
            
            
            grupoActual.addSchedule(scheduleConflict);
            grupoNuevo.addSchedule(scheduleNoConflict);

            student.enrollSubject(subject, grupoActual);
            assertTrue(student.hasSubject(subject));
            assertTrue(student.getAcademicProgress().isSubjectCursando(subject));
            assertNotEquals(grupoActual.getId(), grupoNuevo.getId());
            assertTrue(subject.hasGroup(grupoNuevo));
            assertTrue(grupoNuevo.isOpen());
            assertFalse(student.hasScheduleConflictWith(grupoNuevo));
            assertTrue(academicPeriod.isActive());
            assertTrue(grupoNuevo.sameAcademicPeriod(academicPeriod));
            
            assertTrue(student.validateChangeGroup(subject, grupoNuevo));
        } catch (Exception e) {
            fail("No se esperaba una excepción al agregar horarios o inscribir la materia: " + e.getMessage());
        }
    }

    @Test
    void testValidateChangeGroup_GrupoLlenoEstaCerrado() {
        try {
            student.enrollSubject(subject, grupoActual);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el estudiante en el grupo actual: " + e.getMessage());
        }

        try {
            Group grupoLleno = new Group(subject, 2, academicPeriod);
            grupoLleno.setId("777");

            Student otroStudent1 = new Student("100", "otro1", "otro1@test.com", "pass", "OTR001");
            Student otroStudent2 = new Student("101", "otro2", "otro2@test.com", "pass", "OTR002");
            
            grupoLleno.enrollStudent(otroStudent1);
            grupoLleno.enrollStudent(otroStudent2);
            
            assertTrue(grupoLleno.isFull());
            assertFalse(grupoLleno.isOpen());
            
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                student.validateChangeGroup(subject, grupoLleno);
            });
            assertEquals("El nuevo grupo está cerrado", exception.getMessage());
            assertThrows(IllegalArgumentException.class, () -> { student.setEmail("fallo"); });
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo lleno: " + e.getMessage());
        }
        
        
        
    }

}