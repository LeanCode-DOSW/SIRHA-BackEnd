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

    /**
     * Busca una materia por su nombre.
     * @param name nombre de la materia
     * @return Optional de Subject si existe
     */
    Optional<Subject> findByName(String name);
}