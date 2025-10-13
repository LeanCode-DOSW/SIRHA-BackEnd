package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestState;
import edu.dosw.sirha.SIRHA_BackEnd.dto.ResponseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;

public class EstadoRechazada implements RequestState {
    @Override
    public void approveRequest(BaseRequest request, ResponseRequest response) throws SirhaException {
        throw SirhaException.invalidStateTransition(request.getId(), RequestStateEnum.RECHAZADA, RequestStateEnum.APROBADA);
    }

    @Override
    public void rejectRequest(BaseRequest request, ResponseRequest response) throws SirhaException {
        throw SirhaException.invalidStateTransition(request.getId(), RequestStateEnum.RECHAZADA, RequestStateEnum.RECHAZADA);
    }

    @Override
    public void reviewRequest(BaseRequest request, ResponseRequest response) throws SirhaException {
        throw SirhaException.invalidStateTransition(request.getId(), RequestStateEnum.RECHAZADA, RequestStateEnum.EN_REVISION);
    }

    @Override
    public RequestStateEnum getState() {
        return RequestStateEnum.RECHAZADA;
    }
}
