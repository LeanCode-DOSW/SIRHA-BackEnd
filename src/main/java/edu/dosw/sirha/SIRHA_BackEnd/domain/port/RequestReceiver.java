package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;

public interface RequestReceiver {

    boolean canReceiveRequest(BaseRequest request);
    void receiveRequest(BaseRequest request) throws SirhaException; //cuando se recibe la solicitud pasa a en revision
    List<BaseRequest> getPendingRequests();
    void approveRequest(BaseRequest request) throws SirhaException;
    void rejectRequest(BaseRequest request) throws SirhaException;

    void updateRequestsAfterResolveOne();
}
