package view.employeeManagement;

import bean.Designation;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.DesignationService;
import service.impl.DesignationFileWriterServiceImpl;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DesignationPopUpController implements Initializable {
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
    private TextField designationNameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField availabilityTextField;

    @FXML
    private JFXButton cancelButton;

    private static Designation selectedDesignation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    /**
     * Loads department data and populates the department table.
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
     * Searches the department table based on the search text.
     *
     * @param designationObservableList The list of departments to search.
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
    void addSelectedDesignation(ActionEvent event) {
        if (selectedDesignation != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeDetail.fxml"));
            try {
                // Load the DesignationDetail.fxml and get the controller
                Parent root = (Parent) loader.load();
                EmployeeDetailController employeeDetailController = loader.getController();

                // Pass the selected department to the DesignationDetailController
                employeeDetailController.setDesignation(selectedDesignation);
                selectedDesignation = null;
                closeStage();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            AlertPopUp.selectRow("Designation to Add");
        }
    }

    @FXML
    void setSelectedDesignationDataToFields(MouseEvent event) {
        try {
            // Get the selected department from the table
            selectedDesignation = designationTable.getSelectionModel().getSelectedItem();
            designationNameTextField.setText(selectedDesignation.getName());
            descriptionTextArea.setText(selectedDesignation.getDescription());
            availabilityTextField.setText(selectedDesignation.getStatus());
        } catch (NullPointerException exception) {
            // Handle null pointer exception silently
        }
    }

    /**
     * Closes the current stage.
     */
    private void closeStage() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void closeStage(ActionEvent actionEvent) {
        // Close the current stage
        closeStage();
    }

}
