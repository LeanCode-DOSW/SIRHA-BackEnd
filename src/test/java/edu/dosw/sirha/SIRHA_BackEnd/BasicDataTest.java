package edu.dosw.sirha.SIRHA_BackEnd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.DiasSemana;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDTO;
import edu.dosw.sirha.sirha_backend.controller.DecanateController;
import edu.dosw.sirha.sirha_backend.controller.StudentController;
import edu.dosw.sirha.sirha_backend.controller.SubjectAndGroupController;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.util.GroupMapper;
import edu.dosw.sirha.sirha_backend.util.SubjectMapper;

@SpringBootTest
class BasicDataTest {

    private static final String DECANATE_NAME = "Ingenieria de Sistemas";
    private static final String PLAN_NAME = "Ingenieria de Sistemas";
    private static final Careers PLAN_CAREER = Careers.INGENIERIA_DE_SISTEMAS;
    private static final String MATEMATICAS_NAME = "Matematicas Basicas";
    private static final String PERIOD_NAME = "2024-2";
    private static final LocalDate PERIOD_START = LocalDate.of(2025, 8, 15);
    private static final LocalDate PERIOD_END = LocalDate.of(2025, 12, 15);

    private static final String PROF_MATEMATICAS_NAME = "Dr. Garcia";
    private static final String PROF_MATEMATICAS_EMAIL = "garcia@universidad.edu";
    
    private static final String STUDENT_JUAN_USERNAME = "juan.perez";
    private static final String STUDENT_JUAN_EMAIL = "juan.perez@estudiante.edu";
    private static final String STUDENT_JUAN_CODE = "20241001";
    private static final String STUDENT_MARIA_USERNAME = "maria.gonzalez";
    private static final String STUDENT_MARIA_EMAIL = "maria.gonzalez@estudiante.edu";
    private static final String STUDENT_MARIA_CODE = "20241002";

    @Autowired
    private StudentController studentController;
    @Autowired
    private SubjectAndGroupController subjectController;
    @Autowired
    private DecanateController decanateController;

    @Test
    void setupCompleteApplicationFlow() throws SirhaException {

        AcademicPeriod periodo2025= new AcademicPeriod(PERIOD_NAME, PERIOD_START, PERIOD_END);

        Subject matematicas = new Subject(MATEMATICAS_NAME, 4);
        SubjectDTO matematicasDTO = SubjectMapper.toDTO(matematicas);
        Subject matematicasSaved = subjectController.saveSubject(matematicasDTO).getBody();
        
        Professor profMatematicas = new Professor(PROF_MATEMATICAS_NAME, PROF_MATEMATICAS_EMAIL, "hashedpassword123");

        Group mathGroup1 = new Group(matematicasSaved, 30, periodo2025);
        mathGroup1.setAula( "Aula-01");

        Group mathGroup1Saved = subjectController.saveGroup(MATEMATICAS_NAME, GroupMapper.toDTO(mathGroup1)).getBody();

        subjectController.assignProfessor(mathGroup1Saved.getId(), profMatematicas).getBody();
        subjectController.addSchedule(mathGroup1Saved.getId(), new Schedule(DiasSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0))).getBody();
        mathGroup1Saved = subjectController.addSchedule(mathGroup1Saved.getId(), new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(16, 0))).getBody();

        GroupDTO mathGrupoDTO1 = GroupMapper.toDTO(mathGroup1Saved);
        Group mathGrupo1Saved = subjectController.addGroupToSubject(MATEMATICAS_NAME, mathGrupoDTO1);




        Group mathGroup2 = new Group(matematicasSaved, 30, periodo2025);
        mathGroup2.setAula( "Aula-02");

        Group mathGroup2Saved = subjectController.saveGroup(MATEMATICAS_NAME, GroupMapper.toDTO(mathGroup2)).getBody();

        subjectController.assignProfessor(mathGroup2Saved.getId(), profMatematicas).getBody();
        subjectController.addSchedule(mathGroup2Saved.getId(), new Schedule(DiasSemana.VIERNES, LocalTime.of(8, 0), LocalTime.of(12, 0))).getBody();
        mathGroup2Saved = subjectController.addSchedule(mathGroup2Saved.getId(), new Schedule(DiasSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(18, 0))).getBody();

        GroupDTO mathGrupoDTO2 = GroupMapper.toDTO(mathGroup2Saved);
        Group mathGrupo2Saved = subjectController.addGroupToSubject(MATEMATICAS_NAME, mathGrupoDTO2);




        Decanate decanateIngenieria = new Decanate(PLAN_CAREER);
        Decanate decanateSaved = decanateController.createDecanate(PLAN_CAREER).getBody();

        StudyPlan sistemasProgram = new StudyPlan(PLAN_NAME, PLAN_CAREER);
        sistemasProgram.addSubject(matematicas);

        StudyPlan sistemasProgramSaved = decanateController.addPlanToDecanate(DECANATE_NAME, sistemasProgram).getBody().get(0);




        StudentDTO juanDTO = new StudentDTO("123", STUDENT_JUAN_USERNAME, STUDENT_JUAN_EMAIL, STUDENT_JUAN_CODE, PLAN_CAREER);
        studentController.create(juanDTO);

        StudentDTO mariaDTO = new StudentDTO("124", STUDENT_MARIA_USERNAME, STUDENT_MARIA_EMAIL, STUDENT_MARIA_CODE, PLAN_CAREER);
        studentController.create(mariaDTO);


        assertNotNull(matematicasSaved);
        assertNotNull(mathGrupo1Saved);
        assertNotNull(mathGrupo2Saved);
        assertNotNull(mathGrupo1Saved.getId());
        assertNotNull(mathGrupo2Saved.getId());
    }
}
