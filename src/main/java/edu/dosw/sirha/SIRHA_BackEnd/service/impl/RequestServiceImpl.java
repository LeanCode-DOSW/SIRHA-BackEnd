package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.BaseRequestMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.RequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {
    private final BaseRequestMongoRepository repository;

    public RequestServiceImpl(BaseRequestMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BaseRequest> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<BaseRequest> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public BaseRequest save(BaseRequest request) {
        return repository.save(request);
    }

    @Override
    public void aprobarSolicitud(String id) {
        BaseRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        //request.aprobar();
        repository.save(request);
    }

    @Override
    public void rechazarSolicitud(String id) {
        BaseRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        //request.rechazar();
        repository.save(request);
    }
}