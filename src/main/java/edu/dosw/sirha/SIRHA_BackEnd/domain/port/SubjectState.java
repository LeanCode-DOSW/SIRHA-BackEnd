package edu.dosw.sirha.sirha_backend.domain.port;

import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;

public interface SubjectState  {
    void setState(SubjectDecorator subject);
    void setSemester(SubjectDecorator subject, int semester);
    void setGroup(SubjectDecorator subject, Group group);
    void setGrade(SubjectDecorator subject, int grade);

    void inscribir(SubjectDecorator subject, Group group);
    void aprobar(SubjectDecorator subject);
    void reprobar(SubjectDecorator subject);
    void retirar(SubjectDecorator subject);

    boolean canEnroll();
    boolean canApprove();
    boolean canFail();
    boolean canDropSubject();
    boolean hasAssignedGroup();
    String getStatusName();

}
