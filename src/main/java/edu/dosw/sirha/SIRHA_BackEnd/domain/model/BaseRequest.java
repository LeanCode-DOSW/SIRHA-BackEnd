package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.time.LocalDateTime;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.*;

/**
 * Abstract base class for all academic requests in the SIRHA system.
 *
 * This class defines the common structure and base behavior for all types
 * of requests that students may submit, such as:
 * - Group change requests
 * - Course change requests
 * - Re-enrollment requests
 *
 * It follows the Template Method pattern, providing the general workflow
 * for requests and delegating the specific implementations of {@link #approve()}
 * and {@link #reject()} to concrete subclasses.
 *
 * Key features:
 * - Priority system for request ordering
 * - Well-defined states (PENDING, APPROVED, REJECTED, UNDER_REVIEW)
 * - Automatic creation timestamp
 * - Shared contract for all request types
 *
 * Possible request states:
 * - {@link RequestState#PENDING}: Newly created request, waiting for review
 * - {@link RequestState#APPROVED}: Request accepted and processed
 * - {@link RequestState#REJECTED}: Request denied with a reason
 * - {@link RequestState#UNDER_REVIEW}: Request currently being evaluated
 *
 * @see RequestState
 * @see Request
 * @see GroupChangeRequest
 * @see CourseChangeRequest
 */
public abstract class BaseRequest implements Request {
    private String id;
    private int priority;
    private RequestState state;
    private LocalDateTime createdAt;

    /**
     * Base constructor for all requests.
     *
     * Initializes a request with the specified priority,
     * default state {@link RequestState#PENDING}, and sets
     * the creation timestamp automatically.
     *
     * @param priority the priority level of the request (1-5).
     */
    public BaseRequest(int priority) {
        this.priority = priority;
        this.state = RequestState.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Approves the request.
     * The specific approval logic must be implemented by subclasses.
     */
    public abstract void approve();

    /**
     * Rejects the request.
     * The specific rejection logic must be implemented by subclasses.
     */
    public abstract void reject();

    // ---------- Getters & Setters ----------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the priority level of the request.
     *
     * @return the priority level (1-5).
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority level of the request.
     * Only allowed while the request is pending.
     *
     * @param priority new priority (1-5).
     * @throws IllegalArgumentException if the priority is out of range.
     */
    public void setPriority(int priority) {
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Priority must be between 1 and 5");
        }
        this.priority = priority;
    }

    /**
     * Gets the current state of the request.
     *
     * @return the request state.
     */
    public RequestState getState() {
        return state;
    }

    /**
     * Sets the current state of the request.
     * Intended to be used internally by {@link #approve()} and {@link #reject()}.
     *
     * @param state new request state.
     * @throws IllegalArgumentException if the state is null.
     */
    protected void setState(RequestState state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        this.state = state;
    }

    /**
     * Gets the timestamp of when the request was created.
     *
     * @return creation timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * Typically used for testing or data migration.
     *
     * @param createdAt new creation date and time.
     * @throws IllegalArgumentException if the timestamp is null or in the future.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Creation date cannot be null");
        }
        if (createdAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Creation date cannot be in the future");
        }
        this.createdAt = createdAt;
    }

    /**
     * Provides a string representation of the request with its basic information.
     *
     * @return formatted string including id, priority, state, and creation timestamp.
     */
    @Override
    public String toString() {
        return String.format("%s{id='%s', priority=%d, state=%s, createdAt=%s}",
                getClass().getSimpleName(), id, priority, state, createdAt);
    }
}
