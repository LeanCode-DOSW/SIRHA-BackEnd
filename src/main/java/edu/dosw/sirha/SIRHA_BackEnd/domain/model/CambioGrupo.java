package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.Map;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.AdminState;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;

public class CambioGrupo extends BaseRequest {

    public CambioGrupo(Subject previousSubject, Group previousGroup,
                       Subject newSubject, Group newGroup, String motivo,int prioridad, Student student) {
        super(previousSubject, previousGroup, newSubject, newGroup, motivo,prioridad, student);
       
    }

    @Override
    public boolean validar(Subject newSubject, Group newGroup) {
        String subjectName = newSubject.getName();

        Student student = this.getStudent();
        Semaforo semaforo = student.getSemaforo();
        Map<String, SemaforoColores> colores = semaforo.getAll();

        

        if (colores.get(subjectName) == SemaforoColores.ROJO){
            return false;
        }
        return true;

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
