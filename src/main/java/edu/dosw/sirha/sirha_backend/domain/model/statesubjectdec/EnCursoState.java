package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectState;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

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
    public void inscribir(SubjectDecorator materia, Group grupo) throws SirhaException { throw SirhaException.of(ErrorCodeSirha.SUBJECT_ALREADY_ENROLLED,"La materia ya está inscrita"); }

    public void retirar(SubjectDecorator materia) throws SirhaException {
        materia.setState(new NoCursadaState());
        materia.getState().setState(materia);
    }

    public void aprobar(SubjectDecorator materia) throws SirhaException {
        materia.setState(new AprobadaState());
        materia.getState().setState(materia);
    }

    public void reprobar(SubjectDecorator materia) throws SirhaException {
        materia.setState(new ReprobadaState());
        materia.getState().setState(materia); 
    }

    public boolean canEnroll() { return false; }
    public boolean canApprove() { return true; }
    public boolean canFail() { return true; }
    public boolean canDropSubject() { return true; }
    public boolean hasAssignedGroup() { return true; }
    public String getStatusName() { return "En Curso"; }
}
