package edu.dosw.sirha.sirha_backend.exception;

public enum ErrorCodeSirha {
    // ========== REQUEST STATE ERRORS ==========
    REQUEST_ALREADY_APPROVED("La solicitud ya está aprobada"),
    REQUEST_ALREADY_REJECTED("La solicitud ya está rechazada"),
    REQUEST_ALREADY_PENDING("La solicitud ya está en estado pendiente"),
    REQUEST_ALREADY_IN_REVIEW("La solicitud ya está en revisión"),
    INVALID_STATE_TRANSITION("Transición de estado inválida"),
    REQUEST_NOT_IN_REVIEW("La solicitud debe estar en revisión para esta operación"),
    
    // ========== RESOURCE NOT FOUND ERRORS ==========
    STUDENT_NOT_FOUND("Estudiante no encontrado"),
    SUBJECT_NOT_FOUND("Materia no encontrada"),
    GROUP_NOT_FOUND("Grupo no encontrado"),
    REQUEST_NOT_FOUND("Solicitud no encontrada"),
    ACADEMIC_PERIOD_NOT_FOUND("Período académico no encontrado"),
    
    // ========== VALIDATION ERRORS ==========
    INVALID_CREDENTIALS("Credenciales inválidas"),
    VALIDATION_ERROR("Error de validación de datos"),
    INVALID_ARGUMENT("Argumento inválido proporcionado"),
    MISSING_REQUIRED_FIELD("Campo requerido faltante"),
    
    // ========== CONFLICT ERRORS ==========
    EMAIL_ALREADY_EXISTS("El email ya está registrado"),
    CODE_ALREADY_EXISTS("El código estudiantil ya está registrado"),
    USERNAME_ALREADY_EXISTS("El nombre de usuario ya existe"),
    DUPLICATE_REQUEST("Ya existe una solicitud similar"),
    
    // ========== BUSINESS LOGIC ERRORS ==========
    INSUFFICIENT_PERMISSIONS("Permisos insuficientes para realizar esta operación"),
    OPERATION_NOT_ALLOWED("Operación no permitida en el estado actual"),
    SCHEDULE_CONFLICT("Conflicto de horarios detectado"),
    
    // ========== GROUP STATE ERRORS ==========
    GROUP_CLOSED("El grupo está cerrado para inscripciones"),
    GROUP_FULL("El grupo está lleno"),
    SAME_GROUP("El nuevo grupo es el mismo que el actual"),
    STUDENT_ALREADY_IN_GROUP("El estudiante ya está inscrito en el grupo"),
    STUDENT_NOT_IN_GROUP("El estudiante no está inscrito en el grupo"),
    NO_STUDENTS_TO_REMOVE("No hay estudiantes para remover del grupo"),
    
    // ========== SYSTEM ERRORS ==========
    INTERNAL_ERROR("Error interno del sistema"),
    EXTERNAL_SERVICE_ERROR("Error en servicio externo"),
    DATABASE_ERROR("Error de base de datos"), 
    INVALID_CAPACITY_GROUP("Capacidad del grupo inválida"),

    INVALID_CAREER("Carrera inválida para esta operación"),
    INVALID_DATE_RANGE("Rango de fechas inválido"),
    // ============ SUBJECT ERRORS ========
    SUBJECT_NOT_IN_PROGRESS("La materia no está en curso"),
    SAME_SUBJECT("La materia nueva es la misma que la antigua"),
    ACADEMIC_PERIOD_NOT_VALID("El período académico no es válido"),
    SUBJECT_ALREADY_ENROLLED("El estudiante ya tiene la materia inscrita o aprobada"),
    SUBJECT_NOT_IN_STUDY_PLAN("La materia no está en el plan de estudios del estudiante"),
    PREREQUISITES_NOT_MET("No se cumplen los prerrequisitos para inscribir la materia"), 
    CANNOT_CHANGE_SEMESTER("No se puede cambiar el semestre"),
    CANNOT_CHANGE_GROUP("No se puede cambiar el grupo"),
    CANNOT_CHANGE_GRADE("No se puede cambiar la nota"),
    CANNOT_ENROLL("No se puede inscribir la materia"),
    CANNOT_APPROVE("No se puede aprobar la materia"),
    CANNOT_FAIL("No se puede reprobar la materia"),
    CANNOT_DROP_SUBJECT("No se puede retirar la materia"),
    SUBJECT_ALREADY_EXISTS("La materia ya existe"),
    PROFESSOR_NOT_FOUND("Profesor no encontrado"), 
    DECANATE_NOT_FOUND("Decanatura no encontrada"), 
    STUDY_PLAN_NOT_FOUND("Plan de estudios no encontrado")
    ;
    private final String defaultMessage;
    
    ErrorCodeSirha(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
    
    public String getCode() { 
        return this.name(); // Retorna el nombre del enum como código
    }
    
    public String getDefaultMessage() { 
        return defaultMessage; 
    }
}