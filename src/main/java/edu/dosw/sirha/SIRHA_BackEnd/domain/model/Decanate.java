package edu.dosw.sirha.sirha_backend.domain.model;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.domain.port.RequestReceiver;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

@Document("decanaturas")
public class Decanate implements RequestReceiver {
    private String name;
    private Careers career;
    private List<BaseRequest> receivedRequests;

    public Decanate() {
        this.receivedRequests = new ArrayList<>();
    }

    public Decanate(Careers career) {
        this();
        this.name = career.getDisplayName();
        this.career = career;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public boolean canReceiveRequest(BaseRequest request) {
        if (request == null) {
            return false;
        }

        Careers studentCareer = request.getStudentCareer();
        return this.career == studentCareer;
    }


    @Override
    public void receiveRequest(BaseRequest request) throws SirhaException {
        if (request == null) {
            throw SirhaException.of(ErrorCodeSirha.VALIDATION_ERROR);
        }
        
        if (!canReceiveRequest(request)) {
            throw SirhaException.of(ErrorCodeSirha.INVALID_CAREER, 
                "Esta decanatura no puede recibir solicitudes de la carrera: %s", 
                request.getStudentCareer());
        }
        
        setAutoPrioritie(request);
        
        receivedRequests.add(request);

        request.reviewRequest(new ResponseRequest("Solicitud recibida por la decanatura " + this.name, RequestStateEnum.EN_REVISION));
    }

    private void setAutoPrioritie(BaseRequest request) { //FIFO??
        int priority = receivedRequests.size() + 1;
        request.setPriority(priority);
    }

    @Override
    public List<BaseRequest> getPendingRequests() {
        return receivedRequests.stream()
            .filter(req -> req.getActualState() == RequestStateEnum.EN_REVISION || req.getActualState() == RequestStateEnum.PENDIENTE)
            .toList();
    }

    @Override
    public void approveRequest(BaseRequest request) throws SirhaException {
        if (request == null) {
            throw SirhaException.of(ErrorCodeSirha.VALIDATION_ERROR, "La solicitud no puede ser nula");
        }

        if (!receivedRequests.contains(request)) {
            throw SirhaException.of(ErrorCodeSirha.REQUEST_NOT_FOUND, "La solicitud no se encontró.");
        }

        request.approveRequest(new ResponseRequest("Solicitud aprobada por la decanatura " + this.name, RequestStateEnum.APROBADA));

        receivedRequests.remove(request);
        updateRequestsAfterResolveOne();
    }

    @Override
    public void rejectRequest(BaseRequest request) throws SirhaException {
        if (request == null) {
            throw SirhaException.of(ErrorCodeSirha.VALIDATION_ERROR, "La solicitud no puede ser nula");
        }

        if (!receivedRequests.contains(request)) {
            throw SirhaException.of(ErrorCodeSirha.REQUEST_NOT_FOUND, "La solicitud no se encontró.");
        }

        request.rejectRequest(new ResponseRequest("Solicitud rechazada por la decanatura " + this.name, RequestStateEnum.RECHAZADA));
        receivedRequests.remove(request);
        updateRequestsAfterResolveOne();
    }

    @Override
    public void updateRequestsAfterResolveOne() {
        
        List<BaseRequest> pendingRequests = getPendingRequests();
        for (int i = 0; i < pendingRequests.size(); i++) {
            BaseRequest req = pendingRequests.get(i);
            req.setPriority(i + 1); // Reasigna prioridades secuencialmente
        }
    }


}