package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;
import java.util.Optional;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.AcademicPeriod;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.CambioGrupo;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.CambioMateria;

import java.util.Map;
import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.SubjectDecoratorDTO;

public interface StudentService {
    AuthResponse registerStudent(RegisterRequest request);
    AuthResponse loginStudent(LoginRequest request);


    List<Student> findAll();
    Optional<Student> findById(String id);
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);

    boolean existsByCodigo(String codigo);
    boolean existsByEmail(String email);

    Student save(Student student);

    List<Schedule> getCurrentSchedule(String username);
    List<Schedule> getScheduleForPeriod(String username, String period);
    Map<AcademicPeriod,List<Schedule>> getAllSchedules(String username);
    //List<BaseRequest> getAllRequests(String username);

    Map<SemaforoColores,List<SubjectDecoratorDTO>> getAcademicPensum(String username);

    CambioGrupo createRequestCambioGrupo(String studentName, String subjectName, String codeNewGroup);
    CambioMateria createRequestCambioMateria(String studentName, String subjectName, String newSubjectName, String codeNewGroup);

}
