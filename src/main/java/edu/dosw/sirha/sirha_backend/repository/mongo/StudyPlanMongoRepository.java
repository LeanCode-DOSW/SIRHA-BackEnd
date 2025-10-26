package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;

public interface StudyPlanMongoRepository extends MongoRepository<StudyPlan, String> {

    List<StudyPlan> findByCareer(Careers career);
    StudyPlan findStudyPlanByName(String name);
}
    