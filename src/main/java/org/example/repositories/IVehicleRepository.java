package org.example.repositories;

import org.example.models.Vehicle;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IVehicleRepository {

    abstract List<Vehicle> findAll();

    abstract Optional<Vehicle> findById(String id);

    abstract Vehicle save(Vehicle vehicle) throws IOException;

    abstract void deleteById(String id) throws IOException;
}

