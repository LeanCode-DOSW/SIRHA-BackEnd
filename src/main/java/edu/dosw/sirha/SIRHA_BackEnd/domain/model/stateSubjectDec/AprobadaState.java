package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class AprobadaState implements SubjectState {

    @Override
    public void setState(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.VERDE);
    }

    public void setSemester(SubjectDecorator materia, int semestre) {throw new IllegalStateException("No se puede cambiar semestre de materia aprobada");}
    public void setGroup(SubjectDecorator materia, Group grupo) {throw new IllegalStateException("No se puede asignar grupo a materia aprobada");}
    public void setGrade(SubjectDecorator materia, int grade) {throw new IllegalStateException("No se puede cambiar la nota de una materia aprobada");}
    public void setInscripcion(SubjectDecorator materia, Group grupo) {throw new IllegalStateException("No se puede inscribir una materia ya aprobada");}
    public void inscribir(SubjectDecorator materia, Group grupo) {throw new IllegalStateException("No se puede inscribir una materia ya aprobada");}
    public void aprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede aprobar una materia ya aprobada");}
    public void reprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede reprobar una materia aprobada");}
    public void retirar(SubjectDecorator materia) {throw new IllegalStateException("No se puede retirar una materia aprobada");}


    
    public boolean puedeInscribirse() { return false; }
    public boolean puedeAprobar() { return false; }
    public boolean puedeReprobar() { return false; }
    public boolean puedeRetirar() { return false; }
    public boolean tieneGrupoAsignado() { return true; }
    public String getEstadoNombre() { return "Aprobada"; }
}
