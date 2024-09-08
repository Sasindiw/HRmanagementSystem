package view.designationManagement;

import bean.Department;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.DepartmentService;
import service.impl.DepartmentFileWriterServiceImpl;
import utility.dataHandler.MasterData;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentPopUpController implements Initializable {
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
    private TextArea descriptionTextArea;

    @FXML
    private TextField availabilityTextField;

    @FXML
    private JFXButton cancelButton;

    private static Department selectedDepartment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    /**
     * Loads department data and populates the department table.
     */
    private void loadData() {
        DepartmentService departmentService = new DepartmentFileWriterServiceImpl();
        ObservableList<Department> departmentObservableList = FXCollections.observableArrayList();

        // Load all departments and filter by availability status
        for (Department department : departmentService.loadAllDepartments()) {
            if (department.getDeptStatus().equals(MasterData.DEPT_STATUS_AVAILABLE)) {
                departmentObservableList.add(department);
            }
        }

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

        // Create a new list to store the sorted and filtered data
        ObservableList<Department> list = FXCollections.observableArrayList();

        // Filter the data by availability status
        for (Department department : sortedData) {
            if (department.getDeptStatus().equals(MasterData.DEPT_STATUS_AVAILABLE)) {
                list.add(department);
            }
        }

        // Set the filtered data in the table
        departmentTable.setItems(list);
    }

    @FXML
    void addSelectedDepartment(ActionEvent event) {
        if (selectedDepartment != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DesignationDetail.fxml"));
            try {
                // Load the DesignationDetail.fxml and get the controller
                Parent root = (Parent) loader.load();
                DesignationDetailController designationDetailController = loader.getController();

                // Pass the selected department to the DesignationDetailController
                designationDetailController.setDepartment(selectedDepartment);
                selectedDepartment = null;
                closeStage();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            AlertPopUp.selectRow("Department to Add");
        }
    }

    @FXML
    void setSelectedDepartmentDataToFields(MouseEvent event) {
        try {
            // Get the selected department from the table
            selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
            nameTextField.setText(selectedDepartment.getDeptName());
            descriptionTextArea.setText(selectedDepartment.getDeptDescription());
            availabilityTextField.setText(selectedDepartment.getDeptStatus());
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
