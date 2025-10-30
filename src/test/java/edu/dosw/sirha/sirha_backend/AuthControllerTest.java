package edu.dosw.sirha.sirha_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private edu.dosw.sirha.sirha_backend.repository.mongo.AccountMongoRepository accountRepo;

    @MockBean
    private edu.dosw.sirha.sirha_backend.service.StudentService studentService;

    @MockBean
    private edu.dosw.sirha.sirha_backend.service.DecanateService decanateService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private edu.dosw.sirha.sirha_backend.util.JwtUtil jwt;

    @Test
    void registerStudent_ShouldReturnOk_WhenNewUser() throws Exception {
        edu.dosw.sirha.sirha_backend.dto.RegisterRequest req = new edu.dosw.sirha.sirha_backend.dto.RegisterRequest();
        req.setUsername("newUser");
        req.setEmail("test@mail.com");
        req.setPassword("123");

        when(accountRepo.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("encoded");

        mockMvc.perform(post("/api/auth/register-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}

