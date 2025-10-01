package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;

public interface AcademicProgressViewer {
    // Conteos por estado
    int getMateriasCursandoCount();
    int getMateriasAprobadasCount();
    int getMateriasReprobadasCount();
    int getMateriasNoCursadasCount();
    
    // Listas por estado
    List<SubjectDecorator> getMateriasCursando();
    
    // Consultas por semestre
    List<SubjectDecorator> getMateriasPorSemestre(int semestre);
    List<Integer> getSemestresHistoricos();
    int getSemestreActual();
    
    // Créditos
    int getCreditosPorColor(SemaforoColores color);
    int getCreditosEnCurso();
    
    // Resúmenes
    String getResumenAcademico();
    
}
