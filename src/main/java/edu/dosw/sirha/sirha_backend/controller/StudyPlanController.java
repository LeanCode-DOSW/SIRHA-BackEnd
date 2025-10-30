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

import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
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
    
    @GetMapping("/career/{career}")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener planes de estudio por carrera", description = "Retorna una lista de planes de estudio para una carrera dada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de planes de estudio obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<StudyPlan>> getStudyPlansByCareer(@PathVariable Careers career) throws SirhaException {
        List<StudyPlan> plans = studyPlanService.getStudyPlansByCareer(career);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener plan de estudio por nombre", description = "Retorna un plan de estudio por su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plan de estudio obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Plan de estudio no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudyPlan> getStudyPlanByName(@PathVariable String name) throws SirhaException {
        StudyPlan plan = studyPlanService.getStudyPlanByName(name);
        return ResponseEntity.ok(plan);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Crear plan de estudio", description = "Crea un nuevo plan de estudio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Plan de estudio creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudyPlan> createStudyPlan(@RequestBody edu.dosw.sirha.sirha_backend.dto.StudyPlanDTO studyPlanDto) throws SirhaException {
        // Map DTO -> domain
        StudyPlan plan = new StudyPlan(studyPlanDto.getCareer());
        if (studyPlanDto.getName() != null && !studyPlanDto.getName().isBlank()) {
            // StudyPlan currently sets name from career in constructor and has no setter for name.
            // If custom name is required, consider adding setter in domain model. For now, ignore custom name.
        }
        if (studyPlanDto.getSubjects() != null) {
            for (edu.dosw.sirha.sirha_backend.dto.SubjectDTO sDto : studyPlanDto.getSubjects()) {
                // Create domain Subject from SubjectDTO
                edu.dosw.sirha.sirha_backend.domain.model.Subject subject = new edu.dosw.sirha.sirha_backend.domain.model.Subject(sDto.getName(), sDto.getCredits());
                // set groups/prerequisites if provided
                if (sDto.getGroups() != null) subject.setGroups(sDto.getGroups());
                if (sDto.getPrerequisites() != null) subject.setPrerequisites(sDto.getPrerequisites());
                plan.addSubject(subject);
            }
        }
        StudyPlan saved = studyPlanService.saveStudyPlan(plan);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/{studyPlanName}/subjects/{subjectName}")
    @PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Agregar materia a plan de estudio", description = "Agrega una materia existente a un plan de estudio por nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materia agregada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Plan de estudio o materia no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudyPlan> addSubjectToStudyPlan(@PathVariable String studyPlanName, @PathVariable String subjectName) throws SirhaException {
        StudyPlan updated = studyPlanService.addSubjectToStudyPlan(studyPlanName, subjectName);
        return ResponseEntity.ok(updated);
    }
    
}
