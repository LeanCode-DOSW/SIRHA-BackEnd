package edu.dosw.sirha.sirha_backend.dto;

import java.util.List;
import java.util.Map;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;

public class StudentReportDTO {
    private StudentDTO studentDTO;
    private AcademicIndicatorsDTO academicIndicators;
    private RequestApprovalRateDTO requestApprovalRate;

    public StudentReportDTO(StudentDTO studentDTO, AcademicIndicatorsDTO academicIndicators, RequestApprovalRateDTO requestApprovalRate) {
        this.studentDTO = studentDTO;
        this.academicIndicators = academicIndicators;
        this.requestApprovalRate = requestApprovalRate;
    }
    public String getId() {
        return studentDTO.getId();
    }

    public String getUsername() {
        return studentDTO.getUsername();
    }

    public String getCode() {
        return studentDTO.getCode();
    }

    public List<String> getRequestIds() {
        return studentDTO.getRequestIds();
    }

    public String getEmail() {
        return studentDTO.getEmail();
    }

    public Careers getCareer() {
        return studentDTO.getCareer();
    }

    public AcademicIndicatorsDTO getAcademicIndicators() {
        return academicIndicators;
    }
    public StudentDTO getStudentDTO() {
        return studentDTO;
    }

    public RequestApprovalRateDTO getRequestApprovalRate() {
        return requestApprovalRate;
    }

    public double getOverallProgressPercentage() {
        return academicIndicators.getOverallProgressPercentage();
    }
    public SemaforoColores getGlobalProgressIndicator() {
        return academicIndicators.getGlobalProgressIndicator();
    }
    public double getAcademicSuccessRate() {
        return academicIndicators.getAcademicSuccessRate();
    }
    public double getCreditsCompletionPercentage() {
        return academicIndicators.getCreditsCompletionPercentage();
    }
    public boolean isAcademicRisk() {
        return academicIndicators.isAcademicRisk();
    }
    public String getAcademicStatus() {
        return academicIndicators.getAcademicStatus();
    }
    public Map<SemaforoColores, Double> getTrafficLightSummary() {
        return academicIndicators.getTrafficLightSummary();
    }

    public int getTotalRequests() {
        return requestApprovalRate.getTotalRequests();
    }
    public int getApprovedRequests() {
        return requestApprovalRate.getApprovedRequests();
    }
    public int getRejectedRequests() {
        return requestApprovalRate.getRejectedRequests();
    }
    public int getPendingRequests() {
        return requestApprovalRate.getPendingRequests();
    }
    public int getInReviewRequests() {
        return requestApprovalRate.getInReviewRequests();
    }
    public double getApprovalRatePercentage() {
        return requestApprovalRate.getApprovalRatePercentage();
    }
    public double getRejectionRatePercentage() {
        return requestApprovalRate.getRejectionRatePercentage();
    }
    public double getPendingRatePercentage() {
        return requestApprovalRate.getPendingRatePercentage();
    }
    public double getInReviewRatePercentage() {
        return requestApprovalRate.getInReviewRatePercentage();
    }
}
