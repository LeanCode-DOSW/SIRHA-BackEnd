package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;

/**
 * Repositorio MongoDB para consultas específicas de horarios de grupos.
 * 
 * Proporciona métodos de consulta optimizados para la funcionalidad
 * de horarios y programación académica.
 */
public interface ScheduleMongoRepository extends MongoRepository<Group, String> {
    
    /**
     * Busca todos los grupos que imparten una materia específica.
     * 
     * @param subjectId ID de la materia
     * @return Lista de grupos que imparten la materia
     */
    @Query("{ 'subject.$id': ObjectId('?0') }")
    List<Group> findGroupsBySubjectId(String subjectId);
    
    /**
     * Busca grupos por código de materia.
     * 
     * @param subjectCode Código de la materia
     * @return Lista de grupos que imparten la materia
     */
    @Query("{ 'subject.codigo': ?0 }")
    List<Group> findGroupsBySubjectCode(String subjectCode);
    
    /**
     * Busca grupos disponibles (con cupos) para una materia.
     * 
     * @param subjectId ID de la materia
     * @return Lista de grupos con cupos disponibles
     */
    @Query("{ 'subject.$id': ObjectId('?0'), 'cuposDisponibles': { $gt: 0 } }")
    List<Group> findAvailableGroupsBySubjectId(String subjectId);
    
    /**
     * Busca grupos por día de la semana.
     * 
     * @param day Día de la semana
     * @return Lista de grupos que tienen clases ese día
     */
    @Query("{ 'schedule.dia': ?0 }")
    List<Group> findGroupsByScheduleDay(String day);
    
    /**
     * Busca grupos por rango de horas.
     * 
     * @param startTime Hora de inicio
     * @param endTime Hora de fin
     * @return Lista de grupos en ese rango horario
     */
    @Query("{ 'schedule.horaInicio': { $gte: ?0 }, 'schedule.horaFin': { $lte: ?1 } }")
    List<Group> findGroupsByTimeRange(String startTime, String endTime);
    
    /**
     * Busca grupos por profesor.
     * 
     * @param professorId ID del profesor
     * @return Lista de grupos del profesor
     */
    @Query("{ 'profesor.$id': ObjectId('?0') }")
    List<Group> findGroupsByProfessorId(String professorId);
    
    /**
     * Busca grupo por nombre específico.
     * 
     * @param groupName Nombre del grupo
     * @return Grupo si existe
     */
    Optional<Group> findByNombre(String groupName);
    
    /**
     * Busca grupos por aula.
     * 
     * @param classroom Aula
     * @return Lista de grupos que usan esa aula
     */
    @Query("{ 'aula': ?0 }")
    List<Group> findGroupsByClassroom(String classroom);
    
    /**
     * Busca grupos con conflictos de horario (mismo día, hora y aula).
     * 
     * @param day Día
     * @param startTime Hora inicio
     * @param endTime Hora fin
     * @param classroom Aula
     * @return Lista de grupos con posibles conflictos
     */
    @Query("{ 'schedule.dia': ?0, 'schedule.horaInicio': ?1, 'schedule.horaFin': ?2, 'aula': ?3 }")
    List<Group> findConflictingGroups(String day, String startTime, String endTime, String classroom);
}