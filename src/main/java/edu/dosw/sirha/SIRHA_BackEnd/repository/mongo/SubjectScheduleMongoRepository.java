package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;

/**
 * Repositorio MongoDB para consultas específicas de materias relacionadas con horarios.
 * 
 * Extiende las funcionalidades básicas del SubjectMongoRepository
 * con consultas optimizadas para horarios y programación académica.
 */
public interface SubjectScheduleMongoRepository extends MongoRepository<Subject, String> {
    
    /**
     * Busca materias por semestre.
     * 
     * @param semester Número del semestre
     * @return Lista de materias del semestre
     */
    @Query("{ 'semestre': ?0 }")
    List<Subject> findSubjectsBySemester(int semester);
    
    /**
     * Busca materias por código.
     * 
     * @param code Código de la materia
     * @return Materia si existe
     */
    Optional<Subject> findByCodigo(String code);
    
    /**
     * Busca materias por nombre (búsqueda parcial).
     * 
     * @param name Nombre o parte del nombre
     * @return Lista de materias que coinciden
     */
    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    List<Subject> findSubjectsByNameContaining(String name);
    
    /**
     * Busca materias por rango de créditos.
     * 
     * @param minCredits Créditos mínimos
     * @param maxCredits Créditos máximos
     * @return Lista de materias en el rango
     */
    @Query("{ 'creditos': { $gte: ?0, $lte: ?1 } }")
    List<Subject> findSubjectsByCreditRange(int minCredits, int maxCredits);
    
    /**
     * Busca materias prerrequisito de una materia específica.
     * 
     * @param subjectId ID de la materia
     * @return Lista de materias prerrequisito
     */
    @Query("{ '_id': { $in: ?0 } }")
    List<Subject> findPrerequisiteSubjects(List<String> prerequisiteIds);
    
    /**
     * Busca materias que tienen una materia específica como prerrequisito.
     * 
     * @param prerequisiteId ID de la materia prerrequisito
     * @return Lista de materias que la requieren
     */
    @Query("{ 'prerrequisitos': { $in: [ObjectId('?0')] } }")
    List<Subject> findSubjectsRequiringPrerequisite(String prerequisiteId);
    
    /**
     * Busca materias por plan de estudios.
     * 
     * @param studyPlanId ID del plan de estudios
     * @return Lista de materias del plan
     */
    @Query("{ 'studyPlan.$id': ObjectId('?0') }")
    List<Subject> findSubjectsByStudyPlan(String studyPlanId);
    
    /**
     * Busca materias electivas.
     * 
     * @return Lista de materias electivas
     */
    @Query("{ 'esElectiva': true }")
    List<Subject> findElectiveSubjects();
    
    /**
     * Busca materias obligatorias por semestre.
     * 
     * @param semester Número del semestre
     * @return Lista de materias obligatorias del semestre
     */
    @Query("{ 'semestre': ?0, 'esElectiva': { $ne: true } }")
    List<Subject> findMandatorySubjectsBySemester(int semester);
    
    /**
     * Busca materias disponibles para inscripción en un semestre específico.
     * 
     * @param semester Semestre objetivo
     * @param completedSubjectIds IDs de materias ya completadas
     * @return Lista de materias disponibles
     */
    @Query("{ $and: [" +
           "{ 'semestre': { $lte: ?0 } }," +
           "{ $or: [" +
           "{ 'prerrequisitos': { $size: 0 } }," +
           "{ 'prerrequisitos': { $all: ?1 } }" +
           "] }" +
           "] }")
    List<Subject> findAvailableSubjectsForSemester(int semester, List<String> completedSubjectIds);
}