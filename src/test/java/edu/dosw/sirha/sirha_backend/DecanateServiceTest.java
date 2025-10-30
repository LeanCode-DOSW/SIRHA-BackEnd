package edu.dosw.sirha.sirha_backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static sun.java2d.cmm.ProfileDataVerifier.verify;

@ExtendWith(MockitoExtension.class)
class DecanateServiceTest {

    @Mock
    private edu.dosw.sirha.sirha_backend.repository.mongo.AccountMongoRepository repo;

    @InjectMocks
    private edu.dosw.sirha.sirha_backend.service.DecanateService service;

    @Test
    void registerDecanate_ShouldSave_WhenValidData() {
        edu.dosw.sirha.sirha_backend.dto.RegisterRequest req = new edu.dosw.sirha.sirha_backend.dto.RegisterRequest();
        req.setUsername("dean");
        req.setEmail("d@mail.com");
        req.setPassword("123");

        when(repo.existsByUsername(anyString())).thenReturn(false);
        service.registerDecanate(req);
        verify(repo, times(1)).save(any());
    }
}


