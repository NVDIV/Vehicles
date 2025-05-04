package org.example.repositories.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Rental;
import org.example.repositories.IRentalRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalJsonRepository implements IRentalRepository {
    private static final String FILE_NAME = "rentals.json";
    private final Gson gson = new Gson();
    private List<Rental> rentals;

    public RentalJsonRepository() {
        load();
    }

    private void load() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type rentalListType = new TypeToken<ArrayList<Rental>>() {}.getType();
            rentals = gson.fromJson(reader, rentalListType);
            if (rentals == null) {
                rentals = new ArrayList<>();
            }
        } catch (IOException e) {
            rentals = new ArrayList<>();
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(rentals, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Optional<Rental> findById(String id) {
        return rentals.stream().filter(rental -> rental.getId().equals(id)).findFirst();
    }

    @Override
    public List<Rental> findByUserId(String userId) {
        List<Rental> result = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getUserId().equals(userId)) {
                result.add(rental);
            }
        }
        return result;
    }

    @Override
    public List<Rental> findByVehicleId(String vehicleId) {
        List<Rental> result = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getVehicleId().equals(vehicleId)) {
                result.add(rental);
            }
        }
        return result;
    }

    @Override
    public Rental save(Rental rental) {
        rentals.removeIf(r -> r.getId().equals(rental.getId())); // Unikalność ID
        rentals.add(rental);
        save();
        return rental;
    }

    @Override
    public void deleteById(String id) {
        rentals.removeIf(rental -> rental.getId().equals(id));
        save();
    }
}
