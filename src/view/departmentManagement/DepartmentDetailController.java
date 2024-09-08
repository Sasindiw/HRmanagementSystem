package view.departmentManagement;

import bean.Department;
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
import service.DepartmentService;
import service.impl.DepartmentFileWriterServiceImpl;
import utility.dataHandler.DataValidation;
import utility.dataHandler.MasterData;
import utility.popUp.AlertPopUp;
import view.designationManagement.DepartmentPopUpController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Department> departmentTable;

    @FXML
    private TableColumn<Department, String> IDColumn;

    @FXML
    private TableColumn<Department, String> nameColumn;

    @FXML
    private TableColumn<Department, String> descriptionColumn;

    @FXML
    private TableColumn<Department, String> statusColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    private static Department selectedDepartment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusChoiceBox.setValue(MasterData.DEPT_STATUS_OBSERVABLE_LIST.get(0));
        statusChoiceBox.setItems(MasterData.DEPT_STATUS_OBSERVABLE_LIST);
        loadData();

    }

    /**
     * Loads department data and populates the department table.
     */
    private void loadData() {
        DepartmentService departmentService = new DepartmentFileWriterServiceImpl();
        ObservableList<Department> departmentObservableList = departmentService.loadAllDepartments();

        // Set cell value factories for table columns
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("deptDescription"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("deptStatus"));

        // Clear and set the department data in the table
        departmentTable.setItems(null);
        departmentTable.setItems(departmentObservableList);

        // Perform table search
        searchTable(departmentObservableList);
    }

    /**
     * Searches the department table based on the search text.
     *
     * @param departmentObservableList The list of departments to search.
     */
    public void searchTable(ObservableList<Department> departmentObservableList) {
        DepartmentService departmentService = new DepartmentFileWriterServiceImpl();

        // Perform search and get sorted data
        SortedList<Department> sortedData = departmentService.searchTable(departmentObservableList, searchTextField);

        // Bind the SortedList to the TableView
        sortedData.comparatorProperty().bind(departmentTable.comparatorProperty());

        // Set the sorted and filtered data in the table
        departmentTable.setItems(sortedData);
    }

    @FXML
    void addDepartment(ActionEvent event) {
        clearLabels();

        // Validate the input fields
        if (fieldValidation()) {
            Department department = new Department();
            DepartmentService departmentService = new DepartmentFileWriterServiceImpl();

            // Set department properties from input fields
            department.setDeptName(nameTextField.getText());
            department.setDeptDescription(descriptionTextArea.getText());
            department.setDeptStatus(statusChoiceBox.getValue());

            // Add the department and handle the result
            if (departmentService.addDepartment(department)) {
                AlertPopUp.insertSuccessfully("Department");
                loadData();
                clearFields(event);
            } else {
                AlertPopUp.insertionFailed("Department");
            }
        } else {
            setFieldValidationMessage();
        }
    }

    @FXML
    void updateDepartment(ActionEvent event) {
        clearLabels();

        // Validate the input fields
        if (fieldValidation()) {
            Department department = new Department();
            DepartmentService departmentService = new DepartmentFileWriterServiceImpl();

            // Set department properties from input fields
            department.setId(selectedDepartment.getId());
            department.setDeptName(nameTextField.getText());
            department.setDeptDescription(descriptionTextArea.getText());
            department.setDeptStatus(statusChoiceBox.getValue());

            // Update the department and handle the result
            if (departmentService.updateDepartment(department)) {
                AlertPopUp.updateSuccessfully("Department");
                loadData();
                clearFields(event);
            } else {
                AlertPopUp.updateFailed("Department");
            }
        } else {
            setFieldValidationMessage();
        }
    }

    @FXML
    private void deleteDepartment(ActionEvent event) {
        if (selectedDepartment != null) {
            DepartmentService departmentService = new DepartmentFileWriterServiceImpl();

            // Show delete confirmation dialog
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Department");
            if (action.get() == ButtonType.OK) {
                // Delete the department and handle the result
                if (departmentService.deleteDepartment(selectedDepartment.getId())) {
                    AlertPopUp.deleteSuccessfull("Department");
                    loadData();
                    clearFields(event);
                } else {
                    AlertPopUp.deleteFailed("Department");
                }
            } else {
                loadData();
            }
        } else {
            AlertPopUp.selectRowToDelete("Department");
        }
    }

    @FXML
    void setSelectedDepartmentDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);

            // Get the selected department from the table
            selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
            nameTextField.setText(selectedDepartment.getDeptName());
            descriptionTextArea.setText(selectedDepartment.getDeptDescription());
            statusChoiceBox.setValue(selectedDepartment.getDeptStatus());
        } catch (NullPointerException exception) {
            // Handle null pointer exception silently
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        // Clear input fields and reset components
        nameTextField.setText("");
        descriptionTextArea.setText("");
        statusChoiceBox.setValue(MasterData.DEPT_STATUS_OBSERVABLE_LIST.get(0));
        resetComponents();
        clearLabels();
        selectedDepartment = null;
    }

    /**
     * Clears the error labels.
     */
    private void clearLabels() {
        nameLabel.setText("");
        descriptionLabel.setText("");
    }

    /**
     * Performs field validation for the department input fields.
     *
     * @return true if all fields are valid, false otherwise.
     */
    private boolean fieldValidation() {
        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(descriptionTextArea.getText())
                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(descriptionTextArea.getText(), 400);
    }

    /**
     * Sets the field validation error messages for the department input fields.
     */
    private void setFieldValidationMessage() {
        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(descriptionTextArea.getText()))) {
            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Department Name Required!");
            DataValidation.TextFieldNotEmpty(descriptionTextArea.getText(), descriptionLabel, "Department Description Required!");
        }
        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(descriptionTextArea.getText(), 400))) {
            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(descriptionTextArea.getText(), 400, descriptionLabel, "Field Limit 400 Exceeded!");
        }
    }

    /**
     * Resets the components after adding or updating a department.
     */
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }
}
