package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
public interface GroupMongoRepository extends MongoRepository<Group, Integer> {
    Optional<Group> findByCode(String code);
    
}
