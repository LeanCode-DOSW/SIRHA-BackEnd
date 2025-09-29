package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentDTO;
import edu.dosw.sirha.SIRHA_BackEnd.service.StudentService;
import edu.dosw.sirha.SIRHA_BackEnd.util.MapperUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
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
     * @param dto objeto StudentDTO con los datos del nuevo estudiante.
     *           Debe contener al menos username y codigo válidos.
     *           No debe ser null.
     * @return StudentDTO del estudiante creado, incluyendo el ID asignado
     * 
     * @example
     * POST /api/students
     * Content-Type: application/json
     * 
     * {
     *   "username": "carlos.ruiz",
     *   "codigo": "202112347"
     * }
     * 
     * Respuesta (200):
     * {
     *   "id": "generated-id-67890",
     *   "username": "carlos.ruiz",
     *   "codigo": "202112347",
     *   "solicitudesIds": []
     * }
     */
    @PostMapping
    public StudentDTO create(@RequestBody StudentDTO dto) {
        // Crear nueva instancia de Student con datos del DTO
        Student newStudent = new Student(
            dto.getUsername(), 
            dto.getEmail(),  
            "defaultPass", // Contraseña temporal - debe ser actualizada
            dto.getCodigo()
        );
        
        // Guardar el estudiante y convertir a DTO para la respuesta
        Student savedStudent = studentService.save(newStudent);
        return MapperUtils.toDTO(savedStudent);
    }

}