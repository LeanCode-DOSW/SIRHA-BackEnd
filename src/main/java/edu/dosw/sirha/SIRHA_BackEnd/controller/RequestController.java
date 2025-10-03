package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.service.RequestService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<BaseRequest> getAll() {
        return requestService.findAll();
    }

    @PostMapping
    public BaseRequest create(@RequestBody BaseRequest request) {
        return requestService.save(request);
    }

    @PutMapping("/{id}/approve")
    public void approve(@PathVariable String id) {
        requestService.aprobarSolicitud(id);
    }

    @PutMapping("/{id}/reject")
    public void reject(@PathVariable String id) {
        requestService.rechazarSolicitud(id);
    }
}
