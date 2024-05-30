package com.javacourse.courseprojectfx.fxControllers;

import com.javacourse.courseprojectfx.model.Customer;
import com.javacourse.courseprojectfx.model.User;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Test {
    public ListView<User> userList;
    public TextField loginField;
    public TextField nameField;
    public PasswordField passwordField;
    public TextField surnameField;

    public void createUser() {

        Customer customer = new Customer(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText());
        //userList.getItems().clear();
        userList.getItems().add(customer);
    }
}
