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

import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.StudyPlanService;

@RestController
@RequestMapping("/api/study-plans")
@Tag(name = "Study Plans", description = "API para gestión de planes de estudio")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    public StudyPlanController(StudyPlanService studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    // ========== CRUD básico ==========

    @GetMapping
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener todos los planes de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de planes obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<StudyPlan>> findAll() throws SirhaException {
        List<StudyPlan> plans = studyPlanService.getAllStudyPlans();
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{name}")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener plan de estudio por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan encontrado"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado")
    })
    public ResponseEntity<StudyPlan> findByName(@PathVariable String name) throws SirhaException {
        StudyPlan plan = studyPlanService.getStudyPlanByName(name);
        return ResponseEntity.ok(plan);
    }

    @GetMapping("/career/{career}")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener planes por carrera",
            description = "Obtiene todos los planes de estudio de una carrera específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planes encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron planes para la carrera")
    })
    public ResponseEntity<List<StudyPlan>> findByCareer(@PathVariable Careers career) throws SirhaException {
        List<StudyPlan> plans = studyPlanService.getStudyPlansByCareer(career);
        return ResponseEntity.ok(plans);
    }

    @PostMapping
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Crear plan de estudio",
            description = "Crea un nuevo plan de estudio para una carrera")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<StudyPlan> create(@RequestBody Careers career) throws SirhaException {
        StudyPlan saved = studyPlanService.createStudyPlan(career);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{name}")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar plan de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado")
    })
    public ResponseEntity<StudyPlan> delete(@PathVariable String name) throws SirhaException {
        StudyPlan deleted = studyPlanService.deleteStudyPlan(name);
        return ResponseEntity.ok(deleted);
    }

    // ========== Gestión de Materias del Plan ==========

    @GetMapping("/{studyPlanName}/subjects")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Obtener materias de un plan",
            description = "Obtiene todas las materias asociadas a un plan de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materias obtenidas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado")
    })
    public ResponseEntity<List<Subject>> getSubjectsOfPlan(@PathVariable String studyPlanName) throws SirhaException {
        List<Subject> subjects = studyPlanService.getSubjectsOfStudyPlan(studyPlanName);
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/{studyPlanName}/subjects/{subjectName}")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Agregar materia a plan de estudio",
            description = "Asocia una materia existente a un plan de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia agregada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Plan o materia no encontrada"),
            @ApiResponse(responseCode = "400", description = "Materia ya existe en el plan")
    })
    public ResponseEntity<StudyPlan> addSubject(
            @PathVariable String studyPlanName,
            @PathVariable String subjectName) throws SirhaException {
        StudyPlan updated = studyPlanService.addSubjectToStudyPlan(studyPlanName, subjectName);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{studyPlanName}/subjects/{subjectName}")
    // @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar materia de plan de estudio",
            description = "Desasocia una materia de un plan de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Plan o materia no encontrada")
    })
    public ResponseEntity<StudyPlan> removeSubject(
            @PathVariable String studyPlanName,
            @PathVariable String subjectName) throws SirhaException {
        StudyPlan updated = studyPlanService.removeSubjectFromStudyPlan(studyPlanName, subjectName);
        return ResponseEntity.ok(updated);
    }

    // ========== Información del Plan ==========

    @GetMapping("/{studyPlanName}/credits")
    // @PreAuthorize("hasAnyRole('STUDENT','DEAN','ADMIN')")
    @Operation(summary = "Calcular créditos totales",
            description = "Calcula el total de créditos de un plan de estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Créditos calculados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado")
    })
    public ResponseEntity<Integer> calculateTotalCredits(@PathVariable String studyPlanName) throws SirhaException {
        int totalCredits = studyPlanService.calculateTotalCredits(studyPlanName);
        return ResponseEntity.ok(totalCredits);
    }

    @GetMapping("/{studyPlanName}/exists")
    // @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verificar existencia de plan")
    public ResponseEntity<Boolean> existsByName(@PathVariable String studyPlanName) {
        boolean exists = studyPlanService.existsByName(studyPlanName);
        return ResponseEntity.ok(exists);
    }
}