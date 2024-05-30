package com.javacourse.courseprojectfx.fxControllers;

import com.javacourse.courseprojectfx.HelloApplication;
import com.javacourse.courseprojectfx.fxControllers.tableParameters.CustomerTableParameters;
import com.javacourse.courseprojectfx.fxControllers.tableParameters.ManagerTableParameters;
import com.javacourse.courseprojectfx.fxControllers.tableParameters.OrdersTableParameters;
import com.javacourse.courseprojectfx.hibernate.GenericHibernate;
import com.javacourse.courseprojectfx.hibernate.ShopHibernate;
import com.javacourse.courseprojectfx.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {



    //<editor-fold desc="Here are the fields for User tab">
    public TableColumn<ManagerTableParameters, Integer> managerColId;
    public TableColumn<ManagerTableParameters, String> managerColLogin;
    public TableColumn<ManagerTableParameters, String> managerColName;
    public TableView<ManagerTableParameters> managerTable;
    public TableView<CustomerTableParameters> customerTable;
    public TableColumn<ManagerTableParameters, Void> dummyCol;
    public Tab usersTab;
    public Tab shopTab;
    public ListView<Product> shopProducts;
    public ListView<Product> myCartItems;
    public ListView<Product> productAdminList;
    public TextField productTitleField;
    public TextArea productDescriptionField;
    public TextField productQuantityField;
    public DatePicker productDateMadeField;
    public TextField productManufacturerField;
    public RadioButton productCoffeeRadio;
    public ToggleGroup productType;
    public RadioButton productTeaRadio;
    public RadioButton productWaterRadio;
    public ComboBox<CoffeeType> ComboCoffe;
    public ComboBox<TeaType> ComboTea;
    public ComboBox<WaterType> ComboWater;
    public TextField productCapacityField;
    public Tab productsTab;
    public TableColumn <ManagerTableParameters, String> managerColPassword;//
    public TableColumn<ManagerTableParameters, String> managerColSurname;//
    public TableColumn<CustomerTableParameters, Integer> customerColID;
    public TableColumn<CustomerTableParameters, String> customerColLogin;
    public TableColumn<CustomerTableParameters, String> customerColPassword;
    public TableColumn<CustomerTableParameters, String> customerColName;
    public TableColumn<CustomerTableParameters, String> customerColSurname;
    public TableColumn<CustomerTableParameters, Void> customerdummyCol;
    public TableColumn <OrdersTableParameters, Integer> productColId;
    public TableColumn <OrdersTableParameters, String> productColTitle;
    public TableColumn <OrdersTableParameters, String> productColManufacturer;
    public TableColumn <OrdersTableParameters, Integer> productColQuantity;
    public TableColumn <OrdersTableParameters, Integer> productColCapacity;
    public TableView <OrdersTableParameters> ordersTable;
    public ListView <Cart> ordersListView;
    public ListView <Warehouse> warehousesListView;
    public ListView <Manager> warehouseManagerListView;
    public ListView <Product> warehouseProductsListView;
    public DatePicker sortDateFieldFrom;
    public DatePicker sortDateFieldTo;
    public TextField sortManagerId;
    public TextField sortCustomerId;
    public TextField sortOrderId;
    public ComboBox<OrderStatusType> StatusComboBox;
    private ObservableList<ManagerTableParameters> managerData = FXCollections.observableArrayList();
    private ObservableList<OrdersTableParameters> ordersData = FXCollections.observableArrayList();
    //</editor-fold>
    private ObservableList<CustomerTableParameters> customerData = FXCollections.observableArrayList();
    public Tab ordersTab;
    public Tab warehousesTab;
    @FXML
    public TabPane tabPane;
    private EntityManagerFactory entityManagerFactory;
    //This class has methods for entity manipulation
    private ShopHibernate shopHibernate;
    //I need to know which user is selected
    private User user;
    private GenericHibernate genericHibernate;

    //When class implements Initializable interface, you will be required to implements this method.
    // It allows us to access all the fields before they are rendered
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Populating ComboBox with SeedType values
        ComboCoffe.getItems().addAll(CoffeeType.values());
        ComboTea.getItems().addAll(TeaType.values());
        ComboWater.getItems().addAll(WaterType.values());
        StatusComboBox.getItems().addAll(OrderStatusType.values());

        //Initializing TableViews
        //TODO Complete remaining columns
        managerTable.setEditable(true);
        customerTable.setEditable(true);
        ordersTable.setEditable(true);
        //setCellValueFactory allows to display the data

        managerColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerColID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productColId.setCellValueFactory(new PropertyValueFactory<>("id"));

        managerColLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        managerColLogin.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            //Before updating, get the latest version from database
            Manager manager = shopHibernate.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setLogin(event.getNewValue());
            shopHibernate.update(manager);
        });

        customerColLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        customerColLogin.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            //Before updating, get the latest version from database
            Customer customer = shopHibernate.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setLogin(event.getNewValue());
            shopHibernate.update(customer);
        });
//paklausti
        productColTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        productColTitle.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setTitle(event.getNewValue());
            //Before updating, get the latest version from database
            Product product = shopHibernate.getEntityById(Product.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            product.setTitle(event.getNewValue());
            shopHibernate.update(product);
        });

        managerColLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        customerColLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        productColTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));

        managerColPassword.setCellFactory(TextFieldTableCell.forTableColumn());
        managerColPassword.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            //Before updating, get the latest version from database
            Manager manager = shopHibernate.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setPassword(event.getNewValue());
            shopHibernate.update(manager);
        });

        customerColPassword.setCellFactory(TextFieldTableCell.forTableColumn());
        customerColPassword.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            //Before updating, get the latest version from database
            Customer customer = shopHibernate.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setPassword(event.getNewValue());
            shopHibernate.update(customer);
        });

        productColManufacturer.setCellFactory(TextFieldTableCell.forTableColumn());
        productColManufacturer.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setManufacturer(event.getNewValue());
            //Before updating, get the latest version from database
            Product product = shopHibernate.getEntityById(Product.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            product.setManufacturer(event.getNewValue());
            shopHibernate.update(product);
        });

        managerColPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        customerColPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        productColManufacturer.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));

        managerColSurname.setCellFactory(TextFieldTableCell.forTableColumn());
        managerColSurname.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSurname(event.getNewValue());
            //Before updating, get the latest version from database
            Manager manager = shopHibernate.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setSurname(event.getNewValue());
            shopHibernate.update(manager);
        });

        customerColSurname.setCellFactory(TextFieldTableCell.forTableColumn());
        customerColSurname.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSurname(event.getNewValue());
            //Before updating, get the latest version from database
            Customer customer = shopHibernate.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setSurname(event.getNewValue());
            shopHibernate.update(customer);
        });

       /* productColQuantity.setCellFactory(FieldTableCell.forTableColumn());
        productColQuantity.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
            //Before updating, get the latest version from database
            Product product = shopHibernate.getEntityById(Product.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            product.setQty(event.getNewValue());
            shopHibernate.update(product);
        });*/

        managerColSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        customerColSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        productColQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        //setCellFactory and setOnEditCommit allows us to edit cell value
        managerColName.setCellFactory(TextFieldTableCell.forTableColumn());
        managerColName.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            //Before updating, get the latest version from database
            Manager manager = shopHibernate.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setName(event.getNewValue());
            shopHibernate.update(manager);
        });

        customerColName.setCellFactory(TextFieldTableCell.forTableColumn());
        customerColName.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            //Before updating, get the latest version from database
            Customer customer = shopHibernate.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setName(event.getNewValue());
            shopHibernate.update(customer);
        });

        managerColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productColCapacity.setCellValueFactory(new PropertyValueFactory<>("Capacity"));

        //This portion of the code is responsible for generating a graphic element (button) in a cell


        Callback<TableColumn<ManagerTableParameters, Void>, TableCell<ManagerTableParameters, Void>> callback = param -> {
            final TableCell<ManagerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        ManagerTableParameters row = getTableView().getItems().get(getIndex());
                        shopHibernate.delete(Manager.class, row.getId());

                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };
        dummyCol.setCellFactory(callback);

        Callback<TableColumn<CustomerTableParameters, Void>, TableCell<CustomerTableParameters, Void>> callback2 = param -> {
            final TableCell<CustomerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        CustomerTableParameters row = getTableView().getItems().get(getIndex());
                        shopHibernate.delete(Customer.class, row.getId());

                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };
        customerdummyCol.setCellFactory(callback2);
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        this.entityManagerFactory = entityManagerFactory;
        this.shopHibernate = new ShopHibernate(entityManagerFactory);
        this.user = user;
        loadTabData();
        setCustomerView();
    }

    private void setCustomerView() {
        //Customer should not have any access or knowledge about tabs that are intended for Managers/Admins
        if (user instanceof Customer) {
            //You could simply disable tabs, but it is better to not render them
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(productsTab);
            tabPane.getTabs().remove(warehousesTab);
        } else if (!((Manager) user).isAdmin()) {
            tabPane.getTabs().remove(usersTab);
        }
    }

    //<editor-fold desc="Logic for User Tab">
    private void fillManagerTable() {
        //get all records from the database for Manager TableView
        List<Manager> managers = shopHibernate.getAllRecords(Manager.class);
        for (Manager m : managers) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setId(m.getId());
            managerTableParameters.setLogin(m.getLogin());
            managerTableParameters.setName(m.getName());
            managerTableParameters.setPassword(m.getPassword());
            managerTableParameters.setSurname(m.getSurname());
            managerData.add(managerTableParameters);
        }
        managerTable.setItems(managerData);
    }

    @FXML
    private void fillOrdersTable() {
        //get all records from the database for Manager TableView
        ordersTable.getItems().clear();
        Cart cart = ordersListView.getSelectionModel().getSelectedItem();
        if(cart!=null)
        {
            cart=shopHibernate.getEntityById(Cart.class, cart.getId());
            List<Product>products=cart.getProductList();
            for (Product m : products) {
                OrdersTableParameters ordersTableParameters = new OrdersTableParameters();
                ordersTableParameters.setId(m.getId());
                ordersTableParameters.setTitle(m.getTitle());
                ordersTableParameters.setManufacturer(m.getManufacturer());
                ordersTableParameters.setQuantity(m.getQty());
                ordersTableParameters.setCapacity(m.getCapacity());
                ordersData.add(ordersTableParameters);
            }
            ordersTable.setItems(ordersData);
        }

    }

    private void fillCustomerTable() {
        //get all records from the database for Manager TableView
        List<Customer> customers = shopHibernate.getAllRecords(Customer.class);
        for (Customer m : customers) {
            CustomerTableParameters customerTableParameters = new CustomerTableParameters();
            customerTableParameters.setId(m.getId());
            customerTableParameters.setLogin(m.getLogin());
            customerTableParameters.setName(m.getName());
            customerTableParameters.setPassword(m.getPassword());
            customerTableParameters.setSurname(m.getSurname());
            customerData.add(customerTableParameters);
        }
        customerTable.setItems(customerData);
    }
    //</editor-fold>

    //<editor-fold desc="Logic for Products Tab">
    //A method that is called once Add button is clicked
    public void createRecord() {
        //TODO change product creation based on your classes
        //Check if a plant is selected. If true, then get the data from the fields that are required to be filled for Plants and create Plant object
        if (productCoffeeRadio.isSelected()) {
            Coffee coffee = new Coffee(productTitleField.getText(),
                    productDescriptionField.getText(),
                    Integer.parseInt(productQuantityField.getText()),
                    productManufacturerField.getText(),
                    Float.parseFloat(productCapacityField.getText()),
                    ComboCoffe.getValue());
            //Save to database
            shopHibernate.create(coffee);
        } else if (productTeaRadio.isSelected()) {
            //Check if seed is selected. Create a seed object
            Tea tea = new Tea(productTitleField.getText(),
                    productDescriptionField.getText(),
                    Integer.parseInt(productQuantityField.getText()),
                    productManufacturerField.getText(),
                    Float.parseFloat(productCapacityField.getText()),
                    ComboTea.getValue());
            //Save to database
            shopHibernate.create(tea);
        } else {
            //Check if tool is selected. Create a tool object
            Water water = new Water(productTitleField.getText(),
                    productDescriptionField.getText(),
                    Integer.parseInt(productQuantityField.getText()),
                    productManufacturerField.getText(),
                    Float.parseFloat(productCapacityField.getText()),
                    ComboWater.getValue());
            //Save to database
            shopHibernate.create(water);
        }
        //refresh the product list
        productAdminList.getItems().clear();
        productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
    }

    public void updateRecord() {
        //Once the product is selected, load that information to the fields for easier editing
        //You can also implement a TableView for easier manipulation
        Product product = shopHibernate.getEntityById(Product.class, productAdminList.getSelectionModel().getSelectedItem().getId());
        //We need to determine what kind of object it is.
        //If it is Plant - I need to disable non plant fields and fill plant fields with data
        if (product instanceof Coffee) {
            Coffee coffee = (Coffee) product;
            coffee.setTitle(productTitleField.getText());
            //productDescriptionField.setText(plant.getDescription());
            coffee.setDescription(productDescriptionField.getText());
            //Once all information from the fields is collected, update the record
            shopHibernate.update(coffee);
            productAdminList.getItems().clear();
            productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));

        } else if (product instanceof Tea) {
            Tea tea = (Tea) product;
            tea.setTitle(productTitleField.getText());
            //productDescriptionField.setText(plant.getDescription());
            tea.setDescription(productDescriptionField.getText());
            //Once all information from the fields is collected, update the record
            shopHibernate.update(tea);
            productAdminList.getItems().clear();
            productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
        } else {
            Water water = (Water) product;
            water.setTitle(productTitleField.getText());
            //productDescriptionField.setText(plant.getDescription());
            water.setDescription(productDescriptionField.getText());
            //Once all information from the fields is collected, update the record
            shopHibernate.update(water);
            productAdminList.getItems().clear();
            productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
        }
    }

    //Delete operations are more complicated, because we need to control what stays in the database and what should be removed
    //For this reason generic delete will not work for us, therefore I create custom delete methods
    public void deleteRecord() {
      //  Product product = productAdminList.getSelectionModel().getSelectedItem();
      //  productAdminList.getItems().remove(product);

        shopHibernate.delete(Product.class, productAdminList.getSelectionModel().getSelectedItem().getId());
        loadProductData();
        fillLists();
    }

    private void fillLists() {
        productAdminList.getItems().clear();
        productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
    }

    //This method enables/disables fields based on product type
    public void disableFields() {
        if (productCoffeeRadio.isSelected()) {
            productTitleField.setDisable(false);
            productManufacturerField.setDisable(false);
            productDescriptionField.setDisable(false);
            productQuantityField.setDisable(false);
            productCapacityField.setDisable(false);
            productDateMadeField.setDisable(false);
            ComboCoffe.setDisable(false);
            ComboTea.setDisable(true);
            ComboWater.setDisable(true);

        } else if (productTeaRadio.isSelected()) {
            productTitleField.setDisable(false);
            productManufacturerField.setDisable(false);
            productDescriptionField.setDisable(false);
            productQuantityField.setDisable(false);
            productCapacityField.setDisable(false);
            productDateMadeField.setDisable(false);
            ComboTea.setDisable(false);
            ComboWater.setDisable(true);
            ComboCoffe.setDisable(true);

        } else {
            productTitleField.setDisable(false);
            productManufacturerField.setDisable(false);
            productDescriptionField.setDisable(false);
            productQuantityField.setDisable(false);
            productCapacityField.setDisable(false);
            productDateMadeField.setDisable(false);
            ComboWater.setDisable(false);
            ComboTea.setDisable(true);
            ComboCoffe.setDisable(true);
        }
    }

    //This method is called when you select a product from ListView. This ListView displays all products that are currently in the database
    //It populates the data in the GUI fields for faster data manipulation
    public void loadProductData() {
        //ListView element has getSelectionModel().getSelectedItem() method, it will return the selected item, which is a Product
        Product product = productAdminList.getSelectionModel().getSelectedItem();
        //Because ListView<Product> stores Product, we can add all child class objects
        //This way we can access only those attributes and methods that are defined in Product class
        //Use instanceof to determine what child class object is there and fill the appropriate fields
        if (product instanceof Coffee coffee) {
            //See above, I have a pattern variable, this way I do not have to initialize in a separate line
            productTitleField.setText(coffee.getTitle());
            productManufacturerField.setText(String.valueOf(coffee.getManufacturer()));
            productDescriptionField.setText(coffee.getDescription());
            productQuantityField.setText(String.valueOf(coffee.getQty()));
            productCapacityField.setText(String.valueOf(coffee.getCapacity()));
            productDateMadeField.setValue(coffee.getDateMade());
            ComboCoffe.setValue(coffee.getCoffeeType());
        } else if (product instanceof Tea tea) {
            productTitleField.setText(tea.getTitle());
            productManufacturerField.setText(String.valueOf(tea.getManufacturer()));
            productDescriptionField.setText(tea.getDescription());
            productQuantityField.setText(String.valueOf(tea.getQty()));
            productCapacityField.setText(String.valueOf(tea.getCapacity()));
            productDateMadeField.setValue(tea.getDateMade());
            ComboTea.setValue(tea.getTeaType());
        } else if (product instanceof Water water) {
            productTitleField.setText(water.getTitle());
            productManufacturerField.setText(String.valueOf(water.getManufacturer()));
            productDescriptionField.setText(water.getDescription());
            productQuantityField.setText(String.valueOf(water.getQty()));
            productCapacityField.setText(String.valueOf(water.getCapacity()));
            productDateMadeField.setValue(water.getDateMade());
            ComboWater.setValue(water.getWaterType());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Logic for Shop tab">
    //Do not create cart, only when user clicks "Buy"
    public void removeFromCart() {
        Product product = myCartItems.getSelectionModel().getSelectedItem();
        shopProducts.getItems().add(product);
        myCartItems.getItems().remove(product);
    }

    public void addToCart() {
        Product product = shopProducts.getSelectionModel().getSelectedItem();
        myCartItems.getItems().add(product);
        shopProducts.getItems().remove(product);
    }

    public void buyItems() {
        //When the user clicks buy, call a specific method, not generic, because this is more complicated
        shopHibernate.createCart(myCartItems.getItems(), user);
    }
    //</editor-fold>

    public void loadTabData() {
        if (shopTab.isSelected()) {
            shopProducts.getItems().clear();
            shopProducts.getItems().addAll(shopHibernate.loadAvailableProducts());
        } else if (usersTab.isSelected()) {
            managerTable.getItems().clear();
            fillManagerTable();

            customerTable.getItems().clear();
            fillCustomerTable();
        } else if (ordersTab.isSelected()) {
            ordersListView.getItems().clear();
            ordersListView.getItems().addAll(shopHibernate.getAllRecords(Cart.class));
        } else if (productsTab.isSelected()) {
            productAdminList.getItems().clear();
            productAdminList.getItems().addAll(shopHibernate.loadAvailableProducts());
        }
        else if (warehousesTab.isSelected()) {
            warehousesListView.getItems().clear();
            warehousesListView.getItems().addAll(shopHibernate.getAllRecords(Warehouse.class));
        }
    }
    public void deleteOrder () {
        Cart cart = ordersListView.getSelectionModel().getSelectedItem();
        ordersListView.getItems().remove(cart);
        shopHibernate.deleteCart(cart.getId());
    }
    public void AddWarehouse() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("warehouse-window.fxml"));
        Parent parent = fxmlLoader.load();
        WarehouseController warehouseController= fxmlLoader.getController();
        warehouseController.setData(entityManagerFactory,false,null);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void createUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration.fxml"));
        Parent parent = fxmlLoader.load();
        //Access controller of main window. Each form has its own controller, so make sure that you make no mistake here
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory, true);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        FxUtils.setStageParameters(stage, scene, false);
    }

    public void loadProductReviewForm() throws IOException {
        //Get resources: fxml, controller, grapics, styles...
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("product-review.fxml"));
        //Load resources, without this step I cannot access controllers
        Parent parent = fxmlLoader.load();

        ProductReview productReview = fxmlLoader.getController();
        //Forms do not know about each other, therefore I must pass info between them
        productReview.setData(entityManagerFactory, user);
        //Create a completely new window
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        FxUtils.setStageParameters(stage, scene, true);
    }

    public void deleteCart(ActionEvent actionEvent) {
        Cart cart = ordersListView.getSelectionModel().getSelectedItem();
        ordersListView.getItems().remove(cart);
        shopHibernate.deleteCart(cart.getId());

    }

    public void UpdateWarehouse(ActionEvent actionEvent) throws IOException {
        Warehouse warehouse = warehousesListView.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("warehouse-window.fxml"));
        Parent parent = fxmlLoader.load();
        WarehouseController warehouseController= fxmlLoader.getController();
        warehouseController.setData(entityManagerFactory,true,warehouse);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    public void DeleteWarehouse(ActionEvent actionEvent) {
        Warehouse warehouse = warehousesListView.getSelectionModel().getSelectedItem();
        warehousesListView.getItems().remove(warehouse);
        shopHibernate.deleteWarehouse(warehouse.getId());
        warehouseProductsListView.getItems().clear();
        warehouseManagerListView.getItems().clear();
    }

    public void LoadWarehouseData(){

        Warehouse warehouse = warehousesListView.getSelectionModel().getSelectedItem();
        if(warehouse != null) {
            warehouseProductsListView.getItems().clear();
            warehouseManagerListView.getItems().clear();
            warehouseManagerListView.getItems().addAll(warehouse.getEmployees());
            warehouseProductsListView.getItems().addAll(warehouse.getProductList());
        }

    }



    public void filterCart(ActionEvent actionEvent) {
        String id = null;
        Customer customer = null;
        if(!sortOrderId.getText().isEmpty()){
            id = sortOrderId.getText();
        }
        if(!sortCustomerId.getText().isEmpty()){
            customer = genericHibernate.getEntityById(Customer.class, Integer.parseInt(sortCustomerId.getText()));
        }
        List<Cart> filteredOrders = shopHibernate.filterOrders(sortDateFieldFrom.getValue(), customer, id);
        ordersListView.getItems().clear();
        ordersListView.getItems().addAll(filteredOrders);
    }

    public void UpdateOrders(ActionEvent actionEvent) {
        Cart cart = ordersListView.getSelectionModel().getSelectedItem();
        cart.setOrderStatusType(StatusComboBox.getValue());
        genericHibernate.update(cart);
        ordersListView.getItems().clear();
        ordersListView.getItems().addAll(shopHibernate.getAllRecords(Cart.class));
    }
}
