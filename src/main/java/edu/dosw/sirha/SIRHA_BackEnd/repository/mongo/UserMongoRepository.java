package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.User;
import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}