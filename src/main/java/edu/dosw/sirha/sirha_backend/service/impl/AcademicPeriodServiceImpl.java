package edu.dosw.sirha.sirha_backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.repository.mongo.AcademicPeriodMongoRepository;
import edu.dosw.sirha.sirha_backend.service.AcademicPeriodService;

@Service
public class AcademicPeriodServiceImpl implements AcademicPeriodService {

    private static final Logger log = LoggerFactory.getLogger(AcademicPeriodServiceImpl.class);

    private final AcademicPeriodMongoRepository academicPeriodRepository;

    public AcademicPeriodServiceImpl(AcademicPeriodMongoRepository academicPeriodRepository) {
        this.academicPeriodRepository = academicPeriodRepository;
        }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcademicPeriod> getCurrentAcademicPeriod() {
        log.debug("Fetching current academic period");
        return academicPeriodRepository.findCurrentAcademicPeriodOptional();
    }

    @Override
    @Transactional
    public AcademicPeriod saveAcademicPeriod(AcademicPeriod ap) {
        log.info("Saving academic period: {}", ap.getPeriod());
        return academicPeriodRepository.save(ap);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsAcademicPeriod(String period) {
        return academicPeriodRepository.existsByPeriod(period);
    }

    @Override
    @Transactional
    public void deleteAcademicPeriodById(String id) {
        log.info("Deleting academic period by id: {}", id);
        academicPeriodRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcademicPeriod> findByPeriod(String period) {
        return academicPeriodRepository.findByPeriod(period);
    }

    @Override
    public Optional<AcademicPeriod> findById(String id) {
        return academicPeriodRepository.findById(id);
    }

    @Override
    public List<AcademicPeriod> findAll() {
        return academicPeriodRepository.findAll();
    }
}
