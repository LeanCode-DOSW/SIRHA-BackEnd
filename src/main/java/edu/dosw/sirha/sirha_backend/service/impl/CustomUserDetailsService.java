package edu.dosw.sirha.sirha_backend.service.impl;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.dosw.sirha.sirha_backend.repository.mongo.AccountMongoRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountMongoRepository accounts;

    public CustomUserDetailsService(AccountMongoRepository accounts) {
        this.accounts = accounts;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var acc = accounts.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(acc.getUsername())
            .password(acc.getPasswordHash())
            .authorities(List.of(() -> "ROLE_" + acc.getRole().name()))
            .build();
    }
}
