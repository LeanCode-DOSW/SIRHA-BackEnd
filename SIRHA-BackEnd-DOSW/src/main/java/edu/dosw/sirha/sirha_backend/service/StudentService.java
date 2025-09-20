package edu.dosw.sirha.sirha_backend.service;



import edu.dosw.sirha.sirha_backend.domain.AcademicStatus;
import edu.dosw.sirha.sirha_backend.domain.Student;
import edu.dosw.sirha.sirha_backend.dto.StudentScheduleDTO;
import edu.dosw.sirha.sirha_backend.exception.NotFoundException;
import edu.dosw.sirha.sirha_backend.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentScheduleDTO getSchedule(Long id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id " + id));
        return new StudentScheduleDTO(s.getId(), s.getSubjects());
    }

    public AcademicStatus getAcademicStatus(Long id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id " + id));

        // Regla mock: semester >=4 -> VERDE, semester 2-3 -> AZUL, else ROJO
        int sem = s.getSemester();
        if (sem >= 4) return AcademicStatus.VERDE;
        if (sem >= 2) return AcademicStatus.AZUL;
        return AcademicStatus.ROJO;
    }
}
