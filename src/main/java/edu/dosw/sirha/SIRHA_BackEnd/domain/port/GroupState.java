package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;

public interface GroupState {
    void inscribirEstudiante(Group grupo, Student estudiante);
}
