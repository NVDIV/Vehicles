package org.example.repositories.impl;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.User;
import org.example.repositories.IUserRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Type;

public class UserJsonRepository implements IUserRepository {
    private static final String FILE_NAME = "users.json";
    private final Gson gson = new Gson();
    private List<User> users = new ArrayList<>();

    public UserJsonRepository() {
        load();
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private void load() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            users = gson.fromJson(reader, userListType);
            if (users == null) {
                users = new ArrayList<>();
            }
        } catch (IOException e) {
            users = new ArrayList<>();
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst();
    }

    @Override
    public User save(User user) {
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        saveToFile();
        return user;
    }

    @Override
    public void deleteById(String id) {
        users.removeIf(user -> user.getId().equals(id));
        saveToFile();
    }
}
