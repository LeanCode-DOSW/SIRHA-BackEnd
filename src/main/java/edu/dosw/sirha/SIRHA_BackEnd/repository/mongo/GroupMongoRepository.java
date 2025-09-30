package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
public interface GroupMongoRepository extends MongoRepository<Group, Integer> {
}
