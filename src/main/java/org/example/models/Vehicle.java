package org.example.models;

import lombok.*;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    private String id;
    private String type;
    private String brand;
    private String model;
    private int year;
    private String plate;

    @Builder.Default
    private Map<String, Object> attributes = Map.of();

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return year == vehicle.year &&
                Objects.equals(id, vehicle.id) &&
                Objects.equals(type, vehicle.type) &&
                Objects.equals(brand, vehicle.brand) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(plate, vehicle.plate) &&
                Objects.equals(attributes, vehicle.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, brand, model, year, plate, attributes);
    }
}
