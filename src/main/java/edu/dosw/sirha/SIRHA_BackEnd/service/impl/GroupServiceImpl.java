package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.StatusClosed;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.StatusOpen;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.GroupMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.SubjectMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.GroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de {@link GroupService} enfocada en la gestión de materias y grupos.
 */
@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupMongoRepository groupRepository;
    private final SubjectMongoRepository subjectRepository;

    public GroupServiceImpl(GroupMongoRepository groupRepository, SubjectMongoRepository subjectRepository) {
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
        log.info("GroupServiceImpl inicializado correctamente");
    }


    /**
     * Registra un grupo en una materia
     */
    @Transactional
    @Override
    public Group saveGroup(String subjectName, Group group) {
        Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> new RuntimeException("Materia con nombre " + subjectName + " no encontrada"));

        subject.addGroup(group);
        subjectRepository.save(subject);
        return groupRepository.save(group);
    }

    @Transactional
    @Override
    public Group deleteGroupById(Integer id) {               //REVISAR
        if (!groupRepository.existsById(id)) {
            throw new RuntimeException("Grupo con id " + id + " no existe");
        } 
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo con id " + id + " no encontrado"));
        Subject subject = group.getCurso();
        subject.removeGroup(group);
        groupRepository.deleteById(id);
        return group;
    }

    @Override
    public Group assignProfessor(Integer groupId, Professor professor) {
        Group group = findById(groupId);
        group.setProfesor(professor);
        return groupRepository.save(group);
    }

    @Override
    public Group addSchedule(Integer groupId, Schedule schedule) {
        Group group = findById(groupId);
        group.getSchedules().add(schedule);
        return groupRepository.save(group);
    }
/*  
    @Override
    public Group cerrarGrupo(Integer groupId) {
        log.info("Cerrando grupo con ID: {}", groupId);
        Group group = findById(groupId);
        group.closeGroup();
        return groupRepository.save(group);
    }

    @Override
    public Group abrirGrupo(Integer groupId) {
        log.info("Abriendo grupo con ID: {}", groupId);
        Group group = findById(groupId).orElseThrow(() -> new RuntimeException("Grupo con id " + groupId + " no encontrado"));
        log.info("Estado actual del grupo (antes de abrir): {}", group.getGroupState().getClass().getSimpleName());
        group.openGroup();
        log.info("Estado actual del grupo (después de abrir): {}", group.getGroupState().getClass().getSimpleName());
        return groupRepository.save(group);
    }

*/



    // ===========================GETS ============================

    @Override
    public List<Schedule> getSchedules(Integer groupId) {
        return findById(groupId).getSchedules();
    }


    @Override
    public boolean isFull(Integer groupId) {
        Group group = findById(groupId);
        return group.getCuposDisponibles() <= 0;
    }

    @Override
    public int getAvailableSeats(Integer groupId) {
        return findById(groupId).getCuposDisponibles();
    }
    @Override
    public Group findById(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo con id " + id + " no encontrado"));
    }
    @Override
    public Professor getProfessor(Integer groupId) {
        return findById(groupId).getProfesor();
    }

    @Override
    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }
}