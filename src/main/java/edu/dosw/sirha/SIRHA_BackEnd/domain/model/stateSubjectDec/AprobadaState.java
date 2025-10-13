package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectState;


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


    
    public boolean canEnroll() { return false; }
    public boolean canApprove() { return false; }
    public boolean canFail() { return false; }
    public boolean canDropSubject() { return false; }
    public boolean hasAssignedGroup() { return true; }
    public String getStatusName() { return "Aprobada"; }
}
