package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;

public class Semaforo {
    List<SubjectDecorator> subjects;
    StudyPlan studyPlan;

    public Semaforo(StudyPlan studyPlan) {
        this.subjects = new ArrayList<>();
        this.studyPlan = studyPlan;
    }


}
