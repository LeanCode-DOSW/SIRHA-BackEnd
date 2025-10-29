package edu.dosw.sirha.sirha_backend.service;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.PrerequisiteRule;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface SubjectService {
    // CRUD básico de Subject
    List<Subject> findAll();
    Subject findById(String id) throws SirhaException;
    Subject findByName(String name) throws SirhaException;
    Subject save(Subject subject) throws SirhaException;
    Subject deleteByName(String name) throws SirhaException;
    boolean existsByName(String name);

    // Prerequisitos (parte del Subject)
    Subject addPrerequisite(String subjectName, PrerequisiteRule prerequisite) throws SirhaException;

    // Query sobre grupos RELACIONADOS con la materia
    List<Group> getGroupsBySubjectName(String subjectName) throws SirhaException;
    List<Group> getOpenGroupsBySubjectName(String subjectName) throws SirhaException;

}
