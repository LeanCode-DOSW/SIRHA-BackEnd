package edu.dosw.sirha.sirha_backend.util;


import edu.dosw.sirha.sirha_backend.domain.Group;
import edu.dosw.sirha.sirha_backend.domain.Student;
import edu.dosw.sirha.sirha_backend.repository.GroupRepository;
import edu.dosw.sirha.sirha_backend.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public DataLoader(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        studentRepository.clear();
        groupRepository.clear();

        Student s1 = new Student(1L, "Juan Perez", "Ingenieria", 4,
                List.of("Matematicas I", "Programacion I"));
        Student s2 = new Student(2L, "Ana Gomez", "Sistemas", 2,
                List.of("Introduccion a S/W", "Calculo"));

        studentRepository.save(s1);
        studentRepository.save(s2);

        Group g1 = new Group(10L, "Matematicas", "Dr. Ruiz", 30, "Lun 8-10", 25);
        Group g2 = new Group(11L, "Matematicas", "M. Perez", 25, "Mar 10-12", 20);
        Group g3 = new Group(20L, "Programacion", "Ing. Torres", 40, "Mie 14-16", 38);

        groupRepository.save(g1);
        groupRepository.save(g2);
        groupRepository.save(g3);
    }
}

