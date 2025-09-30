package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestState;

public class EstadoRechazada implements RequestState {
    @Override
    public void approveRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes aprobar algo rechazado.");}

    @Override
    public void rejectRequest(BaseRequest solicitud) {throw new IllegalStateException("Ya está rechazada.");}

    @Override
    public void pendingRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes poner algo rechazado en pendiente.");}

    @Override
    public void reviewRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes revisar algo rechazado.");}

    @Override
    public RequestStateEnum getState() {
        return RequestStateEnum.RECHAZADA;
    }
}
