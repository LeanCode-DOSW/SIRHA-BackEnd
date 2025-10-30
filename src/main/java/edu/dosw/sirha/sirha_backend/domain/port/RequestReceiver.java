package edu.dosw.sirha.sirha_backend.domain.port;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface RequestReceiver {

    boolean canReceiveRequest(BaseRequest request);
    void receiveRequest(BaseRequest request) throws SirhaException; //cuando se recibe la solicitud pasa a en revision
    List<BaseRequest> getPendingRequests();
    void approveRequest(BaseRequest request) throws SirhaException;
    void rejectRequest(BaseRequest request) throws SirhaException;

    void updateRequestsAfterResolveOne();
}
