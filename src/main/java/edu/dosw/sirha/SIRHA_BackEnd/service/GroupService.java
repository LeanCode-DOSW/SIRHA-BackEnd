package edu.dosw.sirha.SIRHA_BackEnd.service;


import java.util.List;
import java.util.Optional;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;

public interface GroupService {
    List<Group> findAll();
    Optional<Group> findById(String id);
    Group save(Group group);
    void inscribirEstudiante(String groupId, Student student);
}
