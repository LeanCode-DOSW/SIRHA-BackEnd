package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;

public interface AcademicPeriodMongoRepository extends MongoRepository<AcademicPeriod, String> {
    Optional<AcademicPeriod> findByPeriod(String period);

    List<AcademicPeriod> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate start, LocalDate end);

    default Optional<AcademicPeriod> findCurrentAcademicPeriodOptional() {
        var now = LocalDate.now();
        return findByStartDateLessThanEqualAndEndDateGreaterThanEqual(now, now).stream().findFirst();
    }

    boolean existsByPeriod(String period);
    void deleteByPeriod(String period);
}
