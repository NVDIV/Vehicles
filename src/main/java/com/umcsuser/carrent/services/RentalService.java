package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.repositories.RentalRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    // Pobranie wszystkich wypożyczeń
    public List<Rental> findAllRentals() {
        return rentalRepository.findAll();
    }

    // Pobranie wypożyczenia po ID
    public Optional<Rental> findById(String id) {
        return rentalRepository.findById(id);
    }

    // Wypożyczenie pojazdu
    public Rental rentVehicle(String vehicleId, String userId) {
        // Sprawdzenie czy pojazd jest już wypożyczony
        Optional<Rental> activeRental = rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
        if (activeRental.isPresent()) {
            throw new IllegalStateException("Pojazd jest już wypożyczony!");
        }

        Rental rental = Rental.builder()
                .vehicleId(vehicleId)
                .userId(userId)
                .rentDate(LocalDate.now().toString())
                .build();

        return rentalRepository.save(rental);
    }

    // Zwrot pojazdu
    public Rental returnVehicle(String vehicleId) {
        Optional<Rental> activeRental = rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
        if (activeRental.isEmpty()) {
            throw new IllegalStateException("Pojazd nie jest aktualnie wypożyczony!");
        }

        Rental rental = activeRental.get();
        rental.setReturnDate(LocalDate.now().toString());
        return rentalRepository.save(rental);
    }
}
