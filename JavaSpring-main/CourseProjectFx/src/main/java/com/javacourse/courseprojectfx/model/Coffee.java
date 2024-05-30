package com.javacourse.courseprojectfx.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public final class Coffee extends Product {
    CoffeeType coffeeType;

    public Coffee(String title, String description, int qty, String manufacturer, float capacity, CoffeeType coffeeType) {
        super(title, description, qty, manufacturer, capacity);
        this.coffeeType = coffeeType;
    }
}
