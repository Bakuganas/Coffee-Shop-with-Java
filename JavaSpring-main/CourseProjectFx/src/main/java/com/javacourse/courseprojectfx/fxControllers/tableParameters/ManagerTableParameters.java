package com.javacourse.courseprojectfx.fxControllers.tableParameters;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ManagerTableParameters extends UserTableParameters{

    //TODO Here only those attributes that we get from Manager class

    public ManagerTableParameters() {
    }

    public ManagerTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty name, SimpleStringProperty password, SimpleStringProperty surname) {
        super(id, login, name, password, surname);
    }
}
