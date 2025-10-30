package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class EnCursoState extends AbstractSubjectState {

    @Override
    public void setState(SubjectDecorator materia) { materia.setEstadoColor(SemaforoColores.AMARILLO); }

    @Override
    public void setSemester(SubjectDecorator materia, int semestre) { materia.setSemesterDirect(semestre); }

    @Override
    public void setGroup(SubjectDecorator materia, Group grupo) {
        if (grupo != null) {
            materia.setGroupDirect(grupo);
        } else {
            throw new IllegalArgumentException("El grupo no puede ser nulo");
        }
    }

    @Override
    public void setGrade(SubjectDecorator materia, int grade) { materia.setGradeDirect(grade); }

    @Override
    public void inscribir(SubjectDecorator materia, Group grupo) throws SirhaException { throw SirhaException.of(ErrorCodeSirha.SUBJECT_ALREADY_ENROLLED, "La materia ya está inscrita"); }

    @Override
    public void retirar(SubjectDecorator materia) throws SirhaException {
        materia.setState(new NoCursadaState());
        materia.getState().setState(materia);
    }

    @Override
    public void aprobar(SubjectDecorator materia) throws SirhaException {
        materia.setState(new AprobadaState());
        materia.getState().setState(materia);
    }

    @Override
    public void reprobar(SubjectDecorator materia) throws SirhaException {
        materia.setState(new ReprobadaState());
        materia.getState().setState(materia);
    }

    @Override
    public boolean canEnroll() { return false; }

    @Override
    public boolean canApprove() { return true; }

    @Override
    public boolean canFail() { return true; }

    @Override
    public boolean canDropSubject() { return true; }

    @Override
    public boolean hasAssignedGroup() { return true; }

    @Override
    public String getStatusName() { return "En Curso"; }
}
