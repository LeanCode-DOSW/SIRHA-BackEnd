package edu.dosw.sirha.sirha_backend.domain.port;

/**
 * Define la capacidad de generar reportes para las entidades que implementen esta interfaz.
 */
public interface Reportable {

    /**
     * Genera un reporte en formato de texto.
     * @return reporte generado.
     */
    String generarReporte();
}
