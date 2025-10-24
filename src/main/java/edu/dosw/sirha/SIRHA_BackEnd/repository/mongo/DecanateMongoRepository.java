package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import edu.dosw.sirha.sirha_backend.domain.model.Decanate;

public interface DecanateMongoRepository extends MongoRepository<Decanate, String> {
    Optional<Decanate> findByName(String name);
}
