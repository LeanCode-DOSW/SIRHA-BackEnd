package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@ActiveProfiles("test")
class AccountMongoRepositoryTest {

    @Autowired
    private edu.dosw.sirha.sirha_backend.repository.mongo.AccountMongoRepository repo;

    @Test
    void findByUsername_ShouldReturnAccount() {
        edu.dosw.sirha.sirha_backend.domain.model.Account acc = new edu.dosw.sirha.sirha_backend.domain.model.Account("user", "mail@mail.com", "123", edu.dosw.sirha.sirha_backend.domain.model.enums.Role.STUDENT);
        repo.save(acc);

        var found = repo.findByUsername("user");
        assertTrue(found.isPresent());
    }
}

