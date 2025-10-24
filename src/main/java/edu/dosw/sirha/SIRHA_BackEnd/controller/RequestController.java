package edu.dosw.sirha.sirha_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.RequestService;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de solicitudes académicas en el sistema SIRHA.
 * 
 * Este controlador maneja todas las operaciones relacionadas con solicitudes académicas, incluyendo:
 * - Consulta de solicitudes (por ID, estudiante, estado)
 * - Procesamiento de solicitudes (revisar, aprobar, rechazar)
 * - Eliminación de solicitudes pendientes
 * - Historial de solicitudes por estudiante
 * 
 * Las solicitudes académicas incluyen cambios de grupo, cambios de materia,
 * y otros tipos de modificaciones a la inscripción académica del estudiante.
 * 
 * Los endpoints están mapeados bajo la ruta base "/api/requests" siguiendo
 * las convenciones REST para recursos de colección.
 * 
 * @see RequestService
 * @see BaseRequest
 * @see ResponseRequest
 * @see RequestStateEnum
 */
@RestController
@RequestMapping("/api/requests")
@Tag(name = "Requests", description = "API para gestión de solicitudes académicas (cambios de grupo y materia)")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las solicitudes", description = "Retorna una lista completa de todas las solicitudes registradas en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BaseRequest>> getAll() throws SirhaException {
        List<BaseRequest> requests = requestService.findAll();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar solicitud por ID", description = "Obtiene una solicitud específica por su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BaseRequest> getById(@PathVariable String id) throws SirhaException {
        Optional<BaseRequest> request = requestService.findById(id);
        return request.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar solicitudes por estado", description = "Obtiene todas las solicitudes que se encuentran en un estado específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitudes filtradas por estado obtenidas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BaseRequest>> getRequestsByStatus(@PathVariable RequestStateEnum status) throws SirhaException {
        List<BaseRequest> requests = requestService.getByStatus(status);
        return ResponseEntity.ok(requests);
    }


    @GetMapping("/student/{studentUsername}")
    @Operation(summary = "Obtener solicitudes por estudiante", description = "Obtiene todas las solicitudes activas de un estudiante específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitudes del estudiante obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BaseRequest>> getByStudentUsername(@PathVariable String studentUsername) throws SirhaException {
        List<BaseRequest> requests = requestService.getAllRequests(studentUsername);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/student/{studentUsername}/history")
    @Operation(summary = "Obtener historial de solicitudes", description = "Obtiene el historial completo de solicitudes de un estudiante, incluyendo aprobadas, rechazadas y pendientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial de solicitudes obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BaseRequest>> getRequestsHistory(@PathVariable String studentUsername) throws SirhaException {
        List<BaseRequest> requests = requestService.getRequestsHistory(studentUsername);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/student/{studentUsername}/{requestId}")
    @Operation(summary = "Obtener solicitud específica de estudiante", description = "Obtiene una solicitud específica de un estudiante por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud o estudiante no encontrado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - la solicitud no pertenece al estudiante"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BaseRequest> getRequestById(@PathVariable String studentUsername, @PathVariable String requestId) throws SirhaException {
        BaseRequest request = requestService.getRequestById(studentUsername, requestId);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/{requestId}")
    @Operation(summary = "Eliminar solicitud", description = "Elimina una solicitud del sistema (solo si está en estado PENDIENTE)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "400", description = "No se puede eliminar - solicitud ya procesada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BaseRequest> deleteRequest(@PathVariable String requestId) throws SirhaException {
        BaseRequest deletedRequest = requestService.deleteById(requestId);
        return ResponseEntity.ok(deletedRequest);
    }
}
