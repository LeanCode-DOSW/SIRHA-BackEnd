package edu.dosw.sirha.sirha_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize; // ← puedes comentar también la importación si no usarás seguridad
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.dosw.sirha.sirha_backend.domain.model.MustHaveApprovedSubject;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.SubjectDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.SubjectService;
import edu.dosw.sirha.sirha_backend.util.SubjectMapper;

/**
 * Controlador REST para la gestión de MATERIAS únicamente.
 */
@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subjects", description = "API para gestión de materias")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener todas las materias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materias obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Subject>> findAll() {
        List<Subject> subjects = subjectService.findAll();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{name}")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Buscar materia por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia encontrada"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<Subject> findByName(@PathVariable String name) throws SirhaException {
        Subject subject = subjectService.findByName(name);
        return ResponseEntity.ok(subject);
    }

    @PostMapping
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Crear nueva materia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Materia creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "La materia ya existe")
    })
    public ResponseEntity<Subject> save(@RequestBody SubjectDTO subjectDTO) throws SirhaException {
        Subject subject = SubjectMapper.toEntity(subjectDTO);
        Subject savedSubject = subjectService.save(subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubject);
    }

    @DeleteMapping("/{name}")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar materia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<Subject> deleteByName(@PathVariable String name) throws SirhaException {
        Subject deletedSubject = subjectService.deleteByName(name);
        return ResponseEntity.ok(deletedSubject);
    }

    @GetMapping("/{name}/exists")
    // @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verificar existencia de materia")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        boolean exists = subjectService.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/{subjectName}/prerequisites")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Agregar prerrequisito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prerrequisito agregado"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<Subject> addPrerequisite(
            @PathVariable String subjectName,
            @RequestBody MustHaveApprovedSubject prerequisite) throws SirhaException {
        Subject updated = subjectService.addPrerequisite(subjectName, prerequisite);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    // Endpoints de conveniencia para consultar grupos de una materia
    @GetMapping("/{subjectName}/groups")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener grupos de una materia",
            description = "Obtiene todos los grupos asociados a una materia específica")
    public ResponseEntity<List<Group>> getGroups(@PathVariable String subjectName) throws SirhaException {
        List<Group> groups = subjectService.getGroupsBySubjectName(subjectName);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{subjectName}/groups/open")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener grupos abiertos de una materia",
            description = "Obtiene solo los grupos abiertos para inscripción de una materia específica")
    public ResponseEntity<List<Group>> getOpenGroups(@PathVariable String subjectName) throws SirhaException {
        List<Group> groups = subjectService.getOpenGroupsBySubjectName(subjectName);
        return ResponseEntity.ok(groups);
    }
}
