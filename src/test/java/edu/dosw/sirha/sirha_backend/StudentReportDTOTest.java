package edu.dosw.sirha.sirha_backend;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.dto.AcademicIndicatorsDTO;
import edu.dosw.sirha.sirha_backend.dto.RequestApprovalRateDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentReportDTO;

class StudentReportDTOTest {

    @Test
    void studentReportDtoDelegatesToInnerDtos_withoutMocks() {
        StudentDTO student = new StudentDTO(
            "stu-1",
            "user1",
            "user1@uni.edu",
            "2025-0001",
            Careers.INGENIERIA_DE_SISTEMAS
        );

        AcademicIndicatorsDTO indicators = new AcademicIndicatorsDTO(
            72.5,
            SemaforoColores.VERDE,
            85.0,
            60.0,
            false,
            "GOOD",
            Map.of(SemaforoColores.VERDE, 0.6)
        );

        RequestApprovalRateDTO rates = new RequestApprovalRateDTO(4,3,0,1,0);


        // DTO bajo prueba
        StudentReportDTO report = new StudentReportDTO(student, indicators, rates);

        assertEquals("stu-1", report.getId());
        assertEquals("user1", report.getUsername());
        assertEquals("2025-0001", report.getCode());
        assertEquals("user1@uni.edu", report.getEmail());
        assertEquals(Careers.INGENIERIA_DE_SISTEMAS, report.getCareer());

        assertEquals(72.5, report.getOverallProgressPercentage());
        assertEquals(SemaforoColores.VERDE, report.getGlobalProgressIndicator());
        assertEquals(85.0, report.getAcademicSuccessRate());
        assertEquals(60.0, report.getCreditsCompletionPercentage());
        assertFalse(report.isAcademicRisk());
        assertEquals("GOOD", report.getAcademicStatus());
        assertEquals(0.6, report.getTrafficLightSummary().get(SemaforoColores.VERDE));

        assertEquals(4, report.getTotalRequests());
        assertEquals(3, report.getApprovedRequests());
        assertEquals(0, report.getRejectedRequests());
        assertEquals(1, report.getPendingRequests());
        assertEquals(0, report.getInReviewRequests());
        assertEquals(75.0, report.getApprovalRatePercentage());
        assertEquals(0.0, report.getRejectionRatePercentage());
        assertEquals(25.0, report.getPendingRatePercentage());
        assertEquals(0.0, report.getInReviewRatePercentage());
    }

    @Test
    void percentagesAreComputedCorrectlyWhenTotalGreaterThanZero() {
        RequestApprovalRateDTO dto = new RequestApprovalRateDTO(4, 3, 0, 1, 0);

        assertEquals(4, dto.getTotalRequests());
        assertEquals(3, dto.getApprovedRequests());
        assertEquals(0, dto.getRejectedRequests());
        assertEquals(1, dto.getPendingRequests());
        assertEquals(0, dto.getInReviewRequests());

        assertEquals(75.0, dto.getApprovalRatePercentage(), 1e-6);
        assertEquals(0.0, dto.getRejectionRatePercentage(), 1e-6);
        assertEquals(25.0, dto.getPendingRatePercentage(), 1e-6);
        assertEquals(0.0, dto.getInReviewRatePercentage(), 1e-6);
    }

    @Test
    void percentagesAreZeroWhenTotalIsZero() {
        RequestApprovalRateDTO dto = new RequestApprovalRateDTO(0, 0, 0, 0, 0);

        assertEquals(0, dto.getTotalRequests());
        assertEquals(0, dto.getApprovedRequests());
        assertEquals(0, dto.getRejectedRequests());
        assertEquals(0, dto.getPendingRequests());
        assertEquals(0, dto.getInReviewRequests());

        assertEquals(0.0, dto.getApprovalRatePercentage(), 1e-6);
        assertEquals(0.0, dto.getRejectionRatePercentage(), 1e-6);
        assertEquals(0.0, dto.getPendingRatePercentage(), 1e-6);
        assertEquals(0.0, dto.getInReviewRatePercentage(), 1e-6);
    }
}
