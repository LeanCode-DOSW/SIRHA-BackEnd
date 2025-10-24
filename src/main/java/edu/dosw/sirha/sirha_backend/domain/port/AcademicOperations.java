package edu.dosw.sirha.sirha_backend.domain.port;

import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface AcademicOperations {
    boolean canEnroll(Subject subject) throws SirhaException;
    boolean canEnrollInGroup(Subject subject, Group group) throws SirhaException;
    void enrollSubject(Subject subject, Group group) throws SirhaException;
    void unenrollSubject(Subject subject, Group group) throws SirhaException;
    void approveSubject(Subject subject) throws SirhaException;
    void failSubject(Subject subject) throws SirhaException;
    
    boolean hasSubject(Subject subject);
    boolean hasActiveRequests();
}
