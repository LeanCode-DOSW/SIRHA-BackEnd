package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.dto.*;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para Student - Funcionalidad de reportes académicos
 */
@SpringBootTest
class StudentAcademicReportsTest {

    private Student student;
    private Student studentSinProgreso;
    private Student studentSinSolicitudes;
    private AcademicPeriod currentPeriod;
    private StudyPlan studyPlan;
    private Semaforo academicProgress;

    // Materias del plan de estudios
    private Subject matematicas;
    private Subject fisica;
    private Subject programacion;
    private Subject calculo;
    private Subject estructuras;
    private Subject quimica;
    private Subject estadistica;

    // Grupos
    private Group grupoMatematicas;
    private Group grupoFisica;
    private Group grupoProgramacion;

    private CambioGrupo requestAprobada;


    @BeforeEach
    void setUp() throws SirhaException {
        currentPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(5));

        matematicas = new Subject("MAT101", "Matemáticas I", 4);
        fisica = new Subject("FIS101", "Física I", 4);
        programacion = new Subject("PROG101", "Programación I", 3);
        calculo = new Subject("CAL201", "Cálculo I", 4);
        estructuras = new Subject("EST301", "Estructuras de Datos", 3);
        quimica = new Subject("QUI101", "Química I", 4);
        estadistica = new Subject("EST201", "Estadística", 3);

        studyPlan = new StudyPlan("Ingeniería de Sistemas", Careers.INGENIERIA_DE_SISTEMAS);
        studyPlan.addSubject(matematicas);    // Semestre 1
        studyPlan.addSubject(fisica);         // Semestre 1
        studyPlan.addSubject(programacion);   // Semestre 1
        
        studyPlan.addSubject(calculo);        // Semestre 2
        studyPlan.addSubject(estructuras);    // Semestre 3
        studyPlan.addSubject(quimica);        // Semestre 1
        studyPlan.addSubject(estadistica);    // Semestre 4

        Group grupoMatematicasAntes = new Group(matematicas, 30, currentPeriod);
        Group grupoFisicaAntes = new Group(fisica, 25, currentPeriod);
        Group grupoProgramacionAntes = new Group(programacion, 20, currentPeriod);
        Group grupoCalculoAntes = new Group(calculo, 20, currentPeriod);

        grupoMatematicas = new Group(matematicas, 30, currentPeriod);
        grupoFisica = new Group(fisica, 25, currentPeriod);
        grupoProgramacion = new Group(programacion, 20, currentPeriod);
        Group grupoCalculo = new Group(calculo, 20, currentPeriod);

        matematicas.addGroup(grupoCalculoAntes);
        matematicas.addGroup(grupoMatematicas);
        fisica.addGroup(grupoFisicaAntes);
        fisica.addGroup(grupoFisica);
        programacion.addGroup(grupoProgramacionAntes);
        programacion.addGroup(grupoProgramacion);
        calculo.addGroup(grupoCalculoAntes);
        calculo.addGroup(grupoCalculo);

        student = new Student(
            "juan.perez",
            "juan@universidad.edu",
            "password123",
            "2024001"
        );

        academicProgress = new Semaforo(studyPlan);
        student.setAcademicProgress(academicProgress);
        student.setCurrentPeriod(currentPeriod);

        student.enrollSubject(matematicas, grupoMatematicasAntes, 1);
        
        student.enrollSubject(programacion, grupoProgramacionAntes, 1);

        student.enrollSubject(fisica, grupoFisicaAntes, 1);

        student.enrollSubject(calculo, grupoCalculoAntes, 2);

        
        requestAprobada = student.createGroupChangeRequest(matematicas, grupoMatematicas);
        requestAprobada.reviewRequest(new ResponseRequest("En revision", RequestStateEnum.EN_REVISION));
        

        student.approveSubject(matematicas);
        student.approveSubject(programacion);
        student.failSubject(calculo);
        studentSinProgreso = new Student(
            "sin.progreso",
            "sinprogreso@universidad.edu",
            "password456",
            "2024002"
        );

        studentSinSolicitudes = new Student(
            "sin.solicitudes",
            "sinsolicitudes@universidad.edu",
            "password789",
            "2024003"
        );
        Semaforo progressoVacio = new Semaforo(studyPlan);
        studentSinSolicitudes.setAcademicProgress(progressoVacio);
        studentSinSolicitudes.setCurrentPeriod(currentPeriod);
    }

    @Test
    void testGetStudentBasicInfo() {
        StudentDTO basicInfo = student.getStudentBasicInfo();

        assertEquals(student.getId(), basicInfo.getId());
        assertEquals("juan.perez", basicInfo.getUsername());
        assertEquals("2024001", basicInfo.getCode());
        assertEquals("juan@universidad.edu", basicInfo.getEmail());
        assertEquals(Careers.INGENIERIA_DE_SISTEMAS, basicInfo.getCareer());
        assertEquals("Ingeniería de Sistemas", basicInfo.getCareer().getDisplayName());

        /* 
        assertEquals(2, basicInfo.getApprovedSubjects()); // matematicas, programacion aprobadas
        assertEquals(1, basicInfo.getCurrentSubjects());  // fisica cursando
        assertEquals(1, basicInfo.getFailedSubjects());   // calculo reprobada

        assertNotNull(basicInfo.getCreatedAt());
        assertEquals(currentPeriod.getId(), basicInfo.getCurrentPeriodId());
        */

    }

    @Test
    void testGetPercentageByColor() {
        Map<SemaforoColores, Double> percentages = student.getPercentageByColor();

        assertNotNull(percentages);
        assertEquals(4, percentages.size());
        
        double expectedVerde = (2.0 / 7.0) * 100;  
        double expectedAmarillo = (1.0 / 7.0) * 100; 
        double expectedRojo = (1.0 / 7.0) * 100;
        double expectedGris = (3.0 / 7.0) * 100;

        
        assertEquals(expectedAmarillo, percentages.get(SemaforoColores.AMARILLO), 0.1);
        assertEquals(expectedRojo, percentages.get(SemaforoColores.ROJO), 0.1);
        assertEquals(expectedGris, percentages.get(SemaforoColores.GRIS), 0.1);
        assertEquals(expectedVerde, percentages.get(SemaforoColores.VERDE), 0.1);
    }

    @Test
    void testGetRequestApprovalRate() {
        assertEquals(1, student.getTotalRequestsMade());

        RequestApprovalRateDTO approvalRate = student.getRequestApprovalRate();

        assertEquals(0, approvalRate.getApprovedRequests());  // requestAprobada
        assertEquals(0, approvalRate.getRejectedRequests());  // requestRechazada
        assertEquals(0, approvalRate.getPendingRequests());   // requestPendiente
        assertEquals(1, approvalRate.getInReviewRequests());  // requestEnRevision

        assertEquals(0.0, approvalRate.getApprovalRatePercentage(), 0.1);  
        assertEquals(0.0, approvalRate.getRejectionRatePercentage(), 0.1);
        assertEquals(0.0, approvalRate.getPendingRatePercentage(), 0.1);
    }

    @Test
    void testRequestPercentages() {
        
        assertEquals(0.0, student.getPendingRequestPercentage(), 0.1);
        assertEquals(100.0, student.getInReviewRequestPercentage(), 0.1);
        assertEquals(0.0, student.getApprovalRequestPercentage(), 0.1);
        assertEquals(0.0, student.getRejectionRequestPercentage(), 0.1);
        assertEquals(1, student.getTotalRequestsMade());
        assertTrue(student.hasActiveRequests());
    }

    @Test
    void testGetOverallProgressPercentage() {
        double overallProgress = student.getOverallProgressPercentage();

        double expected = (2.0 / 7.0) * 100;
        assertEquals(expected, overallProgress, 0.1);
    }

    @Test
    void testGetAcademicSuccessRate() {
        double successRate = student.getAcademicSuccessRate();

        assertEquals(66.67, successRate, 0.1);
    }

    @Test
    void testGetCompletedCreditsPercentage() {
        double creditsPercentage = student.getCompletedCreditsPercentage();

        assertEquals(28.0, creditsPercentage, 0.1);
    }

    @Test
    void testGetAcademicSummary() {
        String summary = student.getAcademicSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Ingeniería de Sistemas"));
        assertTrue(summary.contains("2")); // Materias aprobadas
        assertTrue(summary.contains("1")); // Materias cursando
        assertTrue(summary.contains("1")); // Materias reprobadas
        assertTrue(summary.contains("28")); // Porcentaje aproximado
    }

    @Test
    void testGetAcademicPensum() {
        Map<SemaforoColores, List<SubjectDecoratorDTO>> pensum = student.getAcademicPensum();

        assertNotNull(pensum);
        assertTrue(pensum.containsKey(SemaforoColores.VERDE));
        assertTrue(pensum.containsKey(SemaforoColores.AMARILLO));
        assertTrue(pensum.containsKey(SemaforoColores.ROJO));
        assertTrue(pensum.containsKey(SemaforoColores.GRIS));

        assertEquals(2, pensum.get(SemaforoColores.VERDE).size());   // Aprobadas: mat, prog
        assertEquals(1, pensum.get(SemaforoColores.AMARILLO).size()); // Cursando: fisica
        assertEquals(1, pensum.get(SemaforoColores.ROJO).size());     // Reprobadas: calculo
        assertEquals(3, pensum.get(SemaforoColores.GRIS).size());     // No cursadas: est, qui, esta

        List<SubjectDecoratorDTO> aprobadas = pensum.get(SemaforoColores.VERDE);
        assertTrue(aprobadas.stream().anyMatch(s -> s.getName().equals("Matemáticas I")));
        assertTrue(aprobadas.stream().anyMatch(s -> s.getName().equals("Programación I")));
    }

    @Test
    void testGetAcademicIndicators() {
        AcademicIndicatorsDTO indicators = student.getAcademicIndicators();
        assertEquals(7, student.getTotalSubjectsCount());

        assertEquals(28.57, indicators.getOverallProgressPercentage(), 0.1);
        assertEquals(66.67, indicators.getAcademicSuccessRate(), 0.1);
        assertEquals(28.0, indicators.getCreditsCompletionPercentage(), 0.1);

        assertNotNull(indicators.getTrafficLightSummary());
        
        assertEquals(42.86, indicators.getTrafficLightSummary().get(SemaforoColores.GRIS), 0.1);
        assertEquals(28.57, indicators.getTrafficLightSummary().get(SemaforoColores.VERDE), 0.1);
        assertNotNull(indicators.getGlobalProgressIndicator());
        assertNotNull(indicators.getAcademicStatus());
    }


    @Test
    void testStudentWithoutAcademicProgress() {
        assertEquals(0.0, studentSinProgreso.getOverallProgressPercentage());
        assertEquals(0.0, studentSinProgreso.getAcademicSuccessRate());
        assertEquals(0.0, studentSinProgreso.getCompletedCreditsPercentage());
        
        Map<SemaforoColores, Double> percentages = studentSinProgreso.getPercentageByColor();
        assertNull(percentages.get(SemaforoColores.VERDE));
        assertNull(percentages.get(SemaforoColores.AMARILLO));
        assertNull(percentages.get(SemaforoColores.ROJO));
        assertNull(percentages.get(SemaforoColores.GRIS));

        assertNull( studentSinProgreso.getAcademicIndicators());
    }

    @Test
    void testStudentWithoutRequests() {
        RequestApprovalRateDTO approvalRate = studentSinSolicitudes.getRequestApprovalRate();

        assertEquals(0, approvalRate.getTotalRequests());
        assertEquals(0, approvalRate.getApprovedRequests());
        assertEquals(0, approvalRate.getRejectedRequests());
        assertEquals(0, approvalRate.getPendingRequests());
        assertEquals(0, approvalRate.getInReviewRequests());
        
        assertEquals(0.0, studentSinSolicitudes.getApprovalRequestPercentage());
        assertEquals(0.0, studentSinSolicitudes.getRejectionRequestPercentage());
        assertEquals(0.0, studentSinSolicitudes.getPendingRequestPercentage());
        assertEquals(0, studentSinSolicitudes.getTotalRequestsMade());
        assertFalse(studentSinSolicitudes.hasActiveRequests());

    }


    @Test
    void testGenerateCompleteReport() {
        StudentReportDTO report = student.generateCompleteReport();

        assertNotNull(report);
        assertEquals("juan.perez", report.getUsername());

        assertNotNull(report.getStudentDTO());
        assertNotNull(report.getAcademicIndicators());
        assertNotNull(report.getRequestApprovalRate());

    }

    @Test
    void testFilterSubjectsBySemester() {
        List<SubjectDecorator> semestre1 = student.getSubjectsBySemester(1);
        List<SubjectDecorator> semestre2 = student.getSubjectsBySemester(2);

        assertEquals(3, semestre1.size());
        assertTrue(semestre1.stream().anyMatch(s -> s.getName().equals("Matemáticas I")));
        assertTrue(semestre1.stream().anyMatch(s -> s.getName().equals("Física I")));
        assertTrue(semestre1.stream().anyMatch(s -> s.getName().equals("Programación I")));

        assertEquals(1, semestre2.size());
        assertEquals("Cálculo I", semestre2.get(0).getName());
    }

}
