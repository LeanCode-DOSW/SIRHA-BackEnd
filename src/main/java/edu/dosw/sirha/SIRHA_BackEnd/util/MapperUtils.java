package edu.dosw.sirha.SIRHA_BackEnd.util;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.dto.StudentDTO;
import java.util.stream.Collectors;

/**
 * Clase utilitaria para la conversión entre entidades del dominio y DTOs (Data Transfer Objects).
 * 
 * Esta clase implementa el patrón Mapper, proporcionando métodos estáticos para convertir
 * objetos del dominio a sus representaciones DTO correspondientes y viceversa.
 * Los DTOs son utilizados para transferir datos entre las diferentes capas de la aplicación,
 * especialmente entre la capa de presentación (controladores) y la capa de servicio.
 * 
 * Ventajas del uso de DTOs:
 * - Encapsulación: Controla qué información se expone externamente
 * - Versionado: Permite evolucionar la API sin afectar el modelo de dominio
 * - Seguridad: Evita exponer información sensible del modelo interno
 * - Performance: Reduce el tamaño de los datos transferidos
 * 
 */
public class MapperUtils {

    /**
     * Convierte una entidad Student del dominio a su representación DTO.
     * 
     * Esta conversión incluye:
     * - Información básica del estudiante (id, username, código)
     * - Lista de IDs de solicitudes asociadas (si existen)
     * - Omite información sensible como el hash de contraseña
     * 
     * El método maneja de forma segura los valores null, evitando excepciones
     * de NullPointerException durante la conversión.
     * 
     * @param student la entidad Student del dominio a convertir.
     *               Puede ser null, en cuyo caso se retorna null.
     * @return el DTO correspondiente al estudiante, o null si el parámetro es null
     * 
     * @example
     * <pre>
     * Student student = new Student("1", "juan.perez", "hashedPass", "ESTUDIANTE", "202112345");
     * StudentDTO dto = MapperUtils.toDTO(student);
     * // Resultado:
     * // dto.getId() = "1"
     * // dto.getUsername() = "juan.perez"  
     * // dto.getCodigo() = "202112345"
     * // dto.getSolicitudesIds() = [] 
     * </pre>
     * 
     * @see StudentDTO
     * @see Student
     */
    public static StudentDTO toDTO(Student student) {
        // Manejo defensivo de null
        if (student == null) {
            return null;
        }

        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setUsername(student.getUsername());
        dto.setCodigo(student.getCodigo());

        // Conversión segura de la lista de solicitudes a IDs
        if (student.getSolicitudes() != null) {
            dto.setSolicitudesIds(
                student.getSolicitudes().stream()
                        .filter(solicitud -> solicitud != null) // Filtrar nulls
                        .map(solicitud -> solicitud.getId())
                        .filter(id -> id != null) // Filtrar IDs null
                        .collect(Collectors.toList())
            );
        } else {
            // Inicializar con lista vacía si no hay solicitudes
            dto.setSolicitudesIds(java.util.Collections.emptyList());
        }

        return dto;
    }

    /**
     * Convierte un StudentDTO a una entidad Student del dominio.
     * 
     * Esta conversión es útil cuando se reciben datos desde la capa de presentación
     * y necesitan ser convertidos a entidades del dominio para procesamiento
     * en la lógica de negocio.
     * 
     * @param dto el DTO a convertir a entidad del dominio.
     * @return la entidad Student correspondiente
     * @example
     * <pre>
     * StudentDTO dto = new StudentDTO();
     * dto.setId("1");
     * dto.setUsername("maria.garcia");
     * dto.setCodigo("202112346");
     * Student student = MapperUtils.fromDTO(dto);
     * // student.getId() = "1"
     * // student.getUsername() = "maria.garcia"
     * // student.getCodigo() = "202112346"
     * // student.getSolicitudes() = null (debe ser cargado por separado)
     * </pre>
     */
    public static Student fromDTO(StudentDTO dto) {
        if (dto == null) {
            return null;
        }

        // Crear entidad Student con contraseña por defecto
        // En un escenario real, la contraseña debería ser manejada por separado
        return new Student(
            dto.getId(),
            dto.getUsername(),
            "defaultPassword", // Contraseña temporal - debe ser actualizada
            "ESTUDIANTE", // Rol por defecto
            dto.getCodigo()
        );
    }
}
