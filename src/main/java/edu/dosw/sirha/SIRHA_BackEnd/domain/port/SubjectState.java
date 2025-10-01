package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;

public interface SubjectState  {
    void setState(SubjectDecorator materia);
    void setSemestre(SubjectDecorator materia, int semestre);
    void setGroup(SubjectDecorator materia, Group grupo);

    void inscribir(SubjectDecorator materia, Group grupo);
    void aprobar(SubjectDecorator materia);
    void reprobar(SubjectDecorator materia);
    void retirar(SubjectDecorator materia);

    boolean puedeInscribirse();
    boolean puedeAprobar();
    boolean puedeReprobar();
    boolean puedeRetirar();
    boolean tieneGrupoAsignado();
    String getEstadoNombre();

}
