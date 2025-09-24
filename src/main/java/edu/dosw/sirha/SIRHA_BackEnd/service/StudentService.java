package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;
import java.util.Optional;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;

public interface StudentService {
    List<Student> findAll();
    Optional<Student> findById(String id);
    Student save(Student student);
}
