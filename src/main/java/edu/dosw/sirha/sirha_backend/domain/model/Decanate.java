package edu.dosw.sirha.sirha_backend.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
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
    @Id
    private String id;
    private String name;
    private Careers career;

    // ✅ CAMBIO: Solo guardar IDs de StudyPlan
    private List<String> studyPlanIds; // En lugar de List<StudyPlan>

    private List<BaseRequest> receivedRequests;

    public Decanate() {
        this.receivedRequests = new ArrayList<>();
        this.studyPlanIds = new ArrayList<>();
    }

    public Decanate(Careers career) {
        this();
        this.name = career.getDisplayName();
        this.career = career;
    }

    public Decanate(String username, Careers career) {
        this(career);
        this.name = username;
    }

    // ========== Getters básicos ==========

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Careers getCareer() {
        return career;
    }

    public List<String> getStudyPlanIds() {
        return studyPlanIds;
    }

    public List<BaseRequest> getReceivedRequests() {
        return receivedRequests;
    }

    // ========== Setters ==========

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCareer(Careers career) {
        this.career = career;
    }

    public void setStudyPlanIds(List<String> studyPlanIds) {
        this.studyPlanIds = studyPlanIds;
    }

    public void setReceivedRequests(List<BaseRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    // ========== Métodos de StudyPlan (trabajan con IDs) ==========

    /**
     * Agrega un plan de estudio a la decanatura (solo el ID).
     * @param studyPlanId ID del plan de estudio
     */
    public void addStudyPlan(String studyPlanId) {
        if (studyPlanId != null && !studyPlanIds.contains(studyPlanId)) {
            studyPlanIds.add(studyPlanId);
        }
    }

    /**
     * Verifica si la decanatura tiene un plan de estudio específico.
     * @param studyPlanId ID del plan
     * @return true si existe, false en caso contrario
     */
    public boolean hasStudyPlan(String studyPlanId) {
        return studyPlanIds.contains(studyPlanId);
    }

    /**
     * Elimina un plan de estudio de la decanatura.
     * @param studyPlanId ID del plan a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean removeStudyPlan(String studyPlanId) {
        return studyPlanIds.remove(studyPlanId);
    }

    /**
     * Obtiene el número de planes de estudio asociados.
     * @return cantidad de planes
     */
    public int getStudyPlanCount() {
        return studyPlanIds.size();
    }

    // ========== Métodos de Request (sin cambios) ==========

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

        setAutoPriority(request);

        receivedRequests.add(request);

        request.reviewRequest(new ResponseRequest("Solicitud recibida por la decanatura " + this.name, RequestStateEnum.EN_REVISION));
    }

    private void setAutoPriority(BaseRequest request) {
        int priority = receivedRequests.size() + 1;
        request.setPriority(priority);
    }

    @Override
    public List<BaseRequest> getPendingRequests() {
        return receivedRequests.stream()
                .filter(req -> req.getActualState() == RequestStateEnum.EN_REVISION ||
                        req.getActualState() == RequestStateEnum.PENDIENTE)
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
            req.setPriority(i + 1);
        }
    }
}