package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public class CambioGrupo extends BaseRequest {
    private Subject subject;
    private Group group;

    public CambioGrupo(Student student, Subject subject, Group group, AcademicPeriod currentPeriod) {
        super(student, currentPeriod);
        this.subject = subject;
        this.group = group;
    }

    @Override
    public boolean validateRequest() {// para validar si se puede hacer el cambio de grupo necesitamos, primero ver el grupo que antes tenia, ver si el nuevo no tiene problemas con el horario
        return false;
    }

    
    

}
