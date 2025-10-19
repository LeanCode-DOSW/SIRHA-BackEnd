package edu.dosw.sirha.sirha_backend.controller;

import org.springframework.web.bind.annotation.*;

import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.RequestService;

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
    public List<BaseRequest> getAll() throws SirhaException{
        return requestService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<BaseRequest> getById(@PathVariable String id) throws SirhaException{
        return requestService.findById(id);
    }

    @GetMapping("/student/{studentUsername}")
    public List<BaseRequest> getByStudentUsername(@PathVariable String studentUsername) throws SirhaException{
        return requestService.getAllRequests(studentUsername);
    }
    @GetMapping("/student/{studentUsername}/history")
    public List<BaseRequest> getRequestsHistory(@PathVariable String studentUsername) throws SirhaException{
        return requestService.getRequestsHistory(studentUsername);
    }
    @GetMapping("/student/{studentUsername}/{requestId}")
    public BaseRequest getRequestById(@PathVariable String studentUsername, @PathVariable String requestId) throws SirhaException{
        return requestService.getRequestById(studentUsername, requestId);
    }
}
