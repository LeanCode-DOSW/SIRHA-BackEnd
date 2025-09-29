package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

public class CambioMateria extends BaseRequest {

    private Subject oldSubject;
    private Subject newSubject;

    public CambioMateria(Student student, Subject oldSubject, Subject newSubject) {
        super(student);
        this.oldSubject = oldSubject;
        this.newSubject = newSubject;
    }

    @Override
    public boolean validateRequest() {// para validar si se puede hacer el cambio de materia necesitamos ver si la materia nueva no tiene problemas con el horario y si el estudiante cumple con los requisitos para cursarla
        return false;
    }

}
