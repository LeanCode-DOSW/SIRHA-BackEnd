package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import java.util.ArrayList;
import java.util.List;
import edu.dosw.sirha.sirha_backend.domain.model.AcademicPeriod;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.SubjectProgress;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.*;

public class SubjectDecorator {
    private final Subject subject;
    private SemaforoColores estadoColor;
    private int semestre;
    private SubjectState state;
    private Group group;
    private int grade;
    private List<SubjectStateProcess> history;

    public SubjectDecorator(Subject subject) {
        this.subject = subject;
        this.state = new NoCursadaState();
        state.setState(this);
        history = new ArrayList<>();
    }

    public void setGroup(Group group) { state.setGroup(this, group); }
    public void setSemester(int semester) { state.setSemester(this, semester); }
    public void setGrade(int grade) { state.setGrade(this, grade); }

    void setState(SubjectState state) {this.state = state;}
    void setEstadoColor(SemaforoColores estadoColor) {this.estadoColor = estadoColor;}
    void setSemesterDirect(int semester){this.semestre = semester;}
    void setGroupDirect(Group group) { this.group = group; }
    void setGradeDirect(int grade) { this.grade = grade; }

    public void inscribir(Group grupo) { 
        state.inscribir(this, grupo); 
        recordChangeState(new SubjectProgress(SemaforoColores.GRIS, semestre, grupo, 0));
    }

    public void inscribir(Group grupo, int semester) {
        inscribir(grupo);
        state.setSemester(this, semester);
        
    }
    public void aprobar()   { state.aprobar(this); recordChangeState(new SubjectProgress(SemaforoColores.VERDE, this.semestre, this.group, this.grade));}
    public void reprobar()  { state.reprobar(this); recordChangeState(new SubjectProgress(SemaforoColores.ROJO, this.semestre, this.group, this.grade)); }
    public void retirar()   { state.retirar(this); recordChangeState(new SubjectProgress(SemaforoColores.AMARILLO, this.semestre, this.group, this.grade));}
    
    private void recordChangeState(SubjectStateProcess stateProcess){
        addState(stateProcess);
    }
    public List<Schedule> getSchedules() {return group.getSchedules();}
    public AcademicPeriod getAcademicPeriod() {return group.getCurrentPeriod();}

    public boolean canEnroll() {return state.canEnroll();}
    public boolean estaCursando() {return estadoColor == SemaforoColores.AMARILLO;}
    public boolean estaAprobada() {return estadoColor == SemaforoColores.VERDE;}
    public boolean estaReprobada() {return estadoColor == SemaforoColores.ROJO;}

    public void addState(SubjectStateProcess state) {history.add(state);}
    public SubjectStateProcess getLastStateProcess() {return history.get(history.size() - 1);}

    public String getName() {return subject.getName();}
    public int getCredits() {return subject.getCredits();}    
    public List<Group> getGroups() {return subject.getGroups();}
    public String getId() { return subject.getId(); }
    public SemaforoColores getEstadoColor() {return estadoColor;}
    public int getSemester() {return semestre;}
    public Subject getSubject() {return subject;}
    public SubjectState getState() {return state;}
    public Group getGroup() { return group; }
    public int getGrade() { return grade; }
    public List<SubjectStateProcess> getHistory() { return new ArrayList<>(history); }
}
