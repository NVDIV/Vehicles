package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public VehicleService(VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    // Pobranie wszystkich pojazdów
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Pobranie dostępnych pojazdów (te które nie są aktualnie wypożyczone)
    public List<Vehicle> findAvailableVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(v -> rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isEmpty())
                .toList();
    }

    // Pobranie pojazdu po ID
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id);
    }

    // Dodanie lub aktualizacja pojazdu
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    // Usunięcie pojazdu (pamiętaj, że jeśli jest wynajęty, trzeba obsłużyć błąd)
    public void deleteVehicle(String id) {
        // opcjonalnie: sprawdzenie czy pojazd jest wynajęty
        if (rentalRepository.findByVehicleIdAndReturnDateIsNull(id).isPresent()) {
            throw new IllegalStateException("Nie można usunąć pojazdu, który jest aktualnie wypożyczony.");
        }
        vehicleRepository.deleteById(id);
    }
}
