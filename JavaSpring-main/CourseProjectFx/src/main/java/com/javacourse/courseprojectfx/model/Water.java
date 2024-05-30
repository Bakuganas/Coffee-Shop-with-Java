package com.javacourse.courseprojectfx.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Water extends Product {
    WaterType waterType;

    public Water(String title, String description, int qty, String manufacturer, float capacity, WaterType waterType) {
        super(title, description, qty, manufacturer, capacity);
        this.waterType = waterType;
    }
}
