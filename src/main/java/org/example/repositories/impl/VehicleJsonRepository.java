package org.example.repositories.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Vehicle;
import org.example.repositories.IVehicleRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehicleJsonRepository implements IVehicleRepository {
    private static final String FILE_NAME = "vehicles.json";
    private final Gson gson = new Gson();
    private final List<Vehicle> vehicles;

    public VehicleJsonRepository() {
        this.vehicles = new ArrayList<>(load());
    }

    private List<Vehicle> load() {
        try {
            if (Files.exists(Path.of(FILE_NAME))) {
                String json = Files.readString(Path.of(FILE_NAME));
                return gson.fromJson(json, new TypeToken<List<Vehicle>>() {}.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void save() {
        try {
            String json = gson.toJson(vehicles);
            Files.writeString(Path.of(FILE_NAME), json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicles.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
        } else {
            deleteById(vehicle.getId());
        }
        vehicles.add(vehicle);
        save();
        return vehicle;
    }

    @Override
    public void deleteById(String id) {
        vehicles.removeIf(v -> v.getId().equals(id));
        save();
    }
}
