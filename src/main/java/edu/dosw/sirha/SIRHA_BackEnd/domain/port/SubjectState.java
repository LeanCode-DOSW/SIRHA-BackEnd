package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;

public interface SubjectState  {
    void setState(SubjectDecorator materia);
    void setSemestre(SubjectDecorator materia, int semestre);
    void agregarGrupo(SubjectDecorator materia, Group grupo);
    void inscribir(SubjectDecorator materia);
    void aprobar(SubjectDecorator materia);
    void reprobar(SubjectDecorator materia);
}
