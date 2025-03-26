package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Tworzymy przykładową listę pojazdów
        ArrayList<Vehicle> sampleVehicles = new ArrayList<>();
        sampleVehicles.add(new Car("C001", "Toyota", "Corolla", 2018, 120, true));
        sampleVehicles.add(new Car("C002", "Ford", "Focus", 2020, 150, false));
        sampleVehicles.add(new Motorcycle("M001", "Yamaha", "YZF-R3", 2019, 100, false, "Sport"));
        sampleVehicles.add(new Motorcycle("M002", "Honda", "CB500F", 2021, 130, false, "Naked"));

        // Tworzymy repozytorium z pojazdami
        VehicleRepository repository = new VehicleRepository();
        repository.setVehicles(sampleVehicles);

        // Zapisujemy pojazdy do pliku (opcjonalne, jeśli chcemy wczytać je później)
        try {
            repository.save();
        } catch (Exception e) {
            System.out.println("Błąd zapisu: " + e.getMessage());
        }

        ArrayList<User> sampleUsers = new ArrayList<>();
        sampleUsers.add(new User("alice", "password123", UserRole.CLIENT, "C001"));
        sampleUsers.add(new User("bob", "securePass", UserRole.ADMIN, null));

        UserRepository userRepository = new UserRepository();
        userRepository.setUsers(sampleUsers);

        try {
            userRepository.save();
        } catch (Exception e) {
            System.out.println("Błąd zapisu: " + e.getMessage());
        }

        Authentication auth = new Authentication(userRepository);

        // Uruchamiamy interfejs użytkownika
        UserInterface ui = new UserInterface(repository, userRepository, auth);
        ui.start();
    }
}