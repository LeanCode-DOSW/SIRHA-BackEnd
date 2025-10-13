package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.service.RequestService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public Optional<BaseRequest> getById(@PathVariable String id) {
        return requestService.findById(id);
    }

    @GetMapping("/student/{studentUsername}")
    public List<BaseRequest> getByStudentUsername(@PathVariable String studentUsername) {
        return requestService.getAllRequests(studentUsername);
    }
    @GetMapping("/student/{studentUsername}/history")
    public List<BaseRequest> getRequestsHistory(@PathVariable String studentUsername) {
        return requestService.getRequestsHistory(studentUsername);
    }
    @GetMapping("/student/{studentUsername}/{requestId}")
    public BaseRequest getRequestById(@PathVariable String studentUsername, @PathVariable String requestId) {
        return requestService.getRequestById(studentUsername, requestId);
    }

    @PostMapping
    public BaseRequest create(@RequestBody BaseRequest request) {
        return requestService.save(request);
    }
}
