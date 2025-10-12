package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface GroupService {

    Group registerGroup(String subjectName, Group group);
    Group deleteGroup(Integer id);

    List<Group> findAll();
    

    Group asignarProfesor(Integer groupId, Professor professor);
    
    Group agregarHorario(Integer groupId, Schedule schedule);

    Group findById(Integer id);
    Professor getProfesor(Integer groupId);
    List<Schedule> getHorarios(Integer groupId);
    boolean estaLleno(Integer groupId);
    int getCuposDisponibles(Integer groupId);

    // Estado del grupo
    //Group cerrarGrupo(Integer groupId);
    //Group abrirGrupo(Integer groupId);
}