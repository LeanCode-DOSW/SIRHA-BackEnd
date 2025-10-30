package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;

import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectState;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Clase base para estados de SubjectDecorator que provee comportamientos por defecto
 * para evitar duplicación de código en las diferentes implementaciones de estado.
 */
public abstract class AbstractSubjectState implements SubjectState {

    @Override
    public void setState(SubjectDecorator subject) throws SirhaException {
        // cada subclase debe sobreescribir para establecer el color correspondiente
        throw SirhaException.of(ErrorCodeSirha.OPERATION_NOT_ALLOWED);
    }

    @Override
    public void setSemester(SubjectDecorator subject, int semester) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_SEMESTER);
    }

    @Override
    public void setGroup(SubjectDecorator subject, Group group) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_GROUP);
    }

    @Override
    public void setGrade(SubjectDecorator subject, int grade) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_GRADE);
    }

    @Override
    public void inscribir(SubjectDecorator subject, Group group) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.CANNOT_ENROLL);
    }

    @Override
    public void aprobar(SubjectDecorator subject) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.CANNOT_APPROVE);
    }

    @Override
    public void reprobar(SubjectDecorator subject) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.CANNOT_FAIL);
    }

    @Override
    public void retirar(SubjectDecorator subject) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.CANNOT_DROP_SUBJECT);
    }

    @Override
    public boolean canEnroll() { return false; }

    @Override
    public boolean canApprove() { return false; }

    @Override
    public boolean canFail() { return false; }

    @Override
    public boolean canDropSubject() { return false; }

    @Override
    public boolean hasAssignedGroup() { return false; }

}
