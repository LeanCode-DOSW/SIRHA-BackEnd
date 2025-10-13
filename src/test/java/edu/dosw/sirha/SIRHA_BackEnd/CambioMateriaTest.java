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
 * Pruebas unitarias para CambioMateria - Funcionalidad específica de cambio de materia
 */
@SpringBootTest
class CambioMateriaTest {

    private Student student;
    private AcademicPeriod academicPeriod;
    private Subject materiaAntigua;
    private Subject materiaNueva;
    private Subject prerequisito;
    private Group grupoAntiguo;
    private Group grupoNuevo;
    private Group grupoPrerequisito;
    private StudyPlan studyPlan;
    private Schedule scheduleConflict;
    private Schedule scheduleNoConflict;

    @BeforeEach
    void setUp() {
        student = new Student("1", "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

        materiaAntigua = new Subject("101", "Álgebra", 3);
        materiaNueva = new Subject("102", "Cálculo", 4);
        prerequisito = new Subject("100", "Matemáticas Básicas", 3);
        try {
            grupoAntiguo = new Group(materiaAntigua, 30, academicPeriod);
            grupoNuevo = new Group(materiaNueva, 25, academicPeriod);
            grupoPrerequisito = new Group(prerequisito, 35, academicPeriod);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }

        studyPlan = new StudyPlan("Ingeniería de Sistemas", Careers.INGENIERIA_DE_SISTEMAS);
        studyPlan.addSubject(materiaAntigua);
        studyPlan.addSubject(materiaNueva);
        studyPlan.addSubject(prerequisito);
        
        Semaforo semaforo = new Semaforo(studyPlan);
        student.setAcademicProgress(semaforo);
        student.setCurrentPeriod(academicPeriod);
        
        // Configurar horarios para probar conflictos
        scheduleConflict = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        scheduleNoConflict = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
    }

    @Test
    void testCambioMateriaCreation() {
        CambioMateria cambioMateria = new CambioMateria(student, materiaAntigua, materiaNueva, grupoNuevo, academicPeriod);
        
        assertNotNull(cambioMateria);
        assertEquals(student, cambioMateria.getStudent());
        assertEquals(academicPeriod, cambioMateria.getCurrentPeriod());
        assertNotNull(cambioMateria.getCreadoEn());
        student.getAcademicProgress().getContadoresPorEstado();
    }

    @Test
    void testCambioMateriaWithPrerequisites() {
        // Configurar prerequisito para la materia nueva
        MustHaveApprovedSubject prerequisiteRule = new MustHaveApprovedSubject(prerequisito);
        materiaNueva.addPrerequisite(prerequisiteRule);
        
        assertTrue(materiaNueva.hasPrerequisites());
        
    }

    @Test
    void testCambioMateriaWithPrerequisitesMet() {
        // Configurar prerequisito para la materia nueva
        MustHaveApprovedSubject prerequisiteRule = new MustHaveApprovedSubject(prerequisito);
        materiaNueva.addPrerequisite(prerequisiteRule);
        
        // Inscribir y aprobar el prerequisito
        try{
            student.enrollSubject(prerequisito, grupoPrerequisito);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir el prerequisito: " + e.getMessage());
        }
        assertTrue(student.hasSubject(prerequisito));

    }


    @Test
    void testCambioMateriaCapacityValidation() {
        try{
            for (int i = 0; i < 25; i++) {
                Student otroStudent = new Student(String.valueOf(i + 100), "student" + i, "student" + i + "@test.com", "pass", "202400" + i);
                grupoNuevo.enrollStudent(otroStudent);
            }
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiantes en el grupo nuevo: " + e.getMessage());
        }
        
        assertTrue(grupoNuevo.isFull());
        assertEquals(0, grupoNuevo.getCuposDisponibles());
        
    }

    @Test
    void testCambioMateriaWithAvailableCapacity() {
        assertEquals(25, grupoNuevo.getCuposDisponibles());
        assertFalse(grupoNuevo.isFull());
        
        
        // Verificar que hay cupos disponibles
        assertTrue(grupoNuevo.getCuposDisponibles() > 0);
    }

    @Test
    void testCambioMateriaDifferentSubjects() {        
        assertNotEquals(materiaAntigua.getId(), materiaNueva.getId());
        assertNotEquals(materiaAntigua.getName(), materiaNueva.getName());
        assertNotEquals(materiaAntigua.getCredits(), materiaNueva.getCredits());
    }

    @Test
    void testCambioMateriaValidPeriod() {
        assertTrue(academicPeriod.isActive());
        
        CambioMateria cambioMateria = new CambioMateria(student, materiaAntigua, materiaNueva, grupoNuevo, academicPeriod);
        
        assertEquals(academicPeriod, cambioMateria.getCurrentPeriod());
        assertTrue(cambioMateria.getCurrentPeriod().isActive());
    }

    @Test
    void testCambioMateriaInactivePeriod() {
        // Crear período inactivo
        AcademicPeriod periodoInactivo = new AcademicPeriod("2023-1", 
            LocalDate.now().minusMonths(6), 
            LocalDate.now().minusMonths(2));
        
        assertFalse(periodoInactivo.isActive());
        
        
    }

    @Test
    void testCambioMateriaWithClosedGroup() {
        try{
            grupoNuevo.closeGroup();
        } catch (Exception e) {
            fail("No se esperaba una excepción al cerrar el grupo nuevo: " + e.getMessage());
        }
        assertFalse(grupoNuevo.isOpen());    
    }


    @Test
    void testCambioMateriaCreditsComparison() {
        
        assertEquals(3, materiaAntigua.getCredits());
        assertEquals(4, materiaNueva.getCredits());
        
        assertTrue(materiaNueva.getCredits() > materiaAntigua.getCredits());
    }

    @Test
    void testCambioMateriaStudyPlanValidation() {
        // Verificar que ambas materias están en el plan de estudios
        assertTrue(studyPlan.hasSubject(materiaAntigua));
        assertTrue(studyPlan.hasSubject(materiaNueva));
        assertTrue(studyPlan.getSubjects().containsValue(materiaAntigua));
        assertTrue(studyPlan.getSubjects().containsValue(materiaNueva));
    }

    @Test
    void testCambioMateriaSubjectNotInStudyPlan() {
        Subject materiaFueraPlan = new Subject("999", "Materia Externa", 2);
        try{

            assertFalse(studyPlan.hasSubject(materiaFueraPlan));

        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo externo: " + e.getMessage());
        }
    }

    @Test
    void testCambioMateriaScenario() {
        try {
            grupoAntiguo.addSchedule(scheduleConflict);
            grupoNuevo.addSchedule(scheduleNoConflict);
            student.enrollSubject(materiaAntigua, grupoAntiguo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al agregar horarios: " + e.getMessage());
        }

        
        assertTrue(student.hasSubject(materiaAntigua));
        
        assertTrue(grupoNuevo.getCuposDisponibles() > 0);
        assertTrue(grupoNuevo.isOpen());
        
        
        assertFalse(scheduleConflict.seSolapaCon(scheduleNoConflict));
        
        assertTrue(studyPlan.hasSubject(materiaAntigua));
        assertTrue(studyPlan.hasSubject(materiaNueva));

        CambioMateria solicitud = student.createSubjectChangeRequest(materiaAntigua, materiaNueva, grupoNuevo);
        assertNotNull(solicitud);
        assertEquals(student, solicitud.getStudent());
        assertEquals(materiaAntigua, solicitud.getOldSubject());
        assertEquals(materiaNueva, solicitud.getNewSubject());
        assertEquals(grupoNuevo, solicitud.getNewGroup());
        solicitud.validateRequest();
    }

    @Test
    void testValidateChangeSubjectSuccessfulCase() {
        try {
            grupoAntiguo.addSchedule(scheduleConflict);
            grupoNuevo.addSchedule(scheduleNoConflict);
            student.enrollSubject(materiaAntigua, grupoAntiguo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al agregar horarios: " + e.getMessage());
        }

        assertTrue(student.hasSubject(materiaAntigua));
        assertTrue(student.getAcademicProgress().isSubjectCursando(materiaAntigua));
        assertNotEquals(materiaAntigua, materiaNueva);
        assertTrue(student.getAcademicProgress().isSubjectNoCursada(materiaNueva));
        assertTrue(student.getAcademicProgress().getStudyPlan().hasSubject(materiaNueva));
        assertTrue(materiaNueva.hasGroup(grupoNuevo));
        assertTrue(grupoNuevo.isOpen());
        assertFalse(student.hasScheduleConflictWith(grupoNuevo));
        assertTrue(academicPeriod.isActive());
        assertTrue(grupoNuevo.sameAcademicPeriod(academicPeriod));
        
        assertTrue(student.validateChangeSubject(materiaAntigua, materiaNueva, grupoNuevo));
    }



    @Test
    void testCambioMateriaComplexPrerequisiteScenario() {
        // Crear cadena de prerequisitos: Básicas -> Álgebra -> Cálculo
        Subject matematicasBasicas = new Subject("90", "Matemáticas Básicas", 2);
        try {
            Group grupoBasicas = new Group(matematicasBasicas, 40, academicPeriod);

            studyPlan.addSubject(matematicasBasicas);
        
            MustHaveApprovedSubject ruleBasicas = new MustHaveApprovedSubject(matematicasBasicas);
            materiaAntigua.addPrerequisite(ruleBasicas);
            
            MustHaveApprovedSubject ruleAlgebra = new MustHaveApprovedSubject(materiaAntigua);
            materiaNueva.addPrerequisite(ruleAlgebra);
            
            student.enrollSubject(matematicasBasicas, grupoBasicas);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo de matemáticas básicas: " + e.getMessage());
        }
        
        
        assertThrows(IllegalStateException.class, () -> student.enrollSubject(materiaAntigua, grupoAntiguo));
        
        assertTrue(student.hasSubject(matematicasBasicas));
        assertTrue(student.hasSubject(materiaAntigua));

        assertTrue(materiaNueva.hasPrerequisites());
    }


    @Test
    void testError1_EstudianteNoTieneMateriaAntigua() {
        Subject materiaNoInscrita = new Subject("999", "Materia No Inscrita", 3);
        studyPlan.addSubject(materiaNoInscrita);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeSubject(materiaNoInscrita, materiaNueva, grupoNuevo);
        });
        assertEquals("El estudiante no tiene la materia antigua especificada", exception.getMessage());
    }

    @Test
    void testError2_MateriaAntiguaNoEstaEnCurso() {    
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeSubject(materiaAntigua, materiaNueva, grupoNuevo);
        });
        assertEquals("La materia antigua no está en curso", exception.getMessage());
    }

    @Test
    void testError3_MateriaNuevaEsLaMismaQueAntigua() {
        try {
            student.enrollSubject(materiaAntigua, grupoAntiguo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia antigua: " + e.getMessage());
        }
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeSubject(materiaAntigua, materiaAntigua, grupoAntiguo);
        });
        assertEquals("La materia nueva es la misma que la antigua", exception.getMessage());
    }

    @Test
    void testError4_EstudianteYaTieneMateriaNueva() {
        try {
            student.enrollSubject(materiaAntigua, grupoAntiguo);
            student.enrollSubject(materiaNueva, grupoNuevo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir las materias: " + e.getMessage());
        }
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeSubject(materiaAntigua, materiaNueva, grupoNuevo);
        });
        assertEquals("El estudiante ya tiene la materia nueva inscrita o aprobada", exception.getMessage());
    }

    @Test
    void testError5_MateriaNuevaNoEstaEnPlanEstudios() {
        try {
            student.enrollSubject(materiaAntigua, grupoAntiguo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia antigua: " + e.getMessage());
        }

        Subject materiaFueraPlan = new Subject("888", "Materia Externa", 2);
        try {
            Group grupoExterno = new Group(materiaFueraPlan, 15, academicPeriod);

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeSubject(materiaAntigua, materiaFueraPlan, grupoExterno);
            });
            assertEquals("La materia nueva no está en el plan de estudios del estudiante", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo externo: " + e.getMessage());
        }
        
        
    }

    @Test
    void testError6_NoSeCumplenPrerequisitos() {
        try {
            student.enrollSubject(materiaAntigua, grupoAntiguo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia antigua: " + e.getMessage());
        }

        MustHaveApprovedSubject prerequisiteRule = new MustHaveApprovedSubject(prerequisito);
        materiaNueva.addPrerequisite(prerequisiteRule);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeSubject(materiaAntigua, materiaNueva, grupoNuevo);
        });
        assertEquals("No se cumplen los prerrequisitos para inscribir la materia nueva", exception.getMessage());
    }

    @Test
    void testError7_GrupoNoPerteneceMaterianNueva() {
        // Error: El nuevo grupo no pertenece a la materia nueva especificada
        assertFalse(student.hasCoursesInProgress());
        try {
            student.enrollSubject(materiaAntigua, grupoAntiguo);
            assertTrue(student.hasCoursesInProgress());
            Group grupoIncorrecto = new Group(materiaAntigua, 20, academicPeriod);
            grupoIncorrecto.addSchedule(new Schedule(DiasSemana.DOMINGO, LocalTime.of(14, 0), LocalTime.of(16, 0)));
            
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                student.createSubjectChangeRequest(materiaAntigua, materiaNueva, grupoIncorrecto);
            });
            assertEquals("El nuevo grupo no pertenece a la materia nueva especificada", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia antigua o crear el grupo incorrecto: " + e.getMessage());
        }
        
    }

    @Test
    void testError8_GrupoNuevoEstaCerrado() {
        try {
            student.enrollSubject(materiaAntigua, grupoAntiguo);
            grupoNuevo.closeGroup();
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia antigua: " + e.getMessage());
        }
        
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeSubject(materiaAntigua, materiaNueva, grupoNuevo);
        });
        assertEquals("El nuevo grupo está cerrado", exception.getMessage());
    }

    @Test
    void testError9_ConflictoDeHorarios() {
        Schedule horarioConflicto = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        try {
            grupoAntiguo.addSchedule(horarioConflicto);
            grupoNuevo.addSchedule(horarioConflicto); 
            student.enrollSubject(materiaAntigua, grupoAntiguo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia antigua: " + e.getMessage());
        }
        
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            student.validateChangeSubject(materiaAntigua, materiaNueva, grupoNuevo);
        });
        assertEquals("Conflicto de horarios con el nuevo grupo", exception.getMessage());
    }

    @Test
    void testError10_PeriodoAcademicoInvalido() {
        try {
            student.enrollSubject(materiaAntigua, grupoAntiguo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia antigua: " + e.getMessage());
        }
        AcademicPeriod periodoInactivo = new AcademicPeriod("2023-1", 
            LocalDate.now().minusMonths(6), 
            LocalDate.now().minusMonths(2));
        try {
            Group grupoPerodoInvalido = new Group(materiaNueva, 20, periodoInactivo);
            
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                student.validateChangeSubject(materiaAntigua, materiaNueva, grupoPerodoInvalido);
            });
            assertEquals("El período académico no es válido para el nuevo grupo", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo con período inválido: " + e.getMessage());
        }
        
    }

    @Test
    void testError13_GrupoNoDifferentePeriodo() {
        try {
            student.enrollSubject(materiaAntigua, grupoAntiguo);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir la materia antigua: " + e.getMessage());
        }
        
        AcademicPeriod otroPeriodo = new AcademicPeriod("2024-2", 
            LocalDate.now().plusMonths(6), 
            LocalDate.now().plusMonths(10));
        try {
            Group grupoOtroPeriodo = new Group(materiaNueva, 20, otroPeriodo);        
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                student.validateChangeSubject(materiaAntigua, materiaNueva, grupoOtroPeriodo);

            });
            assertEquals("El período académico no es válido para el nuevo grupo", exception.getMessage());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo con otro período: " + e.getMessage());
        }
        
    }

    @Test
    void testValidateChangeSubjectCasoExitoso() {
        try {
            grupoAntiguo.addSchedule(scheduleConflict);
            grupoNuevo.addSchedule(scheduleNoConflict);
            student.enrollSubject(materiaAntigua, grupoAntiguo);     
        } catch (Exception e) {
            fail("No se esperaba una excepción al agregar horarios: " + e.getMessage());
        }
        
        assertTrue(student.hasSubject(materiaAntigua));
        assertTrue(student.getAcademicProgress().isSubjectCursando(materiaAntigua));
        assertNotEquals(materiaAntigua, materiaNueva);
        assertTrue(student.getAcademicProgress().isSubjectNoCursada(materiaNueva));
        assertTrue(student.getAcademicProgress().getStudyPlan().hasSubject(materiaNueva));
        assertTrue(materiaNueva.hasGroup(grupoNuevo));
        assertTrue(grupoNuevo.isOpen());
        assertFalse(student.hasScheduleConflictWith(grupoNuevo));
        assertTrue(academicPeriod.isActive());
        assertTrue(grupoNuevo.sameAcademicPeriod(academicPeriod));
        
        assertTrue(student.validateChangeSubject(materiaAntigua, materiaNueva, grupoNuevo));

        CambioMateria solicitud = student.createSubjectChangeRequest(materiaAntigua, materiaNueva, grupoNuevo);
        assertNotNull(solicitud);
        assertEquals(materiaAntigua, solicitud.getOldSubject());
        assertEquals(materiaNueva, solicitud.getNewSubject());
    }
}