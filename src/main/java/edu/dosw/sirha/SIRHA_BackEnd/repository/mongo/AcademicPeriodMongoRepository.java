package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.AcademicPeriod;

public interface AcademicPeriodMongoRepository extends MongoRepository<AcademicPeriod, String> {
    Optional<AcademicPeriod> findByPeriod(String period);

}
