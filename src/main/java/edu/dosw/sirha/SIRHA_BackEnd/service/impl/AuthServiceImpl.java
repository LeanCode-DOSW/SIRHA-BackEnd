package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.UserMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import org.springframework.stereotype.Service;
import java.util.Optional;  
import edu.dosw.sirha.SIRHA_BackEnd.service.*;
import edu.dosw.sirha.SIRHA_BackEnd.util.PasswordUtils;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserMongoRepository userRepository;

    public AuthServiceImpl(UserMongoRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> PasswordUtils.verifyPassword(password, u.getPasswordHash()));
    }

    @Override
    public User register(User user) {
        user.setPasswordHash(PasswordUtils.hashPassword(user.getPasswordHash()));
        return userRepository.save(user);
    }
}
