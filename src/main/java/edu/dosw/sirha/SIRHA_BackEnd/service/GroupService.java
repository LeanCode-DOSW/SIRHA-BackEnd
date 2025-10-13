package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface GroupService {
    List<Group> findAllGroups();
    Group saveGroup(Subject subject, Group group);
    Group deleteGroupById(String id);

    Group assignProfessor(String groupId, Professor professor);
    Group addSchedule(String groupId, Schedule schedule);

    Group findById(String id);
    Professor getProfessor(String groupId);
    List<Schedule> getSchedules(String groupId);
    boolean isFull(String groupId);
    int getAvailableSeats(String groupId);

    Group closeGroup(String groupId);
    Group openGroup(String groupId);
}