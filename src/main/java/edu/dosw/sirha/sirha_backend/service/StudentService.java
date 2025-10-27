package edu.dosw.sirha.sirha_backend.service;

import java.util.List;
import java.util.Optional;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.CambioGrupo;
import edu.dosw.sirha.sirha_backend.domain.model.CambioMateria;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.dto.RequestApprovalRateDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentReportDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import java.util.Map;

public interface StudentService {
    Student registerStudent(RegisterRequest request) throws SirhaException;

    List<Student> findAll() throws SirhaException;
    Optional<Student> findById(String id) throws SirhaException;
    Optional<Student> findByUsername(String username) throws SirhaException;
    Optional<Student> findByEmail(String email) throws SirhaException;

    boolean existsByCode(String code) throws SirhaException;
    boolean existsByEmail(String email) throws SirhaException;

    Student save(StudentDTO studentDTO) throws SirhaException;
    Student deleteById(String id) throws SirhaException;

    List<Schedule> getCurrentSchedule(String username) throws SirhaException;
    List<Schedule> getScheduleForPeriod(String username, String period) throws SirhaException;
    Map<AcademicPeriod,List<Schedule>> getAllSchedules(String username) throws SirhaException;
    
    List<BaseRequest> getAllRequests(String username) throws SirhaException;
    BaseRequest getRequestById(String username, String requestId) throws SirhaException;
    List<BaseRequest> getRequestsHistory(String username) throws SirhaException; // el historial son las solicitudes que ya fueron aprobadas o rechazadas

    Map<SemaforoColores,List<SubjectDecoratorDTO>> getAcademicPensum(String username) throws SirhaException;

    CambioGrupo createRequestCambioGrupo(String studentName, String subjectName, String codeNewGroup) throws SirhaException;
    CambioMateria createRequestCambioMateria(String studentName, String subjectName, String newSubjectName, String codeNewGroup) throws SirhaException;

    Map<SemaforoColores, Double> getPercentageByColor(String username) throws SirhaException;
    StudentDTO getStudentBasicInfo(String username) throws SirhaException;
    StudentReportDTO generateCompleteReport(String username) throws SirhaException;
    String getAcademicSummary(String username) throws SirhaException;
    RequestApprovalRateDTO getRequestApprovalRate(String username) throws SirhaException;
    int getSubjectsByColorCount(String username, SemaforoColores color) throws SirhaException;

    double getApprovalRequestPercentage(String username) throws SirhaException;
    double getRejectionRequestPercentage(String username) throws SirhaException;
    double getPendingRequestPercentage(String username) throws SirhaException;
    double getInReviewRequestPercentage(String username) throws SirhaException;

    void enrollSubject( String username, String subjectName, String groupCode ) throws SirhaException;
    void unenrollSubject( String username, String subjectName, String groupCode ) throws SirhaException;

}
