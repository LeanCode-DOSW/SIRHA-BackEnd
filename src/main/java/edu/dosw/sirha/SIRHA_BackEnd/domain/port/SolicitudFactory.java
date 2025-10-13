package edu.dosw.sirha.sirha_backend.domain.port;

import edu.dosw.sirha.sirha_backend.domain.model.CambioGrupo;
import edu.dosw.sirha.sirha_backend.domain.model.CambioMateria;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;

public interface SolicitudFactory {
    CambioGrupo createGroupChangeRequest(Subject subject, Group newGroup);
    CambioMateria createSubjectChangeRequest(Subject oldSubject, Subject newSubject, Group newGroup);
    
    //Request createSubjectEnrollmentRequest(Subject subject);
}
