package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestState;
import edu.dosw.sirha.SIRHA_BackEnd.domain.exception.SirhaException;

public class EstadoAprobada implements RequestState {

    @Override
    public void approveRequest(BaseRequest solicitud) throws SirhaException {
        throw SirhaException.requestAlreadyApproved(solicitud.getId());
    }

    @Override
    public void rejectRequest(BaseRequest solicitud) throws SirhaException {
        throw SirhaException.invalidStateTransition(solicitud.getId(), "APROBADA", "RECHAZADA");
    }

    @Override
    public void pendingRequest(BaseRequest solicitud) throws SirhaException {
        throw SirhaException.invalidStateTransition(solicitud.getId(), "APROBADA", "PENDIENTE");
    }

    @Override
    public void reviewRequest(BaseRequest solicitud) throws SirhaException {
        throw SirhaException.invalidStateTransition(solicitud.getId(), "APROBADA", "EN_REVISION");
    }

    @Override
    public RequestStateEnum getState() {
        return RequestStateEnum.APROBADA;
    }
}
