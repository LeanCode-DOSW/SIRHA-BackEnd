package edu.dosw.sirha.sirha_backend.service;

import java.util.List;
import edu.dosw.sirha.sirha_backend.domain.model.Decanate;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Servicio para gestión de Decanaturas.
 * Responsable SOLO de operaciones relacionadas con Decanate.
 */
public interface DecanateService {
    // ========== CRUD de Decanatos ==========
    Decanate registerDecanate(RegisterRequest request) throws SirhaException;
    Decanate saveDecanate(Careers career) throws SirhaException;
    Decanate getDecanateByName(String name) throws SirhaException;
    Decanate getDecanateById(String id) throws SirhaException;
    List<Decanate> getAllDecanates() throws SirhaException;

    // ========== Gestión de Solicitudes DE LA DECANATURA ==========
    List<BaseRequest> getAllRequests() throws SirhaException;
    List<BaseRequest> getAllRequestsForDecanate(String decanateName) throws SirhaException;
    BaseRequest getRequestById(String requestId) throws SirhaException;
    BaseRequest receiveRequest(String decanateName, String requestId) throws SirhaException;
    BaseRequest approveRequest(String decanateName, String requestId) throws SirhaException;
    BaseRequest rejectRequest(String decanateName, String requestId) throws SirhaException;

    // ========== Relación con Study Plans ==========
    // ✅ Estos métodos gestionan la ASOCIACIÓN entre Decanate y StudyPlan
    List<StudyPlan> getStudyPlansOfDecanate(String decanateName) throws SirhaException;
    Decanate addStudyPlanToDecanate(String decanateName, String studyPlanId) throws SirhaException;
    Decanate removeStudyPlanFromDecanate(String decanateName, String studyPlanId) throws SirhaException;

    // ========== Query de conveniencia (delegación) ==========
    StudentDTO getStudentBasicInfo(String username) throws SirhaException;
}