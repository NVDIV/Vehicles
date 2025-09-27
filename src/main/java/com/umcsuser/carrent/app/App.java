package com.umcsuser.carrent.app;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.impl.json.UserJsonRepository;
import com.umcsuser.carrent.repositories.impl.json.VehicleJsonRepository;
import com.umcsuser.carrent.services.AuthService;
import com.umcsuser.carrent.services.RentalService;
import com.umcsuser.carrent.services.VehicleService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run() {
        System.out.println("=== Witaj w CarRent! ===");

        User currentUser = null;

        // Login or registration loop
        while (currentUser == null) {
            System.out.println("1. Zaloguj się\n2. Zarejestruj się");
            String option = scanner.nextLine().trim();
            switch (option) {
                case "1" -> currentUser = login();
                case "2" -> register();
                default -> System.out.println("Nieprawidłowa opcja.");
            }
        }

        // Admin or client menu
        if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            adminMenu();
        } else {
            clientMenu(currentUser);
        }
    }

    private User login() {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();

        Optional<User> userOpt = authService.login(login, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Zalogowano jako: " + user.getLogin());
            return user;
        } else {
            System.out.println("Niepoprawny login lub hasło.");
            return null;
        }
    }

    private void register() {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();
        System.out.print("Czy rejestrujesz się jako administrator? (tak/nie): ");
        boolean isAdmin = scanner.nextLine().trim().equalsIgnoreCase("tak");

        authService.register(login, password, isAdmin ? "ADMIN" : "USER");
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n=== Menu Admina ===");
            System.out.println("1. Dodaj pojazd");
            System.out.println("2. Usuń pojazd");
            System.out.println("3. Wyświetl wszystkie pojazdy");
            System.out.println("0. Wyloguj się");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    Vehicle vehicle = createVehicle();
                    vehicleService.saveVehicle(vehicle);
                    System.out.println("Dodano pojazd.");
                }
                case "2" -> {
                    System.out.print("Podaj ID pojazdu do usunięcia: ");
                    String id = scanner.nextLine().trim();
                    vehicleService.deleteVehicle(id);
                    System.out.println("Usunięto pojazd.");
                }
                case "3" -> vehicleService.findAllVehicles().forEach(System.out::println);
                case "0" -> {
                    System.out.println("Wylogowano.");
                    return;
                }
                default -> System.out.println("Niepoprawna opcja.");
            }
        }
    }

    private void clientMenu(User user) {
        while (true) {
            System.out.println("\n=== Menu Klienta ===");
            System.out.println("1. Wypożycz pojazd");
            System.out.println("2. Zwróć pojazd");
            System.out.println("3. Wyświetl dostępne pojazdy");
            System.out.println("0. Wyloguj się");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> rentVehicle(user);
                case "2" -> returnVehicle(user);
                case "3" -> vehicleService.findAvailableVehicles().forEach(System.out::println);
                case "0" -> {
                    System.out.println("Wylogowano.");
                    return;
                }
                default -> System.out.println("Niepoprawna opcja.");
            }
        }
    }

    private void rentVehicle(User user) {
        vehicleService.findAvailableVehicles().forEach(System.out::println);
        System.out.print("Podaj ID pojazdu do wypożyczenia: ");
        String id = scanner.nextLine().trim();
        try {
            rentalService.rentVehicle(id, user.getId());
            System.out.println("Pojazd został wypożyczony!");
        } catch (IllegalStateException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    private void returnVehicle(User user) {
        System.out.print("Podaj ID pojazdu do zwrotu: ");
        String id = scanner.nextLine().trim();
        try {
            rentalService.returnVehicle(id);
            System.out.println("Pojazd został zwrócony!");
        } catch (IllegalStateException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    private Vehicle createVehicle() {
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Kategoria: ");
        String category = scanner.nextLine().trim();

        System.out.print("Marka: ");
        String brand = scanner.nextLine().trim();

        System.out.print("Model: ");
        String model = scanner.nextLine().trim();

        System.out.print("Rok: ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Tablica rejestracyjna: ");
        String plate = scanner.nextLine().trim();

        System.out.print("Cena: ");
        double price = Double.parseDouble(scanner.nextLine().trim());

        return Vehicle.builder()
                .id(id)
                .category(category)
                .brand(brand)
                .model(model)
                .year(year)
                .plate(plate)
                .price(price)
                .build();
    }
}
