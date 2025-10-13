package edu.dosw.sirha.sirha_backend.domain.model;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;

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
    public boolean validateRequest() {
        return student.validateChangeGroup(subject, newGroup);
    }

}
