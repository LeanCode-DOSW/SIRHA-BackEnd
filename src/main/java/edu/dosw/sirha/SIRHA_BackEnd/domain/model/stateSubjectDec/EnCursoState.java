package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class EnCursoState implements SubjectState {

    public void setState(SubjectDecorator materia) {materia.setEstadoColor(SemaforoColores.AMARILLO);}

    public void setSemester(SubjectDecorator materia, int semestre) {materia.setSemesterDirect(semestre);}
    public void setGroup(SubjectDecorator materia, Group grupo) {
        if (grupo != null) {
            materia.setGroupDirect(grupo);
        } else {
            throw new IllegalArgumentException("El grupo no puede ser nulo");
        }
    }
    public void setGrade(SubjectDecorator materia, int grade) {materia.setGradeDirect(grade);}
    public void inscribir(SubjectDecorator materia, Group grupo) {throw new IllegalStateException("La materia ya está inscrita");}

    public void retirar(SubjectDecorator materia) {
        materia.setState(new NoCursadaState());
        materia.getState().setState(materia);
        System.out.println("Materia retirada.");
    }

    public void aprobar(SubjectDecorator materia) {
        materia.setState(new AprobadaState());
        materia.getState().setState(materia);
        System.out.println("Materia aprobada.");
    }

    public void reprobar(SubjectDecorator materia) {
        materia.setState(new ReprobadaState());
        materia.getState().setState(materia); 
        System.out.println("Materia reprobada.");
    }

    public boolean canEnroll() { return false; }
    public boolean canApprove() { return true; }
    public boolean canFail() { return true; }
    public boolean canDropSubject() { return true; }
    public boolean hasAssignedGroup() { return true; }
    public String getStatusName() { return "En Curso"; }
}
