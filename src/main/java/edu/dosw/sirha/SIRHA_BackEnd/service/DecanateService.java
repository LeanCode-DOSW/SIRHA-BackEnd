package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentDTO;

public interface DecanateService {
    List<BaseRequest> getAllRequests();
    BaseRequest getRequestById(String requestId);
    //alertas 90 grupos llenos falta
    BaseRequest approvRequest(String requestId);
    BaseRequest rejectRequest(String requestId);
    StudentDTO getStudentBasicInfo(String username);
    //configurar academic periods
}
