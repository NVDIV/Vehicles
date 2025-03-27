package org.example;

public class User {
    private String login;
    private String password;
    private UserRole role;
    private String rentedVehicleID;

    public User(String login, String password, UserRole role, String rentedVehicleID) {
        this.login = login;
        this.password = Authentication.hashPassword(password);
        this.role = role;
        this.rentedVehicleID = rentedVehicleID;
    }

    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public String getRentedVehicleID() {
        return rentedVehicleID;
    }

    public void setRentedVehicleID(String rentedVehicleID) {
        this.rentedVehicleID = rentedVehicleID;
    }

    public String toCsv() {
        return login + ";" + password + ";" + role + ";" + (rentedVehicleID == null ? "null" : rentedVehicleID);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", role=" + role +
                ", rentedVehicleID=" + (rentedVehicleID == null ? "None" : rentedVehicleID) +
                '}';
    }
}
