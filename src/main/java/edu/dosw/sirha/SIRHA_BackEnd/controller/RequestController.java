package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.service.RequestService;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.BaseRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de solicitudes académicas en el sistema SIRHA.
 *
 * Proporciona endpoints para:
 * - Consultar todas las solicitudes registradas
 * - Crear nuevas solicitudes
 * - Aprobar solicitudes existentes
 * - Rechazar solicitudes existentes
 *
 * Todos los endpoints están mapeados bajo la ruta base "/api/requests".
 *
 * @see BaseRequest
 * @see RequestService
 */
@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Obtiene todas las solicitudes académicas registradas en el sistema.
     *
     * @return lista de objetos {@link BaseRequest}.
     */
    @GetMapping
    public List<BaseRequest> getAll() {
        return requestService.findAll();
    }

    /**
     * Crea una nueva solicitud académica.
     *
     * @param request objeto {@link BaseRequest} con la información de la solicitud.
     * @return la solicitud creada.
     */
    @PostMapping
    public BaseRequest create(@RequestBody BaseRequest request) {
        return requestService.save(request);
    }

    /**
     * Aprueba una solicitud académica específica.
     *
     * @param id identificador único de la solicitud a aprobar.
     */
    @PutMapping("/{id}/approve")
    public void approve(@PathVariable String id) {
        requestService.approveRequest(id);
    }

    /**
     * Rechaza una solicitud académica específica.
     *
     * @param id identificador único de la solicitud a rechazar.
     */
    @PutMapping("/{id}/reject")
    public void reject(@PathVariable String id) {
        requestService.rejectRequest(id);
    }
}

