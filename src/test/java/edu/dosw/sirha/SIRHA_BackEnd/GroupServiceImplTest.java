package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.GroupMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.SubjectMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupMongoRepository groupRepository;

    @Mock
    private SubjectMongoRepository subjectRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    // ---------- Materias ----------
    @Test
    void registrarMateria_debeGuardarEnRepositorio() {
        Subject subject = new Subject("S1", "Matemáticas", 3);
        when(subjectRepository.save(subject)).thenReturn(subject);

        Subject result = groupService.registrarMateria(subject);

        assertEquals(subject, result);
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    void listarMaterias_retornaTodas() {
        List<Subject> materias = Arrays.asList(
                new Subject("S1", "Matemáticas", 3),
                new Subject("S2", "Historia", 2)
        );
        when(subjectRepository.findAll()).thenReturn(materias);

        List<Subject> result = groupService.listarMaterias();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(materias));
    }



    @Test
    void listarGruposPorMateria_ok() {
        Subject subject = new Subject("S1", "Matemáticas", 3);
        Group g1 = new Group(20);
        Group g2 = new Group(40);
        subject.addGrupo(g1);
        subject.addGrupo(g2);

        when(subjectRepository.findById("S1")).thenReturn(Optional.of(subject));

        List<Group> result = groupService.listarGruposPorMateria("S1");

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(g1, g2)));
    }

    @Test
    void actualizarGrupo_existenteOk() {
        Group group = new Group(30);

        when(groupRepository.existsById(group.getId())).thenReturn(true);
        when(groupRepository.save(group)).thenReturn(group);

        Group result = groupService.actualizarGrupo(group);

        assertEquals(group, result);
        verify(groupRepository).save(group);
    }

    @Test
    void actualizarGrupo_inexistenteLanzaExcepcion() {
        Group group = new Group(30);

        when(groupRepository.existsById(group.getId())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> groupService.actualizarGrupo(group));
    }

    @Test
    void eliminarGrupo_existente() {
        when(groupRepository.existsById("G1")).thenReturn(true);

        groupService.eliminarGrupo("G1");

        verify(groupRepository).deleteById("G1");
    }

    @Test
    void eliminarGrupo_inexistenteLanzaExcepcion() {
        when(groupRepository.existsById("G1")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> groupService.eliminarGrupo("G1"));
    }

    // ---------- Profesores ----------
    @Test
    void asignarProfesor_ok() {
        Group group = new Group(30);
        Professor professor = new Professor("P1", "juanperez", "hashedPass123", "PROFESOR", "Lunes 8-10");

        when(groupRepository.findById("G1")).thenReturn(Optional.of(group));
        when(groupRepository.save(group)).thenReturn(group);

        Group result = groupService.asignarProfesor("G1", professor);

        assertEquals(professor, result.getProfesor());
        verify(groupRepository).save(group);
    }

    @Test
    void getProfesor_ok() {
        Professor professor = new Professor("P1", "maria", "hashedPass456", "PROFESOR", "Martes 14-16");
        Group group = new Group(25);
        group.setProfesor(professor);

        when(groupRepository.findById("G1")).thenReturn(Optional.of(group));

        Professor result = groupService.getProfesor("G1");

        assertEquals(professor, result);
    }

    // ---------- Horarios ----------
//    @Test
//    void agregarHorario_ok() {
//        Group group = new Group(20);
//        group.setId("G1");
//        Schedule horario = new Schedule("Lunes", 8, 10);
//        group.addHorario(horario);
//
//        when(groupRepository.findById("G1")).thenReturn(Optional.of(group));
//        when(groupRepository.save(group)).thenReturn(group);
//
//        Schedule schedule = new Schedule("Lunes", 8, 10);
//        Group result = groupService.agregarHorario("G1", schedule);
//
//        assertTrue(result.getHorarios().contains(schedule));
//        verify(groupRepository).save(group);
//    }
//
//    @Test
//    void getHorarios_ok() {
//        Group group = new Group(40);
//        group.setId("G1");
//        Schedule horario = new Schedule("Martes", 12, 14);
//        group.addHorario(horario);
//
//        when(groupRepository.findById("G1")).thenReturn(Optional.of(group));
//
//        List<Schedule> result = groupService.getHorarios("G1");
//
//        assertEquals(1, result.size());
//    }

    // ---------- Capacidad ----------
    @Test
    void estaLleno_trueCuandoNoHayCupos() {
        Group group = new Group(30);
        for (int i = 0; i<30; i++){
            Student s = new Student();
            group.addEstudiante(s);
        }

        when(groupRepository.findById("G1")).thenReturn(Optional.of(group));

        assertTrue(groupService.estaLleno("G1"));
    }

    @Test
    void getCuposDisponibles_ok() {
        Group group = new Group(30);


        when(groupRepository.findById("G1")).thenReturn(Optional.of(group));

        assertEquals(30, groupService.getCuposDisponibles("G1"));
    }

    // ---------- Estado ----------
    @Test
    void cerrarGrupo_cambiaEstado() {
        Group group = new Group(30);

        when(groupRepository.findById("G1")).thenReturn(Optional.of(group));
        when(groupRepository.save(group)).thenReturn(group);

        Group result = groupService.cerrarGrupo("G1");

        assertTrue(result.getEstadoGrupo() instanceof StatusClosed);
    }

    @Test
    void abrirGrupo_cambiaEstado() {
        Group group = new Group(30);

        when(groupRepository.findById("G1")).thenReturn(Optional.of(group));
        when(groupRepository.save(group)).thenReturn(group);

        Group result = groupService.abrirGrupo("G1");

        assertTrue(result.getEstadoGrupo() instanceof StatusOpen);
    }
}
