package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class NoCursadaState implements SubjectState {

    public NoCursadaState() {}

    public void setState(SubjectDecorator materia) {materia.setEstadoColor(SemaforoColores.GRIS);}

    public void setSemester(SubjectDecorator materia,int semester) {throw new IllegalStateException("No se puede cambiar semestre de materia no cursada");}
    public void setGroup(SubjectDecorator materia, Group grupo) {throw new IllegalStateException("No se puede asignar grupo a materia no cursada");}
    public void setGrade(SubjectDecorator materia, int grade) {throw new IllegalStateException("No se puede cambiar la nota de una materia no cursada");}

    public void inscribir(SubjectDecorator materia, Group grupo) {
        materia.setState(new EnCursoState());
        materia.setGroup(grupo);
        materia.getState().setState(materia);
        System.out.println("Materia inscrita. Ahora está en curso.");
    }

    public void aprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede aprobar una materia no cursada");}
    public void reprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede reprobar una materia no cursada");}
    public void retirar(SubjectDecorator materia) {throw new IllegalStateException("No se puede retirar una materia no cursada");}

    public boolean puedeInscribirse() { return true; }
    public boolean puedeAprobar() { return false; }
    public boolean puedeReprobar() { return false; }
    public boolean puedeRetirar() { return false; }
    public boolean tieneGrupoAsignado() { return false; }
    public String getEstadoNombre() { return "No Cursada"; }
}
