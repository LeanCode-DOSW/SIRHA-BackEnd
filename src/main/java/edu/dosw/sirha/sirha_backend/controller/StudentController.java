package edu.dosw.sirha.sirha_backend.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.CambioGrupo;
import edu.dosw.sirha.sirha_backend.domain.model.CambioMateria;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Semaforo;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgress;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentReportDTO;
import edu.dosw.sirha.sirha_backend.dto.RequestApprovalRateDTO;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.StudentService;
import edu.dosw.sirha.sirha_backend.util.StudentMapper;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de estudiantes en el sistema SIRHA.
 * 
 * Este controlador maneja todas las operaciones CRUD (Create, Read, Update, Delete)
 * relacionadas con los estudiantes, incluyendo:
 * - Consulta de todos los estudiantes registrados
 * - Búsqueda de estudiantes por ID
 * - Creación de nuevos estudiantes
 * 
 * Todos los endpoints trabajan con objetos StudentDTO para la transferencia de datos,
 * proporcionando una capa de abstracción entre el modelo interno y la API pública.
 * 
 * Los endpoints están mapeados bajo la ruta base "/api/students" siguiendo
 * las convenciones REST para recursos de colección.
 * 
 * @see StudentService
 * @see StudentDTO
 * @see Student
 * @see StudentMapper
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "API para gestión de estudiantes y sus horarios académicos")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener todos los estudiantes", description = "Retorna una lista completa de todos los estudiantes registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<StudentDTO>> getAll() throws SirhaException {
        List<StudentDTO> students = studentService.findAll()
                .stream()
                .map(StudentMapper::toDTO)
                .toList();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Buscar estudiante por ID", description = "Obtiene un estudiante específico por su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudentDTO> getById(@PathVariable String id) throws SirhaException {
        Student student = studentService.findById(id)
                .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND, id));
        return ResponseEntity.ok(StudentMapper.toDTO(student));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN') or (hasRole('STUDENT') and authentication.name == #username)")
    @Operation(summary = "Buscar estudiante por username", description = "Obtiene un estudiante específico por su nombre de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudentDTO> getByUsername(@PathVariable String username) throws SirhaException {
        Student student = studentService.findByUsername(username)
                .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND, username));
        return ResponseEntity.ok(StudentMapper.toDTO(student));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Buscar estudiante por email", description = "Obtiene un estudiante específico por su dirección de correo electrónico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudentDTO> getByEmail(@PathVariable String email) throws SirhaException {
        Student student = studentService.findByEmail(email)
                .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.STUDENT_NOT_FOUND, email));
        return ResponseEntity.ok(StudentMapper.toDTO(student));
    }

    @GetMapping("/exists/code/{code}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verificar existencia por código", description = "Verifica si existe un estudiante con el código especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) throws SirhaException {
        boolean exists = studentService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email/{email}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verificar existencia por email", description = "Verifica si existe un estudiante con el email especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) throws SirhaException {
        boolean exists = studentService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estudiante", description = "Elimina un estudiante del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Estudiante eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable String id) throws SirhaException {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{username}/academic-period")
    @Operation(summary = "Establecer período académico del estudiante", description = "Asigna el período académico actual al estudiante especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Período académico establecido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante o período no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAnyRole('DEAN','ADMIN') or (hasRole('STUDENT') and authentication.name == #username)")
    public ResponseEntity<AcademicPeriod> setAcademicPeriodForStudent(@PathVariable String username,
                                                                       @RequestParam String period) throws SirhaException {
        AcademicPeriod ap = studentService.setAcademicPeriodForStudent(username, period);
        return ResponseEntity.ok(ap);
    }

    @PostMapping("/{username}/academic-progress")
    @Operation(summary = "Establecer progreso académico del estudiante", description = "Establece el objeto de progreso académico para el estudiante (Semáforo)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Progreso académico actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Payload inválido"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAnyRole('DEAN','ADMIN') or (hasRole('STUDENT') and authentication.name == #username)")
    public ResponseEntity<AcademicProgress> setAcademicProgressForStudent(
            @PathVariable String username,
            @RequestBody Semaforo academicProgress) throws SirhaException {
        AcademicProgress result = studentService.setAcademicProgressForStudent(username, academicProgress);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/schedule/{username}")
    @Operation(summary = "Obtener horario actual", description = "Obtiene el horario actual del estudiante para el período académico vigente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<List<Schedule>> getCurrentSchedule(@PathVariable String username) throws SirhaException {
        List<Schedule> schedules = studentService.getCurrentSchedule(username);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/schedule/{username}/period")
    @Operation(summary = "Obtener horario por período", description = "Obtiene el horario del estudiante para un período académico específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario del período obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado o período no válido"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<List<Schedule>> getScheduleForPeriod(@PathVariable String username, @RequestParam String period) throws SirhaException {
        List<Schedule> schedules = studentService.getScheduleForPeriod(username, period);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/schedules/{username}")
    @Operation(summary = "Obtener todos los horarios", description = "Obtiene el historial completo de horarios del estudiante organizados por período académico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial de horarios obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Map<AcademicPeriod,List<Schedule>>> getAllSchedules(@PathVariable String username) throws SirhaException {
        Map<AcademicPeriod,List<Schedule>> schedules = studentService.getAllSchedules(username);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/academicPensum/{username}")
    @Operation(summary = "Obtener pensum académico", description = "Obtiene el progreso académico del estudiante organizado por colores de semáforo (materias disponibles, cursando, aprobadas)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pensum académico obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Map<SemaforoColores,List<SubjectDecoratorDTO>>> getAcademicPensum(@PathVariable String username) throws SirhaException {
        Map<SemaforoColores,List<SubjectDecoratorDTO>> pensum = studentService.getAcademicPensum(username);
        return ResponseEntity.ok(pensum);
    }

    @GetMapping("/{username}/percentage-by-color")
    @Operation(summary = "Obtener porcentajes por color de semáforo", description = "Obtiene los porcentajes de materias por cada color del semáforo académico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Porcentajes obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Map<SemaforoColores, Double>> getPercentageByColor(@PathVariable String username) throws SirhaException {
        Map<SemaforoColores, Double> percentages = studentService.getPercentageByColor(username);
        return ResponseEntity.ok(percentages);
    }

    @GetMapping("/{username}/basic-info")
    @Operation(summary = "Obtener información básica del estudiante", description = "Obtiene la información básica y esencial del estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información básica obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<StudentDTO> getStudentBasicInfo(@PathVariable String username) throws SirhaException {
        StudentDTO basicInfo = studentService.getStudentBasicInfo(username);
        return ResponseEntity.ok(basicInfo);
    }

    @GetMapping("/{username}/subjects/color/{color}")
    @Operation(summary = "Obtener materias por color", description = "Obtiene todas las materias del estudiante que tienen un color específico en el semáforo académico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materias obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "400", description = "Color de semáforo inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Integer> getSubjectsByColorCount(@PathVariable String username, @PathVariable SemaforoColores color) throws SirhaException {
        int count = studentService.getSubjectsByColorCount(username, color);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/{studentName}/solicitudes/cambio-grupo")
    @Operation(summary = "Crear solicitud de cambio de grupo", description = "Crea una nueva solicitud para cambiar de grupo en una materia específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud de cambio de grupo creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos o solicitud no válida"),
        @ApiResponse(responseCode = "404", description = "Estudiante, materia o grupo no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - no se puede realizar el cambio"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('STUDENT') and authentication.name == #studentName")
    public ResponseEntity<CambioGrupo> createRequestCambioGrupo(
            @PathVariable String studentName, 
            @RequestParam String subjectName, 
            @RequestParam String codeNewGroup) throws SirhaException {
        CambioGrupo cambioGrupo = studentService.createRequestCambioGrupo(studentName, subjectName, codeNewGroup);
        return ResponseEntity.ok(cambioGrupo);
    }

    @PostMapping("/{studentName}/solicitudes/cambio-materia")
    @Operation(summary = "Crear solicitud de cambio de materia", description = "Crea una nueva solicitud para cambiar una materia por otra diferente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud de cambio de materia creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos o solicitud no válida"),
        @ApiResponse(responseCode = "404", description = "Estudiante, materias o grupo no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - prerrequisitos no cumplidos o conflictos de horario"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('STUDENT') and authentication.name == #studentName")
    public ResponseEntity<CambioMateria> createRequestCambioMateria(
            @PathVariable String studentName, 
            @RequestParam String subjectName, 
            @RequestParam String newSubjectName, 
            @RequestParam String codeNewGroup) throws SirhaException {
        CambioMateria cambioMateria = studentService.createRequestCambioMateria(studentName, subjectName, newSubjectName, codeNewGroup);
        return ResponseEntity.ok(cambioMateria);
    }

    @GetMapping("/{username}/requests")
    @Operation(summary = "Obtener todas las solicitudes", description = "Obtiene todas las solicitudes del estudiante, activas e históricas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitudes obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<List<BaseRequest>> getAllRequests(@PathVariable String username) throws SirhaException {
        List<BaseRequest> requests = studentService.getAllRequests(username);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{username}/requests/{requestId}")
    @Operation(summary = "Obtener solicitud específica", description = "Obtiene una solicitud específica del estudiante por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante o solicitud no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<BaseRequest> getRequestById(@PathVariable String username, @PathVariable String requestId) throws SirhaException {
        BaseRequest request = studentService.getRequestById(username, requestId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/{username}/requests/history")
    @Operation(summary = "Obtener historial de solicitudes", description = "Obtiene el historial de solicitudes completadas (aprobadas o rechazadas) del estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<List<BaseRequest>> getRequestsHistory(@PathVariable String username) throws SirhaException {
        List<BaseRequest> history = studentService.getRequestsHistory(username);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{username}/complete-report")
    @Operation(summary = "Generar reporte completo", description = "Genera un reporte académico completo del estudiante con todas sus estadísticas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<StudentReportDTO> generateCompleteReport(@PathVariable String username) throws SirhaException {
        StudentReportDTO report = studentService.generateCompleteReport(username);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{username}/academic-summary")
    @Operation(summary = "Obtener resumen académico", description = "Obtiene un resumen académico con estadísticas clave del estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen académico obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<String> getAcademicSummary(@PathVariable String username) throws SirhaException {
        String summary = studentService.getAcademicSummary(username);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{username}/request-approval-rate")
    @Operation(summary = "Obtener tasa de aprobación de solicitudes", description = "Obtiene estadísticas detalladas sobre la tasa de aprobación de solicitudes del estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasa de aprobación obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<RequestApprovalRateDTO> getRequestApprovalRate(@PathVariable String username) throws SirhaException {
        RequestApprovalRateDTO approvalRate = studentService.getRequestApprovalRate(username);
        return ResponseEntity.ok(approvalRate);
    }

    @GetMapping("/{username}/requests/approval-percentage")
    @Operation(summary = "Porcentaje de solicitudes aprobadas", description = "Obtiene el porcentaje de solicitudes aprobadas del estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Porcentaje calculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Double> getApprovalRequestPercentage(@PathVariable String username) throws SirhaException {
        Double percentage = studentService.getApprovalRequestPercentage(username);
        return ResponseEntity.ok(percentage);
    }

    @GetMapping("/{username}/requests/rejection-percentage")
    @Operation(summary = "Porcentaje de solicitudes rechazadas", description = "Obtiene el porcentaje de solicitudes rechazadas del estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Porcentaje calculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Double> getRejectionRequestPercentage(@PathVariable String username) throws SirhaException {
        Double percentage = studentService.getRejectionRequestPercentage(username);
        return ResponseEntity.ok(percentage);
    }

    @GetMapping("/{username}/requests/pending-percentage")
    @Operation(summary = "Porcentaje de solicitudes pendientes", description = "Obtiene el porcentaje de solicitudes pendientes del estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Porcentaje calculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Double> getPendingRequestPercentage(@PathVariable String username) throws SirhaException {
        Double percentage = studentService.getPendingRequestPercentage(username);
        return ResponseEntity.ok(percentage);
    }

    @GetMapping("/{username}/requests/review-percentage")
    @Operation(summary = "Porcentaje de solicitudes en revisión", description = "Obtiene el porcentaje de solicitudes en estado de revisión del estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Porcentaje calculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("#username == authentication.name or hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Double> getInReviewRequestPercentage(@PathVariable String username) throws SirhaException {
        Double percentage = studentService.getInReviewRequestPercentage(username);
        return ResponseEntity.ok(percentage);
    }

    @PostMapping("/{studentName}/enroll")
    @Operation(summary = "Inscribir materia", description = "Inscribe una materia en un grupo para el estudiante autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materia inscrita exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante, materia o grupo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos o conflicto de inscripción"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('STUDENT') and authentication.name == #studentName")
    public ResponseEntity<Void> enrollSubject(
            @PathVariable String studentName,
            @RequestParam String subjectName,
            @RequestParam String groupCode) throws SirhaException {
        studentService.enrollSubject(studentName, subjectName, groupCode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studentName}/unenroll")
    @Operation(summary = "Desinscribir materia", description = "Desinscribe una materia de un grupo para el estudiante autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materia desinscrita exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante, materia o grupo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos o no es posible desinscribir"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('STUDENT') and authentication.name == #studentName")
    public ResponseEntity<Void> unenrollSubject(
            @PathVariable String studentName,
            @RequestParam String subjectName,
            @RequestParam String groupCode) throws SirhaException {
        studentService.unenrollSubject(studentName, subjectName, groupCode);
        return ResponseEntity.ok().build();
    }
}