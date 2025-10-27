package edu.dosw.sirha.sirha_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.dosw.sirha.sirha_backend.util.GroupMapper;
import edu.dosw.sirha.sirha_backend.util.SubjectMapper;
import edu.dosw.sirha.sirha_backend.domain.model.MustHaveApprovedSubject;
import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.SubjectService;

/**
 * Controlador REST para la gestión de materias y grupos en el sistema SIRHA.
 * 
 * Este controlador maneja todas las operaciones CRUD relacionadas con:
 * - Materias: creación, consulta, actualización y eliminación
 * - Grupos: gestión completa incluyendo horarios, profesores y capacidad
 * - Asignaciones: profesores a grupos, horarios a grupos
 * - Consultas: disponibilidad, capacidad, estados de grupos
 * 
 * Las materias representan las asignaturas del plan de estudios, mientras que
 * los grupos son las instancias específicas de cada materia con horarios,
 * profesores y estudiantes asignados.
 * 
 * Los endpoints están mapeados bajo la ruta base "/api/subjects" siguiendo
 * las convenciones REST para recursos jerárquicos.
 * 
 * @see SubjectService
 * @see Subject
 * @see Group
 * @see Professor
 * @see Schedule
 */
@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subjects and Groups", description = "API para gestión de materias y grupos")
public class SubjectAndGroupController {

    private final SubjectService subjectService;

    public SubjectAndGroupController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
        
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener todas las materias", description = "Retorna una lista de todas las materias disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de materias obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Subject>> findAllSubjects() {
        List<Subject> subjects = subjectService.findAll();
        return ResponseEntity.ok(subjects);
    }
    
    @GetMapping("/{name}")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Buscar materia por nombre", description = "Obtiene una materia específica por su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materia encontrada"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Subject> findSubjectByName(@PathVariable String name) throws SirhaException {
        Subject subject = subjectService.findByName(name);
        return ResponseEntity.ok(subject);
    }
    
    @PostMapping
    @Operation(summary = "Crear nueva materia", description = "Crea una nueva materia en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Materia creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la materia inválidos"),
        @ApiResponse(responseCode = "409", description = "La materia ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Subject> saveSubject(@RequestBody SubjectDTO subjectDTO) throws SirhaException {
        Subject subject = SubjectMapper.toEntity(subjectDTO);
        Subject savedSubject = subjectService.save(subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubject);
    }
    
    @DeleteMapping("/{name}")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar materia", description = "Elimina una materia del sistema por su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materia eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Subject> deleteSubjectByName(@PathVariable String name) throws SirhaException {
        Subject deletedSubject = subjectService.deleteByName(name);
        return ResponseEntity.ok(deletedSubject);
    }
    
    @GetMapping("/{name}/exists")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verificar existencia de materia", description = "Verifica si una materia existe por su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> existsSubjectByName(@PathVariable String name) throws SirhaException {
        boolean exists = subjectService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener todos los grupos", description = "Retorna una lista de todos los grupos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de grupos obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Group>> findAllGroups() throws SirhaException {
        List<Group> groups = subjectService.findAllGroups();
        return ResponseEntity.ok(groups);
    }
    
    @GetMapping("/{subjectName}/groups")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener grupos de una materia", description = "Obtiene todos los grupos de una materia específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grupos obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Group>> getGroupsOfSubject(@PathVariable String subjectName) throws SirhaException {
        List<Group> groups = subjectService.getGroupsOfSubject(subjectName);
        return ResponseEntity.ok(groups);
    }
    
    @GetMapping("/{subjectName}/groups/open")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener grupos abiertos de una materia", description = "Obtiene los grupos abiertos para inscripción de una materia específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grupos abiertos obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Group>> getOpenGroupsOfSubject(@PathVariable String subjectName) throws SirhaException {
        List<Group> openGroups = subjectService.getOpenGroupsOfSubject(subjectName);
        return ResponseEntity.ok(openGroups);
    }
    
    @GetMapping("/groups/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Buscar grupo por ID", description = "Obtiene un grupo específico por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Group> getGroupById(@PathVariable String id) throws SirhaException {
        Group group = subjectService.getGroupById(id);
        return ResponseEntity.ok(group);
    }
    
    @PostMapping("/{subjectName}/groups")
    @Operation(summary = "Crear nuevo grupo", description = "Crea un nuevo grupo para una materia específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Grupo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del grupo inválidos"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    public ResponseEntity<Group> saveGroup(@PathVariable String subjectName, @RequestBody GroupDTO groupDTO) throws SirhaException {
        Subject subject = subjectService.findByName(subjectName);
        Group group = GroupMapper.toEntity(subject, groupDTO);
        Group savedGroup = subjectService.saveGroup(subjectName, group);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }
    
    @DeleteMapping("/groups/{id}")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar grupo", description = "Elimina un grupo del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grupo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Group> deleteGroupById(@PathVariable String id) throws SirhaException {
        Group deletedGroup = subjectService.deleteGroupById(id);
        return ResponseEntity.ok(deletedGroup);
    }
    
    @GetMapping("/groups/{id}/exists")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verificar existencia de grupo", description = "Verifica si un grupo existe por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> existsGroupById(@PathVariable String id) throws SirhaException {
        boolean exists = subjectService.existsGroupById(id);
        return ResponseEntity.ok(exists);
    }
    
    @DeleteMapping("/{subjectName}/groups")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar todos los grupos de una materia", description = "Elimina todos los grupos asociados a una materia específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grupos eliminados exitosamente"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Group>> deleteGroupsBySubjectName(@PathVariable String subjectName) throws SirhaException {
        List<Group> deletedGroups = subjectService.deleteGroupsBySubjectName(subjectName);
        return ResponseEntity.ok(deletedGroups);
    }
    
    @PutMapping("/groups/{groupId}/professor")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Asignar profesor a grupo", description = "Asigna un profesor a un grupo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profesor asignado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos del profesor inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Group> assignProfessor(@PathVariable String groupId, @RequestBody Professor professor) throws SirhaException {
        Group updatedGroup = subjectService.assignProfessor(groupId, professor);
        return ResponseEntity.ok(updatedGroup);
    }
    
    @PostMapping("/groups/{groupId}/schedules")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Agregar horario a grupo", description = "Agrega un nuevo horario a un grupo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario agregado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos del horario inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto de horarios"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Group> addSchedule(@PathVariable String groupId, @RequestBody Schedule schedule) throws SirhaException {
        Group updatedGroup = subjectService.addSchedule(groupId, schedule);
        return ResponseEntity.ok(updatedGroup);
    }
    
    @GetMapping("/groups/{groupId}/professor")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener profesor de grupo", description = "Obtiene el profesor asignado a un grupo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profesor obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado o profesor no asignado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Professor> getProfessor(@PathVariable String groupId) throws SirhaException {
        Professor professor = subjectService.getProfessor(groupId);
        return ResponseEntity.ok(professor);
    }
    
    @GetMapping("/groups/{groupId}/schedules")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener horarios de grupo", description = "Obtiene todos los horarios de un grupo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horarios obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable String groupId) throws SirhaException {
        List<Schedule> schedules = subjectService.getSchedules(groupId);
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/groups/{groupId}/full")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Verificar si grupo está lleno", description = "Verifica si un grupo ha alcanzado su capacidad máxima")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> isGroupFull(@PathVariable String groupId) throws SirhaException {
        boolean isFull = subjectService.isFull(groupId);
        return ResponseEntity.ok(isFull);
    }

    @GetMapping("/groups/{groupId}/available-seats")
    @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener cupos disponibles", description = "Obtiene el número de cupos disponibles en un grupo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupos disponibles obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Integer> getAvailableSeats(@PathVariable String groupId) throws SirhaException {
        int availableSeats = subjectService.getAvailableSeats(groupId);
        return ResponseEntity.ok(availableSeats);
    }
    
    @PutMapping("/groups/{groupId}/close")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Cerrar grupo", description = "Cierra un grupo para que no se puedan realizar más inscripciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grupo cerrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "400", description = "El grupo ya está cerrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Group> closeGroup(@PathVariable String groupId) throws SirhaException {
        Group closedGroup = subjectService.closeGroup(groupId);
        return ResponseEntity.ok(closedGroup);
    }

    @PutMapping("/groups/{groupId}/open")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Abrir grupo", description = "Abre un grupo para permitir inscripciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grupo abierto exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "400", description = "El grupo ya está abierto"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Group> openGroup(@PathVariable String groupId) throws SirhaException {
        Group openedGroup = subjectService.openGroup(groupId);
        return ResponseEntity.ok(openedGroup);
    }

    @PostMapping("/{subjectName}/prerequisites")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Agregar prerrequisito", description = "Agrega una regla de prerrequisito a una materia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Prerrequisito agregado"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Subject> addPrerequisite(@PathVariable String subjectName,
                                                   @RequestBody MustHaveApprovedSubject prerequisite) throws SirhaException {
        Subject updated = subjectService.addPrerequisite(subjectName, prerequisite);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }
    
}
