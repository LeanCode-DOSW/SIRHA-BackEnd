package edu.dosw.sirha.SIRHA_BackEnd.service;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.AcademicPeriod;

public interface AcademicPeriodService {
    AcademicPeriod getCurrentAcademicPeriod();
    AcademicPeriod saveAcademicPeriod(AcademicPeriod academicPeriod);
    boolean existsAcademicPeriod(String name);
    void deleteAcademicPeriod(String name);
    AcademicPeriod findByPeriod(String period);
    AcademicPeriod findById(Integer id);
    Iterable<AcademicPeriod> findAll();

}
