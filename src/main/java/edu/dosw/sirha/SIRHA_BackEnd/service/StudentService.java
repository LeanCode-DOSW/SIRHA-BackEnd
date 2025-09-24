package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;
import java.util.Optional;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;

public interface StudentService {
    Optional<Student> login(String username, String password);
    Student register(Student student);
    

    AuthResponse registerStudent(RegisterRequest request);
    AuthResponse loginStudent(LoginRequest request);

    List<Student> findAll();
    Optional<Student> findById(String id);
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);

    boolean existsByCodigo(String codigo);
    boolean existsByEmail(String email);

    Student save(Student student);
}
