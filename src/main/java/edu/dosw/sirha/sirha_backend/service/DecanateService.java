package edu.dosw.sirha.sirha_backend.service;

import java.util.List;
import edu.dosw.sirha.sirha_backend.domain.model.Decanate;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface DecanateService {

    Decanate saveDecanate(Careers decanate) throws SirhaException;
    Decanate getDecanateByName(String name) throws SirhaException;
    List<Decanate> getAllDecanates() throws SirhaException;
    List<BaseRequest> getAllRequests() throws SirhaException;
    List<BaseRequest> getAllRequestsForDecanate(String decanateName) throws SirhaException; //deberia buscar las solicitudes que la decanatura puede solucionar
    BaseRequest getRequestById(String requestId) throws SirhaException;
    //alertas 90 grupos llenos falta
    BaseRequest receiveRequest(String decanateName, String requestId) throws SirhaException;
    BaseRequest approveRequest(String decanateName, String requestId) throws SirhaException;
    BaseRequest rejectRequest(String decanateName, String requestId) throws SirhaException;
    StudentDTO getStudentBasicInfo(String username) throws SirhaException;
    List<StudyPlan> getStudyPlansByDecanateName(String decanateName) throws SirhaException;
    List<StudyPlan> addPlanToDecanate(String decanateName, String studyPlan) throws SirhaException;

    //configurar academic periods

    //abrir o cerrar un grupo
    //approveSubject //metodos que la decanatura puede hacer 
    //failSubject


    //De StudyPlanService
    List<StudyPlan> getStudyPlansByCareer(Careers career) throws SirhaException;
    StudyPlan saveStudyPlan(Careers career) throws SirhaException;
    StudyPlan addSubjectToStudyPlan(String studyPlanName, String subjectName) throws SirhaException;
}
