package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Semaforo;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Reportable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de reportes y estadísticas para el sistema SIRHA.
 *
 * Implementa los casos de uso:
 * - Indicador de aprobación
 * - Semáforo académico global
 * - Materias más solicitadas
 */
public class ReportService implements Reportable {

    @Override
    public String generarReporte() {
        return "Generando reporte de estadísticas académicas...";
    }

    /**
     * Calcula el indicador de aprobación global.
     * Supone que el 70% de los inscritos aprueban como lógica de ejemplo.
     *
     * @param grupos lista de grupos académicos
     * @return porcentaje de aprobación (0.0 - 1.0)
     */
    public double calcularIndicadorAprobacion(List<Group> grupos) {
        int total = 0;
        int aprobados = 0;

        for (Group g : grupos) {
            total += g.getInscritos();
            aprobados += (int) (g.getInscritos() * 0.7); // Ejemplo
        }

        return total == 0 ? 0.0 : (double) aprobados / total;
    }

    /**
     * Obtiene un semáforo global para todos los estudiantes.
     *
     * @param estudiantes lista de estudiantes
     * @return mapa con estudiante -> color del semáforo
     */
    public Map<String, SemaforoColores> obtenerSemaforoGlobal(List<Student> estudiantes) {
        Map<String, SemaforoColores> global = new HashMap<>();

        for (Student s : estudiantes) {
            Semaforo sem = s.getSemaforo();
            if (sem != null && s.getPlanGeneral() != null) {
                // Ejemplo: se toma el primer curso del plan de estudios
                String materia = s.getPlanGeneral().getMaterias().isEmpty()
                        ? null
                        : s.getPlanGeneral().getMaterias().get(0).getCodigo();

                if (materia != null) {
                    global.put(s.getCodigo(), sem.getColor(materia));
                }
            }
        }

        return global;
    }

    /**
     * Devuelve las materias más solicitadas según número de inscritos en grupos.
     *
     * @param grupos lista de grupos
     * @return lista de materias ordenadas por popularidad
     */
    public List<String> obtenerMateriasMasSolicitadas(List<Group> grupos) {
        Map<String, Integer> contador = new HashMap<>();

        for (Group g : grupos) {
            String codigo = g.getCurso() != null ? g.getCurso().getCodigo() : "DESCONOCIDO";
            contador.put(codigo, contador.getOrDefault(codigo, 0) + g.getInscritos());
        }

        return contador.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}