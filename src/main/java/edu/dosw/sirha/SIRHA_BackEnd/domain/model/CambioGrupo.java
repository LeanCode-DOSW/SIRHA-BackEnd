package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.AdminState;

public class CambioGrupo extends BaseRequest {

    public CambioGrupo(Subject previousSubject, Group previousGroup,
                       Subject newSubject, Group newGroup, String motivo,int prioridad, Student student) {
        super(previousSubject, previousGroup, newSubject, newGroup, motivo,prioridad, student);
       
    }

    @Override
    public boolean validar() {
        return false;
    }

    @Override
    public boolean aplicar() {
        return false;
    }

    @Override
    public void aprobar() {
        
    }

    @Override
    public void rechazar() {
    }

    @Override
    public boolean valideResquest() {
        return false;
    }

    @Override
    public AdminState approveRequest() {
        return null;
    }
    @Override
    public void proccessRequest() {
    }
}
