package edu.dosw.sirha.sirha_backend.domain.port;

import edu.dosw.sirha.sirha_backend.domain.model.CambioGrupo;
import edu.dosw.sirha.sirha_backend.domain.model.CambioMateria;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface SolicitudFactory {
    CambioGrupo createGroupChangeRequest(Subject subject, Group newGroup) throws SirhaException;
    CambioMateria createSubjectChangeRequest(Subject oldSubject, Subject newSubject, Group newGroup) throws SirhaException;
    }
