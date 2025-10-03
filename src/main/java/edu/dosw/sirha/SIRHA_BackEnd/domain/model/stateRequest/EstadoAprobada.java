package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestState;

public class EstadoAprobada implements RequestState {

    @Override
    public void approveRequest(BaseRequest solicitud) {throw new IllegalStateException("Ya está aprobada.");}

    @Override
    public void rejectRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes rechazar algo aprobado.");}

    @Override
    public void pendingRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes poner algo aprobado en pendiente.");}

    @Override
    public void reviewRequest(BaseRequest solicitud) {throw new IllegalStateException("No puedes revisar algo aprobado.");}

    @Override
    public RequestStateEnum getState() {
        return RequestStateEnum.APROBADA;
    }
    
    
}
