package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.sirha_backend.domain.model.Account;

public interface AccountMongoRepository extends MongoRepository<Account, String> {
    Optional<Account> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
