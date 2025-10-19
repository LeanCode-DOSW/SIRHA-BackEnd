package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectState;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class NoCursadaState implements SubjectState {

    public void setState(SubjectDecorator materia) {materia.setEstadoColor(SemaforoColores.GRIS);}
    public void setSemester(SubjectDecorator materia, int semester) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_SEMESTER,"No se puede cambiar semestre de materia no cursada");}
    public void setGroup(SubjectDecorator materia, Group grupo) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_GROUP,"No se puede asignar grupo a materia no cursada");}
    public void setGrade(SubjectDecorator materia, int grade) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_GRADE,"No se puede cambiar la nota de una materia no cursada");}

    public void inscribir(SubjectDecorator materia, Group grupo) throws SirhaException {
        materia.setState(new EnCursoState());
        materia.setGroup(grupo);
        materia.getState().setState(materia);
    }

    public void aprobar(SubjectDecorator materia) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_APPROVE,"No se puede aprobar una materia no cursada");}
    public void reprobar(SubjectDecorator materia) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_FAIL,"No se puede reprobar una materia no cursada");}
    public void retirar(SubjectDecorator materia) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_DROP_SUBJECT,"No se puede retirar una materia no cursada");}

    public boolean canEnroll() { return true; }
    public boolean canApprove() { return false; }
    public boolean canFail() { return false; }
    public boolean canDropSubject() { return false; }
    public boolean hasAssignedGroup() { return false; }
    public String getStatusName() { return "No Cursada"; }
}
