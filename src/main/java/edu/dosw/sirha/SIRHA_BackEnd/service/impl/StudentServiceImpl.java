package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.StudentMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.StudentService;
import edu.dosw.sirha.SIRHA_BackEnd.util.ValidationUtil;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentMongoRepository studentRepository;

    public StudentServiceImpl(StudentMongoRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    
    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(String id) {
        return studentRepository.findById(id);
    }
    @Override
    public Optional<Student> findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }
    @Override
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return studentRepository.existsByCodigo(codigo);
    }

    @Override
    public Optional<Student> login(String username, String password) {
        return studentRepository.findByUsername(username)
                .filter(student -> student.verificarContraseña(password));
    }

    @Override
    public Student register(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }

    @Override
    public AuthResponse registerStudent(RegisterRequest request) {
        ValidationUtil.validateStudentRegistration(
            request.getUsername(), 
            request.getEmail(), 
            request.getPassword(), 
            request.getCodigo()
        );
        
        // Verificar que el código estudiantil no exista
        if (existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("El código estudiantil ya está registrado");
        }
        
        // Verificar que el email no exista
        if (existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // Crear nuevo estudiante
        Student student = new Student(
            null, // ID se genera automáticamente
            request.getUsername(),
            request.getEmail(),
            request.getPassword(), // Se hashea en el método register
            request.getCodigo()
        );
        
        // Registrar estudiante
        Student savedStudent = (Student) register(student);
        
        // Retornar respuesta
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
        // Buscar estudiante por username
        Optional<Student> userOpt = login(request.getUsername(), request.getPassword());
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        
        Student student = userOpt.get();
        
        // Verificar que sea un estudiante
        if (!(student instanceof Student)) {
            throw new IllegalArgumentException("Usuario no es un estudiante");
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