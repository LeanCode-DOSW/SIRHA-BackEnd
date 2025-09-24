package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;
import java.util.Optional;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.BaseRequest;

public interface RequestService {
    List<BaseRequest> findAll();
    Optional<BaseRequest> findById(String id);
    BaseRequest save(BaseRequest request);
    void aprobarSolicitud(String id);
    void rechazarSolicitud(String id);
}
