package edu.dosw.sirha.sirha_backend.service;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;

public interface DecanateService {
    List<BaseRequest> getAllRequests();
    BaseRequest getRequestById(String requestId);
    //alertas 90 grupos llenos falta
    BaseRequest approvRequest(String requestId);
    BaseRequest rejectRequest(String requestId);
    StudentDTO getStudentBasicInfo(String username);
    //configurar academic periods
}
