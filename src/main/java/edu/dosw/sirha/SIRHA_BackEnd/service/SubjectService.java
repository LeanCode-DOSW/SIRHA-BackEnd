package edu.dosw.sirha.sirha_backend.service;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;

public interface SubjectService {
    List<Subject> findAll();
    Subject findByName(String name);
    Subject save(Subject subject);
    Subject deleteByName(String name);
    boolean existsByName(String name);
    List<Group> getGroupsOfSubject(String subjectName);

    List<Group> getOpenGroupsOfSubject(String subjectName);
    Group getGroupById(String id);
    Group saveGroup(String subjectName, Group group);
    Group deleteGroupById(String id);
    boolean existsGroupById(String id);
    
    List<Group> findAllGroups();

    Group assignProfessor(String groupId, Professor professor);
    Group addSchedule(String groupId, Schedule schedule);

    Professor getProfessor(String groupId);
    List<Schedule> getSchedules(String groupId);
    boolean isFull(String groupId);
    int getAvailableSeats(String groupId);

    List<Group> deleteGroupsBySubjectName(String subjectName);
    
    //Group cerrarGrupo(String groupId);
    //Group abrirGrupo(String groupId);
}   
