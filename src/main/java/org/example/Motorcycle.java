package org.example;

import java.util.Objects;

public class Motorcycle extends Vehicle {
    private String category;

    public Motorcycle(String id, String brand, String model, int year, double price, boolean rented, String category) {
        super(id, brand, model, year, price, rented);
        this.category = category;
    }

    public Motorcycle(Motorcycle other) {
        super(other);
        this.category = other.category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Motorcycle that = (Motorcycle) o;
        return Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category);
    }

    @Override
    public Vehicle copy() {
        return new Motorcycle(this);
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toCsv() {
        return super.toCsv() + ";" + category;
    }
}