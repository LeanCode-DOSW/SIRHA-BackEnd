package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;

public interface RequestState {
    void approveRequest(BaseRequest solicitud);
    void rejectRequest(BaseRequest solicitud);
    void pendingRequest(BaseRequest solicitud);
    void reviewRequest(BaseRequest solicitud);
    RequestStateEnum getState();
}
