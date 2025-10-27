package edu.dosw.sirha.sirha_backend.domain.port;

import java.util.List;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.RequestApprovalRateDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface RequestManager {

    void addRequest(BaseRequest solicitud);

    void removeRequest(BaseRequest solicitud) throws SirhaException;

    List<BaseRequest> getSolicitudes();

    int getTotalRequestsMade();

    boolean hasActiveRequests();

    RequestApprovalRateDTO getRequestApprovalRate();

    int getTotalApprovedRequests();

    int getTotalRejectedRequests();

    int getTotalPendingRequests();

    int getTotalInReviewRequests();

    BaseRequest getRequestById(String requestId);

    List<BaseRequest> getRequestsHistory();

    double getApprovalRequestPercentage();

    double getRejectionRequestPercentage();

    double getPendingRequestPercentage();

    double getInReviewRequestPercentage();

}