package org.example;

import java.util.Objects;

abstract public class Vehicle {
    private String id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private boolean rented;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Vehicle(String id, String brand, String model, int year, double price, boolean rented) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
    }

    public Vehicle() {
    }

    public Vehicle(Vehicle other) {
        this.id = other.id;
        this.brand = other.brand;
        this.model = other.model;
        this.year = other.year;
        this.price = other.price;
        this.rented = other.rented;

        // TODO: Opcjonalnie można przenieść "funkcjonal konstruktora kopijujacego" do copy()"
    }

    public abstract Vehicle copy();

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public String toCsv() {
        return id + ";" + brand + ";" + model + ";" + year + ";" + price + ";" + rented;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", rented=" + rented +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vehicle other = (Vehicle) object;
        return (Objects.equals(this.id, other.id)
                && Objects.equals(this.brand, other.brand)
                && Objects.equals(this.model, other.model)
                && Objects.equals(this.year, other.year))
                && Objects.equals(this.price, other.price)
                && Objects.equals(this.rented, other.rented);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, year, price, rented);
    }
}


