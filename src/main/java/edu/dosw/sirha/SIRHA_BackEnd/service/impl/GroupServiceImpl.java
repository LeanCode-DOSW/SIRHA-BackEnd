package edu.dosw.sirha.sirha_backend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.repository.mongo.GroupMongoRepository;
import edu.dosw.sirha.sirha_backend.service.GroupService;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;

import java.util.List;

/**
 * Implementación de {@link GroupService} enfocada en la gestión de materias y grupos.
 */
@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupMongoRepository groupRepository;

    public GroupServiceImpl(GroupMongoRepository groupRepository) {
        this.groupRepository = groupRepository;
        log.info("GroupServiceImpl inicializado correctamente");
    }


    /**
     * Registra un grupo en una materia
     */
    @Transactional
    @Override
    public Group saveGroup(Subject subject, Group group) throws SirhaException {
        try {
            if (subject == null) {
                log.error("Error: La materia no puede ser null");
                throw SirhaException.of( ErrorCodeSirha.SUBJECT_NOT_FOUND, "La materia no puede ser null");
            }
            
            if (group == null) {
                log.error("Error: El grupo no puede ser null");
                throw SirhaException.of( ErrorCodeSirha.GROUP_NOT_FOUND, "El grupo no puede ser null");
            }

            log.info("Guardando grupo {} para la materia: {}", group.getId(), subject.getName());
            Group savedGroup = groupRepository.save(group);
            subject.addGroup(savedGroup);
            
            log.info("Grupo guardado exitosamente con ID: {}", savedGroup.getId());
            return savedGroup;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of( ErrorCodeSirha.INTERNAL_ERROR, "Error interno al guardar grupo", e);
        }
    }

    @Transactional
    @Override
    public Group deleteGroupById(String id) throws SirhaException {
        log.info("Eliminando grupo con ID: {}", id);

        try {
            if (id == null) {
                log.error("Error: ID del grupo no puede ser null");
                throw SirhaException.of( ErrorCodeSirha.GROUP_NOT_FOUND, "El ID del grupo no puede ser null");
            }

            if (!groupRepository.existsById(id)) {
                log.warn("Grupo con ID {} no existe", id);
                throw SirhaException.of( ErrorCodeSirha.GROUP_NOT_FOUND, "Grupo no existe");
            }

            Group group = groupRepository.findById(id)
                    .orElseThrow(() -> SirhaException.of( ErrorCodeSirha.GROUP_NOT_FOUND, "Grupo no encontrado"));
            log.info("Grupo encontrado - Código: {}, Inscritos: {}", 
                    group.getCode(), 
                    group.getInscritos());

            groupRepository.deleteById(id);
            log.info("Grupo con ID {} eliminado exitosamente", id);
            return group;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of( ErrorCodeSirha.INTERNAL_ERROR, "Error interno al eliminar grupo", e);
        }
        
    }

    @Transactional
    @Override
    public Group assignProfessor(String groupId, Professor professor) throws SirhaException {
        try {
            if (professor == null) {
                log.error("Error: El profesor no puede ser null");
                throw SirhaException.of( ErrorCodeSirha.INVALID_ARGUMENT, "El profesor no puede ser null");
            }

            log.info("Asignando profesor {} al grupo con ID: {}", professor.getUsername(), groupId);
            Group group = findById(groupId);
            group.setProfessor(professor);
            Group updatedGroup = groupRepository.save(group);
            log.info("Profesor {} asignado exitosamente al grupo con ID: {}", professor.getUsername(), groupId);

            return updatedGroup;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of( ErrorCodeSirha.INTERNAL_ERROR, "Error interno al asignar profesor", e);
        }
    }

    @Transactional
    @Override
    public Group addSchedule(String groupId, Schedule schedule) throws SirhaException {
        log.info("Agregando horario al grupo con ID: {}", groupId);

        try {
            if (schedule == null) {
                log.error("Error: El horario no puede ser null");
                throw SirhaException.of( ErrorCodeSirha.INVALID_ARGUMENT, "El horario no puede ser null");
            }
            Group group = findById(groupId);
            group.addSchedule(schedule);
            log.info("Horario agregado exitosamente al grupo con ID: {}", groupId);
            Group updatedGroup = groupRepository.save(group);
            log.info("Grupo con ID {} actualizado con nuevo horario", groupId);
            return updatedGroup;
        } catch (SirhaException e) {
            throw e;
        }catch (Exception e) {
            throw SirhaException.of( ErrorCodeSirha.INTERNAL_ERROR, "Error interno al agregar horario", e);
        }
    }

    @Transactional
    @Override
    public Group closeGroup(String groupId) throws SirhaException {
        log.info("Cerrando grupo con ID: {}", groupId);
        try{
            Group group = findById(groupId);
            log.info("Estado actual del grupo (antes de cerrar): {}", group.getGroupState().getClass().getSimpleName());
            group.closeGroup();
            log.info("Estado actual del grupo (después de cerrar): {}", group.getGroupState().getClass().getSimpleName());
            Group updatedGroup = groupRepository.save(group);
            log.info("Grupo con ID {} cerrado exitosamente", groupId);
            return updatedGroup;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al cerrar grupo: %s", e.getMessage());
        }
    }

    @Transactional
    @Override
    public Group openGroup(String groupId) throws SirhaException {
        log.info("Abriendo grupo con ID: {}", groupId);
        try{
            Group group = findById(groupId);
            log.info("Estado actual del grupo (antes de abrir): {}", group.getGroupState().getClass().getSimpleName());
            group.openGroup();
            log.info("Estado actual del grupo (después de abrir): {}", group.getGroupState().getClass().getSimpleName());
            Group updatedGroup = groupRepository.save(group);
            log.info("Grupo con ID {} abierto exitosamente", groupId);
            return updatedGroup;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al abrir grupo: %s", e.getMessage());
        }
    }
    
    @Transactional
    @Override
    public List<Schedule> getSchedules(String groupId) throws SirhaException {
        log.info("Obteniendo horarios del grupo con ID: {}", groupId);
        try {
            Group group = findById(groupId);
            List<Schedule> schedules = group.getSchedules();
            log.info("Horarios obtenidos exitosamente para el grupo con ID: {}", groupId);
            return schedules;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al obtener horarios: %s", e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean isFull(String groupId) throws SirhaException     {
        log.info("Verificando si el grupo con ID {} está lleno", groupId);
        try {
            Group group = findById(groupId);
            boolean isFull = group.isFull();
            log.info("El grupo con ID {} está {}", groupId, isFull ? "lleno" : "disponible");
            return isFull;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al verificar si el grupo está lleno: %s", e.getMessage());
        }
    }

    @Transactional
    @Override
    public int getAvailableSeats(String groupId) throws SirhaException {
        log.info("Obteniendo asientos disponibles del grupo con ID: {}", groupId);
        try {
            Group group = findById(groupId);
            int availableSeats = group.getCuposDisponibles();
            log.info("El grupo con ID {} tiene {} asientos disponibles", groupId, availableSeats);
            return availableSeats;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al obtener asientos disponibles: %s", e.getMessage());
        }
    }

    @Transactional
    @Override
    public Group findById(String id) throws SirhaException {
        try {
            if (id == null) {
                log.error("Error: ID del grupo no puede ser null");
                throw SirhaException.of( ErrorCodeSirha.GROUP_NOT_FOUND, "El ID del grupo no puede ser null");
            }
            log.info("Buscando grupo con ID: {}", id);
            Group group = groupRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Grupo con ID {} no encontrado", id);
                        return SirhaException.of( ErrorCodeSirha.GROUP_NOT_FOUND, "Grupo con id " + id + " no encontrado");
                    });
            log.info("Grupo encontrado: {}", group);
            return group;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.DATABASE_ERROR, "Error interno al buscar grupo: %s", e.getMessage());
        }
    }

    @Transactional
    @Override
    public Professor getProfessor(String groupId) throws SirhaException {
        log.info("Obteniendo profesor del grupo con ID: {}", groupId);
        try {
            Group group = findById(groupId);
            Professor professor = group.getProfessor();
            if (professor == null) {
                log.warn("El grupo con ID {} no tiene un profesor asignado", groupId);
                throw SirhaException.of( ErrorCodeSirha.PROFESSOR_NOT_FOUND, "El grupo con id " + groupId + " no tiene un profesor asignado");
            }
            log.info("Profesor del grupo con ID {}: {}", groupId, professor.getUsername());
            return professor;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of( ErrorCodeSirha.INTERNAL_ERROR, "Error interno al obtener profesor", e);
        }
    }

    
    @Transactional
    @Override
    public List<Group> findAllGroups() throws SirhaException {
        log.info("Obteniendo todos los grupos");
        try {
            List<Group> groups = groupRepository.findAll();
            log.info("Total de grupos encontrados: {}", groups.size());
            return groups;
        } catch (Exception e) {
            throw SirhaException.of( ErrorCodeSirha.INTERNAL_ERROR, "Error interno al obtener grupos", e);
        }
    }

}