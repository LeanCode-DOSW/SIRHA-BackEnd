package edu.dosw.sirha.sirha_backend.controller;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.AcademicPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    //@PreAuthorize("hasAnyRole('ADMIN','DEAN')") // Solo usuarios con rol ADMIN o DEAN pueden listar todos los períodos
    public ResponseEntity<List<AcademicPeriod>> getAll() {
        List<AcademicPeriod> periods = academicPeriodService.findAll();
        return ResponseEntity.ok(periods);
    }

    @GetMapping("/current")
    //@PreAuthorize("hasAnyRole('ADMIN','DEAN','STUDENT')") // ADMIN, DEAN y STUDENT pueden consultar el período actual
    public ResponseEntity<AcademicPeriod> getCurrentPeriod() throws SirhaException {
        return academicPeriodService.getCurrentAcademicPeriod()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_FOUND,
                        "No hay un período académico activo."));
    }

    @GetMapping("/{period}")
    //@PreAuthorize("hasAnyRole('ADMIN','DEAN')")
    public ResponseEntity<AcademicPeriod> getByPeriod(@PathVariable String period) throws SirhaException {
        return academicPeriodService.findByPeriod(period)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_FOUND,
                        "Período: %s", period));
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede crear un nuevo período académico
    public ResponseEntity<AcademicPeriod> create(@RequestBody AcademicPeriod period) throws SirhaException {
        if (academicPeriodService.existsAcademicPeriod(period.getPeriod())) {
            throw SirhaException.of(ErrorCodeSirha.SUBJECT_ALREADY_EXISTS,
                    "El período '%s' ya está registrado.", period.getPeriod());
        }
        AcademicPeriod saved = academicPeriodService.saveAcademicPeriod(period);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{period}")
    //@PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede eliminar un período académico
    public ResponseEntity<Void> delete(@PathVariable String period) throws SirhaException {
        if (!academicPeriodService.existsAcademicPeriod(period)) {
            throw SirhaException.of(ErrorCodeSirha.ACADEMIC_PERIOD_NOT_FOUND,
                    "Período: %s", period);
        }
        academicPeriodService.deleteAcademicPeriod(period);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{period}")
    //@PreAuthorize("hasAnyRole('ADMIN','DEAN')") // ADMIN y DEAN pueden verificar si un período existe
    public ResponseEntity<Boolean> exists(@PathVariable String period) {
        boolean exists = academicPeriodService.existsAcademicPeriod(period);
        return ResponseEntity.ok(exists);
    }
}
