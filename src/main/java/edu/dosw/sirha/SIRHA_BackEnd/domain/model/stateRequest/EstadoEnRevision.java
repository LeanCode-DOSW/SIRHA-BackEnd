package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.ResponseProcess;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestState;
import edu.dosw.sirha.SIRHA_BackEnd.dto.ResponseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.exception.ErrorCodeSirha;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;

public class EstadoEnRevision implements RequestState {

    @Override
    public void approveRequest(BaseRequest request, ResponseRequest response) {
        request.setState(new EstadoAprobada());
        request.changeState(new ResponseProcess(response));
    }

    @Override
    public void rejectRequest(BaseRequest request, ResponseRequest response) {
        request.setState(new EstadoRechazada());
        request.changeState(new ResponseProcess(response));
    }

    @Override
    public void reviewRequest(BaseRequest request, ResponseRequest response) throws SirhaException {
        throw SirhaException.invalidStateTransition(request.getId(), RequestStateEnum.EN_REVISION, RequestStateEnum.EN_REVISION);
    }

    @Override
    public RequestStateEnum getState() {
        return RequestStateEnum.EN_REVISION;
    }
}
