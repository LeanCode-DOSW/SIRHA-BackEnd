package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupControllerTest {

    private GroupService groupService;
    private GroupController controller;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        controller = new GroupController(groupService);
    }

    @Test
    void getAll_ShouldReturnGroups() {
        Group g = new Group("1", "Grupo A");
        when(groupService.findAll()).thenReturn(List.of(g));

        List<Group> result = controller.getAll();

        assertEquals(1, result.size());
        assertEquals("Grupo A", result.get(0).getName());
    }

    @Test
    void create_ShouldReturnSavedGroup() {
        Group g = new Group("2", "Grupo B");
        when(groupService.save(g)).thenReturn(g);

        Group result = controller.create(g);

        assertEquals("Grupo B", result.getName());
    }

    @Test
    void enrollStudent_ShouldInvokeService() {
        Student student = new Student();
        controller.enrollStudent("1", student);

        verify(groupService).enrollStudent("1", student);
    }
}
+