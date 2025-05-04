package org.example.services;

import org.example.models.User;
import org.example.repositories.IUserRepository;
import org.example.repositories.impl.UserRole;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

public class AuthService {
    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> register(String login, String password, UserRole role) {
        if (userRepository.findByLogin(login).isPresent()) {
            return Optional.empty(); // login already taken
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = User.builder()
                .id(UUID.randomUUID().toString())
                .login(login)
                .password(hashedPassword)
                .role(role)
                .build();

        userRepository.save(newUser);
        return Optional.of(newUser);
    }

    public Optional<User> login(String login, String password) {
        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty(); // incorrect login or password
    }
}
