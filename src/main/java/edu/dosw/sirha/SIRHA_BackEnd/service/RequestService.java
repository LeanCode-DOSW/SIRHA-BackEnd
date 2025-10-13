package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;
import java.util.Optional;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;

public interface RequestService {
    List<BaseRequest> findAll();
    Optional<BaseRequest> findById(String id);
    BaseRequest save(BaseRequest request);
    BaseRequest deleteById(String id);

    List<BaseRequest> getAllRequests(String username);
    BaseRequest getRequestById(String username, String requestId);
    List<BaseRequest> getRequestsHistory(String username); // el historial son las solicitudes que ya fueron aprobadas o rechazadas
}
