package edu.dosw.sirha.sirha_backend.repository;


import edu.dosw.sirha.sirha_backend.domain.Group;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class GroupRepository {
    private final Map<Long, Group> store = new ConcurrentHashMap<>();

    public Optional<Group> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Group> findAll() {
        return new ArrayList<>(store.values());
    }

    public List<Group> findBySubject(String subject) {
        return store.values()
                .stream()
                .filter(g -> g.getSubject() != null && g.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }

    public void save(Group group) {
        store.put(group.getId(), group);
    }

    public void clear() {
        store.clear();
    }
}

