package view.employeeManagement;

import bean.Designation;
import bean.Employee;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.EmployeeService;
import service.impl.EmployeeFileWriterServiceImpl;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;
import view.designationManagement.DepartmentPopUpController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> IDColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> NICColumn;

    @FXML
    private TableColumn<Employee, Integer> phoneColumn;

    @FXML
    private TableColumn<Employee, String> designationColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField designationTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label designationLabel;

    @FXML
    private TextField nicTextField;

    @FXML
    private Label nicLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton updateButton;

    private static Employee selectedEmployee = null;

    public static Designation selectedDesignation = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData() {
        // Load employee data from the service
        EmployeeService employeeService = new EmployeeFileWriterServiceImpl();
        ObservableList<Employee> employeeObservableList = employeeService.loadAllEmployeeData();

        // Set cell value factories for table columns
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("eID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("eName"));
        NICColumn.setCellValueFactory(new PropertyValueFactory<>("eNIC"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("eContactNo"));
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("eDesignationName"));

        // Clear and set the employee data to the table
        employeeTable.setItems(null);
        employeeTable.setItems(employeeObservableList);

        // Perform table search
        searchTable(employeeObservableList);
    }

    public void searchTable(ObservableList<Employee> employeeObservableList) {
        // Load employee service
        EmployeeService employeeService = new EmployeeFileWriterServiceImpl();

        // Perform search and get the sorted data
        SortedList<Employee> sortedData = employeeService.searchTable(employeeObservableList, searchTextField);

        // Bind the sorted list to the table comparator
        sortedData.comparatorProperty().bind(employeeTable.comparatorProperty());

        // Set the sorted and filtered data to the table
        employeeTable.setItems(sortedData);
    }

    @FXML
    void addEmployee(ActionEvent event) {
        // Clear error labels
        clearLabels();

        // Perform employee validation
        if (employeeValidation()) {
            // Create a new employee object
            Employee employee = new Employee();

            // Load employee service
            EmployeeService employeeService = new EmployeeFileWriterServiceImpl();

            // Set employee data
            employee.seteName(nameTextField.getText());
            employee.seteNIC(nicTextField.getText());
            employee.seteContactNo(Integer.valueOf(phoneTextField.getText()));
            employee.seteDesignation(selectedDesignation);

            // Insert employee data and handle result
            if (employeeService.insertEmployeeData(employee)) {
                AlertPopUp.insertSuccessfully("Employee");
                loadData();
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("Employee");

        } else
            employeeValidationMessage();
    }

    @FXML
    void updateEmployee(ActionEvent event) {
        // Clear error labels
        clearLabels();

        // Perform employee validation
        if (employeeValidation()) {
            // Create a new employee object
            Employee employee = new Employee();

            // Load employee service
            EmployeeService employeeService = new EmployeeFileWriterServiceImpl();

            // Set employee data
            employee.seteID(selectedEmployee.geteID());
            employee.seteName(nameTextField.getText());
            employee.seteNIC(nicTextField.getText());
            employee.seteContactNo(Integer.valueOf(phoneTextField.getText()));
            if(selectedDesignation != null)
                employee.seteDesignation(selectedDesignation);
            else
                employee.seteDesignation(selectedEmployee.geteDesignation());

            // Update employee data and handle result
            if (employeeService.updateEmployeeData(employee)) {
                AlertPopUp.updateSuccessfully("Employee");
                loadData();
                clearFields(event);
            } else
                AlertPopUp.updateFailed("Employee");
        } else
            employeeValidationMessage();
    }

    @FXML
    private void deleteEmployee(ActionEvent event) {
        if (selectedEmployee != null) {
            // Load employee service
            EmployeeService employeeService = new EmployeeFileWriterServiceImpl();

            // Show delete confirmation dialog
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Employee");
            if (action.get() == ButtonType.OK) {
                // Delete employee data and handle result
                if (employeeService.deleteEmployeeData((selectedEmployee.geteID()))) {
                    AlertPopUp.deleteSuccessfull("Employee");
                    loadData();
                    clearFields(event);
                } else
                    AlertPopUp.deleteFailed("Employee");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("Employee");
        }
    }

    @FXML
    void setSelectedEmployeeDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);

            // Get selected employee from the table
            selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

            // Set the selected employee's data to the input fields
            nameTextField.setText(selectedEmployee.geteName());
            nicTextField.setText(selectedEmployee.geteNIC());
            phoneTextField.setText("0"+selectedEmployee.geteContactNo());
            designationTextField.setText(selectedEmployee.geteDesignation().getName());
        } catch (NullPointerException exception) {
            // Handle null pointer exception if no row is selected
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        // Clear input fields
        nameTextField.setText("");
        nicTextField.setText("");
        phoneTextField.setText("");
        designationTextField.setText("");

        // Reset components and labels
        resetComponents();
        clearLabels();

        // Reset components and labels
        selectedEmployee = null;
    }

    private void clearLabels() {
        // Clear error labels
        nameLabel.setText("");
        nicLabel.setText("");
        phoneLabel.setText("");
        designationLabel.setText("");
    }

    private boolean employeeValidation() {
        // Perform employee data validation and return the result

        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
                && DataValidation.TextFieldNotEmpty(designationTextField.getText())

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)

                && DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText());


    }

    private void employeeValidationMessage() {
        // Display validation error messages for employee data

        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
                && DataValidation.TextFieldNotEmpty(designationTextField.getText()))) {
            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Employee Name Required!");
            DataValidation.TextFieldNotEmpty(nicTextField.getText(), nicLabel, "Employee NIC Required!");
            DataValidation.TextFieldNotEmpty(phoneTextField.getText(), phoneLabel, "Employee Contact Number Required!");
            DataValidation.TextFieldNotEmpty(designationTextField.getText(), designationLabel, "Employee designation Required!");

        }
        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10))) {

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(nicTextField.getText(), 12, nicLabel, "Field Limit 12 Exceeded!");
            DataValidation.isValidMaximumLength(phoneTextField.getText(), 10, phoneLabel, "Field Limit 10 Exceeded!");
        }
        if (!(DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText()))) {
            DataValidation.isValidNIC(nicTextField, nicLabel, "Invalid NIC !!");
            DataValidation.isValidPhoneNo(phoneTextField.getText(), phoneLabel, "Invalid Contact Number!!");
        }
    }

    @FXML
    private void browseDesignation(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("designationPopUp.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DepartmentPopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.setResizable(false);
        stage.sizeToScene();

        stage.showAndWait();

        if (selectedDesignation != null) {
            designationTextField.setText(selectedDesignation.getId() + " - " + selectedDesignation.getName());
        }
    }
    private void resetComponents() {
        // Reset visibility of buttons
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    /**
     * Sets the selected designation for the employee form.
     *
     * @param designation The selected department.
     */
    public void setDesignation(Designation designation) {
        selectedDesignation = designation;
    }

}
