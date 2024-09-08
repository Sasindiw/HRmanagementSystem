package view.designationManagement;

import bean.Department;
import bean.Designation;
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
import service.DesignationService;
import service.impl.DesignationFileWriterServiceImpl;
import utility.dataHandler.DataValidation;
import utility.dataHandler.MasterData;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DesignationDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Designation> designationTable;

    @FXML
    private TableColumn<Designation, String> idColumn;

    @FXML
    private TableColumn<Designation, String> designationNameColumn;

    @FXML
    private TableColumn<Designation, String> designationDescriptionColumn;

    @FXML
    private TableColumn<Designation, String> departmentColumn;

    @FXML
    private TableColumn<Designation, Integer> deptIdColumn;

    @FXML
    private TableColumn<Designation, String> statusColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField departmentIDTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label departmentIDLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField designationNameTextField;

    @FXML
    private Label designationNameLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    public static Department selectedDepartment = null;

    private static Designation selectedDesignation = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        
        statusChoiceBox.setValue(MasterData.DSG_STATUS_OBSERVABLE_LIST.get(0));
        statusChoiceBox.setItems(MasterData.DSG_STATUS_OBSERVABLE_LIST);
    }

    /**
     * Loads the data for the designation table from the designation service.
     * Binds the data to the table and applies search filtering if necessary.
     */
    private void loadData() {
        DesignationService designationService = new DesignationFileWriterServiceImpl();
        ObservableList<Designation> designationObservableList = designationService.loadAllDesignations();

        // Set cell value factories for table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        designationNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        designationDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        deptIdColumn.setCellValueFactory(new PropertyValueFactory<>("deptId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Clear and set the data to the designation table
        designationTable.setItems(null);
        designationTable.setItems(designationObservableList);

        // Apply search filtering to the table
        searchTable(designationObservableList);
    }

    /**
     * Searches and filters the designation table based on the search text.
     * Binds the sorted data to the table.
     *
     * @param designationObservableList The list of designations to search within.
     */
    public void searchTable(ObservableList<Designation> designationObservableList) {
        DesignationService designationService = new DesignationFileWriterServiceImpl();

        // Get sorted data based on search text
        SortedList<Designation> sortedData = designationService.searchTable(designationObservableList, searchTextField);

        // Bind the SortedList to the TableView
        sortedData.comparatorProperty().bind(designationTable.comparatorProperty());
        // Set the sorted and filtered data to the table
        designationTable.setItems(sortedData);
    }

    @FXML
    void addDesignation(ActionEvent event) {
        clearLabels();
        if (designationValidation()) {
            Designation designation = new Designation();
            DesignationService designationService = new DesignationFileWriterServiceImpl();

            // Set designation properties based on user input
            designation.setDepartment(selectedDepartment);
            designation.setName(designationNameTextField.getText());
            designation.setDescription(descriptionTextArea.getText());
            designation.setStatus(statusChoiceBox.getValue());

            // Add the designation using the service
            if (designationService.addDesignation(designation)) {
                AlertPopUp.insertSuccessfully("Designation");
                loadData();
                clearFields(event);
            } else {
                AlertPopUp.insertionFailed("Designation");
            }
        } else {
            designationValidationMessage();
        }
    }

    @FXML
    void updateDesignation(ActionEvent event) {
        clearLabels();
        if (designationValidation()) {
            Designation designation = new Designation();
            DesignationService designationService = new DesignationFileWriterServiceImpl();

            // Set designation properties based on user input
            designation.setDepartment(selectedDepartment);
            designation.setId(selectedDesignation.getId());
            designation.setName(designationNameTextField.getText());
            designation.setDescription(descriptionTextArea.getText());
            designation.setStatus(statusChoiceBox.getValue());

            // Update the designation using the service
            if (designationService.updateDesignation(designation)) {
                AlertPopUp.updateSuccessfully("Designation");
                loadData();
                clearFields(event);
            } else {
                AlertPopUp.updateFailed("Designation");
            }
        } else {
            designationValidationMessage();
        }
    }

    @FXML
    private void deleteDesignation(ActionEvent event) {
        if (selectedDesignation != null) {
            DesignationService designationService = new DesignationFileWriterServiceImpl();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Designation");

            if (action.get() == ButtonType.OK) {
                // Delete the selected designation using the service
                if (designationService.deleteDesignation((selectedDesignation.getId()))) {
                    AlertPopUp.deleteSuccessfull("Designation");
                    loadData();
                    clearFields(event);
                } else {
                    AlertPopUp.deleteFailed("Designation");
                }
            } else {
                loadData();
            }
        } else {
            AlertPopUp.selectRowToDelete("Designation");
        }
    }

    @FXML
    void setSelectedDesignationDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);

            // Get the selected designation from the table
            selectedDesignation = designationTable.getSelectionModel().getSelectedItem();
            departmentIDTextField.setText(selectedDesignation.getDepartment().getId() + ' ' + selectedDesignation.getDepartment().getDeptName());
            designationNameTextField.setText(selectedDesignation.getName());
            descriptionTextArea.setText(selectedDesignation.getDescription());
            statusChoiceBox.setValue(selectedDesignation.getStatus());
        } catch (NullPointerException exception) {
            // Handle null pointer exception silently
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        // Clear input fields and reset components
        departmentIDTextField.setText("");
        designationNameTextField.setText("");
        descriptionTextArea.setText("");
        resetComponents();
        clearLabels();
        selectedDesignation = null;
    }

    /**
     * Clears the labels for displaying validation messages.
     */
    private void clearLabels() {
        departmentIDLabel.setText("");
        designationNameLabel.setText("");
        descriptionLabel.setText("");
    }

    /**
     * Performs validation checks for the designation input fields.
     * Returns true if all checks pass, false otherwise.
     *
     * @return True if all validation checks pass, false otherwise.
     */
    private boolean designationValidation() {
        return DataValidation.TextFieldNotEmpty(departmentIDTextField.getText())
                && DataValidation.TextFieldNotEmpty(designationNameTextField.getText())
                && DataValidation.isValidMaximumLength(designationNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(descriptionTextArea.getText(), 400);
    }

    /**
     * Displays validation messages for the designation input fields if they fail the validation checks.
     */
    private void designationValidationMessage() {
        if (!(DataValidation.TextFieldNotEmpty(departmentIDTextField.getText())
                && DataValidation.TextFieldNotEmpty(designationNameTextField.getText()))) {
            // Show validation messages for empty fields
            DataValidation.TextFieldNotEmpty(departmentIDTextField.getText(), departmentIDLabel, "Please Select a Department!");
            DataValidation.TextFieldNotEmpty(designationNameTextField.getText(), designationNameLabel, "Designation Name Required!");
        }
        if (!(DataValidation.isValidMaximumLength(designationNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(descriptionTextArea.getText(), 400))) {
            // Show validation messages for exceeding maximum field length
            DataValidation.isValidMaximumLength(designationNameTextField.getText(), 45, designationNameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(descriptionTextArea.getText(), 400, descriptionLabel, "Field Limit 400 Exceeded!");
        }
    }

    /**
     * Resets the visibility of add and update buttons.
     */
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    @FXML
    private void browseDepartment(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("departmentPopUp.fxml"));
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

        if (selectedDepartment != null) {
            departmentIDTextField.setText(selectedDepartment.getId() + " - " + selectedDepartment.getDeptName());
        }
    }

    /**
     * Sets the selected department for the designation form.
     *
     * @param department The selected department.
     */
    public void setDepartment(Department department) {
        selectedDepartment = department;
    }
}
