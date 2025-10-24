package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import edu.dosw.sirha.sirha_backend.domain.model.Student;

public interface StudentMongoRepository extends MongoRepository<Student, String> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    Optional<Student> findById(String id);
    Optional<Student> findStudentByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    
    @Query("{ 'materias': { $elemMatch: { 'subject.id': ?0, 'state': 'EN_CURSO' } } }")
    List<Student> findStudentsTakingSubject(String subjectId);

    

    @Query("{ 'materias': { $elemMatch: { " +
           "'group.schedules': { $elemMatch: { 'day': ?0 } } " +
           "} } }")
    List<Student> findStudentsWithClassesOnDay(String day);
    
    @Query("{ 'materias': { $elemMatch: { " +
           "'group.profesor.id': ?0 " +
           "} } }")
    List<Student> findStudentsByProfessor(String professorId);
    
}