package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface SubjectService {
    List<Subject> findAll();
    Subject findByName(String name);
    Subject save(Subject subject);
    void deleteByName(String name);
    boolean existsByName(String name);
    List<Group> getGroupsOfSubject(String subjectName);

    List<Group> getOpenGroupsOfSubject(String subjectName);
    Group getGroupById(String id);
    Group saveGroup(String subjectName, Group group);
    void deleteGroupById(String id);
    boolean existsGroupById(String id);
    
    List<Group> findAllGroups();

    Group assignProfessor(Integer groupId, Professor professor);
    Group addSchedule(Integer groupId, Schedule schedule);

    Professor getProfessor(Integer groupId);
    List<Schedule> getSchedules(Integer groupId);
    boolean isFull(Integer groupId);
    int getAvailableSeats(Integer groupId);
    //Group cerrarGrupo(Integer groupId);
    //Group abrirGrupo(Integer groupId);
}   
