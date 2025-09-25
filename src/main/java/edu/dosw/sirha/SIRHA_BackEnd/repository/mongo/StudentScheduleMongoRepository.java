package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;

/**
 * Repositorio MongoDB para consultas específicas de horarios de estudiantes.
 * 
 * Extiende las funcionalidades básicas del StudentMongoRepository
 * con consultas optimizadas para horarios y programación académica.
 */
public interface StudentScheduleMongoRepository extends MongoRepository<Student, String> {
    
    /**
     * Busca estudiantes que están cursando una materia específica.
     * 
     * @param subjectId ID de la materia
     * @return Lista de estudiantes cursando la materia
     */
    @Query("{ 'academicProgress.subjects': { $elemMatch: { 'subject.$id': ObjectId('?0'), 'estado': 'EN_CURSO' } } }")
    List<Student> findStudentsTakingSubject(String subjectId);
    
    /**
     * Busca estudiantes por semestre actual.
     * 
     * @param semester Número del semestre
     * @return Lista de estudiantes en ese semestre
     */
    @Query("{ 'semestreActual': ?0 }")
    List<Student> findStudentsBySemester(int semester);
    
    /**
     * Busca estudiantes que tienen materias en un día específico.
     * 
     * @param day Día de la semana
     * @return Lista de estudiantes con clases ese día
     */
    @Query("{ 'academicProgress.subjects': { $elemMatch: { 'grupo.schedule.dia': ?0, 'estado': 'EN_CURSO' } } }")
    List<Student> findStudentsWithClassesOnDay(String day);
    
    /**
     * Busca estudiantes con conflictos de horario.
     * 
     * @return Lista de estudiantes que tienen conflictos
     */
    @Query("{ $expr: { $gt: [{ $size: '$conflictosHorario' }, 0] } }")
    List<Student> findStudentsWithScheduleConflicts();
    
    /**
     * Busca estudiantes que cursan materias con un profesor específico.
     * 
     * @param professorId ID del profesor
     * @return Lista de estudiantes del profesor
     */
    @Query("{ 'academicProgress.subjects': { $elemMatch: { 'grupo.profesor.$id': ObjectId('?0'), 'estado': 'EN_CURSO' } } }")
    List<Student> findStudentsByProfessor(String professorId);
    
    /**
     * Busca estudiantes por plan de estudios.
     * 
     * @param studyPlanId ID del plan de estudios
     * @return Lista de estudiantes del plan
     */
    @Query("{ 'studyPlan.$id': ObjectId('?0') }")
    List<Student> findStudentsByStudyPlan(String studyPlanId);
    
    /**
     * Busca estudiantes con materias en un rango de créditos.
     * 
     * @param minCredits Créditos mínimos
     * @param maxCredits Créditos máximos
     * @return Lista de estudiantes en el rango de créditos
     */
    @Query("{ $expr: { $and: [" +
           "{ $gte: [{ $sum: '$academicProgress.subjects.creditos' }, ?0] }," +
           "{ $lte: [{ $sum: '$academicProgress.subjects.creditos' }, ?1] }" +
           "] } }")
    List<Student> findStudentsByCreditRange(int minCredits, int maxCredits);
    
    /**
     * Busca un estudiante específico con sus materias actuales populadas.
     * 
     * @param studentId ID del estudiante
     * @return Estudiante con referencias pobladas
     */
    @Query("{ '_id': ObjectId('?0') }")
    Optional<Student> findStudentWithCurrentSubjects(String studentId);
}