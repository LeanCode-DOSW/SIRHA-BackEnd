package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestState;

public class EstadoEnRevision implements RequestState {

    @Override
    public void approveRequest(BaseRequest solicitud) {}

    @Override
    public void rejectRequest(BaseRequest solicitud) {}

    @Override
    public void pendingRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes poner algo en revisión en pendiente.");}

    @Override
    public void reviewRequest(BaseRequest solicitud) {throw new IllegalStateException("Ya está en revisión.");}

    @Override
    public RequestStateEnum getState() {
        return RequestStateEnum.EN_REVISION;
    }
}
