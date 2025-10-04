package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentDTO;
import edu.dosw.sirha.SIRHA_BackEnd.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    private StudentService studentService;
    private StudentController controller;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        controller = new StudentController(studentService);
    }

    @Test
    void getAll_ShouldReturnDTOList() {
        Student s = new Student("1", "juan", "juan@mail.com", "pass", "2021");
        when(studentService.findAll()).thenReturn(List.of(s));

        List<StudentDTO> result = controller.getAll();

        assertEquals(1, result.size());
        assertEquals("juan", result.get(0).getUsername());
    }

    @Test
    void getById_ShouldReturnDTO_WhenExists() {
        Student s = new Student("2", "maria", "maria@mail.com", "pass", "2022");
        when(studentService.findById("2")).thenReturn(Optional.of(s));

        StudentDTO result = controller.getById("2");

        assertEquals("maria", result.getUsername());
    }

    @Test
    void getById_ShouldThrow_WhenNotFound() {
        when(studentService.findById("x")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> controller.getById("x"));
    }

    @Test
    void create_ShouldReturnSavedDTO() {
        StudentDTO dto = new StudentDTO("3", "carlos", "2023", List.of());
        Student s = new Student("3", "carlos", "carlos@esuelaing.edu.com", "defaultPass", "2023");
        when(studentService.save(any(Student.class))).thenReturn(s);

        StudentDTO result = controller.create(dto);

        assertEquals("carlos", result.getUsername());
        assertEquals("2023", result.getCodigo());
    }
}
