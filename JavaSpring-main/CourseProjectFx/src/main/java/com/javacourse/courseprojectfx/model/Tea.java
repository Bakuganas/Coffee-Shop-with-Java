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
public class Tea extends Product {
    TeaType teaType;

    public Tea(String title, String description, int qty, String manufacturer, float capacity, TeaType teaType) {
        super(title, description, qty, manufacturer, capacity);
        this.teaType = teaType;
    }
}
