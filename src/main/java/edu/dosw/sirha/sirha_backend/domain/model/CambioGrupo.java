package edu.dosw.sirha.sirha_backend.domain.model;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class CambioGrupo extends BaseRequest {
    private Subject subject;
    private Group newGroup;

    public CambioGrupo(Student student, Subject subject, Group newGroup, AcademicPeriod currentPeriod) {
        super(student, currentPeriod);
        this.subject = subject;
        this.newGroup = newGroup;
        
    }
    public Subject getSubject() {return subject;}
    public Group getNewGroup() {return newGroup;}
    
    @Override
    public boolean validateRequest() throws SirhaException {
        return student.validateChangeGroup(subject, newGroup);
    }

}
