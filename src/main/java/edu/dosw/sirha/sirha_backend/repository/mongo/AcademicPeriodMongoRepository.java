package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;

public interface AcademicPeriodMongoRepository extends MongoRepository<AcademicPeriod, String> {
    Optional<AcademicPeriod> findByPeriod(String period);

}
