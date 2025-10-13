package edu.dosw.sirha.sirha_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.CambioGrupo;
import edu.dosw.sirha.sirha_backend.domain.model.CambioMateria;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.sirha_backend.service.StudentService;
import edu.dosw.sirha.sirha_backend.util.MapperUtils;

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
 * @see MapperUtils
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Endpoint para obtener la lista completa de estudiantes registrados.
     * 
     * Recupera todos los estudiantes almacenados en la base de datos
     * y los convierte a DTOs para su transmisión. La respuesta incluye
     * información básica de cada estudiante sin datos sensibles.
     * 
     * Este endpoint es útil para:
     * - Páginas de administración con listados de estudiantes
     * - Reportes generales del sistema
     * - Búsquedas y filtros en interfaces de usuario
     * 
     * @return Lista de StudentDTO con todos los estudiantes registrados.
     *         Retorna lista vacía si no hay estudiantes registrados.
     * 
     * @example
     * GET /api/students
     * Accept: application/json
     * 
     * Respuesta:
     * [
     *   {
     *     "id": "1",
     *     "username": "juan.perez",
     *     "codigo": "202112345",
     *     "solicitudesIds": ["req1", "req2"]
     *   },
     *   {
     *     "id": "2", 
     *     "username": "maria.garcia",
     *     "codigo": "202112346",
     *     "solicitudesIds": []
     *   }
     * ]
     */
    @GetMapping
    public List<StudentDTO> getAll() {
        return studentService.findAll()
                .stream()
                .map(MapperUtils::toDTO)
                .toList();
    }

    /**
     * Endpoint para obtener un estudiante específico por su ID.
     * 
     * Busca un estudiante en la base de datos usando su identificador único
     * y retorna su información en formato DTO. Si el estudiante no existe,
     * lanza una excepción que resulta en un error HTTP 500.
     * 
     * @param id el identificador único del estudiante a buscar.
     *          No debe ser null o vacío.
     * @return StudentDTO con la información del estudiante encontrado
     * 
     * @throws RuntimeException si no se encuentra un estudiante con el ID especificado
     * 
     * @example
     * GET /api/students/12345
     * Accept: application/json
     * 
     * Respuesta exitosa:
     * {
     *   "id": "12345",
     *   "username": "juan.perez",
     *   "codigo": "202112345",
     *   "solicitudesIds": ["req1", "req2"]
     * }
     * 
     * Respuesta de error (500):
     * {
     *   "error": "Estudiante no encontrado"
     * }
     */
    @GetMapping("/{id}")
    public StudentDTO getById(@PathVariable String id) {
        return studentService.findById(id)
                .map(MapperUtils::toDTO)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
    }

    @GetMapping("/schedule/{username}")
    public ResponseEntity<List<Schedule>> getCurrentSchedule(@PathVariable String username){
        try {
            List<Schedule> schedules = studentService.getCurrentSchedule(username);
            return ResponseEntity.ok(schedules);
        } catch (IllegalArgumentException e) {
            // Retornar 404 cuando el estudiante no existe
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/schedule/{username}/period")
    public ResponseEntity<List<Schedule>> getScheduleForPeriod(@PathVariable String username, @RequestParam String period){
        try {
            List<Schedule> schedules = studentService.getScheduleForPeriod(username, period);
            return ResponseEntity.ok(schedules);
        } catch (IllegalArgumentException e) {
            // Retornar 404 cuando el estudiante no existe
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/schedules/{username}")
    public ResponseEntity<Map<AcademicPeriod,List<Schedule>>> getAllSchedules(@PathVariable String username){
        try {
            Map<AcademicPeriod,List<Schedule>> schedules = studentService.getAllSchedules(username);
            return ResponseEntity.ok(schedules);
        } catch (IllegalArgumentException e) {
            // Retornar 404 cuando el estudiante no existe
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/academicPensum/{username}")
    public ResponseEntity<Map<SemaforoColores,List<SubjectDecoratorDTO>>> getAcademicPensum(@PathVariable String username){
        try {
            Map<SemaforoColores,List<SubjectDecoratorDTO>> pensum = studentService.getAcademicPensum(username);
            return ResponseEntity.ok(pensum);
        } catch (IllegalArgumentException e) {
            // Retornar 404 cuando el estudiante no existe
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{studentName}/solicitudes/cambio-grupo")
    public ResponseEntity<CambioGrupo> createRequestCambioGrupo(
            @PathVariable String studentName, 
            @RequestParam String subjectName, 
            @RequestParam String codeNewGroup) {
        try {
            CambioGrupo cambioGrupo = studentService.createRequestCambioGrupo(studentName, subjectName, codeNewGroup);
            return ResponseEntity.ok(cambioGrupo);
        } catch (IllegalArgumentException e) {
            // Retornar 404 cuando el estudiante no existe
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{studentName}/solicitudes/cambio-materia")
    public ResponseEntity<CambioMateria> createRequestCambioMateria(
            @PathVariable String studentName, 
            @RequestParam String subjectName, 
            @RequestParam String newSubjectName, 
            @RequestParam String codeNewGroup) {
        try {
            CambioMateria cambioMateria = studentService.createRequestCambioMateria(studentName, subjectName, newSubjectName, codeNewGroup);
            return ResponseEntity.ok(cambioMateria);
        } catch (IllegalArgumentException e) {
            // Retornar 404 cuando el estudiante no existe
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Endpoint para crear un nuevo estudiante en el sistema.
     * 
     * Recibe los datos del estudiante a través de un DTO, los convierte
     * a una entidad del dominio y los persiste en la base de datos.
     * La contraseña se asigna temporalmente como "defaultPass" y debe
     * ser actualizada por el administrador o el propio estudiante.
     * 
     * Validaciones automáticas:
     * - El código de estudiante debe ser único
     * - El username debe ser único en el sistema
     * - Los campos obligatorios no deben estar vacíos
     * 
     */
    @PostMapping
    public StudentDTO create(@RequestBody StudentDTO dto) {
        Student newStudent = new Student(
            dto.getUsername(), 
            dto.getEmail(),  
            "defaultPass", // Contraseña temporal - debe ser actualizada
            dto.getCode()
        );
        
        Student savedStudent = studentService.save(newStudent);
        return MapperUtils.toDTO(savedStudent);
    }


    

    

}