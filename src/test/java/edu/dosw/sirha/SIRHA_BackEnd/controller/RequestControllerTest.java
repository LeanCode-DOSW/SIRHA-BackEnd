package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestState;
import edu.dosw.sirha.SIRHA_BackEnd.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestControllerTest {

    private RequestService requestService;
    private RequestController controller;

    static class DummyRequest extends BaseRequest {
        public DummyRequest(int priority) { super(priority); }
        @Override public void approve() { setState(RequestState.APPROVED); }
        @Override public void reject() { setState(RequestState.REJECTED); }
    }

    @BeforeEach
    void setUp() {
        requestService = mock(RequestService.class);
        controller = new RequestController(requestService);
    }

    @Test
    void getAll_ShouldReturnRequests() {
        DummyRequest req = new DummyRequest(3);
        when(requestService.findAll()).thenReturn(List.of(req));

        List<BaseRequest> result = controller.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void create_ShouldReturnSavedRequest() {
        DummyRequest req = new DummyRequest(2);
        when(requestService.save(req)).thenReturn(req);

        BaseRequest result = controller.create(req);

        assertEquals(2, result.getPriority());
    }

    @Test
    void approve_ShouldCallService() {
        controller.approve("123");
        verify(requestService).approveRequest("123");
    }

    @Test
    void reject_ShouldCallService() {
        controller.reject("456");
        verify(requestService).rejectRequest("456");
    }
}

