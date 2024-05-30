package com.javacourse.courseprojectfx.fxControllers.tableParameters;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CustomerTableParameters extends UserTableParameters{
    //TODO complete based on UserTableParameters, customer unique attributes
    public CustomerTableParameters() {

    }

    public CustomerTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty name, SimpleStringProperty password, SimpleStringProperty surname) {
        super(id, login, name, password, surname);
    }
}
