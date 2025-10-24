package edu.dosw.sirha.sirha_backend.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.dosw.sirha.sirha_backend.domain.model.Subject;

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