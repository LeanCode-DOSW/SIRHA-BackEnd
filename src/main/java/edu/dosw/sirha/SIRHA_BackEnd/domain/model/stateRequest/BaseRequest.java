package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.AcademicPeriod;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.ResponseProcess;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.*;

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

    public BaseRequest(Student student, AcademicPeriod currentPeriod) {
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

    public RequestState getEstado() {return state;}
    public AcademicPeriod getCurrentPeriod() {return currentPeriod;}
    public void setCurrentPeriod(AcademicPeriod currentPeriod) {this.currentPeriod = currentPeriod;}


    public void approveRequest(String comentario){
        state.approveRequest(this);
        state = new EstadoAprobada();
        changeState(new ResponseProcess(RequestStateEnum.APROBADA, comentario));
    }
    public void rejectRequest(String comentario){
        state.rejectRequest(this);
        state = new EstadoRechazada();
        changeState(new ResponseProcess(RequestStateEnum.RECHAZADA, comentario));
    }
    public void pendingRequest(String comentario){
        state.pendingRequest(this);
        state = new EstadoPendiente();
        changeState(new ResponseProcess(RequestStateEnum.PENDIENTE, comentario));
    }
    public void reviewRequest(String comentario){
        state.reviewRequest(this);
        state = new EstadoEnRevision();
        changeState(new ResponseProcess(RequestStateEnum.EN_REVISION, comentario));
    }

    private void changeState(ResponseProcess proceso){
        agregarProceso(proceso);
        setActualState(proceso.getEstado());
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public RequestProcess getActualProcess(){return procesos.get(procesos.size() - 1);}
    public RequestStateEnum getActualState(){return getActualProcess().getEstado();}
    private void setActualState(RequestStateEnum estado){getActualProcess().setEstado(estado);}

    /**
     * Obtiene la fecha y hora de creación de la solicitud.
     * @return timestamp de creación
     */
    public LocalDateTime getCreadoEn() {return creadoEn;}
    public Student getStudent() {return student;}

}
