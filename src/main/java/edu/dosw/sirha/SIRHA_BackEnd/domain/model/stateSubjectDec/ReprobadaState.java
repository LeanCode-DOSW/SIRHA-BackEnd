package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectState;

public class ReprobadaState implements SubjectState {

    public void setState(SubjectDecorator materia) {materia.setEstadoColor(SemaforoColores.ROJO);}
    public void setSemester(SubjectDecorator materia, int semester) {throw new IllegalStateException("No se puede cambiar semestre de materia reprobada hasta aprobarla");}
    public void setGroup(SubjectDecorator materia, Group grupo) {throw new IllegalStateException("No se puede cambiar grupo de materia reprobada hasta aprobarla");}
    public void setGrade(SubjectDecorator materia, int grade) {throw new IllegalStateException("No se puede cambiar la nota de una materia reprobada");}

    public void inscribir(SubjectDecorator materia, Group grupo) {
        materia.setState(new EnCursoState());
        materia.setGroup(grupo);        
        materia.getState().setState(materia);
    }

    public void aprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede aprobar una materia reprobada sin inscribirse de nuevo");}
    public void reprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede reprobar una materia que ya está reprobada");}
    public void retirar(SubjectDecorator materia) {throw new IllegalStateException("No se puede retirar una materia reprobada");}
    
    public boolean canEnroll() { return true; }
    public boolean canApprove() { return true; }
    public boolean canFail() { return false; }
    public boolean canDropSubject() { return false; }
    public boolean hasAssignedGroup() { return true; }
    public String getStatusName() { return "Reprobada"; }
}
