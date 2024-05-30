package com.javacourse.courseprojectfx.fxControllers.tableParameters;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserTableParameters {

    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty login = new SimpleStringProperty();
    SimpleStringProperty name = new SimpleStringProperty();
    SimpleStringProperty password = new SimpleStringProperty();
    SimpleStringProperty surname = new SimpleStringProperty();


    //TODO Complete remaining fields that are in User class
    //Do not forget constructor, getters and setters


    public UserTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty name, SimpleStringProperty password, SimpleStringProperty surname) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
        this.surname = surname;
    }

    public UserTableParameters() {
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

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }
}
