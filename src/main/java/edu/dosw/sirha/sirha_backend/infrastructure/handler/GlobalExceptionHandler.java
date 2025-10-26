package edu.dosw.sirha.sirha_backend.infrastructure.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.dosw.sirha.sirha_backend.dto.ApiErrorResponse;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String SIRHA_BUSINESS_ERROR_MESSAGE = "Error de lógica de negocio en SIRHA.";

    /**
     * Maneja todas las excepciones SirhaException (checked exceptions del dominio)
     */
    @ExceptionHandler(SirhaException.class)
    public ResponseEntity<ApiErrorResponse> handleSirhaException(SirhaException ex, HttpServletRequest request) {
        log.error("SIRHA exception: {} - {}", ex.getErrorCode().getCode(), ex.getMessage());

        HttpStatus status = determineHttpStatus(ex.getErrorCode());
        ApiErrorResponse error = new ApiErrorResponse(
            status,
            ex.getErrorCode().getCode(),
            ex.getMessage(),
            SIRHA_BUSINESS_ERROR_MESSAGE,
            request.getRequestURI()
        );
        

        addSuggestion(error, ex.getErrorCode());
        return new ResponseEntity<>(error, status);
    }

    /**
     * Maneja RuntimeExceptions que pueden envolver SirhaExceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        
        // Verificar si es una SirhaException envuelta
        if (ex.getCause() instanceof SirhaException sirhaEx) {
            log.error("Wrapped SIRHA exception: {} - {}", sirhaEx.getErrorCode().getCode(), sirhaEx.getMessage());
            
            HttpStatus status = determineHttpStatus(sirhaEx.getErrorCode());
            ApiErrorResponse error = new ApiErrorResponse(
                status,
                sirhaEx.getErrorCode().getCode(),
                sirhaEx.getMessage(),
                SIRHA_BUSINESS_ERROR_MESSAGE,
                request.getRequestURI()
            );
            
            
            addSuggestion(error, sirhaEx.getErrorCode());
            return new ResponseEntity<>(error, status);
        }
        
        // Manejar otras RuntimeExceptions
        log.error("Runtime exception: {}", ex.getMessage(), ex);
        
        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCodeSirha.INTERNAL_ERROR.getCode(),
            "Ha ocurrido un error interno en el servidor.",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        error.setSuggestion("Intente nuevamente más tarde o contacte soporte técnico.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Illegal argument exception: {}", ex.getMessage());

        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            ErrorCodeSirha.INVALID_ARGUMENT.getCode(),
            ex.getMessage(),
            "Los datos proporcionados no son válidos.",
            request.getRequestURI()
        );

        error.setSuggestion("Verifique los parámetros enviados en la solicitud.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas no capturadas por otros handlers
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        
        // Verificar si es una SirhaException envuelta en Exception
        if (ex.getCause() instanceof SirhaException sirhaEx) {
            log.error("Exception wrapping SIRHA exception: {} - {}", sirhaEx.getErrorCode().getCode(), sirhaEx.getMessage());
            
            HttpStatus status = determineHttpStatus(sirhaEx.getErrorCode());
            
            ApiErrorResponse error = new ApiErrorResponse(
                status,
                sirhaEx.getErrorCode().getCode(),
                sirhaEx.getMessage(),
                SIRHA_BUSINESS_ERROR_MESSAGE,
                request.getRequestURI()
            );
            
            addSuggestion(error, sirhaEx.getErrorCode());
            return new ResponseEntity<>(error, status);
        }
        
        // Manejar excepciones completamente inesperadas
        log.error("Unexpected exception", ex);

        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCodeSirha.INTERNAL_ERROR.getCode(),
            "Ha ocurrido un error inesperado en el servidor.",
            ex.getMessage(),
            request.getRequestURI()
        );

        error.setSuggestion("Intente nuevamente más tarde o contacte soporte técnico.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Determina el HttpStatus basado en el ErrorCodeSirha enum
     */
    private HttpStatus determineHttpStatus(ErrorCodeSirha errorCode) {
        return switch (errorCode) {
            // 404 Not Found - Recursos no encontrados
            case STUDENT_NOT_FOUND, SUBJECT_NOT_FOUND, GROUP_NOT_FOUND, 
                 REQUEST_NOT_FOUND, ACADEMIC_PERIOD_NOT_FOUND -> 
                HttpStatus.NOT_FOUND;
            
            // 409 Conflict - Conflictos de estado o datos
            case REQUEST_ALREADY_APPROVED, REQUEST_ALREADY_REJECTED, REQUEST_ALREADY_PENDING,
                 REQUEST_ALREADY_IN_REVIEW, INVALID_STATE_TRANSITION, EMAIL_ALREADY_EXISTS,
                 CODE_ALREADY_EXISTS, USERNAME_ALREADY_EXISTS, DUPLICATE_REQUEST,
                 SCHEDULE_CONFLICT, GROUP_FULL, STUDENT_ALREADY_IN_GROUP -> 
                HttpStatus.CONFLICT;
            
            // 400 Bad Request - Datos inválidos o errores de validación
            case INVALID_CREDENTIALS, VALIDATION_ERROR, INVALID_ARGUMENT, 
                 MISSING_REQUIRED_FIELD, REQUEST_NOT_IN_REVIEW, INVALID_CAPACITY_GROUP,
                 INVALID_CAREER, GROUP_CLOSED, STUDENT_NOT_IN_GROUP, NO_STUDENTS_TO_REMOVE -> 
                HttpStatus.BAD_REQUEST;
            
            // 403 Forbidden - Permisos insuficientes
            case INSUFFICIENT_PERMISSIONS, OPERATION_NOT_ALLOWED -> 
                HttpStatus.FORBIDDEN;
            
            // 502 Bad Gateway - Errores de servicios externos
            case EXTERNAL_SERVICE_ERROR -> 
                HttpStatus.BAD_GATEWAY;
            
            // 500 Internal Server Error - Errores del sistema
            case INTERNAL_ERROR, DATABASE_ERROR -> 
                HttpStatus.INTERNAL_SERVER_ERROR;
            
            // Default para códigos no reconocidos
            default -> {
                log.warn("Unrecognized error code: {}, defaulting to INTERNAL_SERVER_ERROR", errorCode);
                yield HttpStatus.INTERNAL_SERVER_ERROR;
            }
        };
    }

    /**
     * Añade sugerencias específicas basadas en el ErrorCodeSirha enum
     */
    private void addSuggestion(ApiErrorResponse error, ErrorCodeSirha errorCode) {
        String suggestion = switch (errorCode) {
            // ========== REQUEST STATE ERRORS ==========
            case REQUEST_ALREADY_APPROVED -> 
                "Esta solicitud ya fue aprobada previamente. No se requiere acción adicional.";
                
            case REQUEST_ALREADY_REJECTED -> 
                "Esta solicitud ya fue rechazada. Puede crear una nueva solicitud si es necesario.";
                
            case REQUEST_ALREADY_PENDING -> 
                "Ya existe una solicitud pendiente del mismo tipo. Espere a que sea procesada.";
                
            case REQUEST_ALREADY_IN_REVIEW -> 
                "La solicitud ya está siendo revisada por el personal administrativo.";
                
            case INVALID_STATE_TRANSITION -> 
                "La transición de estado solicitada no es válida para el estado actual de la solicitud.";
                
            case REQUEST_NOT_IN_REVIEW -> 
                "La solicitud debe estar en estado 'EN_REVISION' para realizar esta operación.";
            
            // ========== RESOURCE NOT FOUND ERRORS ==========
            case STUDENT_NOT_FOUND -> 
                "Verifique que el código o ID del estudiante sea correcto.";
                
            case SUBJECT_NOT_FOUND -> 
                "Verifique que el código o ID de la materia sea correcto.";
                
            case GROUP_NOT_FOUND -> 
                "Verifique que el ID del grupo sea correcto y que el grupo exista.";
                
            case REQUEST_NOT_FOUND -> 
                "Verifique que el ID de la solicitud sea correcto.";
                
            case ACADEMIC_PERIOD_NOT_FOUND -> 
                "Verifique que el período académico esté configurado correctamente.";
            
            // ========== VALIDATION ERRORS ==========
            case INVALID_CREDENTIALS -> 
                "Verifique que su usuario y contraseña sean correctos.";
                
            case VALIDATION_ERROR -> 
                "Revise que todos los campos estén correctamente completados según las reglas de validación.";
                
            case INVALID_ARGUMENT -> 
                "Revise que todos los parámetros enviados sean válidos y estén en el formato correcto.";
                
            case MISSING_REQUIRED_FIELD -> 
                "Complete todos los campos obligatorios antes de continuar.";
            
            // ========== CONFLICT ERRORS ==========
            case EMAIL_ALREADY_EXISTS -> 
                "Use un email diferente que no esté registrado en el sistema.";
                
            case CODE_ALREADY_EXISTS -> 
                "Use un código estudiantil diferente que no esté registrado.";
                
            case USERNAME_ALREADY_EXISTS -> 
                "Elija un nombre de usuario diferente que esté disponible.";
                
            case DUPLICATE_REQUEST -> 
                "Ya existe una solicitud similar. Revise sus solicitudes pendientes.";
            
            // ========== BUSINESS LOGIC ERRORS ==========
            case INSUFFICIENT_PERMISSIONS -> 
                "Contacte al administrador del sistema para obtener los permisos necesarios.";
                
            case OPERATION_NOT_ALLOWED -> 
                "Esta operación no está permitida en las circunstancias actuales.";
                
            case SCHEDULE_CONFLICT -> 
                "Existe un conflicto de horarios. Seleccione un horario diferente que no se superponga.";
            
            // ========== GROUP STATE ERRORS ==========
            case GROUP_CLOSED -> 
                "El grupo está cerrado para inscripciones. Contacte al administrador académico.";
                
            case GROUP_FULL -> 
                "El grupo ha alcanzado su capacidad máxima. Intente con otro grupo o espere cupos disponibles.";
            
            case SAME_GROUP -> 
                "El estudiante ya pertenece a este grupo. No es necesario realizar cambios.";

            case STUDENT_ALREADY_IN_GROUP -> 
                "El estudiante ya está inscrito en este grupo. No es necesario inscribirlo nuevamente.";
                
            case STUDENT_NOT_IN_GROUP -> 
                "El estudiante no está inscrito en este grupo. Verifique la información antes de continuar.";
                
            case NO_STUDENTS_TO_REMOVE -> 
                "No hay estudiantes para remover de este grupo.";
                
            case INVALID_CAPACITY_GROUP -> 
                "La capacidad del grupo debe ser un número positivo mayor a cero.";
                
            case INVALID_CAREER -> 
                "La carrera especificada no es válida para esta operación. Verifique los datos.";
            
            
            // ========== SYSTEM ERRORS ==========
            case INTERNAL_ERROR -> 
                "Error interno del servidor. Contacte soporte técnico si el problema persiste.";
                
            case EXTERNAL_SERVICE_ERROR -> 
                "Problema con un servicio externo. Intente nuevamente en unos minutos.";
                
            case DATABASE_ERROR -> 
                "Error de base de datos. Contacte soporte técnico si el problema persiste.";
            case INVALID_DATE_RANGE ->
                "Verifique que las fechas proporcionadas sean correctas y estén en el formato adecuado.";


            // Subject errors
            case SUBJECT_NOT_IN_PROGRESS ->
                "La materia no está en curso. Verifique el estado de la materia.";
            
            case ACADEMIC_PERIOD_NOT_VALID ->
                "El período académico no es válido. Verifique las fechas del período académico.";
        
            case SAME_SUBJECT ->
                "La materia nueva es la misma que la antigua. Seleccione una materia diferente.";

            case SUBJECT_ALREADY_ENROLLED ->
                "El estudiante ya tiene la materia inscrita o aprobada. Verifique su historial académico.";
            case SUBJECT_NOT_IN_STUDY_PLAN ->
                "La materia no está en el plan de estudios del estudiante. Verifique el plan de estudios.";
            case PREREQUISITES_NOT_MET ->
                "No se cumplen los prerrequisitos para inscribir la materia. Revise los requisitos previos.";

            case CANNOT_CHANGE_SEMESTER ->
                "No se puede cambiar el semestre de la materia en su estado actual.";
            case CANNOT_CHANGE_GROUP ->
                "No se puede cambiar el grupo de la materia en su estado actual.";
            case CANNOT_CHANGE_GRADE ->
                "No se puede cambiar la nota de la materia en su estado actual.";
            case CANNOT_ENROLL ->
                "No se puede inscribir la materia en su estado actual.";
            case CANNOT_APPROVE ->
                "No se puede aprobar la materia en su estado actual.";
            case CANNOT_FAIL ->
                "No se puede reprobar la materia en su estado actual.";
            case CANNOT_DROP_SUBJECT ->
                "No se puede retirar la materia en su estado actual.";
            case SUBJECT_ALREADY_EXISTS ->
                "La materia que intenta crear ya existe. Verifique el nombre o código de la materia.";
            case PROFESSOR_NOT_FOUND ->
                "El profesor especificado no fue encontrado. Verifique el ID o la información del profesor.";
            case DECANATE_NOT_FOUND ->
                "La decanatura especificada no fue encontrada. Verifique el nombre o la información de la decanatura.";
            case STUDY_PLAN_NOT_FOUND ->
                "El plan de estudios especificado no fue encontrado. Verifique el nombre o la información del plan de estudios.";
        };
        error.setSuggestion(suggestion);
    }
}