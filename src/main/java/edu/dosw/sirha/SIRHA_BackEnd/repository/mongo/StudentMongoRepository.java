package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.User;

public interface StudentMongoRepository extends MongoRepository<Student, String> {
        Optional<Student> findStudentByCodigo(String codigo);
}