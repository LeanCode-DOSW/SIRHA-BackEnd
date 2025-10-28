package edu.dosw.sirha.sirha_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.GroupService;

/**
 * Controlador REST para la gestión de GRUPOS únicamente.
 */
@RestController
@RequestMapping("/api/groups")
@Tag(name = "Groups", description = "API para gestión de grupos académicos")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener todos los grupos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grupos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Group>> findAll() {
        List<Group> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Buscar grupo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    public ResponseEntity<Group> findById(@PathVariable String id) throws SirhaException {
        Group group = groupService.findById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/code/{code}")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Buscar grupo por código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    public ResponseEntity<Group> findByCode(@PathVariable String code) throws SirhaException {
        Group group = groupService.findByCode(code);
        return ResponseEntity.ok(group);
    }

    @PostMapping
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Crear nuevo grupo")
    public ResponseEntity<Group> save(
            @RequestParam String subjectName,
            @RequestBody GroupDTO groupDTO) throws SirhaException {
        Group savedGroup = groupService.save(groupDTO, subjectName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar grupo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    public ResponseEntity<Group> deleteById(@PathVariable String id) throws SirhaException {
        Group deletedGroup = groupService.deleteById(id);
        return ResponseEntity.ok(deletedGroup);
    }

    @GetMapping("/{id}/exists")
    // @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verificar existencia de grupo")
    public ResponseEntity<Boolean> existsById(@PathVariable String id) {
        boolean exists = groupService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    // ========== Gestión de profesores ==========

    @PutMapping("/{groupId}/professor")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Asignar profesor a grupo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesor asignado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del profesor inválidos")
    })
    public ResponseEntity<Group> assignProfessor(
            @PathVariable String groupId,
            @RequestBody Professor professor) throws SirhaException {
        Group updatedGroup = groupService.assignProfessor(groupId, professor);
        return ResponseEntity.ok(updatedGroup);
    }

    @GetMapping("/{groupId}/professor")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener profesor de grupo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesor obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado o profesor no asignado")
    })
    public ResponseEntity<Professor> getProfessor(@PathVariable String groupId) throws SirhaException {
        Professor professor = groupService.getProfessor(groupId);
        return ResponseEntity.ok(professor);
    }

    // ========== Gestión de horarios ==========

    @PostMapping("/{groupId}/schedules")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Agregar horario a grupo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario agregado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del horario inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto de horarios")
    })
    public ResponseEntity<Group> addSchedule(
            @PathVariable String groupId,
            @RequestBody Schedule schedule) throws SirhaException {
        Group updatedGroup = groupService.addSchedule(groupId, schedule);
        return ResponseEntity.ok(updatedGroup);
    }

    @GetMapping("/{groupId}/schedules")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener horarios de grupo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horarios obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable String groupId) throws SirhaException {
        List<Schedule> schedules = groupService.getSchedules(groupId);
        return ResponseEntity.ok(schedules);
    }

    // ========== Estado y capacidad ==========

    @PutMapping("/{groupId}/close")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Cerrar grupo",
            description = "Cierra un grupo para que no se puedan realizar más inscripciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo cerrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    public ResponseEntity<Group> closeGroup(@PathVariable String groupId) throws SirhaException {
        Group closedGroup = groupService.closeGroup(groupId);
        return ResponseEntity.ok(closedGroup);
    }

    @PutMapping("/{groupId}/open")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Abrir grupo",
            description = "Abre un grupo para permitir inscripciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo abierto exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    public ResponseEntity<Group> openGroup(@PathVariable String groupId) throws SirhaException {
        Group openedGroup = groupService.openGroup(groupId);
        return ResponseEntity.ok(openedGroup);
    }

    @GetMapping("/{groupId}/full")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Verificar si grupo está lleno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    public ResponseEntity<Boolean> isFull(@PathVariable String groupId) throws SirhaException {
        boolean isFull = groupService.isFull(groupId);
        return ResponseEntity.ok(isFull);
    }

    @GetMapping("/{groupId}/available-seats")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener cupos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupos disponibles obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    public ResponseEntity<Integer> getAvailableSeats(@PathVariable String groupId) throws SirhaException {
        int availableSeats = groupService.getAvailableSeats(groupId);
        return ResponseEntity.ok(availableSeats);
    }

    // ========== Queries por materia ==========

    @GetMapping("/by-subject/{subjectName}")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Buscar grupos por materia",
            description = "Obtiene todos los grupos de una materia específica")
    public ResponseEntity<List<Group>> findBySubjectName(@PathVariable String subjectName) throws SirhaException {
        List<Group> groups = groupService.findBySubjectName(subjectName);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/by-subject/{subjectName}/open")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Buscar grupos abiertos por materia",
            description = "Obtiene solo los grupos abiertos de una materia específica")
    public ResponseEntity<List<Group>> findOpenGroupsBySubjectName(@PathVariable String subjectName) throws SirhaException {
        List<Group> groups = groupService.findOpenGroupsBySubjectName(subjectName);
        return ResponseEntity.ok(groups);
    }

    @DeleteMapping("/by-subject/{subjectName}")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar todos los grupos de una materia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupos eliminados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<List<Group>> deleteBySubjectName(@PathVariable String subjectName) throws SirhaException {
        List<Group> deletedGroups = groupService.deleteBySubjectName(subjectName);
        return ResponseEntity.ok(deletedGroups);
    }
}
