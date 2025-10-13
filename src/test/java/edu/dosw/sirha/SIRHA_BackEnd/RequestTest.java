package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.StatusClosed;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.StatusOpen;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.NoCursadaState;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;

import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RequestTest {

    @Test
    void contextLoads() {
        // Test básico para verificar que el contexto de Spring Boot se carga correctamente
    }

    @Test
    void testInscriptionRequestValidation() {
        Student student = new Student("1", "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Subject subject = new Subject("101", "Matemáticas", 4);

        try {
            Group group = new Group(subject, 30, period);
            StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas", Careers.INGENIERIA_DE_SISTEMAS);
            studyPlan.addSubject(subject);
            
            Semaforo semaforo = new Semaforo(studyPlan);

            student.setAcademicProgress(semaforo);
            
            SubjectDecorator decorator = new SubjectDecorator(subject);
            assertTrue(decorator.getState() instanceof NoCursadaState);
            
            assertTrue(studyPlan.hasSubject(subject));
            assertTrue(group.getGroupState() instanceof StatusOpen);
            assertFalse(decorator.getState().canApprove());
            assertTrue(decorator.getState().canEnroll());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }

        
    }


    @Test
    void testGroupStateValidation() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Subject subject = new Subject("101", "Matemáticas", 4);
        try {
            Group group = new Group(subject, 30, period);
        
            assertTrue(group.getGroupState() instanceof StatusOpen);
            assertTrue(group.isOpen());
            
            group.closeGroup();
            assertTrue(group.getGroupState() instanceof StatusClosed);
            assertFalse(group.isOpen());
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear el grupo: " + e.getMessage());
        }
    }

    @Test
    void testScheduleConflictDetection() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        Subject subject = new Subject("101", "Matemáticas", 4);

        Schedule schedule1 = new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0));
        try {
            Group group1 = new Group(subject, 30, period);
            group1.addSchedule(schedule1);
            
            Schedule schedule2 = new Schedule(DiasSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0));
            Group group2 = new Group(subject, 25, period);
            group2.addSchedule(schedule2);
            
            assertEquals(1, group1.getSchedules().size());
            assertEquals(1, group2.getSchedules().size());
            
            assertTrue(schedule1.seSolapaCon(schedule2));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos o agregar horarios: " + e.getMessage());
        }
    }

    @Test
    void testPrerequisiteValidation() {
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));

        StudyPlan studyPlan = new StudyPlan("Ingenieria de Sistemas", Careers.INGENIERIA_DE_SISTEMAS);
        Subject prerequisite = new Subject("100", "Algebra", 3);
        MustHaveApprovedSubject rule = new MustHaveApprovedSubject(prerequisite);
        Subject mainSubject = new Subject("101", "Calculo", 4);

        try {
            Group group1 = new Group(prerequisite, 30, period);
            Group group2 = new Group(mainSubject, 25, period);

            studyPlan.addSubject(prerequisite);
            studyPlan.addSubject(mainSubject);

            mainSubject.addPrerequisite(rule);

            Student student = new Student("1", "juan.perez", "juan@example.com", "hashedPassword", "20231001");
            Semaforo semaforo = new Semaforo(studyPlan);
            student.setAcademicProgress(semaforo);
            student.setCurrentPeriod(period);


            assertNotNull(student.getAcademicProgress());
            assertEquals(2, student.getAcademicProgress().getSubjects().size());
            assertEquals(2, student.getAcademicProgress().getSubjectsNotTaken().size());
            assertTrue(student.getAcademicProgress().hasSubject(mainSubject));
            assertTrue(student.getAcademicProgress().hasSubject(prerequisite));


            assertThrows(IllegalStateException.class, () -> {
                student.enrollSubject(mainSubject, group2);
            });

            assertTrue(mainSubject.hasPrerequisites());
            assertTrue(mainSubject.getPrerequisites().contains(rule));

            student.enrollSubject(prerequisite, group1);
            student.approveSubject(prerequisite);
            student.enrollSubject(mainSubject, group2);
            student.approveSubject(mainSubject);

            assertTrue(student.hasSubject(prerequisite));
            assertTrue(student.hasSubject(mainSubject));
        } catch (Exception e) {
            fail("No se esperaba una excepción al crear los grupos o inscribir materias: " + e.getMessage());
        }
        
    }

    @Test
    void testAcademicPeriodValidation() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(4);
        
        AcademicPeriod period = new AcademicPeriod("2024-1", startDate, endDate);
        
        assertEquals("2024-1", period.getPeriod());
        assertEquals(startDate, period.getStartDate());
        assertEquals(endDate, period.getEndDate());
        assertTrue(period.isActive());
    }

    @Test
    void testStudyPlanConfiguration() {
        StudyPlan studyPlan = new StudyPlan("Ingeniería de Sistemas", Careers.INGENIERIA_DE_SISTEMAS); // Constructor con nombre
        Subject subject1 = new Subject("101", "Matemáticas", 4);
        Subject subject2 = new Subject("102", "Física", 3);
        Subject subject3 = new Subject("103", "Química", 4);

        // Agregar materias al plan
        studyPlan.addSubject(subject1);
        studyPlan.addSubject(subject2);
        studyPlan.addSubject(subject3);
        
        // Verificar configuración
        assertEquals(3, studyPlan.getSubjects().size());
        assertTrue(studyPlan.hasSubject(subject1));
        assertTrue(studyPlan.hasSubject(subject2));
        assertTrue(studyPlan.hasSubject(subject3));
    }

    @Test
    void testBaseRequestFlow() {
        Student student = new Student("1", "juan.perez", "juan@example.com", "hashedPassword", "20231001");
        AcademicPeriod period = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        
        assertNotNull(student);
        assertNotNull(period);
        assertEquals("juan.perez", student.getUsername());
        assertTrue(period.isActive());
        
        assertEquals(LocalDate.now(), period.getStartDate());
        assertEquals(LocalDate.now().plusMonths(4), period.getEndDate());
    }

    @Test
    void testRequestStateEnums() {
        assertEquals("PENDIENTE", RequestStateEnum.PENDIENTE.toString());
        assertEquals("APROBADA", RequestStateEnum.APROBADA.toString());
        assertEquals("RECHAZADA", RequestStateEnum.RECHAZADA.toString());
        assertEquals("EN_REVISION", RequestStateEnum.EN_REVISION.toString());
    }
    
}
