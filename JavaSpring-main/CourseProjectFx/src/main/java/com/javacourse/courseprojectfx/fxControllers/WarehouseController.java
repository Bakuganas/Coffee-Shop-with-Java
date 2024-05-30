package com.javacourse.courseprojectfx.fxControllers;

import com.javacourse.courseprojectfx.hibernate.ShopHibernate;
import com.javacourse.courseprojectfx.model.City;
import com.javacourse.courseprojectfx.model.Manager;
import com.javacourse.courseprojectfx.model.Product;
import com.javacourse.courseprojectfx.model.Warehouse;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WarehouseController implements Initializable {
    public TextField addressTextField;
    public ComboBox<City> cityComboBox;
    public ListView<Manager> managerList;
    public ListView<Product> productList;
    public ListView<Manager> managerAddList;
    public ListView<Product> productAddList;
    public Button createButton;
    private ShopHibernate shopHibernate;
    private EntityManagerFactory entityManagerFactory;
    private boolean update;
    Warehouse warehouse;

    public void setData(EntityManagerFactory entityManagerFactory, boolean update, Warehouse warehouse) {
        this.entityManagerFactory = entityManagerFactory;
        this.shopHibernate = new ShopHibernate(entityManagerFactory);
        this.update = update;
        this.warehouse = warehouse;
        setVisibility();
        if (!update) {
            managerList.getItems().addAll(shopHibernate.loadAvailableManagersForWarehouse());
            productList.getItems().addAll(shopHibernate.loadAvailableProductsForWarehouse());
        } else {
            managerList.getItems().addAll(shopHibernate.getAllRecords(Manager.class));
            List<Manager> managers = warehouse.getEmployees();
            int size = shopHibernate.getAllRecords(Manager.class).size();
            for (var i = size - 1; i >= 0; i--) {
                Manager listManager = managerList.getItems().get(i);
                for (Manager m : managers) {
                    if (listManager.getId() == m.getId()) {
                        managerList.getItems().remove(listManager);
                        //size--;
                    }
                }
            }
            managerAddList.getItems().addAll(warehouse.getEmployees());

            productList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
            List<Product> products = warehouse.getProductList();
            size = shopHibernate.getAllRecords(Product.class).size();
            for (var i = size - 1; i >= 0; i--) {
                Product listProduct = productList.getItems().get(i);
                for (Product p : products) {
                    if (listProduct.getId() == p.getId()) {
                        productList.getItems().remove(listProduct);
                        //size--;
                    }
                }
            }
            productAddList.getItems().addAll(warehouse.getProductList());
        }

    }

    public void removeManager() {
        Manager manager = managerAddList.getSelectionModel().getSelectedItem();
        if (manager != null) {
            managerList.getItems().add(manager);
            managerAddList.getItems().remove(manager);
        }

    }

    public void addManager() {
        Manager manager = managerList.getSelectionModel().getSelectedItem();
        if (manager != null) {
            managerList.getItems().remove(manager);
            managerAddList.getItems().add(manager);
        }

    }

    public void removeProduct() {
        Product product = productAddList.getSelectionModel().getSelectedItem();
        if (product != null) {
            productList.getItems().add(product);
            productAddList.getItems().remove(product);
        }
    }

    public void addProduct() {
        Product product = productList.getSelectionModel().getSelectedItem();
        if (product != null) {
            productList.getItems().remove(product);
            productAddList.getItems().add(product);
        }
    }

    public void createWarehouse() {
        if (!update) {
            if (!addressTextField.getText().trim().isEmpty() && !cityComboBox.getSelectionModel().isEmpty()) {
                shopHibernate.createWarehouseInDB(productAddList.getItems(), managerAddList.getItems(), addressTextField.getText(), cityComboBox.getValue());
                FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Successful", "Warehouse was created successfully");
            } else {
                FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Failure", "Warehouse was not created");
            }
        } else {
            shopHibernate.updateWarehouse(warehouse.getId(), productAddList.getItems(), managerAddList.getItems());
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Warehouse was updated successfully");
        }

    }

    public void setVisibility() {
        if (update) {
            createButton.setText("Update");
            addressTextField.setVisible(false);
            cityComboBox.setVisible(false);
        } else {
            createButton.setText("Create");
            addressTextField.setVisible(true);
            cityComboBox.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cityComboBox.getItems().addAll(City.values());
        entityManagerFactory = Persistence.createEntityManagerFactory("Shop");
    }
}
