package edu.dosw.sirha.sirha_backend.service;

import java.util.List;
import java.util.Optional;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;

public interface AcademicPeriodService {
    Optional<AcademicPeriod> getCurrentAcademicPeriod();
    AcademicPeriod saveAcademicPeriod(AcademicPeriod academicPeriod);
    boolean existsAcademicPeriod(String name);
    Optional<AcademicPeriod> findByPeriod(String period);
    Optional<AcademicPeriod> findById(String id);
    List<AcademicPeriod> findAll();
    void deleteAcademicPeriodById(String id);

}
