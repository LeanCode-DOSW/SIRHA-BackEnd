package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
public interface GroupMongoRepository extends MongoRepository<Group, String> {
    Optional<Group> findByCode(String code);
}
