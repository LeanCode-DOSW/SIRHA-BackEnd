package edu.dosw.sirha.sirha_backend.domain.port;

import java.util.List;
import java.util.Map;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec.SubjectDecorator;
import edu.dosw.sirha.sirha_backend.dto.AcademicIndicatorsDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDecoratorDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

import java.util.Collection;

/**
 * Interface que define el contrato para el progreso académico de un estudiante.
 * 
 * Esta interface sigue el principio de Segregación de Interfaces (ISP) 
 * separando las responsabilidades de gestión de progreso académico.
 * 
 * También implementa el principio de Inversión de Dependencias (DIP)
 * permitiendo que las clases dependan de abstracciones en lugar de concreciones.
 */
public interface AcademicProgress {
    
    /**
     * Obtiene todas las materias del semáforo académico.
     * @return colección de materias decoradas
     */
    Collection<SubjectDecorator> getSubjects();
    
    /**
     * Obtiene las materias aprobadas (color verde).
     * @return lista de materias aprobadas
     */
    List<SubjectDecorator> getPassedSubjects();
    
    /**
     * Obtiene las materias que se están cursando actualmente (color amarillo).
     * @return lista de materias en curso
     */
    List<SubjectDecorator> getSubjectsInProgress();
    
    /**
     * Obtiene las materias reprobadas (color rojo).
     * @return lista de materias reprobadas
     */
    List<SubjectDecorator> getFailedSubjects();
    
    /**
     * Obtiene las materias no cursadas (color gris).
     * @return lista de materias no cursadas
     */
    List<SubjectDecorator> getSubjectsNotTaken();
    
    /**
     * Calcula el total de créditos por color de semáforo.
     * @param color el color del semáforo a filtrar
     * @return total de créditos
     */
    int getCreditsByColor(SemaforoColores color);
    

    int getSubjectsByColorCount(SemaforoColores color);
    /**
     * Obtiene las materias de un semestre específico.
     * @param semestre el semestre a consultar
     * @return lista de materias del semestre
     */
    List<SubjectDecorator> getSubjectsBySemester(int semestre);
    
    /**
     * Obtiene contadores por estado académico.
     * @return array con [aprobadas, cursando, reprobadas, noCursadas]
     */
    int[] getContadoresPorEstado();

    boolean hasSubject(Subject subject);

    boolean isSubjectApproved(Subject subject);
    boolean isSubjectCursando(Subject subject);
    boolean isSubjectReprobada(Subject subject);
    boolean isSubjectNoCursada(Subject subject);

    int getTotalSubjectsCount();
    int getPassedSubjectsCount();
    int getSubjectsInProgressCount();
    int getFailedSubjectsCount();
    int getSubjectsNotTakenCount();

    StudyPlan getStudyPlan();
    Map<AcademicPeriod, List<Schedule>> getAllSchedules();
    AcademicPeriod getCurrentAcademicPeriod();
    void setCurrentAcademicPeriod(AcademicPeriod period);

    Map<SemaforoColores, List<SubjectDecoratorDTO>> getAcademicPensum();

    Careers getCareer();
    Map<SemaforoColores, Double> getPercentageByColor();

    double getOverallProgressPercentage();
    double getAcademicSuccessRate();
    double getCompletedCreditsPercentage();

    AcademicIndicatorsDTO getAcademicIndicators();
    
    int getCreditsStudyPlan();
    boolean verifyChangeGroup(Subject subject, Group newGroup);
    void enrollSubjectInGroup(Subject subject, Group group);

    void approveSubject(String subject) throws SirhaException;
    void failSubject(String subject) throws SirhaException;
    void unenrollSubjectFromGroup(String subject, Group group) throws SirhaException;

    void setSubjectSemester(String subjectName, int semester) throws SirhaException;
}