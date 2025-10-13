package edu.dosw.sirha.sirha_backend.domain.port;

import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface RequestState {
    void approveRequest(BaseRequest request, ResponseRequest response) throws SirhaException;
    void rejectRequest(BaseRequest request, ResponseRequest response) throws SirhaException;
    void reviewRequest(BaseRequest request, ResponseRequest response) throws SirhaException;
    RequestStateEnum getState();
}
