package org.example;

public class Car extends Vehicle {

    public Car(String id, String brand, String model, int year, double price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    public Car(Car other) {
        super(other);
    }

    @Override
    public Vehicle copy() {
        return new Car(this);
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}