package org.example;

import java.io.IOException;
import java.util.Scanner;

public class UserInterface {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final Authentication authentication;
    private User loggedInUser;
    private final Scanner scanner;

    public UserInterface(VehicleRepository vehicleRepository, UserRepository userRepository, Authentication authentication) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.authentication = authentication;
        this.loggedInUser = null;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            if (loggedInUser == null) {
                login();
            } else {
                showMenu();
            }
        }
    }

    private void showMenu() {
        System.out.println("\n--- Wypożyczalnia Pojazdów ---");
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            System.out.println("1. Wyświetl dostępne pojazdy");
            System.out.println("2. Wypożycz pojazd");
            System.out.println("3. Zwróć pojazd");
            System.out.println("4. Wyświetl swoje dane");
            System.out.println("5. Wyloguj");
            System.out.println("--- Admin Panel ---");
            System.out.println("6. Dodaj pojazd");
            System.out.println("7. Usuń pojazd");
            System.out.println("8. Wyświetl dane użytkowników");
            System.out.print("Wybierz opcję: ");
        } else {
            System.out.println("1. Wyświetl dostępne pojazdy");
            System.out.println("2. Wypożycz pojazd");
            System.out.println("3. Zwróć pojazd");
            System.out.println("4. Wyświetl swoje dane");
            System.out.println("5. Wyloguj");
            System.out.print("Wybierz opcję: ");
        }

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                showVehicles();
                break;
            case "2":
                rentVehicle();
                break;
            case "3":
                returnVehicle();
                break;
            case "4":
                showUser();
                break;
            case "5":
                logout();
                break;
            case "6":
                if (loggedInUser.getRole() == UserRole.ADMIN) {
                    addVehicle();
                }
                break;
            case "7":
                if (loggedInUser.getRole() == UserRole.ADMIN) {
                    removeVehicle();
                }
                break;
            case "8":
                if (loggedInUser.getRole() == UserRole.ADMIN) {
                    showUsers();
                }

            default:
                System.out.println("Nieprawidłowa opcja, spróbuj ponownie.");
        }
    }

    private void login() {
        System.out.print("\nPodaj login: ");
        String login = scanner.nextLine();
        System.out.print("Podaj hasło: ");
        String password = scanner.nextLine();

        if (authentication.authenticate(login, password)) {
            loggedInUser = userRepository.getUser(login);
            System.out.println("Zalogowano pomyślnie!");
        } else {
            System.out.println("Błąd logowania. Spróbuj ponownie.");
        }
    }

    private void logout() {
        loggedInUser = null;
        System.out.println("Zostałeś wylogowany.");
    }

    private void addVehicle() {
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            // TODO: Check if that motorcycle or car
            System.out.print("\nPodaj dane pojazdu (ID, marka, model, rok, cena): ");
            String id = scanner.nextLine();
            String brand = scanner.nextLine();
            String model = scanner.nextLine();
            int year = Integer.parseInt(scanner.nextLine());
            double price = Double.parseDouble(scanner.nextLine());

            Vehicle newVehicle = new Car(id, brand, model, year, price, false);
            try {
                vehicleRepository.addVehicle(newVehicle);
                System.out.println("Pojazd dodany pomyślnie!");
            } catch (IOException e) {
                System.out.println("Błąd zapisu pojazdu: " + e.getMessage());
            }
        } else {
            System.out.println("Tylko administrator może dodawać pojazdy.");
        }
    }

    private void removeVehicle() {
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            System.out.print("\nPodaj ID pojazdu do usunięcia: ");
            String id = scanner.nextLine();
            Vehicle vehicleToRemove = vehicleRepository.getVehicle(id);
            if (vehicleToRemove != null) {
                try {
                    vehicleRepository.removeVehicle(vehicleToRemove);
                    System.out.println("Pojazd usunięty pomyślnie!");
                } catch (IOException e) {
                    System.out.println("Błąd usuwania pojazdu: " + e.getMessage());
                }
            } else {
                System.out.println("Pojazd nie znaleziony.");
            }
        } else {
            System.out.println("Tylko administrator może usuwać pojazdy.");
        }
    }

    private void showUsers() {
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            System.out.println("\n--- Lista Użytkowników ---");
            for (User user : userRepository.getUsers()) {
                System.out.println(user);
            }
        }
    }

    private void showUser() {
        System.out.println("\n--- Twoje dane ---");
        System.out.println(loggedInUser);
    }

    private void showVehicles() {
        System.out.println("\n--- Lista Pojazdów ---");
        for (Vehicle vehicle : vehicleRepository.getVehicles()) {
            System.out.println(vehicle);
        }
    }

    private void rentVehicle() {
        System.out.print("\nPodaj ID pojazdu do wypożyczenia: ");
        String id = scanner.nextLine();
        try {
            vehicleRepository.rentVehicle(id);
            loggedInUser.setRentedVehicleID(id);
            System.out.println("Pojazd wypożyczony!");
        } catch (IOException e) {
            System.out.println("Błąd wypożyczenia: " + e.getMessage());
        }
    }

    private void returnVehicle() {
        System.out.print("\nPodaj ID pojazdu do zwrotu: ");
        String id = scanner.nextLine();
        try {
            vehicleRepository.returnVehicle(id);
            loggedInUser.setRentedVehicleID(null);
            System.out.println("Pojazd zwrócony!");
        } catch (IOException e) {
            System.out.println("Błąd zwrotu: " + e.getMessage());
        }
    }
}
