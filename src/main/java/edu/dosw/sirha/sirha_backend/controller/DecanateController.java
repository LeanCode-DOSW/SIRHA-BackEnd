package edu.dosw.sirha.sirha_backend.controller;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.dosw.sirha.sirha_backend.domain.model.Decanate;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.DecanateService;

/**
 * Controlador REST para la gestión de Decanaturas.
 */
@RestController
@RequestMapping("/api/decanates")
@Tag(name = "Decanates", description = "API para gestión de decanaturas y procesamiento de solicitudes académicas")
public class DecanateController {

    private final DecanateService decanateService;

    public DecanateController(DecanateService decanateService) {
        this.decanateService = decanateService;
    }

    // ========== CRUD de Decanatos ==========

    @GetMapping
    //@PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener todas las decanaturas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de decanaturas obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Decanate>> getAllDecanates() throws SirhaException {
        List<Decanate> decanates = decanateService.getAllDecanates();
        return ResponseEntity.ok(decanates);
    }

    @GetMapping("/{name}")
    //PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener decanatura por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decanatura encontrada"),
            @ApiResponse(responseCode = "404", description = "Decanatura no encontrada")
    })
    public ResponseEntity<Decanate> getDecanateByName(@PathVariable String name) throws SirhaException {
        Decanate decanate = decanateService.getDecanateByName(name);
        return ResponseEntity.ok(decanate);
    }

    // ========== Gestión de Solicitudes ==========

    @GetMapping("/requests")
    //@PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener todas las solicitudes")
    public ResponseEntity<List<BaseRequest>> getAllRequests() throws SirhaException {
        List<BaseRequest> requests = decanateService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/requests/{requestId}")
    //@PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener solicitud por ID")
    public ResponseEntity<BaseRequest> getRequestById(@PathVariable String requestId) throws SirhaException {
        BaseRequest request = decanateService.getRequestById(requestId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/{decanateName}/requests")
    //@PreAuthorize("hasRole('ADMIN') or (hasRole('DEAN') and authentication.name == #decanateName)")
    @Operation(summary = "Obtener solicitudes de una decanatura")
    public ResponseEntity<List<BaseRequest>> getAllRequestsForDecanate(@PathVariable String decanateName) throws SirhaException {
        List<BaseRequest> requests = decanateService.getAllRequestsForDecanate(decanateName);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{decanateName}/requests/{requestId}/receive")
    //@PreAuthorize("hasRole('ADMIN') or (hasRole('DEAN') and authentication.name == #decanateName)")
    @Operation(summary = "Recibir solicitud")
    public ResponseEntity<BaseRequest> receiveRequest(
            @PathVariable String decanateName,
            @PathVariable String requestId) throws SirhaException {
        BaseRequest request = decanateService.receiveRequest(decanateName, requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{decanateName}/requests/{requestId}/approve")
    //@PreAuthorize("hasRole('ADMIN') or (hasRole('DEAN') and authentication.name == #decanateName)")
    @Operation(summary = "Aprobar solicitud")
    public ResponseEntity<BaseRequest> approveRequest(
            @PathVariable String decanateName,
            @PathVariable String requestId) throws SirhaException {
        BaseRequest request = decanateService.approveRequest(decanateName, requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{decanateName}/requests/{requestId}/reject")
    //@PreAuthorize("hasRole('ADMIN') or (hasRole('DEAN') and authentication.name == #decanateName)")
    @Operation(summary = "Rechazar solicitud")
    public ResponseEntity<BaseRequest> rejectRequest(
            @PathVariable String decanateName,
            @PathVariable String requestId) throws SirhaException {
        BaseRequest request = decanateService.rejectRequest(decanateName, requestId);
        return ResponseEntity.ok(request);
    }

    // ========== Gestión de Study Plans (REFACTORIZADO) ==========

    @GetMapping("/{decanateName}/study-plans")
    //@PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener planes de estudio de una decanatura",
            description = "Obtiene todos los planes de estudio asociados a una decanatura específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planes de estudio obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Decanatura no encontrada")
    })
    public ResponseEntity<List<StudyPlan>> getStudyPlansOfDecanate(@PathVariable String decanateName) throws SirhaException {
        List<StudyPlan> studyPlans = decanateService.getStudyPlansOfDecanate(decanateName);
        return ResponseEntity.ok(studyPlans);
    }

    @PostMapping("/{decanateName}/study-plans/{studyPlanId}")
    //@PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Agregar plan de estudio a decanatura",
            description = "Asocia un plan de estudio existente a una decanatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Plan ya existe en la decanatura"),
            @ApiResponse(responseCode = "404", description = "Decanatura o plan no encontrado")
    })
    public ResponseEntity<Decanate> addStudyPlanToDecanate(
            @PathVariable String decanateName,
            @PathVariable String studyPlanId) throws SirhaException {
        Decanate updated = decanateService.addStudyPlanToDecanate(decanateName, studyPlanId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @DeleteMapping("/{decanateName}/study-plans/{studyPlanId}")
    //@PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Eliminar plan de estudio de decanatura",
            description = "Desasocia un plan de estudio de una decanatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Decanatura o plan no encontrado")
    })
    public ResponseEntity<Decanate> removeStudyPlanFromDecanate(
            @PathVariable String decanateName,
            @PathVariable String studyPlanId) throws SirhaException {
        Decanate updated = decanateService.removeStudyPlanFromDecanate(decanateName, studyPlanId);
        return ResponseEntity.ok(updated);
    }

    // ========== Queries de conveniencia ==========

    @GetMapping("/students/{username}/basic-info")
    //@PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Obtener información básica de estudiante")
    public ResponseEntity<StudentDTO> getStudentBasicInfo(@PathVariable String username) throws SirhaException {
        StudentDTO student = decanateService.getStudentBasicInfo(username);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{decanateName}/statistics/requests-count")
    //@PreAuthorize("hasAnyRole('DEAN','ADMIN')")
    @Operation(summary = "Contar solicitudes pendientes")
    public ResponseEntity<Integer> getPendingRequestsCount(@PathVariable String decanateName) throws SirhaException {
        List<BaseRequest> requests = decanateService.getAllRequestsForDecanate(decanateName);
        return ResponseEntity.ok(requests.size());
    }

    @PostMapping
    public ResponseEntity<Decanate> saveDecanate(@RequestParam Careers career) throws SirhaException {
        Decanate decanate = decanateService.saveDecanate(career);
        return ResponseEntity.ok(decanate);
    }



}