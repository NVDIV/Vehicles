package org.example;

public class Authentication {

    private final UserRepository userRepository;

    public Authentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String login, String password) {
        for (User user : userRepository.getUsers()) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                System.out.println("Authentication successful for user: " + login);
                return true;
            }
        }
        System.out.println("Authentication failed for user: " + login);
        return false;
    }
}