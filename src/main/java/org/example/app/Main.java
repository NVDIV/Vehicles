package org.example.app;

import org.example.repositories.impl.UserRole;
import org.example.services.AuthService;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.impl.UserJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VehicleJsonRepository vehicleRepo = new VehicleJsonRepository();
        UserJsonRepository userRepo = new UserJsonRepository();
        AuthService authService = new AuthService(userRepo);

        User currentUser = null;

        while (currentUser == null) {
            System.out.println("1. Zaloguj się\n2. Zarejestruj się");
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> {
                    System.out.println("Login:");
                    String login = scanner.nextLine();
                    System.out.println("Hasło:");
                    String password = scanner.nextLine();
                    Optional<User> user = authService.login(login, password);
                    if (user.isPresent()) {
                        currentUser = user.get();
                        System.out.println("Zalogowano jako: " + currentUser.getLogin());
                    } else {
                        System.out.println("Niepoprawny login lub hasło.");
                    }
                }
                case "2" -> {
                    System.out.println("Login:");
                    String login = scanner.nextLine();
                    System.out.println("Hasło:");
                    String password = scanner.nextLine();
                    System.out.println("Czy rejestrujesz się jako administrator? (tak/nie):");
                    String isAdminInput = scanner.nextLine();
                    boolean isAdmin = isAdminInput.equalsIgnoreCase("tak");
                    if (isAdmin) {
                        authService.register(login, password, UserRole.ADMIN);
                    } else {
                        authService.register(login, password, UserRole.CLIENT);
                    }

                    System.out.println("Zarejestrowano pomyślnie.");
                }
                default -> System.out.println("Nieprawidłowa opcja.");
            }
        }

        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        while (true) {
            if (isAdmin) {
                System.out.println("1. Dodaj pojazd\n2. Usuń pojazd\n3. Wyświetl wszystkie pojazdy\n0. Wyloguj się");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        Vehicle vehicle = createVehicle(scanner);
                        vehicleRepo.save(vehicle);
                        System.out.println("Dodano pojazd.");
                    }
                    case "2" -> {
                        System.out.println("Podaj ID pojazdu do usunięcia:");
                        String id = scanner.nextLine();
                        vehicleRepo.deleteById(id);
                        System.out.println("Usunięto pojazd.");
                    }
                    case "3" -> {
                        List<Vehicle> vehicles = vehicleRepo.findAll();
                        vehicles.forEach(System.out::println);
                    }
                    case "0" -> {
                        currentUser = null;
                        main(args); // restart aplikacji
                        return;
                    }
                    default -> System.out.println("Nieprawidłowa opcja.");
                }
            } else {
                System.out.println("1. Wypożycz pojazd\n2. Zwróć pojazd\n3. Wyświetl dostępne pojazdy\n0. Wyloguj się");
                String choice = scanner.nextLine();
                User finalCurrentUser = currentUser;
                switch (choice) {
                    case "1" -> {
                        List<Vehicle> available = vehicleRepo.findAll().stream()
                                .filter(v -> v.getAttribute("renter") == null)
                                .toList();
                        if (available.isEmpty()) {
                            System.out.println("Brak dostępnych pojazdów.");
                            break;
                        }
                        available.forEach(System.out::println);
                        System.out.println("Podaj ID pojazdu do wypożyczenia:");
                        String id = scanner.nextLine();
                        Optional<Vehicle> toRent = vehicleRepo.findById(id);
                        if (toRent.isPresent() && toRent.get().getAttribute("renter") == null) {
                            Vehicle v = toRent.get();
                            v.addAttribute("renter", currentUser.getLogin());
                            vehicleRepo.save(v);
                            System.out.println("Wypożyczono pojazd.");
                        } else {
                            System.out.println("Pojazd jest już wypożyczony lub nie istnieje.");
                        }
                    }
                    case "2" -> {
                        List<Vehicle> rented = vehicleRepo.findAll().stream()
                                .filter(v -> finalCurrentUser.getLogin().equals(v.getAttribute("renter")))
                                .toList();
                        if (rented.isEmpty()) {
                            System.out.println("Nie masz wypożyczonych pojazdów.");
                            break;
                        }
                        rented.forEach(System.out::println);
                        System.out.println("Podaj ID pojazdu do zwrotu:");
                        String id = scanner.nextLine();
                        Optional<Vehicle> toReturn = vehicleRepo.findById(id);
                        if (toReturn.isPresent() && currentUser.getLogin().equals(toReturn.get().getAttribute("renter"))) {
                            Vehicle v = toReturn.get();
                            v.removeAttribute("renter");
                            vehicleRepo.save(v);
                            System.out.println("Zwrócono pojazd.");
                        } else {
                            System.out.println("Nie masz uprawnień do zwrotu tego pojazdu.");
                        }
                    }
                    case "3" -> {
                        List<Vehicle> available = vehicleRepo.findAll().stream()
                                .filter(v -> v.getAttribute("renter") == null)
                                .toList();
                        available.forEach(System.out::println);
                    }
                    case "0" -> {
                        currentUser = null;
                        main(args);
                        return;
                    }
                    default -> System.out.println("Nieprawidłowa opcja.");
                }
            }
        }
    }

    private static Vehicle createVehicle(Scanner scanner) {
        System.out.println("ID:");
        String id = scanner.nextLine();
        System.out.println("Typ:");
        String type = scanner.nextLine();
        System.out.println("Marka:");
        String brand = scanner.nextLine();
        System.out.println("Model:");
        String model = scanner.nextLine();
        System.out.println("Rok:");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.println("Tablica rejestracyjna:");
        String plate = scanner.nextLine();
        return Vehicle.builder()
                .id(id)
                .type(type)
                .brand(brand)
                .model(model)
                .year(year)
                .plate(plate)
                .build();
    }
}
