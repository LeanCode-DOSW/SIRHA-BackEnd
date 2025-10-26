package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.StatusClosed;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.StatusOpen;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupTests {

    private AcademicPeriod academicPeriod;
    private Group group;
    private Student student1;
    private Student student2;
    private Student student3;
    private Professor professor;
    private Subject subject;
    private Schedule schedule1;
    private Schedule schedule2;
    private Schedule scheduleConflict;

    @BeforeEach
    void setUp() {
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        try {
            student1 = new Student("jacobo", "jacobo@test.com", "hash123", "20231001");
            student2 = new Student("maria", "maria@test.com", "hash456", "20231002");
            student3 = new Student("carlos", "carlos@test.com", "hash789", "20231003");
            
            professor = new Professor("Dr. Smith", "smith@university.edu", "hash");
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los usuarios: " + e.getMessage());
        }
        
        try {
            subject = new Subject("101", "Cálculo I", 4);
            group = new Group(subject, 5, academicPeriod);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
        try {
            schedule1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
            schedule2 = new Schedule(DiasSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0));
            scheduleConflict = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los horarios: " + e.getMessage());
        }
    }


    @Test
    void groupTest() {
        assertEquals(5, group.getCapacity());
        assertTrue(group.getGroupState() instanceof StatusOpen);
        assertEquals(0, group.getInscritos());
        assertNotNull(group.getEstudiantes());
        assertTrue(group.getEstudiantes().isEmpty());
    }

    @Test
    void capacidadInvalidaTest() {
        assertThrows(SirhaException.class, () -> new Group(subject, 0, academicPeriod));
        assertThrows(SirhaException.class, () -> new Group(subject, -5, academicPeriod));
    }

    @Test
    void verificateIdTest() {
        group.setId("11111");
        assertEquals("11111", group.getId());
    }

    @Test
    void verificarAulaTest() {
        group.setAula("Bloque A");
        assertEquals("Bloque A", group.getAula());
    }

    @Test
    void setProfessorTest() {
        group.setProfessor(professor);
        assertEquals(professor, group.getProfessor());
    }

    @Test
    void estadoInicialEsAbiertoTest() {
        assertTrue(group.getGroupState() instanceof StatusOpen);
    }

    @Test
    void setEstadoTest() {
        try {
            group.closeGroup();
        } catch (Exception e) {
            fail("No se esperaba una excepción al cambiar el estado: " + e.getMessage());
        }
        assertEquals(StatusClosed.class, group.getGroupState().getClass());
    }

    @Test
    void testGroupStatusClosedWhenFull() {
        try {
            student1.generateCompleteReport();
            student1.getTotalSubjectsCount();
            student1.getCurrentSchedule();
            assertFalse(student1.isSubjectApproved(subject.getName()));
            assertFalse(student1.isSubjectCursando(subject.getName()));
            assertFalse(student1.isSubjectNoCursada(subject.getName()));
            assertFalse(student1.isSubjectReprobada(subject.getName()));

            StudyPlan studyPlan = new StudyPlan(Careers.INGENIERIA_AMBIENTAL);
            Group smallGroup = new Group(subject, 2, academicPeriod);
            assertThrows(SirhaException.class, () -> subject.addGroup(smallGroup));
            smallGroup.addSchedule(schedule1);
            smallGroup.addSchedule(schedule2);

            studyPlan.addSubject(subject);

            Semaforo semaforo = new Semaforo(studyPlan);
            semaforo.setCurrentAcademicPeriod(academicPeriod);
            student1.setAcademicProgress(semaforo);

            assertFalse(student1.isSubjectApproved(subject.getName()));
            assertFalse(student1.isSubjectCursando(subject.getName()));
            assertTrue(student1.isSubjectNoCursada(subject.getName()));
            assertFalse(student1.isSubjectReprobada(subject.getName()));
            
            student1.enrollSubject(subject, smallGroup);

            smallGroup.inscribirEstudiante(student2);
            assertTrue(smallGroup.getGroupState() instanceof StatusClosed);
            assertTrue(smallGroup.isFull());
            assertEquals(0, smallGroup.getCuposDisponibles());
            
            student1.getAllSchedules();
            student1.getScheduleForPeriod(academicPeriod);
            student1.getRequestsHistory();
            student1.getRequestApprovalRate();
            student1.getRequestById("nonexistent-id");
            student1.getSubjectsByColorCount(SemaforoColores.AMARILLO);
            student1.hasActiveRequests();

            assertThrows(SirhaException.class, () -> 
                smallGroup.inscribirEstudiante(student3));

            assertFalse(student1.isSubjectApproved(subject.getName()));
            assertTrue(student1.isSubjectCursando(subject.getName()));
            assertFalse(student1.isSubjectNoCursada(subject.getName()));
            assertFalse(student1.isSubjectReprobada(subject.getName()));
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante: " + e.getMessage());
        }
        
    }

    @Test
    void testGroupDesinscribirEstudiante() {
        try {
            Group fullGroup = new Group(subject, 2, academicPeriod);
            
            fullGroup.inscribirEstudiante(student1);
            fullGroup.inscribirEstudiante(student2);
            assertTrue(fullGroup.getGroupState() instanceof StatusClosed);

            fullGroup.unenrollStudent(student1);
            assertEquals(1, fullGroup.getInscritos());
            assertTrue(fullGroup.getGroupState() instanceof StatusOpen);
            assertFalse(fullGroup.isFull());
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir/desinscribir estudiante: " + e.getMessage());
        }
    }


    @Test
    void inscribirEstudianteTest() {
        try {
            group.enrollStudent(student1);
            List<Student> estudiantes = group.getEstudiantes();

            assertTrue(estudiantes.contains(student1));
            assertEquals(1, group.getInscritos());
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante: " + e.getMessage());
        }
        assertEquals(4, group.getCuposDisponibles());
    }

    @Test
    void inscribirEstudianteViaStatePatternTest() {
        try {
            group.inscribirEstudiante(student1);

            assertTrue(group.contieneEstudiante(student1));
            assertEquals(1, group.getInscritos());
            assertEquals(4, group.getCuposDisponibles());

            group.inscribirEstudiante(student2);

            assertTrue(group.contieneEstudiante(student2));
            assertEquals(2, group.getInscritos());
            assertEquals(3, group.getCuposDisponibles());

            assertThrows(SirhaException.class, () -> group.setCapacidad(1));
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante: " + e.getMessage());
        }
    }

    @Test
    void enrollStudentDuplicadoTest() {
        try {
            group.enrollStudent(student1);
            assertThrows(SirhaException.class, () -> group.enrollStudent(student1));
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante duplicado: " + e.getMessage());
        }
    }

    @Test
    void inscribirEstudianteNullTest() {
        assertThrows(SirhaException.class, () -> group.inscribirEstudiante(null));
    }

    @Test
    void unenrollStudentTest() {
        try {
            group.enrollStudent(student1);
            group.enrollStudent(student2);

            boolean removido = group.unenrollStudent(student1);

            assertTrue(removido);
            assertEquals(1, group.getInscritos());
            assertFalse(group.contieneEstudiante(student1));
            assertTrue(group.contieneEstudiante(student2));
        } catch (Exception e) {
            fail("No se esperaba una excepción al desinscribir estudiante: " + e.getMessage());
        }
    }

    @Test
    void unenrollStudentNoExisteTest() {
        try{
            group.enrollStudent(student1);
            assertThrows(SirhaException.class, () -> group.unenrollStudent(student2));
            assertEquals(1, group.getInscritos());
        } catch (Exception e) {
            fail("No se esperaba una excepción al desinscribir estudiante no inscrito: " + e.getMessage());
        }
    }

    @Test
    void contieneEstudianteTest() {
        try {
            group.enrollStudent(student1);
            assertTrue(group.contieneEstudiante(student1));
            assertFalse(group.contieneEstudiante(student2));
        } catch (Exception e) {
            fail("No se esperaba una excepción al verificar si el grupo contiene estudiante: " + e.getMessage());
        }
    }

    @Test
    void contieneEstudianteNullTest() {
        assertFalse(group.contieneEstudiante(null));
    }

    @Test
    void cuposDisponiblesTest() {
        try {
            group.enrollStudent(student1);
            assertEquals(4, group.getCuposDisponibles());
        } catch (Exception e) {
            fail("No se esperaba una excepción al calcular cupos disponibles: " + e.getMessage());
        }
    }

    @Test
    void isFullTest() {
        try {
            Group smallGroup = new Group(subject, 2, academicPeriod);
            smallGroup.enrollStudent(student1);
            smallGroup.enrollStudent(student2);
            assertTrue(smallGroup.isFull());
        } catch (Exception e) {
            fail("No se esperaba una excepción al verificar si el grupo está lleno: " + e.getMessage());
        }
    }

    @Test
    void invalidisFullTest() {
        try {
            group.enrollStudent(student1);
            group.enrollStudent(student2);
            assertFalse(group.isFull());
        } catch (Exception e) {
            fail("No se esperaba una excepción al verificar si el grupo no está lleno: " + e.getMessage());
        }
    }

    @Test
    void cuposDisponiblesNuncaNegativoTest() {
        try {
            for(int i = 0; i < 5; i++) {
                Student s = new Student("student" + i, "email" + i + "@test.com", "hash", "202310" + String.format("%02d", i));
                group.enrollStudent(s);
            }

            assertEquals(0, group.getCuposDisponibles());
            assertTrue(group.getCuposDisponibles() >= 0);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiantes: " + e.getMessage());
        }
    }

    @Test
    void setCapacidadConEstudiantesInscritosTest() {
        try {
            group.enrollStudent(student1);
            assertThrows(SirhaException.class, () -> group.setCapacidad(10));
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante: " + e.getMessage());
        }
    }

    @Test
    void setCapacidadMenorQueInscritosTest() {
        try {
            group.enrollStudent(student1);
            group.enrollStudent(student2);
            
            group.unenrollStudent(student1);
            group.unenrollStudent(student2);
            
            group.enrollStudent(student1);

            assertThrows(SirhaException.class, () -> group.setCapacidad(0));
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante: " + e.getMessage());
        }
    }

    @Test
    void setCapacidadInvalidaTest() {
        assertThrows(SirhaException.class, () -> group.setCapacidad(0));
        assertThrows(SirhaException.class, () -> group.setCapacidad(-1));
    }


    @Test
    void testGroupScheduleConflicts() {
        try {
            Group group1 = new Group(subject, 10, academicPeriod);
            Group group2 = new Group(subject, 10, academicPeriod);
            
            group1.addSchedule(schedule1);

            assertTrue(group1.hasSchedule(schedule1));
            group2.addSchedule(scheduleConflict); // Se solapa con schedule1
            
            assertTrue(group1.conflictoConHorario(group2));
            assertTrue(group2.conflictoConHorario(group1));

            assertThrows(SirhaException.class, () -> group1.addSchedule(scheduleConflict));
            assertFalse(group1.hasSchedule(scheduleConflict));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
    }

    @Test
    void testGroupNoScheduleConflicts() {
        try {
            Group group1 = new Group(subject, 10, academicPeriod);
            Group group2 = new Group(subject, 10, academicPeriod);

            group1.addSchedule(schedule1); // Lunes 8-10
            group2.addSchedule(schedule2); // Martes 10-12
            
            assertFalse(group1.conflictoConHorario(group2));
            assertFalse(group2.conflictoConHorario(group1));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
    }


    @Test
    void notEqualsTest() {
        try {
            Group group1 = new Group(subject, 5, academicPeriod);
            Group group2 = new Group(subject, 5, academicPeriod);

            group1.setId("1");
            group2.setId("2");

            assertNotEquals(group1, group2);
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos: " + e.getMessage());
        }
    }

    @Test
    void getEstudiantesInmutableTest() {
        try {
            group.enrollStudent(student1);
            List<Student> estudiantes = group.getEstudiantes();

            assertThrows(UnsupportedOperationException.class, () -> estudiantes.add(student2));
        } catch (Exception e) {
            fail("No se esperaba una excepción al obtener la lista de estudiantes: " + e.getMessage());
        }
    }

    @Test
    void toStringTest() {
        group.setId("100000");
        group.setAula("A101");
        try{
            group.enrollStudent(student1);

            String resultado = group.toString();

            assertTrue(resultado.contains("100000"));  // ID
            assertTrue(resultado.contains("5"));       // capacidad
            assertTrue(resultado.contains("1"));       // inscritos
            assertTrue(resultado.contains("A101"));    // aula
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante: " + e.getMessage());
        }
    }


    @Test
    void testGroupStatusOpen() {
        assertTrue(group.getGroupState() instanceof StatusOpen);
        assertFalse(group.isFull());
        try {
            group.inscribirEstudiante(student1);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante: " + e.getMessage());
        }
        assertEquals(1, group.getInscritos());
        assertEquals(4, group.getCuposDisponibles());
        try {
            group.inscribirEstudiante(student2);
        } catch (Exception e) {
            fail("No se esperaba una excepción al inscribir estudiante: " + e.getMessage());
        }
        assertEquals(2, group.getInscritos());
        assertEquals(3, group.getCuposDisponibles());
    }

    @Test
    void listaEstudiantesInicializadaTest() {
        assertNotNull(group.getEstudiantes());
        assertTrue(group.getEstudiantes().isEmpty());
        assertEquals(0, group.getInscritos());
    }


    @Test
    void canEnrollInGroup_success() {
        try {
            AcademicPeriod period = academicPeriod;
            StudyPlan plan = new StudyPlan(Careers.INGENIERIA_AMBIENTAL);
            Subject subj = new Subject("MAT101", "Matematicas I", 4);
            plan.addSubject(subj);

            Student s = new Student("alumno", "a@test.edu", "pwd", "2025001");
            Semaforo sem = new Semaforo(plan);
            sem.setCurrentAcademicPeriod(period);
            s.setAcademicProgress(sem);
            s.setCurrentPeriod(period);

            Group g = new Group(subj, 30, period); // abierto por defecto
            assertTrue(s.canEnrollInGroup(subj, g));
        } catch (Exception e) {
            fail("No se esperaba excepción: " + e.getMessage());
        }
    }
    
    @Test
    void canEnrollInGroup_subjectNotInPlan_throws() {
        try {
            AcademicPeriod period = academicPeriod;
            StudyPlan plan = new StudyPlan(Careers.INGENIERIA_AMBIENTAL);
            Subject subjInPlan = new Subject("MAT101", "Matematicas I", 4);
            plan.addSubject(subjInPlan);
            Subject subjNotInPlan = new Subject("HIS101", "Historia I", 3);

            Student s = new Student("alumno2", "b@test.edu", "pwd", "2025002");
            Semaforo sem = new Semaforo(plan);
            sem.setCurrentAcademicPeriod(period);
            s.setAcademicProgress(sem);
            s.setCurrentPeriod(period);

            Group g = new Group(subjNotInPlan, 30, period);
            SirhaException ex = assertThrows(SirhaException.class, () -> s.canEnrollInGroup(subjNotInPlan, g));
            assertEquals(ErrorCodeSirha.INVALID_ARGUMENT, ex.getErrorCode());
        } catch (Exception e) {
            fail("Error en la preparación del test: " + e.getMessage());
        }
    }
    
    @Test
    void canEnrollInGroup_alreadyEnrolled_throws() {
        try {
            AcademicPeriod period = academicPeriod;
            StudyPlan plan = new StudyPlan(Careers.INGENIERIA_AMBIENTAL);
            Subject subj = new Subject("MAT101", "Matematicas I", 4);
            plan.addSubject(subj);

            Student s = new Student("alumno3", "c@test.edu", "pwd", "2025003");
            Semaforo sem = new Semaforo(plan);
            sem.setCurrentAcademicPeriod(period);
            s.setAcademicProgress(sem);
            s.setCurrentPeriod(period);

            Group g = new Group(subj, 30, period);
            // inscribir primero
            s.enrollSubject(subj, g);

            SirhaException ex = assertThrows(SirhaException.class, () -> s.canEnrollInGroup(subj, g));
            assertEquals(ErrorCodeSirha.OPERATION_NOT_ALLOWED, ex.getErrorCode());
        } catch (Exception e) {
            fail("No se esperaba excepción: " + e.getMessage());
        }
    }
    
    @Test
    void canEnrollInGroup_groupClosed_throws() {
        try {
            AcademicPeriod period = academicPeriod;
            StudyPlan plan = new StudyPlan(Careers.INGENIERIA_AMBIENTAL);
            Subject subj = new Subject("MAT101", "Matematicas I", 4);
            plan.addSubject(subj);

            Student s = new Student("alumno4", "d@test.edu", "pwd", "2025004");
            Semaforo sem = new Semaforo(plan);
            sem.setCurrentAcademicPeriod(period);
            s.setAcademicProgress(sem);
            s.setCurrentPeriod(period);

            Group closed = new Group(subj, 30, period);
            closed.closeGroup();

            SirhaException ex = assertThrows(SirhaException.class, () -> s.canEnrollInGroup(subj, closed));
            assertEquals(ErrorCodeSirha.GROUP_CLOSED, ex.getErrorCode());
        } catch (Exception e) {
            fail("No se esperaba excepción: " + e.getMessage());
        }
    }
    
    @Test
    void canEnrollInGroup_academicPeriodMismatch_throws() {
        try {
            AcademicPeriod wrongPeriod = new AcademicPeriod("2023-2", LocalDate.now().minusYears(1), LocalDate.now().minusMonths(6));
            StudyPlan plan = new StudyPlan(Careers.INGENIERIA_AMBIENTAL);
            Subject subj = new Subject("MAT101", "Matematicas I", 4);
            plan.addSubject(subj);

            Student s = new Student("alumno5", "e@test.edu", "pwd", "2025005");
            Semaforo sem = new Semaforo(plan);
            sem.setCurrentAcademicPeriod(academicPeriod); // el alumno tiene otro periodo válido
            s.setAcademicProgress(sem);
            s.setCurrentPeriod(academicPeriod);

            Group otherPeriodGroup = new Group(subj, 30, wrongPeriod);
            SirhaException ex = assertThrows(SirhaException.class, () -> s.canEnrollInGroup(subj, otherPeriodGroup));
            assertEquals(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_VALID, ex.getErrorCode());
        } catch (Exception e) {
            fail("No se esperaba excepción: " + e.getMessage());
        }
    }
    
    @Test
    void canEnrollInGroup_scheduleConflict_throws() {
        try {
            StudyPlan plan = new StudyPlan(Careers.INGENIERIA_AMBIENTAL);
            Subject other = new Subject("QUI101", "Quimica I", 4);
            plan.addSubject(other);
            Group conflicto = new Group(other, 30, academicPeriod);
            conflicto.addSchedule(scheduleConflict); // LUNES 9-11
            Subject subjEnrolled = new Subject("MAT101", "Matematicas I", 4);
            plan.addSubject(subjEnrolled);

            Student s = new Student("alumno6", "f@test.edu", "pwd", "2025006");
            Semaforo sem = new Semaforo(plan);
            sem.setCurrentAcademicPeriod(academicPeriod);
            s.setAcademicProgress(sem);
            s.setCurrentPeriod(academicPeriod);

            Group enrolledGroup = new Group(subjEnrolled, 30, academicPeriod);
            enrolledGroup.addSchedule(schedule1); // LUNES 8-10
            s.enrollSubject(subjEnrolled, enrolledGroup);


            SirhaException ex = assertThrows(SirhaException.class, () -> s.canEnrollInGroup(other, conflicto));
            assertEquals(ErrorCodeSirha.SCHEDULE_CONFLICT, ex.getErrorCode());
        } catch (Exception e) {
            fail("No se esperaba excepción: " + e.getMessage());
        }
    }
}