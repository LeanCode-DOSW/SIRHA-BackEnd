package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.dto.ScheduleDTO;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentScheduleDTO;
import edu.dosw.sirha.SIRHA_BackEnd.service.ScheduleService;
import edu.dosw.sirha.SIRHA_BackEnd.service.StudentService;
import edu.dosw.sirha.SIRHA_BackEnd.service.GroupService;
import edu.dosw.sirha.SIRHA_BackEnd.util.ScheduleMapperUtils;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.ScheduleMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.StudentScheduleMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.SubjectScheduleMongoRepository;

/**
 * Implementación del servicio de horarios para estudiantes.
 * 
 * Gestiona la consulta de horarios actuales e históricos,
 * detecta conflictos y proporciona información detallada
 * sobre la programación académica del estudiante.
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    private static final String NO_ASIGNADO = "Sin asignar";
        
    private final StudentService studentService;
    private final GroupService groupService;
    private final ScheduleMongoRepository scheduleRepository;
    private final StudentScheduleMongoRepository studentScheduleRepository;
    private final SubjectScheduleMongoRepository subjectScheduleRepository;
    
    public ScheduleServiceImpl(StudentService studentService, 
                              GroupService groupService,
                              ScheduleMongoRepository scheduleRepository,
                              StudentScheduleMongoRepository studentScheduleRepository,
                              SubjectScheduleMongoRepository subjectScheduleRepository) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.scheduleRepository = scheduleRepository;
        this.studentScheduleRepository = studentScheduleRepository;
        this.subjectScheduleRepository = subjectScheduleRepository;
    }
    
    @Override
    public Optional<StudentScheduleDTO> getHorarioActual(String studentId) {
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Student student = studentOpt.get();
        List<SubjectDecorator> materiasCursando = student.getMateriasCursando();
        
        List<ScheduleDTO> horarios = ScheduleMapperUtils.toDTOList(materiasCursando);
        
        int totalCreditos = materiasCursando.stream()
            .mapToInt(SubjectDecorator::getCreditos)
            .sum();
        
        StudentScheduleDTO scheduleDTO = new StudentScheduleDTO(
            student.getId(),
            student.getUsername(),
            student.getCodigo(),
            student.getSemestreActual(),
            horarios,
            totalCreditos
        );
        
        scheduleDTO.setSemestreAcademico("2024-2"); // Esto debería venir de configuración        
        return Optional.of(scheduleDTO);
    }
    
    @Override
    public Optional<StudentScheduleDTO> getHorarioPorSemestre(String studentId, int semestre) {
        
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Student student = studentOpt.get();
        List<SubjectDecorator> materiasSemestre = student.getMateriasPorSemestre(semestre);
        
        List<ScheduleDTO> horarios = ScheduleMapperUtils.toDTOList(materiasSemestre);
        
        int totalCreditos = materiasSemestre.stream()
            .mapToInt(SubjectDecorator::getCreditos)
            .sum();
        
        StudentScheduleDTO scheduleDTO = new StudentScheduleDTO(
            student.getId(),
            student.getUsername(),
            student.getCodigo(),
            semestre,
            horarios,
            totalCreditos
        );
        
        
        return Optional.of(scheduleDTO);
    }
    
    @Override
    public List<StudentScheduleDTO> getHistorialHorarios(String studentId) {
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isEmpty()) {
            return Collections.emptyList();
        }
        
        Student student = studentOpt.get();
        
        // Usar el método mejorado del estudiante
        List<Integer> semestres = student.getSemestresHistoricos();
        
        List<StudentScheduleDTO> historial = semestres.stream()
            .map(semestre -> getHorarioPorSemestre(studentId, semestre))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        return historial;
    }
    
    @Override
    public List<ScheduleDTO> getHorariosMateriasEnCurso(String studentId) {        
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isEmpty()) {
            return Collections.emptyList();
        }
        
        Student student = studentOpt.get();
        
        return student.getMateriasCursando().stream()
            .filter(materia -> materia.getGroup() != null)
            .flatMap(materia -> materia.getGroup().getHorarios().stream()
                .map(schedule -> convertToScheduleDTO(schedule, materia)))
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean tieneConflictoHorario(String studentId, String grupoId) {
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isEmpty()) {
            return false;
        }
        
        Group nuevoGrupo = groupService.findById(grupoId);
        if (nuevoGrupo == null) {
            return false;
        }
        
        boolean tieneConflicto = studentOpt.get().tieneConflictoConHorario(nuevoGrupo);        
        return tieneConflicto;
    }
    
    @Override
    public List<ScheduleDTO> getHorarioGrupo(String grupoId) {        
        Group grupo = groupService.findById(grupoId);
        if (grupo == null) {
            return Collections.emptyList();
        }
        
        return grupo.getHorarios().stream()
            .map(schedule -> convertToScheduleDTO(schedule, grupo))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getGruposDisponibles(String studentId, String materiaId) {        
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isEmpty()) {
            return Collections.emptyList();
        }
        
        logger.info("Buscando grupos disponibles para materia {} y estudiante {}", materiaId, studentId);
        
        // Buscar grupos específicos de la materia usando el repositorio optimizado
        List<Group> gruposDeMateria = scheduleRepository.findAvailableGroupsBySubjectId(materiaId);
        
        return gruposDeMateria.stream()
            .filter(grupo -> !studentOpt.get().tieneConflictoConHorario(grupo))
            .filter(grupo -> !groupService.estaLleno(grupo.getId()))
            .map(Group::getId)
            .toList();
    }
    
    /**
     * Convierte un Schedule y SubjectDecorator a ScheduleDTO.
     */
    private ScheduleDTO convertToScheduleDTO(Schedule schedule, SubjectDecorator materia) {
        Group grupo = materia.getGroup();
        
        return new ScheduleDTO(
            schedule.getDia(),
            schedule.getHoraInicio(),
            schedule.getHoraFin(),
            materia.getSubject().getId(),
            materia.getName(),
            grupo != null && grupo.getProfesor() != null ? grupo.getProfesor().getUsername() : NO_ASIGNADO,
            grupo != null ? grupo.getAula() : NO_ASIGNADO,
            grupo != null ? grupo.getId() : null,
            materia.getCreditos()
        );
    }
    
    /**
     * Convierte un Schedule y Group a ScheduleDTO.
     */
    private ScheduleDTO convertToScheduleDTO(Schedule schedule, Group grupo) {
        return new ScheduleDTO(
            schedule.getDia(),
            schedule.getHoraInicio(),
            schedule.getHoraFin(),
            grupo.getCurso() != null ? grupo.getCurso().getId() : null,
            grupo.getCurso() != null ? grupo.getCurso().getName() : "Sin materia",
            grupo.getProfesor() != null ? grupo.getProfesor().getUsername() : NO_ASIGNADO,
            grupo.getAula() != null ? grupo.getAula() : NO_ASIGNADO,
            grupo.getId(),
            grupo.getCurso() != null ? grupo.getCurso().getCreditos() : 0
        );
    }
}