package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.SIRHA_BackEnd.service.*;


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
