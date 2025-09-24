package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.StudentMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ValidationUtils;

import java.util.Optional;  
import edu.dosw.sirha.SIRHA_BackEnd.service.*;
import edu.dosw.sirha.SIRHA_BackEnd.util.*;


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

    @Override
    public Optional<Student> login(String username, String password) {
        return studentService.findByUsername(username)
                .filter(student -> student.verificarContraseña(password));
    }

    @Override
    public Student register(Student student) {
        return studentService.save(student);
    }


    @Override
    public AuthResponse registerStudent(RegisterRequest request) {
        ValidationUtil.validateStudentRegistration(
            request.getUsername(), 
            request.getEmail(), 
            request.getPassword(), 
            request.getCodigo()
        );

        if (studentService.existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("El código estudiantil ya está registrado");
        }

        if (studentService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        Student student = new Student(
            null,
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getCodigo()
        );
        
        Student savedStudent = studentService.save(student);
        
        return new AuthResponse(
            savedStudent.getId(),
            savedStudent.getUsername(),
            savedStudent.getEmail(),
            savedStudent.getCodigo(),
            "Registro exitoso"
        );
    }

    @Override
    public AuthResponse loginStudent(LoginRequest request) {
        Optional<Student> studentOpt = studentService.findByUsername(request.getUsername());
        
        if (studentOpt.isEmpty()) {
            System.out.println("No se encontró por username, intentando por email...");
            studentOpt = studentService.findByEmail(request.getUsername());
        }
        
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Credenciales inválidas: usuario no encontrado");
        }
        
        Student student = studentOpt.get();
        
        if (!student.verificarContraseña(request.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas: contraseña incorrecta");
        }
        
        return new AuthResponse(
            student.getId(),
            student.getUsername(),
            student.getEmail(),
            student.getCodigo(),
            "Login exitoso"
        );
    }
}
