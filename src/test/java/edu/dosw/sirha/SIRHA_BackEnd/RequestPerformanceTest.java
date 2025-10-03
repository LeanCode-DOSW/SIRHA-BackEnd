package edu.dosw.sirha.SIRHA_BackEnd;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.DiasSemana;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de rendimiento y carga para el sistema de solicitudes
 */
@SpringBootTest
@ActiveProfiles("test")
class RequestPerformanceTest {

    private List<Student> students;
    private List<Subject> subjects;
    private List<Group> groups;
    private AcademicPeriod academicPeriod;
    private StudyPlan studyPlan;

    @BeforeEach
    void setUp() {
        academicPeriod = new AcademicPeriod("2024-1", LocalDate.now(), LocalDate.now().plusMonths(4));
        studyPlan = new StudyPlan("Ingeniería de Sistemas");
        
        students = new ArrayList<>();
        subjects = new ArrayList<>();
        groups = new ArrayList<>();
        
        // Crear múltiples estudiantes
        for (int i = 0; i < 100; i++) {
            Student student = new Student(i, "student" + i, "student" + i + "@test.com", "pass", "20240" + String.format("%03d", i));
            students.add(student);
        }
        
        // Crear múltiples materias
        for (int i = 0; i < 10; i++) {
            Subject subject = new Subject(i + 100, "Materia " + i, 3);
            subjects.add(subject);
            studyPlan.addSubject(subject);
            
            // Crear múltiples grupos por materia
            for (int j = 0; j < 3; j++) {
                Group group = new Group(25, academicPeriod);
                group.setId((i * 10) + j);
                
                // Agregar horarios diversos
                Schedule schedule = new Schedule(
                    DiasSemana.values()[j % 7], 
                    LocalTime.of(8 + (j * 2), 0), 
                    LocalTime.of(10 + (j * 2), 0)
                );
                group.addSchedule(schedule);
                
                subject.addGroup(group);
                groups.add(group);
            }
        }
        
        // Configurar progreso académico para todos los estudiantes
        for (Student student : students) {
            Semaforo semaforo = new Semaforo(studyPlan);
            student.setAcademicProgress(semaforo);
            student.setPlanGeneral(studyPlan);
        }
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateManyGroupChangeRequests() {
        // Medir tiempo de creación de múltiples solicitudes
        long startTime = System.currentTimeMillis();
        
        List<CambioGrupo> solicitudes = new ArrayList<>();
        
        for (int i = 0; i < 100; i++) {
            Student student = students.get(i);
            Subject subject = subjects.get(i % subjects.size());
            Group sourceGroup = subject.getGroups().get(0);
            Group targetGroup = subject.getGroups().get(1);
            
            // Inscribir en grupo origen
            student.enrollSubject(subject, sourceGroup);
            
            // Crear solicitud de cambio
            CambioGrupo solicitud = new CambioGrupo(student, subject, targetGroup, academicPeriod);
            solicitudes.add(solicitud);
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // Verificaciones
        assertEquals(100, solicitudes.size());
        assertTrue(elapsedTime < 3000, "La creación de 100 solicitudes tomó más de 3 segundos: " + elapsedTime + "ms");
        
        // Verificar que todas las solicitudes están bien formadas
        for (CambioGrupo solicitud : solicitudes) {
            assertNotNull(solicitud.getStudent());
            assertNotNull(solicitud.getCurrentPeriod());
            assertNotNull(solicitud.getCreadoEn());
            assertTrue(solicitud.getProcesos().isEmpty());
        }
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProcessManyRequestsSequentially() {
        // Crear solicitudes
        List<BaseRequest> solicitudes = new ArrayList<>();
        
        for (int i = 0; i < 50; i++) {
            Student student = students.get(i);
            Subject subject = subjects.get(i % subjects.size());
            Group sourceGroup = subject.getGroups().get(0);
            Group targetGroup = subject.getGroups().get(1);
            
            student.enrollSubject(subject, sourceGroup);
            CambioGrupo solicitud = new CambioGrupo(student, subject, targetGroup, academicPeriod);
            solicitudes.add(solicitud);
        }
        
        // Procesar todas las solicitudes secuencialmente
        long startTime = System.currentTimeMillis();
        
        for (BaseRequest solicitud : solicitudes) {
            
            solicitud.reviewRequest("Revisión automática");
            solicitud.approveRequest("Aprobación automática");
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // Verificaciones
        assertTrue(elapsedTime < 8000, "El procesamiento secuencial tomó más de 8 segundos: " + elapsedTime + "ms");
        
        for (BaseRequest solicitud : solicitudes) {
            assertEquals(RequestStateEnum.APROBADA, solicitud.getActualState());
            assertEquals(2, solicitud.getProcesos().size());
        }
    }

    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testConcurrentRequestProcessing() throws InterruptedException, ExecutionException {
        // Crear solicitudes
        List<BaseRequest> solicitudes = new ArrayList<>();
        
        for (int i = 0; i < 30; i++) {
            Student student = students.get(i);
            Subject subject = subjects.get(i % subjects.size());
            
            if (i % 2 == 0) {
                // Cambio de grupo
                Group sourceGroup = subject.getGroups().get(0);
                Group targetGroup = subject.getGroups().get(1);
                student.enrollSubject(subject, sourceGroup);
                solicitudes.add(new CambioGrupo(student, subject, targetGroup, academicPeriod));
            } else {
                // Cambio de materia
                Subject targetSubject = subjects.get((i + 1) % subjects.size());
                solicitudes.add(new CambioMateria(student, subject, targetSubject, academicPeriod));
            }
        }
        
        // Procesamiento concurrente
        ExecutorService executor = Executors.newFixedThreadPool(5);
        long startTime = System.currentTimeMillis();
        
        List<Future<Void>> futures = new ArrayList<>();
        
        for (BaseRequest solicitud : solicitudes) {
            Future<Void> future = executor.submit(() -> {
                
                // Simular tiempo de procesamiento
                try {
                    Thread.sleep(50); // 50ms de procesamiento
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                solicitud.reviewRequest("Revisión concurrente");
                solicitud.approveRequest("Aprobación concurrente");
                
                return null;
            });
            futures.add(future);
        }
        
        // Esperar a que terminen todos los hilos
        for (Future<Void> future : futures) {
            future.get();
        }
        
        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // Verificaciones
        assertTrue(elapsedTime < 12000, "El procesamiento concurrente tomó más de 12 segundos: " + elapsedTime + "ms");
        
        for (BaseRequest solicitud : solicitudes) {
            assertEquals(RequestStateEnum.APROBADA, solicitud.getActualState());
            assertEquals(2, solicitud.getProcesos().size());
        }
    }

    @Test
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    void testStateTransitionPerformance() {
        CambioGrupo solicitud = new CambioGrupo(students.get(0), subjects.get(0), groups.get(0), academicPeriod);
        
        long startTime = System.currentTimeMillis();
        
        // Realizar muchas transiciones de estado
        for (int i = 0; i < 1000; i++) {
            solicitud.reviewRequest("Revisión " + i);
            assertEquals(RequestStateEnum.EN_REVISION, solicitud.getActualState());
            
            if (i % 2 == 0) {
                solicitud.approveRequest("Aprobación " + i);
                assertEquals(RequestStateEnum.APROBADA, solicitud.getActualState());
            } else {
                solicitud.rejectRequest("Rechazo " + i);
                assertEquals(RequestStateEnum.RECHAZADA, solicitud.getActualState());
            }
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        assertTrue(elapsedTime < 2000, "1000 transiciones de estado tomaron más de 2 segundos: " + elapsedTime + "ms");
        assertEquals(2000, solicitud.getProcesos().size()); // 1000 reviews + 1000 approve/reject
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testMemoryUsageWithManyRequests() {
        Runtime runtime = Runtime.getRuntime();
        
        // Forzar garbage collection antes de la prueba
        System.gc();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        List<BaseRequest> solicitudes = new ArrayList<>();
        
        // Crear muchas solicitudes
        for (int i = 0; i < 500; i++) {
            Student student = students.get(i % students.size());
            Subject subject = subjects.get(i % subjects.size());
            Group group = subject.getGroups().get(0);
            
            student.enrollSubject(subject, group);
            
            if (i % 3 == 0) {
                solicitudes.add(new CambioGrupo(student, subject, subject.getGroups().get(1), academicPeriod));
            } else {
                Subject targetSubject = subjects.get((i + 1) % subjects.size());
                solicitudes.add(new CambioMateria(student, subject, targetSubject, academicPeriod));
            }
            
            // Procesar algunas solicitudes
            if (i % 10 == 0) {
                BaseRequest solicitud = solicitudes.get(solicitudes.size() - 1);
                solicitud.reviewRequest("Test memoria");
                solicitud.approveRequest("Test memoria aprobado");
            }
        }
        
        // Medir memoria después de crear las solicitudes
        System.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = finalMemory - initialMemory;
        
        // Verificaciones
        assertEquals(500, solicitudes.size());
        
        // La memoria usada no debería exceder 50MB (52,428,800 bytes)
        assertTrue(memoryUsed < 50_000_000, 
            "Uso de memoria excesivo: " + (memoryUsed / 1024 / 1024) + "MB");
        
        // Verificar que las solicitudes siguen siendo funcionales
        BaseRequest ultimaSolicitud = solicitudes.get(solicitudes.size() - 1);
        assertNotNull(ultimaSolicitud.getStudent());
        assertNotNull(ultimaSolicitud.getCurrentPeriod());
    }

    @Test
    @Timeout(value = 8, unit = TimeUnit.SECONDS)
    void testHighVolumeGroupOperations() {
        Group testGroup = groups.get(0);
        
        long startTime = System.currentTimeMillis();
        
        // Inscribir y desinscribir estudiantes repetidamente
        for (int i = 0; i < 1000; i++) {
            Student student = students.get(i % students.size());
            
            // Inscribir
            if (!testGroup.isFull()) {
                testGroup.enrollStudent(student);
                assertTrue(testGroup.contieneEstudiante(student));
            }
            
            // Desinscribir cada 10 inscripciones
            if (i % 10 == 0 && testGroup.contieneEstudiante(student)) {
                testGroup.unenrollStudent(student);
                assertFalse(testGroup.contieneEstudiante(student));
            }
            
            // Verificar estado del grupo
            assertTrue(testGroup.getCuposDisponibles() >= 0);
            assertTrue(testGroup.getCuposDisponibles() <= testGroup.getCapacidad());
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        assertTrue(elapsedTime < 6000, "1000 operaciones de grupo tomaron más de 6 segundos: " + elapsedTime + "ms");
    }

    @Test
    @Timeout(value = 4, unit = TimeUnit.SECONDS)
    void testScheduleConflictDetectionPerformance() {
        List<Schedule> schedules = new ArrayList<>();
        
        // Crear muchos horarios
        for (int i = 0; i < 100; i++) {
            Schedule schedule = new Schedule(
                DiasSemana.values()[i % 7],
                LocalTime.of(8 + (i % 10), 0),
                LocalTime.of(10 + (i % 10), 0)
            );
            schedules.add(schedule);
        }
        
        long startTime = System.currentTimeMillis();
        
        int conflictsFound = 0;
        
        // Comparar todos los horarios entre sí
        for (int i = 0; i < schedules.size(); i++) {
            for (int j = i + 1; j < schedules.size(); j++) {
                if (schedules.get(i).seSolapaCon(schedules.get(j))) {
                    conflictsFound++;
                }
            }
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        assertTrue(elapsedTime < 3000, "Detección de conflictos tomó más de 3 segundos: " + elapsedTime + "ms");
        assertTrue(conflictsFound > 0, "Se esperaban algunos conflictos en los horarios generados");
    }

    @Test
    void testRequestDataIntegrity() {
        List<BaseRequest> solicitudes = new ArrayList<>();
        
        // Crear solicitudes con datos complejos
        for (int i = 0; i < 100; i++) {
            Student student = students.get(i % students.size());
            Subject subject = subjects.get(i % subjects.size());
            Group group = subject.getGroups().get(0);
            
            student.enrollSubject(subject, group);
            
            CambioGrupo solicitud = new CambioGrupo(student, subject, subject.getGroups().get(1), academicPeriod);
            solicitudes.add(solicitud);
            
            // Procesar la solicitud
            solicitud.reviewRequest("Integridad de datos - revisión " + i);
            
            if (i % 3 == 0) {
                solicitud.approveRequest("Aprobado por integridad " + i);
            } else {
                solicitud.rejectRequest("Rechazado por integridad " + i);
            }
        }
        
        // Verificar integridad de todos los datos
        for (int i = 0; i < solicitudes.size(); i++) {
            BaseRequest solicitud = solicitudes.get(i);
            
            // Verificar datos básicos
            assertNotNull(solicitud.getStudent(), "Student nulo en solicitud " + i);
            assertNotNull(solicitud.getCurrentPeriod(), "Período nulo en solicitud " + i);
            assertNotNull(solicitud.getCreadoEn(), "Fecha creación nula en solicitud " + i);
            assertNotNull(solicitud.getEstado(), "Estado nulo en solicitud " + i);
            
            // Verificar procesos
            assertEquals(2, solicitud.getProcesos().size(), "Número incorrecto de procesos en solicitud " + i);
            
            // Verificar coherencia de estados
            RequestStateEnum estadoFinal = solicitud.getActualState();
            assertTrue(estadoFinal == RequestStateEnum.APROBADA || estadoFinal == RequestStateEnum.RECHAZADA,
                "Estado final incorrecto en solicitud " + i);
            
            // Verificar comentarios en procesos
            assertNotNull(solicitud.getProcesos().get(0).getComentario());
            assertNotNull(solicitud.getProcesos().get(1).getComentario());
            assertTrue(solicitud.getProcesos().get(0).getComentario().contains("revisión"));
        }
    }
}