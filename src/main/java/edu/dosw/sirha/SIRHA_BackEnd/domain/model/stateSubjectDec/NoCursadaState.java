package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectState;

public class NoCursadaState implements SubjectState {

    public void setState(SubjectDecorator materia) {materia.setEstadoColor(SemaforoColores.GRIS);}

    public void setSemester(SubjectDecorator materia,int semester) {throw new IllegalStateException("No se puede cambiar semestre de materia no cursada");}
    public void setGroup(SubjectDecorator materia, Group grupo) {throw new IllegalStateException("No se puede asignar grupo a materia no cursada");}
    public void setGrade(SubjectDecorator materia, int grade) {throw new IllegalStateException("No se puede cambiar la nota de una materia no cursada");}

    public void inscribir(SubjectDecorator materia, Group grupo) {
        materia.setState(new EnCursoState());
        materia.setGroup(grupo);
        materia.getState().setState(materia);
    }

    public void aprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede aprobar una materia no cursada");}
    public void reprobar(SubjectDecorator materia) {throw new IllegalStateException("No se puede reprobar una materia no cursada");}
    public void retirar(SubjectDecorator materia) {throw new IllegalStateException("No se puede retirar una materia no cursada");}

    public boolean canEnroll() { return true; }
    public boolean canApprove() { return false; }
    public boolean canFail() { return false; }
    public boolean canDropSubject() { return false; }
    public boolean hasAssignedGroup() { return false; }
    public String getStatusName() { return "No Cursada"; }
}
