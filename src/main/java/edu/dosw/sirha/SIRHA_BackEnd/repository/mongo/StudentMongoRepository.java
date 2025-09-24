package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;

public interface StudentMongoRepository extends MongoRepository<Student, String> {
}