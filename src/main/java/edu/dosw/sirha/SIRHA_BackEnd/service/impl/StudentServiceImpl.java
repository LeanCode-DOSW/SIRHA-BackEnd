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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    
    private final StudentMongoRepository studentRepository;
    private final AcademicPeriodMongoRepository academicPeriodRepository;
    private final SubjectMongoRepository subjectRepository;
    private final GroupMongoRepository groupRepository;

    public StudentServiceImpl(StudentMongoRepository studentRepository, 
                            AcademicPeriodMongoRepository academicPeriodRepository, 
                            SubjectMongoRepository subjectRepository, 
                            GroupMongoRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.academicPeriodRepository = academicPeriodRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        
        log.info("StudentServiceImpl inicializado correctamente");
    }

    @Override
    public Student save(Student student) {
        log.info("Guardando estudiante: {}", student.getUsername());
        try {
            Student savedStudent = studentRepository.save(student);
            log.debug("Estudiante guardado exitosamente con ID: {}", savedStudent.getId());
            return savedStudent;
        } catch (Exception e) {
            log.error("Error al guardar estudiante {}: {}", student.getUsername(), e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public List<Student> findAll() {
        log.info("Consultando todos los estudiantes");
        try {
            List<Student> students = studentRepository.findAll();
            log.info("Se encontraron {} estudiantes", students.size());
            return students;
        } catch (Exception e) {
            log.error("Error al consultar todos los estudiantes: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Student> findById(String id) {
        log.debug("Buscando estudiante por ID: {}", id);
        try {
            Optional<Student> student = studentRepository.findById(id);
            if (student.isPresent()) {
                log.debug("Estudiante encontrado por ID: {}", id);
            } else {
                log.debug("No se encontró estudiante con ID: {}", id);
            }
            return student;
        } catch (Exception e) {
            log.error("Error al buscar estudiante por ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Student> findByUsername(String username) {
        log.debug("Buscando estudiante por username: {}", username);
        try {
            Optional<Student> student = studentRepository.findByUsername(username);
            if (student.isPresent()) {
                log.debug("Estudiante encontrado por username: {}", username);
            } else {
                log.debug("No se encontró estudiante con username: {}", username);
            }
            return student;
        } catch (Exception e) {
            log.error("Error al buscar estudiante por username {}: {}", username, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        log.debug("Buscando estudiante por email: {}", email);
        try {
            Optional<Student> student = studentRepository.findByEmail(email);
            if (student.isPresent()) {
                log.debug("Estudiante encontrado por email: {}", email);
            } else {
                log.debug("No se encontró estudiante con email: {}", email);
            }
            return student;
        } catch (Exception e) {
            log.error("Error al buscar estudiante por email {}: {}", email, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        log.debug("Verificando existencia de código: {}", codigo);
        try {
            boolean exists = studentRepository.existsByCodigo(codigo);
            log.debug("Código {} existe: {}", codigo, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error al verificar código {}: {}", codigo, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Verificando existencia de email: {}", email);
        try {
            boolean exists = studentRepository.findByEmail(email).isPresent();
            log.debug("Email {} existe: {}", email, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error al verificar email {}: {}", email, e.getMessage(), e);
            throw e;
        }
    }



    
    private Student registerStudent(Student student) {
        log.info("Registrando estudiante: {}", student.getUsername());
        try {
            Student registeredStudent = studentRepository.save(student);
            log.info("Estudiante registrado exitosamente: {} con ID: {}", 
                    registeredStudent.getUsername(), registeredStudent.getId());
            return registeredStudent;
        } catch (Exception e) {
            log.error("Error al registrar estudiante {}: {}", student.getUsername(), e.getMessage(), e);
            throw e;
        }
    }



    @Override
    public AuthResponse registerStudent(RegisterRequest request) {
        log.info("Iniciando proceso de registro para usuario: {}", request.getUsername());
        
        try {
            log.debug("Validando datos de registro para: {}", request.getUsername());
            ValidationUtil.validateStudentRegistration(
                request.getUsername(), 
                request.getEmail(), 
                request.getPassword(), 
                request.getCodigo()
            );
            log.debug("Validación exitosa para: {}", request.getUsername());
            
            if (existsByCodigo(request.getCodigo())) {
                log.warn("Intento de registro con código duplicado: {} para usuario: {}", 
                        request.getCodigo(), request.getUsername());
                throw new IllegalArgumentException("El código estudiantil ya está registrado");
            }
            
            if (existsByEmail(request.getEmail())) {
                log.warn("Intento de registro con email duplicado: {} para usuario: {}", 
                        request.getEmail(), request.getUsername());
                throw new IllegalArgumentException("El email ya está registrado");
            }
            
            log.debug("Creando nuevo estudiante: {}", request.getUsername());
            Student student = new Student(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getCodigo()
            );
            
            Student savedStudent = registerStudent(student);
            
            log.info("Registro completado exitosamente para usuario: {} con código: {}", 
                    savedStudent.getUsername(), savedStudent.getCodigo());
            
            return new AuthResponse(
                savedStudent.getId(),
                savedStudent.getUsername(),
                savedStudent.getEmail(),
                savedStudent.getCodigo(),
                "Registro exitoso"
            );
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación en registro para {}: {}", request.getUsername(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado durante registro para {}: {}", request.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Error interno durante el registro", e);
        }
    }



    private Optional<Student> loginStudent(String username, String password) {
        log.info("Intento de login para usuario: {}", username);
        try {
            Optional<Student> student = studentRepository.findByUsername(username)
                    .filter(s -> s.verificarContraseña(password));
            
            if (student.isPresent()) {
                log.info("Login exitoso para usuario: {}", username);
            } else {
                log.warn("Login fallido para usuario: {} - credenciales incorrectas", username);
            }
            
            return student;
        } catch (Exception e) {
            log.error("Error durante login para usuario {}: {}", username, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AuthResponse loginStudent(LoginRequest request) {
        log.info("Iniciando proceso de login para usuario: {}", request.getUsername());
        
        try {
            Optional<Student> userOpt = loginStudent(request.getUsername(), request.getPassword());
            
            if (userOpt.isEmpty()) {
                log.warn("Login fallido para usuario: {} - credenciales inválidas", request.getUsername());
                throw new IllegalArgumentException("Credenciales inválidas");
            }
            
            Student student = userOpt.get();
            
            log.info("Login completado exitosamente para usuario: {}", student.getUsername());
            
            return new AuthResponse(
                student.getId(),
                student.getUsername(),
                student.getEmail(),
                student.getCodigo(),
                "Login exitoso"
            );
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de autenticación para {}: {}", request.getUsername(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado durante login para {}: {}", request.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Error interno durante el login", e);
        }
    }

    @Override
    public List<Schedule> getCurrentSchedule(String username) {
        log.info("Consultando horario actual para usuario: {}", username);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado para consulta de horario: {}", username);
                    return new IllegalArgumentException("Estudiante no encontrado");
                });
                
            List<Schedule> schedule = student.getCurrentSchedule();
            log.info("Horario actual obtenido exitosamente para {}: {} materias", username, schedule.size());
            return schedule;
            
        } catch (IllegalArgumentException e) {
            log.warn("Error al consultar horario para {}: {}", username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consultar horario para {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Error interno al consultar horario", e);
        }
    }

    @Override
    public List<Schedule> getScheduleForPeriod(String username, String period) {
        log.info("Consultando horario para usuario: {} en período: {}", username, period);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado: {}", username);
                    return new IllegalArgumentException("Estudiante no encontrado");
                });
                
            AcademicPeriod academicPeriod = academicPeriodRepository.findByPeriod(period)
                .orElseThrow(() -> {
                    log.warn("Período académico no encontrado: {}", period);
                    return new IllegalArgumentException("Periodo académico no encontrado");
                });
                
            List<Schedule> schedule = student.getScheduleForPeriod(academicPeriod);
            log.info("Horario del período {} obtenido para {}: {} materias", period, username, schedule.size());
            return schedule;
            
        } catch (IllegalArgumentException e) {
            log.warn("Error al consultar horario del período {} para {}: {}", period, username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consultar horario del período {} para {}: {}", 
                     period, username, e.getMessage(), e);
            throw new RuntimeException("Error interno al consultar horario del período", e);
        }
    }

    @Override
    public Map<AcademicPeriod, List<Schedule>> getAllSchedules(String username) {
        log.info("Consultando todos los horarios para usuario: {}", username);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado: {}", username);
                    return new IllegalArgumentException("Estudiante no encontrado");
                });
                
            Map<AcademicPeriod, List<Schedule>> allSchedules = student.getAllSchedules();
            log.info("Todos los horarios obtenidos para {}: {} períodos", username, allSchedules.size());
            return allSchedules;
            
        } catch (IllegalArgumentException e) {
            log.warn("Error al consultar todos los horarios para {}: {}", username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consultar todos los horarios para {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Error interno al consultar todos los horarios", e);
        }
    }

    @Override
    public Map<SemaforoColores, List<SubjectDecoratorDTO>> getAcademicPensum(String username) {
        log.info("Consultando pensum académico para usuario: {}", username);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado: {}", username);
                    return new IllegalArgumentException("Estudiante no encontrado");
                });
                
            Map<SemaforoColores, List<SubjectDecoratorDTO>> pensum = student.getAcademicPensum();
            log.info("Pensum académico obtenido para {}: {} categorías", username, pensum.size());
            
            // Log detallado del pensum
            pensum.forEach((color, subjects) -> 
                log.debug("Pensum {} para {}: {} materias - {}", color, username, subjects.size(), subjects));
                
            return pensum;
            
        } catch (IllegalArgumentException e) {
            log.warn("Error al consultar pensum para {}: {}", username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consultar pensum para {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Error interno al consultar pensum académico", e);
        }
    }

    @Override
    public CambioGrupo createRequestCambioGrupo(String studentName, String subjectName, String codeNewGroup) {
        log.info("Creando solicitud de cambio de grupo - Usuario: {}, Materia: {}, Nuevo Grupo: {}", 
                studentName, subjectName, codeNewGroup);
        
        try {
            Student student = studentRepository.findByUsername(studentName)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado para cambio de grupo: {}", studentName);
                    return new IllegalArgumentException("Estudiante no encontrado");
                });
            
            Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> {
                    log.warn("Materia no encontrada: {}", subjectName);
                    return new IllegalArgumentException("Materia no encontrada");
                });

            Group group = groupRepository.findByCode(codeNewGroup)
                .orElseThrow(() -> {
                    log.warn("Grupo no encontrado: {}", codeNewGroup);
                    return new IllegalArgumentException("Grupo no encontrado");
                });

            log.debug("Creando solicitud de cambio de grupo para {}: {} -> {}", 
                     studentName, subjectName, codeNewGroup);
                     
            CambioGrupo cambio = student.createGroupChangeRequest(subject, group);
            
            log.info("Solicitud de cambio de grupo creada exitosamente - ID: {} para usuario: {}", 
                    cambio.getId(), studentName);
            
            return cambio;
            
        } catch (IllegalArgumentException e) {
            log.warn("Error al crear solicitud de cambio de grupo para {}: {}", studentName, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear solicitud de cambio de grupo para {}: {}", 
                     studentName, e.getMessage(), e);
            throw new RuntimeException("Error interno al crear solicitud de cambio de grupo", e);
        }
    }

    @Override
    public CambioMateria createRequestCambioMateria(String studentName, String subjectName, 
                                                   String newSubjectName, String codeNewGroup) {
        log.info("Creando solicitud de cambio de materia - Usuario: {}, De: {} -> A: {}, Grupo: {}", 
                studentName, subjectName, newSubjectName, codeNewGroup);
        
        try {
            Student student = studentRepository.findByUsername(studentName)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado para cambio de materia: {}", studentName);
                    return new IllegalArgumentException("Estudiante no encontrado");
                });

            Subject subjectOld = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> {
                    log.warn("Materia antigua no encontrada: {}", subjectName);
                    return new IllegalArgumentException("Materia antigua no encontrada");
                });

            Subject subjectNew = subjectRepository.findByName(newSubjectName)
                .orElseThrow(() -> {
                    log.warn("Materia nueva no encontrada: {}", newSubjectName);
                    return new IllegalArgumentException("Materia nueva no encontrada");
                });

            Group group = groupRepository.findByCode(codeNewGroup)
                .orElseThrow(() -> {
                    log.warn("Grupo no encontrado: {}", codeNewGroup);
                    return new IllegalArgumentException("Grupo no encontrado");
                });

            log.debug("Creando solicitud de cambio de materia para {}: {} -> {} en grupo {}", 
                     studentName, subjectName, newSubjectName, codeNewGroup);
                     
            CambioMateria cambio = student.createSubjectChangeRequest(subjectOld, subjectNew, group);
            
            log.info("Solicitud de cambio de materia creada exitosamente - ID: {} para usuario: {}", 
                    cambio.getId(), studentName);
            
            return cambio;
            
        } catch (IllegalArgumentException e) {
            log.warn("Error al crear solicitud de cambio de materia para {}: {}", studentName, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear solicitud de cambio de materia para {}: {}", 
                     studentName, e.getMessage(), e);
            throw new RuntimeException("Error interno al crear solicitud de cambio de materia", e);
        }
    }
}