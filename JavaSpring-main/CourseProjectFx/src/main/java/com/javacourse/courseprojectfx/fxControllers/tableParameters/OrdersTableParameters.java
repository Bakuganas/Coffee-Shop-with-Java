package com.javacourse.courseprojectfx.fxControllers.tableParameters;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrdersTableParameters {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty title = new SimpleStringProperty();
    SimpleStringProperty manufacturer = new SimpleStringProperty();
    SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    SimpleFloatProperty capacity = new SimpleFloatProperty();

    public OrdersTableParameters() {
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getManufacturer() {
        return manufacturer.get();
    }

    public SimpleStringProperty manufacturerProperty() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }


    public float getCapacity() {
        return capacity.get();
    }

    public SimpleFloatProperty capacityProperty() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity.set(capacity);
    }
}
