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
    
    @Query("{ 'academicProgress.subjects': { $elemMatch: { 'subject.$id': ObjectId('?0'), 'estado': 'EN_CURSO' } } }")
    List<Student> findStudentsTakingSubject(String subjectId);

    @Query("{ 'semestreActual': ?0 }")
    List<Student> findStudentsBySemester(int semester);

    @Query("{ 'academicProgress.subjects': { $elemMatch: { 'grupo.schedule.dia': ?0, 'estado': 'EN_CURSO' } } }")
    List<Student> findStudentsWithClassesOnDay(String day);
    
    @Query("{ 'academicProgress.subjects': { $elemMatch: { 'grupo.profesor.$id': ObjectId('?0'), 'estado': 'EN_CURSO' } } }")
    List<Student> findStudentsByProfessor(String professorId);
    
    @Query("{ 'studyPlan.$id': ObjectId('?0') }")
    List<Student> findStudentsByStudyPlan(String studyPlanId);
    
    @Query("{ $expr: { $and: [" +
           "{ $gte: [{ $sum: '$academicProgress.subjects.creditos' }, ?0] }," +
           "{ $lte: [{ $sum: '$academicProgress.subjects.creditos' }, ?1] }" +
           "] } }")
    List<Student> findStudentsByCreditRange(int minCredits, int maxCredits);

    
}