package service.impl;

import bean.Designation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import service.DepartmentService;
import service.DesignationService;
import utility.dataHandler.FileHandler;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.io.*;

public class DesignationFileWriterServiceImpl implements DesignationService {

    private String filePath;
    private final String fileName = "designation_data.txt";

    public DesignationFileWriterServiceImpl(){
        initializeService();

    }

    private void initializeService() {
        this.filePath = FileHandler.initializeService(fileName);
    }

    public static void startFileWriterService() {
        new DesignationFileWriterServiceImpl();
    }

    @Override
    public ObservableList<Designation> loadAllDesignations() {
        ObservableList<Designation> designationObservableList = FXCollections.observableArrayList();
        DepartmentService departmentService = new DepartmentFileWriterServiceImpl();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    designationObservableList.add(new Designation(data[0], data[1], data[2], data[3], departmentService.loadSpecificDepartment(data[4])));
                }
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }

        return designationObservableList;
    }

    @Override
    public Designation loadSpecificDesignation(String designationId) {
        DepartmentService departmentService = new DepartmentFileWriterServiceImpl();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5 && data[0].equals(designationId)) {
                    return new Designation(data[0], data[1], data[2], data[3], departmentService.loadSpecificDepartment(data[4]));
                }
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }

        return null;
    }

    @Override
    public Boolean addDesignation(Designation designation) {
        designation.setId(FileHandler.generateId(filePath, 5).toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(designation.getId() + "," + designation.getName() + "," + designation.getDescription() + "," +
                    designation.getStatus() + "," + UtilityMethod.seperateID(designation.getDepartment().getId()));
            writer.newLine();
            return true;
        } catch (IOException e) {
            AlertPopUp.generalError(e);
            return false;
        }
    }

    @Override
    public Boolean updateDesignation(Designation designation) {
        ObservableList<Designation> designationObservableList = loadAllDesignations();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Designation dept : designationObservableList) {
                if (dept.getId().equals(designation.getId())) {
                    writer.write(UtilityMethod.seperateID(designation.getId()) + "," + designation.getName() + "," + designation.getDescription() + "," +
                            designation.getStatus() + "," + UtilityMethod.seperateID(designation.getDepartment().getId()));
                    writer.newLine();
                } else {
                    writer.write(UtilityMethod.seperateID(dept.getId()) + "," + dept.getName() + "," + dept.getDescription() + "," +
                            dept.getStatus() + "," + UtilityMethod.seperateID(dept.getDepartment().getId()));
                    writer.newLine();
                }
            }
            FileHandler.removeEmptyLines(filePath);
            return true;
        } catch (IOException e) {
            AlertPopUp.generalError(e);
            return false;
        }
    }

    @Override
    public Boolean deleteDesignation(String deptId) {
        ObservableList<Designation> designationObservableList = loadAllDesignations();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Designation designation : designationObservableList) {
                if (!designation.getId().equals(UtilityMethod.seperateID(deptId))) {
                    writer.write(UtilityMethod.seperateID(designation.getId()) + "," + designation.getName() + "," + designation.getDescription() + "," +
                            designation.getStatus() + "," + UtilityMethod.seperateID(designation.getDepartment().getId()));
                    writer.newLine();
                }
            }
            FileHandler.removeEmptyLines(filePath);
            return true;
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }
        return false;
    }

    @Override
    public SortedList<Designation> searchTable(ObservableList<Designation> designationObservableList, TextField searchTextField) {
        FilteredList<Designation> filteredData = new FilteredList<>(designationObservableList, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(designation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (designation.getId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (designation.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (designation.getDescription().contains(lowerCaseFilter)) {
                    return true;
                } else if (designation.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Designation> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }
}
