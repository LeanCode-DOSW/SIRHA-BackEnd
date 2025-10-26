package edu.dosw.sirha.sirha_backend.service;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface StudyPlanService {
    List<StudyPlan> getStudyPlansByCareer(Careers career) throws SirhaException;
    StudyPlan saveStudyPlan(StudyPlan studyPlan) throws SirhaException;
    StudyPlan addSubjectToStudyPlan(String studyPlanName, String subjectName) throws SirhaException;

    StudyPlan getStudyPlanByName(String name) throws SirhaException;
}
