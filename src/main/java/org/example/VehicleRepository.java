package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository {
    private ArrayList<Vehicle> vehicles;
    private static final String FILE_NAME = "vehicles.csv";

    public VehicleRepository() throws IOException {
        this.vehicles = new ArrayList<>();
        load();
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public void rentVehicle(String id) throws IOException {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId().equals(id) && !vehicle.isRented()) {
                vehicle.setRented(true);
                save();
                return;
            }
        }
        System.out.println("Vehicle not found or already rented.");
    }

    @Override
    public void returnVehicle(String id) throws IOException {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId().equals(id) && vehicle.isRented()) {
                vehicle.setRented(false);
                save();
                return;
            }
        }
        System.out.println("Vehicle not found or is not rented.");
    }

    @Override
    public Vehicle getVehicle(String id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId().equals(id)) {
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> copy = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            copy.add(vehicle.copy());
        }

        return copy;
    }

    @Override
    public void addVehicle(Vehicle vehicle) throws IOException {
        for (Vehicle existingVehicle : vehicles) {
            if (existingVehicle.equals(vehicle)) {
                System.out.println("Vehicle already exists.");
                return;
            }
        }
        vehicles.add(vehicle.copy());
        save();
        System.out.println("Vehicle successfully added.");
    }

    @Override
    public void removeVehicle(Vehicle vehicle) throws IOException {
        for (Vehicle existingVehicle : vehicles) {
            if (existingVehicle.equals(vehicle)) {
                vehicles.remove(existingVehicle);
                save();
                System.out.println("Vehicle successfully removed.");
                return;
            }
        }
        System.out.println("No such vehicle exists.");
    }

    @Override
    public void load() throws IOException {
        vehicles.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6) {
                    String id = data[0];
                    String brand = data[1];
                    String model = data[2];
                    int year = Integer.parseInt(data[3]);
                    double price = Double.parseDouble(data[4]);
                    boolean rented = Boolean.parseBoolean(data[5]);

                    Vehicle vehicle;
                    if (data.length == 7) {
                        String category = data[6];
                        vehicle = new Motorcycle(id, brand, model, year, price, rented, category);
                    } else {
                        vehicle = new Car(id, brand, model, year, price, rented);
                    }
                    vehicles.add(vehicle);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void save() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
        for (Vehicle vehicle : vehicles) {
            writer.write(vehicle.toCsv() + "\n");
        }
        writer.close();
    }
}
