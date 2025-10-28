package edu.dosw.sirha.sirha_backend.service;

import java.util.List;
import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface GroupService {
    // CRUD básico
    List<Group> findAll();
    Group findById(String id) throws SirhaException;
    Group findByCode(String code) throws SirhaException;
    Group save(GroupDTO groupDTO, String subjectName) throws SirhaException;
    Group deleteById(String id) throws SirhaException;
    boolean existsById(String id);

    // Operaciones de negocio del Group
    Group assignProfessor(String groupId, Professor professor) throws SirhaException;
    Group addSchedule(String groupId, Schedule schedule) throws SirhaException;
    Group closeGroup(String groupId) throws SirhaException;
    Group openGroup(String groupId) throws SirhaException;

    // Queries de información
    Professor getProfessor(String groupId) throws SirhaException;
    List<Schedule> getSchedules(String groupId) throws SirhaException;
    boolean isFull(String groupId) throws SirhaException;
    int getAvailableSeats(String groupId) throws SirhaException;

    // Query por Subject
    List<Group> findBySubjectName(String subjectName) throws SirhaException;
    List<Group> findOpenGroupsBySubjectName(String subjectName) throws SirhaException;

    // Operaciones en lote
    List<Group> deleteBySubjectName(String subjectName) throws SirhaException;
}