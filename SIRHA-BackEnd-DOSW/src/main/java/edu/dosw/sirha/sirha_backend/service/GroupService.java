package edu.dosw.sirha.sirha_backend.service;

import edu.dosw.sirha.sirha_backend.domain.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<GroupDTO> findGroupsBySubject(String subject) {
        List<Group> groups = groupRepository.findBySubject(subject);
        return groups.stream()
                .map(g -> new GroupDTO(
                        g.getId(),
                        g.getSubject(),
                        g.getProfessor(),
                        Math.max(0, g.getCapacity() - g.getEnrolled())
                ))
                .collect(Collectors.toList());
    }
}
