package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestState;

public class EstadoPendiente implements RequestState {

    @Override
    public void approveRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes aprobar una solicitud pendiente directamente.");}

    @Override
    public void rejectRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes rechazar una solicitud pendiente directamente.");}

    @Override
    public void pendingRequest(BaseRequest solicitud) {throw new IllegalStateException("La solicitud ya está en estado pendiente.");}

    @Override
    public void reviewRequest(BaseRequest solicitud) { }

    @Override
    public RequestStateEnum getState() {
        return RequestStateEnum.PENDIENTE;
    }

}
