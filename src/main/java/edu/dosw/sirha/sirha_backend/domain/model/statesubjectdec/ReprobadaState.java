package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
// imports simplified - defaults provided by AbstractSubjectState
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class ReprobadaState extends AbstractSubjectState {

    @Override
    public void setState(SubjectDecorator materia) { materia.setEstadoColor(SemaforoColores.ROJO); }

    @Override
    public void inscribir(SubjectDecorator materia, Group grupo) throws SirhaException {
        materia.setState(new EnCursoState());
        materia.setGroup(grupo);
        materia.getState().setState(materia);
    }

    @Override
    public boolean canEnroll() { return true; }

    @Override
    public boolean canApprove() { return true; }

    @Override
    public boolean hasAssignedGroup() { return true; }

    @Override
    public String getStatusName() { return "Reprobada"; }
}
