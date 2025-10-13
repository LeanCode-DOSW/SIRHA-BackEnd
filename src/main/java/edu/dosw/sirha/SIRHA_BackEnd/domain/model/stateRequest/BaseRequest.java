package edu.dosw.sirha.sirha_backend.domain.model.staterequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.ResponseProcess;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.enums.*;
import edu.dosw.sirha.sirha_backend.domain.port.*;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Clase abstracta base para todas las solicitudes académicas en el sistema SIRHA.
 * 
 * Esta clase define la estructura común y comportamiento básico para todos los tipos
 * de solicitudes que pueden realizar los estudiantes, como cambios de grupo,
 * cambios de materia, solicitudes de reinscripción, etc.
 * 
 * Implementa el patrón Template Method, donde define la estructura común de las
 * solicitudes y delega la implementación específica de aprobar() y rechazar()
 * a las clases concretas.
 * 
 * Características principales:
 * - Timestamp automático de creación
 * 
 * Estados de solicitud:
 * - PENDIENTE: Solicitud creada, esperando revisión
 * - APROBADA: Solicitud aceptada y procesada
 * - RECHAZADA: Solicitud denegada con razón específica
 * - EN_REVISION: Solicitud en proceso de evaluación
 */
@Document(collection = "requests")  
public abstract class BaseRequest implements RequestTo {
    @Id
    private String id;
    private LocalDateTime creadoEn;
    protected Student student;
    private AcademicPeriod currentPeriod;
    private ArrayList<RequestProcess> procesos;
    private RequestState state;
    private int priority;

    protected BaseRequest(Student student, AcademicPeriod currentPeriod) {
        this.creadoEn = LocalDateTime.now();
        this.student = student;
        setCurrentPeriod(currentPeriod);
        this.procesos = new ArrayList<>();
        state = new EstadoPendiente();
        changeState(new ResponseProcess(RequestStateEnum.PENDIENTE, "Solicitud creada y en estado pendiente."));
    }

    public void agregarProceso(RequestProcess proceso) {
        this.procesos.add(proceso);
    }

    public List<RequestProcess> getProcesos() {return procesos;}

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public RequestState getEnumState() {return state;}
    public AcademicPeriod getCurrentPeriod() {return currentPeriod;}
    public void setCurrentPeriod(AcademicPeriod currentPeriod) {this.currentPeriod = currentPeriod;}


    public void approveRequest(ResponseRequest response) throws SirhaException{
        state.approveRequest(this, response);
    }
    public void rejectRequest(ResponseRequest response) throws SirhaException{
        state.rejectRequest(this, response);
    }

    public void reviewRequest(ResponseRequest response) throws SirhaException{
        state.reviewRequest(this, response);
    }

    void changeState(ResponseProcess proceso){
        agregarProceso(proceso);
        setActualState(proceso.getStatus());
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    void setState(RequestState state) {this.state = state;}

    public RequestProcess getActualProcess(){return procesos.get(procesos.size() - 1);}
    public RequestStateEnum getActualState(){return getActualProcess().getStatus();}
    private void setActualState(RequestStateEnum estado){getActualProcess().setStatus(estado);}

    /**
     * Obtiene la fecha y hora de creación de la solicitud.
     * @return timestamp de creación
     */
    public LocalDateTime getCreadoEn() {return creadoEn;}
    public Student getStudent() {return student;}


    public Careers getStudentCareer() {
        if (student == null) {
            return null;
        }
        return student.getCareer();
    }
}
