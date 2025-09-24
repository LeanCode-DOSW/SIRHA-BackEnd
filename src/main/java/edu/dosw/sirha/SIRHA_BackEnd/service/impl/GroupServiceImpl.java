package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.GroupMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupMongoRepository repository;

    public GroupServiceImpl(GroupMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Group> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Group> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Group save(Group group) {
        return repository.save(group);
    }

    @Override
    public void inscribirEstudiante(String groupId, Student student) {
        Group group = repository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        group.inscribirEstudiante(student);
        repository.save(group);
    }
}
