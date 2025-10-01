package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.util.List;
import java.util.Collection;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;

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
    List<SubjectDecorator> getMateriasAprobadas();
    
    /**
     * Obtiene las materias que se están cursando actualmente (color amarillo).
     * @return lista de materias en curso
     */
    List<SubjectDecorator> getMateriasCursando();
    
    /**
     * Obtiene las materias reprobadas (color rojo).
     * @return lista de materias reprobadas
     */
    List<SubjectDecorator> getMateriasReprobadas();
    
    /**
     * Obtiene las materias no cursadas (color gris).
     * @return lista de materias no cursadas
     */
    List<SubjectDecorator> getMateriasNoCursadas();
    
    /**
     * Calcula el total de créditos por color de semáforo.
     * @param color el color del semáforo a filtrar
     * @return total de créditos
     */
    int getCreditosPorColor(SemaforoColores color);
    
    /**
     * Obtiene las materias de un semestre específico.
     * @param semestre el semestre a consultar
     * @return lista de materias del semestre
     */
    List<SubjectDecorator> getMateriasPorSemestre(int semestre);
    
    /**
     * Obtiene contadores por estado académico.
     * @return array con [aprobadas, cursando, reprobadas, noCursadas]
     */
    int[] getContadoresPorEstado();


    int getMateriasAprobadasCount();
    int getMateriasCursandoCount();
    int getMateriasReprobadasCount();
    int getMateriasNoCursadasCount();
}