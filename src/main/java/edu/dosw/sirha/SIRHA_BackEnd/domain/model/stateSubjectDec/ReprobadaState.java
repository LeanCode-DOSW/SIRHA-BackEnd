package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class ReprobadaState implements SubjectState {

    public void setState(SubjectDecorator materia) {materia.setEstadoColor(SemaforoColores.ROJO);}
    public void setSemester(SubjectDecorator materia, int semester) {throw new IllegalStateException("No se puede cambiar semestre de materia reprobada hasta aprobarla");}
    public void setGroup(SubjectDecorator materia, Group grupo) {throw new IllegalStateException("No se puede cambiar grupo de materia reprobada hasta aprobarla");}
    public void setGrade(SubjectDecorator materia, int grade) {throw new IllegalStateException("No se puede cambiar la nota de una materia reprobada");}

    public void inscribir(SubjectDecorator materia, Group grupo) {
        materia.setState(new EnCursoState());
        materia.setGroup(grupo);        
        materia.getState().setState(materia);
        System.out.println("Materia re-inscrita después de reprobar.");
    }

    public void aprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede aprobar una materia reprobada sin inscribirse de nuevo");}
    public void reprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede reprobar una materia que ya está reprobada");}
    public void retirar(SubjectDecorator materia) {throw new IllegalStateException("No se puede retirar una materia reprobada");}
    
    public boolean puedeInscribirse() { return true; }
    public boolean puedeAprobar() { return true; }
    public boolean puedeReprobar() { return false; }
    public boolean puedeRetirar() { return false; }
    public boolean tieneGrupoAsignado() { return true; }
    public String getEstadoNombre() { return "Reprobada"; }
}
