package edu.dosw.sirha.sirha_backend.domain.port;

import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface SubjectState  {
    void setState(SubjectDecorator subject) throws SirhaException;
    void setSemester(SubjectDecorator subject, int semester) throws SirhaException;
    void setGroup(SubjectDecorator subject, Group group) throws SirhaException;
    void setGrade(SubjectDecorator subject, int grade) throws SirhaException;

    void inscribir(SubjectDecorator subject, Group group) throws SirhaException;
    void aprobar(SubjectDecorator subject) throws SirhaException;
    void reprobar(SubjectDecorator subject) throws SirhaException;
    void retirar(SubjectDecorator subject) throws SirhaException;

    boolean canEnroll();
    boolean canApprove();
    boolean canFail();
    boolean canDropSubject();
    boolean hasAssignedGroup();
    String getStatusName();

}
