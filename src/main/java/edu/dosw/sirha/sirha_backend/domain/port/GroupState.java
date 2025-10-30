package edu.dosw.sirha.sirha_backend.domain.port;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.StatusClosed;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.StatusOpen;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Marker interface for group state (State pattern).
 * Added Jackson annotations to support polymorphic serialization/deserialization
 * of concrete state implementations (StatusOpen, StatusClosed).
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "stateType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StatusOpen.class, name = "open"),
    @JsonSubTypes.Type(value = StatusClosed.class, name = "closed")
})
public interface GroupState {
    boolean addStudent(Group group, Student student) throws SirhaException;
    boolean removeStudent(Group group, Student student) throws SirhaException;
}
