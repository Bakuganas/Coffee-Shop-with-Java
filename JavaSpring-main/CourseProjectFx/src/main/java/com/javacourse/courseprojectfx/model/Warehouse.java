package com.javacourse.courseprojectfx.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Product> productList;
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Manager> employees;
    private LocalDate dateCreated;
    private LocalDate dateModified;

    @Enumerated(EnumType.STRING)
    private City city;

    public Warehouse(String address, List<Product> productList, List<Manager> employees, City city) {
        this.address = address;
        this.productList = productList;
        this.employees = employees;
        this.city = city;
    }

    public Warehouse(String address, City city) {
        this.address = address;
        this.city = city;
    }

    @Override
    public String toString() { return id+ ": " +address;}
}
