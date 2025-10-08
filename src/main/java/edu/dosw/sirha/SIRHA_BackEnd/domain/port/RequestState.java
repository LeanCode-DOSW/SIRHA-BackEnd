package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.exception.SirhaException;

public interface RequestState {
    void approveRequest(BaseRequest solicitud) throws SirhaException;
    void rejectRequest(BaseRequest solicitud) throws SirhaException;
    void pendingRequest(BaseRequest solicitud) throws SirhaException;
    void reviewRequest(BaseRequest solicitud) throws SirhaException;
    RequestStateEnum getState();
}
