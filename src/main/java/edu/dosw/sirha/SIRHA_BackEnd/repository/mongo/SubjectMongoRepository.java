package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Subject.
 * Proporciona CRUD básico automáticamente gracias a MongoRepository.
 */
@Repository
public interface SubjectMongoRepository extends MongoRepository<Subject, String> {

    Optional<Subject> findByName(String name);
    boolean existsByName(String name);
    
}