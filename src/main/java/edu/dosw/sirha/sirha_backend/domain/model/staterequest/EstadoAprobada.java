package edu.dosw.sirha.sirha_backend.domain.model.staterequest;

import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.port.RequestState;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class EstadoAprobada implements RequestState {

    @Override
    public void approveRequest(BaseRequest request, ResponseRequest response) throws SirhaException {
        throw SirhaException.invalidStateTransition(request.getId(), RequestStateEnum.APROBADA, RequestStateEnum.APROBADA);
    }

    @Override
    public void rejectRequest(BaseRequest request, ResponseRequest response) throws SirhaException {
        throw SirhaException.invalidStateTransition(request.getId(), RequestStateEnum.APROBADA, RequestStateEnum.RECHAZADA);
    }

    @Override
    public void reviewRequest(BaseRequest request, ResponseRequest response) throws SirhaException {
        throw SirhaException.invalidStateTransition(request.getId(), RequestStateEnum.APROBADA, RequestStateEnum.EN_REVISION);
    }

    @Override
    public RequestStateEnum getState() {
        return RequestStateEnum.APROBADA;
    }
}
