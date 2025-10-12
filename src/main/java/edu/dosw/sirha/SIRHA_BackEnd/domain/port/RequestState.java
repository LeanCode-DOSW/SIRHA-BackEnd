package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.ResponseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;

public interface RequestState {
    void approveRequest(BaseRequest request, ResponseRequest response) throws SirhaException;
    void rejectRequest(BaseRequest request, ResponseRequest response) throws SirhaException;
    void reviewRequest(BaseRequest request, ResponseRequest response) throws SirhaException;
    RequestStateEnum getState();
}
