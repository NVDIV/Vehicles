package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    private ArrayList<User> users;
    private static final String FILE_NAME = "users.csv";

    public UserRepository() throws IOException {
        this.users = new ArrayList<>();
        load();
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public User getUser(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void save() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
        for (User user : users) {
            writer.write(user.toCsv() + "\n");
        }
        writer.close();
    }

    @Override
    public void load() throws IOException {
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 4) {
                    String login = data[0];
                    String password = data[1];
                    UserRole role = UserRole.valueOf(data[2]);
                    String rentedVehicleID = data[3].equals("null") ? null : data[3];

                    users.add(new User(login, password, role, rentedVehicleID));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }
    }
}
