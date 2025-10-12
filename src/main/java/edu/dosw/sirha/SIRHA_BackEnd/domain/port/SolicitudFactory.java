package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.CambioGrupo;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.CambioMateria;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;

public interface SolicitudFactory {
    CambioGrupo createGroupChangeRequest(Subject subject, Group newGroup);
    CambioMateria createSubjectChangeRequest(Subject oldSubject, Subject newSubject, Group newGroup);
    
    //Request createSubjectEnrollmentRequest(Subject subject);
}
