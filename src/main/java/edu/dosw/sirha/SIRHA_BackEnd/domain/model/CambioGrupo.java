package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

public class CambioGrupo extends BaseRequest {
    private Subject subject;
    private Group group;

    public CambioGrupo(Student student, Subject subject, Group group) {
        super(student);
        this.subject = subject;
        this.group = group;
    }

    @Override
    public boolean validateRequest() {// para validar si se puede hacer el cambio de grupo necesitamos, primero ver el grupo que antes tenia, ver si el nuevo no tiene problemas con el horario
        return false;
    }

    
    

}
