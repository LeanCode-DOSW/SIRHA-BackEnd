package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.TrafficLightColor;

/**
 * Entidad del dominio que representa a un estudiante en el sistema SIRHA.
 *
 * Esta clase extiende de User y añade funcionalidades específicas para estudiantes,
 * incluyendo gestión de código estudiantil, plan de estudios, semáforo académico
 * y solicitudes realizadas.
 *
 * El estudiante es la entidad central del sistema, ya que todas las operaciones
 * principales (inscripciones, cambios de grupo, solicitudes) se realizan en
 * torno a esta entidad.
 *
 * Características principales:
 * - Hereda propiedades básicas de User (id, username, password, rol)
 * - Tiene un código estudiantil único en el sistema
 * - Posee un plan de estudios asignado
 * - Mantiene un semáforo de estado académico por materia
 * - Gestiona una lista de solicitudes académicas
 *
 * @see User
 * @see StudyPlan
 * @see TrafficLight
 * @see BaseRequest
 */
@Document(collection = "students")
public class Student extends User {
    private String studentCode;
    private StudyPlan studyPlan;
    private TrafficLight trafficLight;
    private List<BaseRequest> requests;

    public Student() {
        super();
    }

    /**
     * Constructor principal para crear un nuevo estudiante.
     *
     * @param id identificador único del estudiante en el sistema
     * @param username nombre de usuario para acceso al sistema
     * @param email correo electrónico del estudiante
     * @param passwordHash contraseña ya hasheada
     * @param studentCode código estudiantil único
     *
     * @throws IllegalArgumentException si studentCode es null o vacío
     */
    public Student(String id, String username, String email, String passwordHash, String studentCode) {
        super(id, username, email, passwordHash);
        if (studentCode == null || studentCode.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de estudiante no puede ser null o vacío");
        }
        this.studentCode = studentCode;
    }

    /**
     * Obtiene el color del semáforo académico para una materia específica.
     *
     * @param subjectCode código de la materia
     * @return color actual en el semáforo académico
     */
    public TrafficLightColor getSubjectColor(String subjectCode) {
        return trafficLight.getColor(subjectCode);
    }

    /**
     * Agrega una nueva solicitud académica al estudiante.
     *
     * @param request solicitud académica a agregar
     * @throws IllegalArgumentException si la solicitud es null
     */
    public void addRequest(BaseRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser null");
        }
        if (this.requests == null) {
            this.requests = new ArrayList<>();
        }
        this.requests.add(request);
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        if (studentCode == null || studentCode.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede ser null o vacío");
        }
        this.studentCode = studentCode;
    }

    public StudyPlan getStudyPlan() {
        return studyPlan;
    }

    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public List<BaseRequest> getRequests() {
        if (requests == null) {
            requests = new ArrayList<>();
        }
        return requests;
    }

    public void setRequests(List<BaseRequest> requests) {
        this.requests = requests != null ? requests : new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        Student student = (Student) obj;
        return Objects.equals(studentCode, student.studentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentCode);
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', username='%s', studentCode='%s', numRequests=%d}",
                getId(), getUsername(), studentCode,
                requests != null ? requests.size() : 0);
    }
}
