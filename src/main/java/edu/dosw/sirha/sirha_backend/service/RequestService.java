package edu.dosw.sirha.sirha_backend.service;

import java.util.List;
import java.util.Optional;

import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface RequestService {
    List<BaseRequest> findAll() throws SirhaException;
    Optional<BaseRequest> findById(String id) throws SirhaException;
    BaseRequest deleteById(String id) throws SirhaException;

    List<BaseRequest> getAllRequests(String username) throws SirhaException;
    BaseRequest getRequestById(String username, String requestId) throws SirhaException;
    List<BaseRequest> getRequestsHistory(String username) throws SirhaException; // el historial son las solicitudes que ya fueron aprobadas o rechazadas
    List<BaseRequest> getByStatus(RequestStateEnum status) throws SirhaException;
}
