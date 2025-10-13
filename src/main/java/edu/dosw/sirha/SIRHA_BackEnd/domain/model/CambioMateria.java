package edu.dosw.sirha.sirha_backend.domain.model;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;

public class CambioMateria extends BaseRequest {

    private Subject oldSubject;
    private Subject newSubject;
    private Group newGroup;

    public CambioMateria(Student student, Subject oldSubject, Subject newSubject, Group newGroup, AcademicPeriod currentPeriod) {
        super(student, currentPeriod);
        this.oldSubject = oldSubject;
        this.newSubject = newSubject;
        this.newGroup = newGroup;
    }
    public Subject getOldSubject() {return oldSubject;}
    public Subject getNewSubject() {return newSubject;}
    public Group getNewGroup() {return newGroup;}

    @Override
    public boolean validateRequest() {// para validar si se puede hacer el cambio de materia necesitamos ver si la materia nueva no tiene problemas con el horario y si el estudiante cumple con los requisitos para cursarla
        return student.validateChangeSubject(oldSubject, newSubject, newGroup);
    }

}
