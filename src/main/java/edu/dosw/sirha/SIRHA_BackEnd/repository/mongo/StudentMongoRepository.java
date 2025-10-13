package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.sirha_backend.domain.model.Student;

public interface StudentMongoRepository extends MongoRepository<Student, String> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    Optional<Student> findById(String id);
    Optional<Student> findStudentByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    //List<Student> findStudentsTakingSubject(String subjectId);

    //List<Student> findStudentsBySemester(int semester);

    //List<Student> findStudentsWithClassesOnDay(String day);
    
    //List<Student> findStudentsByProfessor(String professorId);
    
    //List<Student> findStudentsByStudyPlan(String studyPlanId);
    
    //List<Student> findStudentsByCreditRange(int minCredits, int maxCredits);
}