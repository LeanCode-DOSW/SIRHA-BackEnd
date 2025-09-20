package edu.dosw.sirha.sirha_backend.repository;

import edu.dosw.sirha.sirha_backend.domain.Student;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StudentRepository {
    private final Map<Long, Student> store = new ConcurrentHashMap<>();

    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Student> findAll() {
        return new ArrayList<>(store.values());
    }

    public void save(Student student) {
        store.put(student.getId(), student);
    }

    public void clear() {
        store.clear();
    }
}
