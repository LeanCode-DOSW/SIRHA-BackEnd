package edu.dosw.sirha.sirha_backend.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.dosw.sirha.sirha_backend.domain.model.Decanate;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.DecanateService;

/**
 * Controlador REST para la gestión de Decanaturas en el sistema SIRHA.
 * 
 * Este controlador maneja todas las operaciones relacionadas con las decanaturas, incluyendo:
 * - Consulta de decanaturas y sus planes de estudio
 * - Gestión de solicitudes académicas (recibir, aprobar, rechazar)
 * - Administración de planes de estudio por decanatura
 * - Consulta de información de estudiantes
 * 
 * Las decanaturas son responsables de gestionar los planes de estudio de cada carrera
 * y procesar las solicitudes académicas de los estudiantes de su facultad.
 * 
 * Los endpoints están mapeados bajo la ruta base "/api/decanates" siguiendo
 * las convenciones REST para recursos de colección.
 * 
 * @see DecanateService
 * @see Decanate
 * @see BaseRequest
 * @see StudyPlan
 */
@RestController
@RequestMapping("/api/decanates")
@Tag(name = "Decanates", description = "API para gestión de decanaturas y procesamiento de solicitudes académicas")
public class DecanateController {

    private final DecanateService decanateService;

    public DecanateController(DecanateService decanateService) {
        this.decanateService = decanateService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva decanatura", description = "Crea una nueva decanatura en el sistema con el nombre especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Decanatura creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la decanatura inválidos"),
        @ApiResponse(responseCode = "409", description = "Decanatura ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Decanate> createDecanate(@RequestBody Careers career) throws SirhaException {
        Decanate createdDecanate = decanateService.saveDecanate(career);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDecanate);
    }



    @GetMapping
    @Operation(summary = "Obtener todas las decanaturas", description = "Retorna una lista completa de todas las decanaturas registradas en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de decanaturas obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Decanate>> getAllDecanates() throws SirhaException {
        List<Decanate> decanates = decanateService.getAllDecanates();
        return ResponseEntity.ok(decanates);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Obtener decanatura por nombre", description = "Obtiene una decanatura específica por su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Decanatura encontrada"),
        @ApiResponse(responseCode = "404", description = "Decanatura no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Decanate> getDecanateByName(@PathVariable String name) throws SirhaException {
        Decanate decanate = decanateService.getDecanateByName(name);
        return ResponseEntity.ok(decanate);
    }

    @GetMapping("/requests")
    @Operation(summary = "Obtener todas las solicitudes", description = "Obtiene todas las solicitudes pendientes de todas las decanaturas del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitudes obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BaseRequest>> getAllRequests() throws SirhaException {
        List<BaseRequest> requests = decanateService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/requests/{requestId}")
    @Operation(summary = "Obtener solicitud por ID", description = "Obtiene una solicitud específica por su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BaseRequest> getRequestById(@PathVariable String requestId) throws SirhaException {
        BaseRequest request = decanateService.getRequestById(requestId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/{decanateName}/requests")
    @Operation(summary = "Obtener solicitudes de una decanatura", description = "Obtiene todas las solicitudes pendientes de una decanatura específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitudes de la decanatura obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Decanatura no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BaseRequest>> getAllRequestsForDecanate(@PathVariable String decanateName) throws SirhaException {
        List<BaseRequest> requests = decanateService.getAllRequestsForDecanate(decanateName);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{decanateName}/requests/{requestId}/receive")
    @Operation(summary = "Recibir solicitud", description = "Registra que una decanatura ha recibido una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud recibida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud no válida o ya procesada"),
        @ApiResponse(responseCode = "404", description = "Decanatura o solicitud no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BaseRequest> receiveRequest(@PathVariable String decanateName, @PathVariable String requestId) throws SirhaException {
        BaseRequest request = decanateService.receiveRequest(decanateName, requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{decanateName}/requests/{requestId}/approve")
    @Operation(summary = "Aprobar solicitud", description = "Aprueba una solicitud académica específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud aprobada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud no válida o no se puede aprobar"),
        @ApiResponse(responseCode = "404", description = "Decanatura o solicitud no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BaseRequest> approveRequest(@PathVariable String decanateName, @PathVariable String requestId) throws SirhaException {
        BaseRequest request = decanateService.approveRequest(decanateName, requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{decanateName}/requests/{requestId}/reject")
    @Operation(summary = "Rechazar solicitud", description = "Rechaza una solicitud académica específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud rechazada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud no válida o no se puede rechazar"),
        @ApiResponse(responseCode = "404", description = "Decanatura o solicitud no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BaseRequest> rejectRequest(@PathVariable String decanateName, @PathVariable String requestId) throws SirhaException {
        BaseRequest request = decanateService.rejectRequest(decanateName, requestId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/students/{username}/basic-info")
    @Operation(summary = "Obtener información básica de estudiante", description = "Obtiene la información básica de un estudiante específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información del estudiante obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudentDTO> getStudentBasicInfo(@PathVariable String username) throws SirhaException {
        StudentDTO student = decanateService.getStudentBasicInfo(username);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{decanateName}/study-plans")
    @Operation(summary = "Obtener planes de estudio", description = "Obtiene todos los planes de estudio de una decanatura específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Planes de estudio obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Decanatura no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<StudyPlan>> getStudyPlansByDecanateName(@PathVariable String decanateName) throws SirhaException {
        List<StudyPlan> studyPlans = decanateService.getStudyPlansByDecanateName(decanateName);
        return ResponseEntity.ok(studyPlans);
    }

    @PostMapping("/{decanateName}/study-plans")
    @Operation(summary = "Agregar plan de estudio", description = "Agrega un nuevo plan de estudio a una decanatura específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Plan de estudio agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del plan de estudio inválidos"),
        @ApiResponse(responseCode = "404", description = "Decanatura no encontrada"),
        @ApiResponse(responseCode = "409", description = "Plan de estudio ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<StudyPlan>> addPlanToDecanate(@PathVariable String decanateName, @RequestBody String studyPlanName) throws SirhaException {
        List<StudyPlan> updatedPlans = decanateService.addPlanToDecanate(decanateName, studyPlanName);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedPlans);
    }

    @GetMapping("/{decanateName}/statistics/requests-count")
    @Operation(summary = "Contar solicitudes pendientes", description = "Obtiene el número de solicitudes pendientes de una decanatura")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Decanatura no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Integer> getPendingRequestsCount(@PathVariable String decanateName) throws SirhaException {
        List<BaseRequest> requests = decanateService.getAllRequestsForDecanate(decanateName);
        return ResponseEntity.ok(requests.size());
    }

    @GetMapping("/{decanateName}/statistics/study-plans-count")
    @Operation(summary = "Contar planes de estudio", description = "Obtiene el número de planes de estudio de una decanatura")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Decanatura no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Integer> getStudyPlansCount(@PathVariable String decanateName) throws SirhaException {
        List<StudyPlan> studyPlans = decanateService.getStudyPlansByDecanateName(decanateName);
        return ResponseEntity.ok(studyPlans.size());
    }

    @PostMapping("/study-plans/{studyPlanName}/subjects/{subjectName}")
    @Operation(summary = "Agregar materia a plan de estudio", description = "Agrega una materia a un plan de estudio existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materia agregada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Plan o materia no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudyPlan> addSubjectToStudyPlan(@PathVariable String studyPlanName,
                                                           @PathVariable String subjectName) throws SirhaException {
        StudyPlan updated = decanateService.addSubjectToStudyPlan(studyPlanName, subjectName);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/study-plans")
    @Operation(summary = "Crear/guardar plan de estudio", description = "Crea o actualiza un plan de estudio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Plan de estudio guardado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StudyPlan> saveStudyPlan(@RequestBody Careers career) throws SirhaException {
        StudyPlan saved = decanateService.saveStudyPlan(career);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/study-plans/career/{career}")
    @Operation(summary = "Obtener planes por carrera", description = "Obtiene los planes de estudio filtrados por carrera")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Planes obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Planes no encontrados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<StudyPlan>> getStudyPlansByCareer(@PathVariable Careers career) throws SirhaException {
        List<StudyPlan> plans = decanateService.getStudyPlansByCareer(career);
        return ResponseEntity.ok(plans);
    }
}