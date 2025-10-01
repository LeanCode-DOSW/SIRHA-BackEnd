package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.StatusClosed;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.StatusOpen;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.GroupMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.SubjectMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación de {@link GroupService} enfocada en la gestión de materias y grupos.
 */
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupMongoRepository groupRepository;
    private final SubjectMongoRepository subjectRepository;

    public GroupServiceImpl(GroupMongoRepository groupRepository, SubjectMongoRepository subjectRepository) {
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
    }

    // ---------- Materias ----------

    /** Registra una nueva materia. */
    public Subject registrarMateria(Subject subject) {
        return subjectRepository.save(subject);
    }

    /** Lista todas las materias. */
    public List<Subject> listarMaterias() {
        return subjectRepository.findAll();
    }

    // ---------- Grupos ----------

    /**
     * Registra un grupo en una materia verificando conflictos de horarios.
     */
    public Group registrarGrupo(String subjectId, Group grupo) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Materia con id " + subjectId + " no encontrada"));

        // Validar conflictos de horarios con grupos existentes
        for (Group gExistente : subject.getGroups()) {
            for (Schedule hNuevo : grupo.getSchedules()) {
                for (Schedule hExistente : gExistente.getSchedules()) {
                    if (hNuevo.seSolapaCon(hExistente)) {
                        throw new RuntimeException("Conflicto de horario con grupo existente: " + gExistente.getId());
                    }
                }
            }
        }

        subject.addGroup(grupo);
        subjectRepository.save(subject);
        return groupRepository.save(grupo);
    }

    /** Lista todos los grupos de una materia. */
    public List<Group> listarGruposPorMateria(String subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Materia con id " + subjectId + " no encontrada"));
        return subject.getGroups();
    }

    /** Actualiza la información de un grupo. */
    public Group actualizarGrupo(Group grupo) {
        if (!groupRepository.existsById(grupo.getId())) {
            throw new RuntimeException("Grupo con id " + grupo.getId() + " no existe");
        }
        return groupRepository.save(grupo);
    }

    /** Elimina un grupo por su ID. */
    public void eliminarGrupo(Integer id) {
        if (!groupRepository.existsById(id)) {
            throw new RuntimeException("Grupo con id " + id + " no existe");
        }
        groupRepository.deleteById(id);
    }

    // ---------- Implementación de métodos de GroupService ----------

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group findById(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo con id " + id + " no encontrado"));
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public void delete(Integer id) {
        eliminarGrupo(id);
    }

    // ---------- Métodos de gestión de profesores ----------

    @Override
    public Group asignarProfesor(Integer groupId, Professor professor) {
        Group group = findById(groupId);
        group.setProfesor(professor);
        return groupRepository.save(group);
    }

    @Override
    public Professor getProfesor(Integer groupId) {
        return findById(groupId).getProfesor();
    }

    // ---------- Métodos de gestión de horarios ----------

    @Override
    public Group agregarHorario(Integer groupId, Schedule schedule) {
        Group group = findById(groupId);
        group.getSchedules().add(schedule);
        return groupRepository.save(group);
    }

    @Override
    public List<Schedule> getHorarios(Integer groupId) {
        return findById(groupId).getSchedules();
    }

    // ---------- Métodos de capacidad ----------

    @Override
    public boolean estaLleno(Integer groupId) {
        Group group = findById(groupId);
        return group.getCuposDisponibles() <= 0;
    }

    @Override
    public int getCuposDisponibles(Integer groupId) {
        return findById(groupId).getCuposDisponibles();
    }

    // ---------- Estado del grupo ----------

    @Override
    public Group cerrarGrupo(Integer groupId) {
        Group group = findById(groupId);
        group.setEstadoGrupo(new StatusClosed()); // cambia el estado al cerrado
        return groupRepository.save(group);
    }

    @Override
    public Group abrirGrupo(Integer groupId) {
        Group group = findById(groupId);
        group.setEstadoGrupo(new StatusOpen()); // cambia el estado al abierto
        return groupRepository.save(group);
    }
}