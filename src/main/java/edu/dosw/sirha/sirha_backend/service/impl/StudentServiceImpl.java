package edu.dosw.sirha.sirha_backend.service.impl;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.CambioGrupo;
import edu.dosw.sirha.sirha_backend.domain.model.CambioMateria;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.dto.RequestApprovalRateDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentReportDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.repository.mongo.AcademicPeriodMongoRepository;
import edu.dosw.sirha.sirha_backend.repository.mongo.GroupMongoRepository;
import edu.dosw.sirha.sirha_backend.repository.mongo.StudentMongoRepository;
import edu.dosw.sirha.sirha_backend.repository.mongo.SubjectMongoRepository;
import edu.dosw.sirha.sirha_backend.service.StudentService;
import edu.dosw.sirha.sirha_backend.util.ValidationUtil;
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

    @Transactional
    @Override
    public Student save(StudentDTO studentDTO) throws SirhaException {
        log.info("Guardando estudiante desde DTO: {}", studentDTO.getUsername());
        try {
            Student student = new Student(
                studentDTO.getUsername(),
                studentDTO.getEmail(),
                "defaultPass", // Contraseña temporal - debe ser actualizada
                studentDTO.getCode()
            );
            log.info("Guardando estudiante: {}", student.getUsername());
            Student savedStudent = studentRepository.save(student);
            log.debug("Estudiante guardado exitosamente con ID: {}", savedStudent.getId());
            return savedStudent;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al guardar estudiante: " + e.getMessage(), e);
        }
    }

    @Transactional    
    @Override
    public List<Student> findAll() throws SirhaException {
        log.info("Consultando todos los estudiantes");
        try {
            List<Student> students = studentRepository.findAll();
            log.info("Se encontraron {} estudiantes", students.size());
            return students;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar todos los estudiantes: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Optional<Student> findById(String id) throws SirhaException {
        log.debug("Buscando estudiante por ID: {}", id);
        try {
            Optional<Student> student = studentRepository.findById(id);
            if (!student.isPresent()) {
                log.debug("No se encontró estudiante con ID: {}", id);
                throw SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND,"Estudiante no encontrado con ID: " + id);
            }
            log.debug("Estudiante encontrado por ID: {}", id);
            return student;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al buscar estudiante por ID: " + id, e);
        }
    }

    @Transactional
    @Override
    public Optional<Student> findByUsername(String username) throws SirhaException {
        log.debug("Buscando estudiante por username: {}", username);
        try {
            Optional<Student> student = studentRepository.findByUsername(username);
            if (!student.isPresent()) {
                log.debug("No se encontró estudiante con username: {}", username);
                throw SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND,"Estudiante no encontrado con username: " + username);
            }
            log.debug("Estudiante encontrado por username: {}", username);
            return student;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al buscar estudiante por username: " + username, e);
        }
    }

    @Transactional
    @Override
    public Optional<Student> findByEmail(String email) throws SirhaException {
        log.debug("Buscando estudiante por email: {}", email);
        try {
            Optional<Student> student = studentRepository.findByEmail(email);
            if (!student.isPresent()) {
                log.debug("No se encontró estudiante con email: {}", email);
                throw SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND,"Estudiante no encontrado con email: " + email);
            }
            log.debug("Estudiante encontrado por email: {}", email);
            return student;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al buscar estudiante por email: " + email, e);
        }
    }

    @Transactional
    @Override
    public boolean existsByCode(String code) throws SirhaException {
        log.debug("Verificando existencia de código: {}", code);
        try {
            boolean exists = studentRepository.existsByCodigo(code);
            log.debug("Código {} existe: {}", code, exists);
            return exists;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al verificar código: " + code, e);
        }
    }

    @Transactional
    @Override
    public boolean existsByEmail(String email) throws SirhaException {
        log.debug("Verificando existencia de email: {}", email);
        try {
            boolean exists = studentRepository.findByEmail(email).isPresent();
            log.debug("Email {} existe: {}", email, exists);
            return exists;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al verificar email: " + email, e);
        }
    }

    private Student registerStudent(Student student) throws SirhaException {
        log.info("Registrando estudiante: {}", student.getUsername());
        try {
            Student registeredStudent = studentRepository.save(student);
            log.info("Estudiante registrado exitosamente: {} con ID: {}", 
                    registeredStudent.getUsername(), registeredStudent.getId());
            return registeredStudent;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al registrar estudiante: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Student registerStudent(RegisterRequest request) throws SirhaException {
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
            
            if (studentRepository.findByUsername(request.getUsername()).isPresent()) {
                log.warn("Intento de registro con username duplicado: {}" , request.getUsername());
                throw SirhaException.of(ErrorCodeSirha.USERNAME_ALREADY_EXISTS,"El username ya está registrado");
            }

            if (studentRepository.existsByCodigo(request.getCodigo())) {
                log.warn("Intento de registro con código duplicado: {} para usuario: {}", 
                        request.getCodigo(), request.getUsername());
                throw SirhaException.of(ErrorCodeSirha.CODE_ALREADY_EXISTS,"El código estudiantil ya está registrado");
            }
            
            if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
                log.warn("Intento de registro con email duplicado: {} para usuario: {}", 
                        request.getEmail(), request.getUsername());
                throw SirhaException.of(ErrorCodeSirha.EMAIL_ALREADY_EXISTS,"El email ya está registrado");
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
            
            return savedStudent;
            
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno durante el registro: " + e.getMessage(), e);
        }
    }


    @Transactional
    @Override
    public List<Schedule> getCurrentSchedule(String username) throws SirhaException {
        log.info("Consultando horario actual para usuario: {}", username);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado para consulta de horario: {}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });
                
            List<Schedule> schedule = student.getCurrentSchedule();
            log.info("Horario actual obtenido exitosamente para {}: {} materias", username, schedule.size());
            return schedule;
            
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar horario actual: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Schedule> getScheduleForPeriod(String username, String period) throws SirhaException {
        log.info("Consultando horario para usuario: {} en período: {}", username, period);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });
                
            AcademicPeriod academicPeriod = academicPeriodRepository.findByPeriod(period)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_FOUND.getDefaultMessage(),"{}", period);
                    return SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_FOUND);
                });
                
            List<Schedule> schedule = student.getScheduleForPeriod(academicPeriod);
            log.info("Horario del período {} obtenido para {}: {} materias", period, username, schedule.size());
            return schedule;
            
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar horario del período", e);
        }
    }

    @Transactional
    @Override
    public Map<AcademicPeriod, List<Schedule>> getAllSchedules(String username) throws SirhaException {
        log.info("Consultando todos los horarios para usuario: {}", username);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });
                
            Map<AcademicPeriod, List<Schedule>> allSchedules = student.getAllSchedules();
            log.info("Todos los horarios obtenidos para {}: {} períodos", username, allSchedules.size());
            return allSchedules;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar todos los horarios: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Map<SemaforoColores, List<SubjectDecoratorDTO>> getAcademicPensum(String username) throws SirhaException {
        log.info("Consultando pensum académico para usuario: {}", username);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });
                
            Map<SemaforoColores, List<SubjectDecoratorDTO>> pensum = student.getAcademicPensum();
            log.info("Pensum académico obtenido para {}: {} categorías", username, pensum.size());
            
            // Log detallado del pensum
            pensum.forEach((color, subjects) -> 
                log.debug("Pensum {} para {}: {} materias - {}", color, username, subjects.size(), subjects));
                
            return pensum;
            
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar pensum académico: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public CambioGrupo createRequestCambioGrupo(String studentName, String subjectName, String codeNewGroup) throws SirhaException {
        log.info("Creando solicitud de cambio de grupo - Usuario: {}, Materia: {}, Nuevo Grupo: {}", 
                studentName, subjectName, codeNewGroup);
        
        try {
            Student student = studentRepository.findByUsername(studentName)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado para cambio de grupo: {}", studentName);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });
            
            Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> {
                    log.warn("Materia no encontrada: {}", subjectName);
                    return SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
                });

            Group group = groupRepository.findByCode(codeNewGroup)
                .orElseThrow(() -> {
                    log.warn("Grupo no encontrado: {}", codeNewGroup);
                    return SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
                });

            log.debug("Creando solicitud de cambio de grupo para {}: {} -> {}", 
                     studentName, subjectName, codeNewGroup);
                     
            CambioGrupo cambio = student.createGroupChangeRequest(subject, group);
            
            log.info("Solicitud de cambio de grupo creada exitosamente - ID: {} para usuario: {}", 
                    cambio.getId(), studentName);
            
            return cambio;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al crear solicitud de cambio de grupo: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public CambioMateria createRequestCambioMateria(String studentName, String subjectName, String newSubjectName, String codeNewGroup) throws SirhaException {
        log.info("Creando solicitud de cambio de materia - Usuario: {}, De: {} -> A: {}, Grupo: {}", 
                studentName, subjectName, newSubjectName, codeNewGroup);
        
        try {
            Student student = studentRepository.findByUsername(studentName)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado para cambio de materia: {}", studentName);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            Subject subjectOld = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> {
                    log.warn("Materia antigua no encontrada: {}", subjectName);
                    return SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
                });

            Subject subjectNew = subjectRepository.findByName(newSubjectName)
                .orElseThrow(() -> {
                    log.warn("Materia nueva no encontrada: {}", newSubjectName);
                    return SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
                });

            Group group = groupRepository.findByCode(codeNewGroup)
                .orElseThrow(() -> {
                    log.warn("Grupo no encontrado: {}", codeNewGroup);
                    return SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
                });

            log.debug("Creando solicitud de cambio de materia para {}: {} -> {} en grupo {}", 
                     studentName, subjectName, newSubjectName, codeNewGroup);
                     
            CambioMateria cambio = student.createSubjectChangeRequest(subjectOld, subjectNew, group);
            
            log.info("Solicitud de cambio de materia creada exitosamente - ID: {} para usuario: {}", 
                    cambio.getId(), studentName);
            
            return cambio;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al crear solicitud de cambio de materia: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Student deleteById(String id) throws SirhaException {
        log.info("Eliminando estudiante por ID: {}", id);
        try {
            Optional<Student> existingStudent = studentRepository.findById(id);
            if (existingStudent.isEmpty()) {
                log.warn("No se encontró estudiante con ID: {} para eliminar", id);
                throw SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
            }
            studentRepository.deleteById(id);
            log.info("Estudiante eliminado exitosamente - ID: {}", id);
            return existingStudent.get();
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al eliminar estudiante: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<BaseRequest> getAllRequests(String username) throws SirhaException {
        log.info("Consultando todas las solicitudes para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });
                
            List<BaseRequest> requests = student.getSolicitudes();
            log.info("Se encontraron {} solicitudes para {}", requests.size(), username);
            return requests;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar solicitudes: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public BaseRequest getRequestById(String username, String requestId) throws SirhaException {
        log.info("Consultando solicitud por ID: {} para usuario: {}", requestId, username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            BaseRequest request = student.getRequestById(requestId);

            log.info("Solicitud encontrada - ID: {} para usuario: {}", requestId, username);
            return request;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar solicitud: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<BaseRequest> getRequestsHistory(String username) throws SirhaException {
        log.info("Consultando historial de solicitudes para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            List<BaseRequest> requests = student.getRequestsHistory();
            log.info("Se encontraron {} solicitudes en el historial para {}", requests.size(), username);
            return requests;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar historial de solicitudes: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<SemaforoColores, Double> getPercentageByColor(String username) throws SirhaException {
        log.info("Consultando porcentajes por color para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            Map<SemaforoColores, Double> percentages = student.getPercentageByColor();
            log.info("Porcentajes por color obtenidos para {}", username);
            return percentages;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar : " + e.getMessage(), e);
        }
    }

    @Override
    public StudentDTO getStudentBasicInfo(String username) throws SirhaException {
        log.info("Consultando información básica del estudiante: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            StudentDTO studentDTO = student.getStudentBasicInfo();
            log.info("Información básica del estudiante obtenida para {}", username);
            return studentDTO;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar informacion del estudiante: " + e.getMessage(), e);
        }
    }

    @Override
    public StudentReportDTO generateCompleteReport(String username) throws SirhaException {
        log.info("Generando reporte completo para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            StudentReportDTO reportDTO = student.generateCompleteReport();
            log.info("Reporte completo generado para {}", username);
            return reportDTO;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al generar el reporte: " + e.getMessage(), e);
        }
    }

    @Override
    public String getAcademicSummary(String username) throws SirhaException {
        log.info("Consultando resumen académico para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            String summary = student.getAcademicSummary();
            log.info("Resumen académico obtenido para {}", username);
            return summary;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar el resumen académico: " + e.getMessage(), e);
        }
    }

    @Override
    public RequestApprovalRateDTO getRequestApprovalRate(String username) throws SirhaException {
        log.info("Consultando tasa de aprobación de solicitudes para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            RequestApprovalRateDTO approvalRateDTO = student.getRequestApprovalRate();
            log.info("Tasa de aprobación de solicitudes obtenida para {}", username);
            return approvalRateDTO;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar el estado de las solicitudes: " + e.getMessage(), e);
        }
    }

    @Override
    public int getSubjectsByColorCount(String username, SemaforoColores color) throws SirhaException {
        log.info("Consultando cantidad de materias por color '{}' para usuario: {}", color, username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            int count = student.getSubjectsByColorCount(color);
            log.info("Cantidad de materias con color {} obtenida para {}: {}", color, username, count);
            return count;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar información del pensum: " + e.getMessage(), e);
        }
    }

    @Override
    public double getApprovalRequestPercentage(String username) throws SirhaException {
        log.info("Consultando porcentaje de solicitudes aprobadas para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            double percentage = student.getApprovalRequestPercentage();
            log.info("Porcentaje de solicitudes aprobadas obtenido para {}: {}", username, percentage);
            return percentage;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar pensum académico: " + e.getMessage(), e);
        }
    }

    @Override
    public double getRejectionRequestPercentage(String username) throws SirhaException {
        log.info("Consultando porcentaje de solicitudes rechazadas para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            double percentage = student.getRejectionRequestPercentage();
            log.info("Porcentaje de solicitudes rechazadas obtenido para {}: {}", username, percentage);
            return percentage;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar información de las solicitudes rechazadas: " + e.getMessage(), e);
        }
    }

    @Override
    public double getPendingRequestPercentage(String username) throws SirhaException {
        log.info("Consultando porcentaje de solicitudes pendientes para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            double percentage = student.getPendingRequestPercentage();
            log.info("Porcentaje de solicitudes pendientes obtenido para {}: {}", username, percentage);
            return percentage;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar información de las solicitudes pendientes: " + e.getMessage(), e);
        }
    }

    @Override
    public double getInReviewRequestPercentage(String username) throws SirhaException {
        log.info("Consultando porcentaje de solicitudes en revisión para usuario: {}", username);
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn(ErrorCodeSirha.STUDENT_NOT_FOUND.getDefaultMessage(),"{}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            double percentage = student.getInReviewRequestPercentage();
            log.info("Porcentaje de solicitudes en revisión obtenido para {}: {}", username, percentage);
            return percentage;
        } catch ( SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar información de las solicitudes en revisión: " + e.getMessage(), e);
        }
    }

    @Override
    public void enrollSubject(String username, String subjectName, String groupCode) throws SirhaException {
        log.info("Inscribiendo materia para usuario: {}, Materia: {}, Grupo: {}", username, subjectName, groupCode);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado para inscripción de materia: {}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> {
                    log.warn("Materia no encontrada para inscripción: {}", subjectName);
                    return SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
                });

            Group group = groupRepository.findByCode(groupCode)
                .orElseThrow(() -> {
                    log.warn("Grupo no encontrado para inscripción: {}", groupCode);
                    return SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
                });

            student.enrollSubject(subject, group);
            studentRepository.save(student);
            
            log.info("Materia inscrita exitosamente para usuario: {}, Materia: {}, Grupo: {}", username, subjectName, groupCode);

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al inscribir materia: " + e.getMessage(), e);
        }
    }

    @Override
    public void unenrollSubject(String username, String subjectName, String groupCode) throws SirhaException {
        log.info("Desinscribiendo materia para usuario: {}, Materia: {}, Grupo: {}", username, subjectName, groupCode);
        
        try {
            Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Estudiante no encontrado para desinscripción de materia: {}", username);
                    return SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND);
                });

            Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> {
                    log.warn("Materia no encontrada para desinscripción: {}", subjectName);
                    return SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
                });

            Group group = groupRepository.findByCode(groupCode)
                .orElseThrow(() -> {
                    log.warn("Grupo no encontrado para desinscripción: {}", groupCode);
                    return SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
                });

            student.unenrollSubject(subject, group);
            studentRepository.save(student);
            
            log.info("Materia desinscrita exitosamente para usuario: {}, Materia: {}, Grupo: {}", username, subjectName, groupCode);

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al desinscribir materia: " + e.getMessage(), e);
        }
    }


}