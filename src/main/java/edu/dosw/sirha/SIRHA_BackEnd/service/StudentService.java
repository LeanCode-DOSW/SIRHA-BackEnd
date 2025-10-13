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
import edu.dosw.sirha.sirha_backend.dto.AuthResponse;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;

import java.util.Map;

public interface StudentService {
    AuthResponse registerStudent(RegisterRequest request);
    AuthResponse loginStudent(LoginRequest request);


    List<Student> findAll();
    Optional<Student> findById(String id);
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);

    boolean existsByCode(String code);
    boolean existsByEmail(String email);

    Student save(Student student); //deberia ser un studentDTO
    Student deleteById(String id);

    List<Schedule> getCurrentSchedule(String username);
    List<Schedule> getScheduleForPeriod(String username, String period);
    Map<AcademicPeriod,List<Schedule>> getAllSchedules(String username);
    
    List<BaseRequest> getAllRequests(String username);
    BaseRequest getRequestById(String username, String requestId);
    List<BaseRequest> getRequestsHistory(String username); // el historial son las solicitudes que ya fueron aprobadas o rechazadas

    Map<SemaforoColores,List<SubjectDecoratorDTO>> getAcademicPensum(String username);

    CambioGrupo createRequestCambioGrupo(String studentName, String subjectName, String codeNewGroup);
    CambioMateria createRequestCambioMateria(String studentName, String subjectName, String newSubjectName, String codeNewGroup);

}
