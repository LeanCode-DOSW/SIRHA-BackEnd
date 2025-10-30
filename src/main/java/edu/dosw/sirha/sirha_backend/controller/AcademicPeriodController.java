package edu.dosw.sirha.sirha_backend.controller;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.AcademicPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de períodos académicos.
 *
 * Los períodos académicos se identifican por un código (por ejemplo: "2025-1").
 * Este controlador permite crear, consultar, verificar y eliminar períodos.
 */
@RestController
@RequestMapping("/api/academic-periods")
@Tag(name = "Academic Periods", description = "API para la gestión de períodos académicos")
public class AcademicPeriodController {

    private final AcademicPeriodService academicPeriodService;

    public AcademicPeriodController(AcademicPeriodService academicPeriodService) {
        this.academicPeriodService = academicPeriodService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DEAN')") // Solo usuarios con rol ADMIN o DEAN pueden listar todos los períodos
    @Operation(summary = "Obtener todos los períodos académicos", description = "Retorna una lista completa de períodos académicos registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de períodos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<AcademicPeriod>> getAll() {
        List<AcademicPeriod> periods = academicPeriodService.findAll();
        return ResponseEntity.ok(periods);
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('ADMIN','DEAN','STUDENT')") // ADMIN, DEAN y STUDENT pueden consultar el período actual
    @Operation(summary = "Obtener período académico actual", description = "Obtiene el período académico que está marcado como activo en el sistema, si existe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Período académico actual obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "No hay un período académico activo"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AcademicPeriod> getCurrentPeriod() throws SirhaException {
    return academicPeriodService.getCurrentAcademicPeriod()
        .map(ResponseEntity::ok)
        .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_FOUND,
            "No hay un período académico activo."));
    }

    @GetMapping("/{period}")
    @PreAuthorize("hasAnyRole('ADMIN','DEAN')")
    @Operation(summary = "Obtener período por código", description = "Busca y retorna un período académico por su código (ej: '2025-1')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Período académico encontrado"),
        @ApiResponse(responseCode = "404", description = "Período académico no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AcademicPeriod> getByPeriod(@PathVariable String period) throws SirhaException {
    return academicPeriodService.findByPeriod(period)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_FOUND,
            "Período: %s", period));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear período académico", description = "Crea y persiste un nuevo período académico en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Período académico creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la petición"),
            @ApiResponse(responseCode = "409", description = "El período ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AcademicPeriod> create(@RequestBody edu.dosw.sirha.sirha_backend.dto.AcademicPeriodDTO periodDto) throws SirhaException {
        if (academicPeriodService.existsAcademicPeriod(periodDto.getPeriod())) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_ALREADY_EXISTS,
                    "El período '%s' ya está registrado.", periodDto.getPeriod());
        }
        // Map DTO -> domain model
        AcademicPeriod period = new AcademicPeriod(periodDto.getPeriod(), periodDto.getStartDate(), periodDto.getEndDate());
        if (periodDto.getStartDateInscripciones() != null || periodDto.getEndDateInscripciones() != null) {
            period.setStartDatesInscripciones(periodDto.getStartDateInscripciones(), periodDto.getEndDateInscripciones());
        }
        AcademicPeriod saved = academicPeriodService.saveAcademicPeriod(period);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping("/exists/{period}")
    @PreAuthorize("hasAnyRole('ADMIN','DEAN')") // ADMIN y DEAN pueden verificar si un período existe
    @Operation(summary = "Verificar existencia de período", description = "Verifica si un período académico con el código dado ya está registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> exists(@PathVariable String period) {
        boolean exists = academicPeriodService.existsAcademicPeriod(period);
        return ResponseEntity.ok(exists);
    }
}
