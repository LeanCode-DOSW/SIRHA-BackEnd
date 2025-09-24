package edu.dosw.sirha.SIRHA_BackEnd.service;


import java.util.Optional;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.User;

public interface AuthService {
    Optional<User> login(String username, String password);
    User register(User user);
    boolean existsByEmail(String email);
}
