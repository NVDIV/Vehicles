package org.example;

import java.io.IOException;
import java.util.List;

public interface IVehicleRepository {

    abstract void rentVehicle(String id) throws IOException;
    abstract void returnVehicle(String id) throws IOException;
    abstract Vehicle getVehicle(String id);
    abstract List<Vehicle> getVehicles();
    abstract void load() throws IOException;
    abstract void save() throws IOException;
    abstract void addVehicle(Vehicle vehicle) throws IOException;
    abstract void removeVehicle(Vehicle vehicle) throws IOException;
}
