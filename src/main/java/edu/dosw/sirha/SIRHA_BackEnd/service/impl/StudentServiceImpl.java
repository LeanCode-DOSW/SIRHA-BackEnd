package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.StudentMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentMongoRepository repository;

    public StudentServiceImpl(StudentMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Student> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Student save(Student student) {
        return repository.save(student);
    }
}