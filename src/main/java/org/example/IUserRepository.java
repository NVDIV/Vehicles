package org.example;

import java.io.IOException;
import java.util.List;

public interface IUserRepository {
    public User getUser(String login);
    public List<User> getUsers();
    public void save() throws IOException;
    public void load() throws IOException;
}
