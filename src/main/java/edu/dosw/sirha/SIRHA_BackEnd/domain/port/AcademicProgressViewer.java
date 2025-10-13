package edu.dosw.sirha.sirha_backend.domain.port;

import java.util.List;
import java.util.Map;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.dto.AcademicIndicatorsDTO;
import edu.dosw.sirha.sirha_backend.dto.RequestApprovalRateDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentReportDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;

public interface AcademicProgressViewer {
    // Conteos por estado (Counts by status)
    int getTotalSubjectsCount();
    int getSubjectsInProgressCount();
    int getPassedSubjectsCount();
    int getFailedSubjectsCount();
    int getSubjectsNotTakenCount();

    // Listas por estado (Lists by status)
    List<SubjectDecorator> getSubjectsInProgress();

    // Consultas por semestre (Queries by semester)
    int getCurrentSemester();
    List<SubjectDecorator> getSubjectsBySemester(int semester);

    // Créditos (Credits)
    int getCreditsByColor(SemaforoColores color);
    int getSubjectsByColorCount(SemaforoColores color);
    int getCreditsInProgress();

    // Resúmenes (Summaries)
    String getAcademicSummary();
    Map<SemaforoColores, List<SubjectDecoratorDTO>> getAcademicPensum();

    // Información general del estudiante (General Student Information)
    Careers getCareer();
    StudentDTO getStudentBasicInfo();

    // Porcentaje por color (Percentage by color)
    Map<SemaforoColores, Double> getPercentageByColor();



    // Tasa de solicitudes y reportes (Request Rate and Reports)
    RequestApprovalRateDTO getRequestApprovalRate();
    StudentReportDTO generateCompleteReport();
    double getApprovalRequestPercentage();
    double getRejectionRequestPercentage();
    double getPendingRequestPercentage();
    double getInReviewRequestPercentage();
    int getTotalRequestsMade();
    int getTotalApprovedRequests();
    int getTotalRejectedRequests();
    int getTotalPendingRequests();
    int getTotalInReviewRequests();

    // Indicadores académicos (Academic Indicators)
    double getOverallProgressPercentage();
    double getAcademicSuccessRate();
    double getCompletedCreditsPercentage();
    AcademicIndicatorsDTO getAcademicIndicators();
}
