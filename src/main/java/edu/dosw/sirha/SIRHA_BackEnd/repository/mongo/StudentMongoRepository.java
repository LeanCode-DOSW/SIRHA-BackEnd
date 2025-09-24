package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;

public interface StudentMongoRepository extends MongoRepository<Student, String> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    Optional<Student> findById(String id);
    Optional<Student> findStudentByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}