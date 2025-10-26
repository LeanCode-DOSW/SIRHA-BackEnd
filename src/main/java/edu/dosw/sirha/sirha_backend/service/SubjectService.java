package edu.dosw.sirha.sirha_backend.service;

import java.util.List;

import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface SubjectService {
    List<Subject> findAll();
    Subject findByName(String name) throws SirhaException;
    Subject save(Subject subject) throws SirhaException;
    Subject deleteByName(String name) throws SirhaException;
    boolean existsByName(String name) throws SirhaException;
    List<Group> getGroupsOfSubject(String subjectName) throws SirhaException;

    List<Group> getOpenGroupsOfSubject(String subjectName) throws SirhaException;
    Group getGroupById(String id) throws SirhaException;
    Group saveGroup(String subjectName, Group group) throws SirhaException;
    boolean existsGroupById(String id) throws SirhaException;
    List<Group> deleteGroupsBySubjectName(String subjectName) throws SirhaException;   


    //de GroupService
    List<Group> findAllGroups() throws SirhaException;
    Group saveGroup(Subject subject, Group group) throws SirhaException;
    Group deleteGroupById(String id) throws SirhaException;

    Group assignProfessor(String groupId, Professor professor) throws SirhaException;
    Group addSchedule(String groupId, Schedule schedule) throws SirhaException;

    Group findById(String id) throws SirhaException;
    Professor getProfessor(String groupId) throws SirhaException;
    List<Schedule> getSchedules(String groupId) throws SirhaException;
    boolean isFull(String groupId) throws SirhaException;
    int getAvailableSeats(String groupId) throws SirhaException;

    Group closeGroup(String groupId) throws SirhaException;
    Group openGroup(String groupId) throws SirhaException;

}   
