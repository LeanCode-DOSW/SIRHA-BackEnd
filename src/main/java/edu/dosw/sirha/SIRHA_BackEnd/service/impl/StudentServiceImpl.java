package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.AcademicPeriod;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.CambioGrupo;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.CambioMateria;

import java.util.Map;
import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.AcademicPeriodMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.GroupMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.StudentMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.SubjectMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.StudentService;
import edu.dosw.sirha.SIRHA_BackEnd.util.ValidationUtil;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentMongoRepository studentRepository;
    private final AcademicPeriodMongoRepository academicPeriodRepository;
    private final SubjectMongoRepository subjectRepository;
    private final GroupMongoRepository  groupRepository;

    public StudentServiceImpl(StudentMongoRepository studentRepository, AcademicPeriodMongoRepository academicPeriodRepository, SubjectMongoRepository subjectRepository, GroupMongoRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.academicPeriodRepository = academicPeriodRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
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
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
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

    @Override
    public List<Schedule> getCurrentSchedule(String username) {
        return studentRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"))
            .getCurrentSchedule();
    }

    @Override
    public List<Schedule> getScheduleForPeriod(String username, String period) {
        
        return studentRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"))
            .getScheduleForPeriod(academicPeriodRepository.findByPeriod(period)
                .orElseThrow(() -> new IllegalArgumentException("Periodo académico no encontrado")));
    }

    @Override
    public Map<AcademicPeriod, List<Schedule>> getAllSchedules(String username) {
        return studentRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado")).getAllSchedules();
    }
    @Override
    public Map<SemaforoColores,List<SubjectDecoratorDTO>> getAcademicPensum(String username) {
        return studentRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"))
            .getAcademicPensum();
    }

    @Override
    public CambioGrupo createRequestCambioGrupo(String studentName, String subjectName, String codeNewGroup) {

        Student student = studentRepository.findByUsername(studentName)
            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
        
        Subject subject = subjectRepository.findByName(subjectName)
            .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));

        Group group = groupRepository.findByCode(codeNewGroup)
            .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado"));
            

        return student.createSolicitudCambioGrupo(subject, group);

        
    }

    @Override
    public CambioMateria createRequestCambioMateria(String studentName, String subjectName, String newSubjectName, String codeNewGroup) {
        
        Student student = studentRepository.findByUsername(studentName)
            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        Subject subjectOld = subjectRepository.findByName(subjectName)
            .orElseThrow(() -> new IllegalArgumentException("Materia antigua no encontrada"));

        Subject subjectNew = subjectRepository.findByName(newSubjectName)
            .orElseThrow(() -> new IllegalArgumentException("Materia nueva no encontrada"));

        Group group = groupRepository.findByCode(codeNewGroup)
            .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado"));

        return student.createSolicitudCambioMateria(subjectOld, subjectNew, group);
    }


}