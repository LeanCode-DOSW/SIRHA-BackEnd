package edu.dosw.sirha.sirha_backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.dto.AuthResponse;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.service.*;


/**
 * Servicio de autenticación para el sistema SIRHA.
 * Maneja registro e inicio de sesión de estudiantes con credenciales institucionales.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final StudentService studentService;
    
    public AuthServiceImpl(StudentService studentService) {
        this.studentService = studentService;
    }
    @Transactional
    @Override
    public AuthResponse registerStudent(RegisterRequest request) {
        return studentService.registerStudent(request);
    }
    @Transactional
    @Override
    public AuthResponse loginStudent(LoginRequest request) {
        return studentService.loginStudent(request);
    }
}
