package service.impl;

import bean.Department;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import service.DepartmentService;
import utility.dataHandler.FileHandler;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.io.*;

public class DepartmentFileWriterServiceImpl implements DepartmentService {

    private String filePath;
    private final String fileName = "department_data.txt";

    public DepartmentFileWriterServiceImpl(){
        initializeService();

    }

    private void initializeService() {
        this.filePath = FileHandler.initializeService(fileName);
    }

    public static void startFileWriterService() {
        new DepartmentFileWriterServiceImpl();
    }

    public DepartmentFileWriterServiceImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public ObservableList<Department> loadAllDepartments() {
        ObservableList<Department> departmentObservableList = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    departmentObservableList.add(new Department(data[0], data[1], data[2], data[3]));
                }
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }

        return departmentObservableList;
    }

    @Override
    public Department loadSpecificDepartment(String departmentId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 && data[0].equals(departmentId)) {
                    return new Department(data[0], data[1], data[2], data[3]);
                }
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }

        return null;
    }

    @Override
    public Boolean addDepartment(Department department) {
        department.setId(FileHandler.generateId(filePath, 4).toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(department.getId() + "," + department.getDeptName() + "," + department.getDeptDescription() + "," +
                    department.getDeptStatus());
            writer.newLine();
            return true;
        } catch (IOException e) {
            AlertPopUp.generalError(e);
            return false;
        }
    }

    @Override
    public Boolean updateDepartment(Department department) {
        ObservableList<Department> departmentObservableList = loadAllDepartments();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Department dept : departmentObservableList) {
                if (dept.getId().equals(department.getId())) {
                    writer.write(UtilityMethod.seperateID(department.getId()) + "," + department.getDeptName() + "," + department.getDeptDescription() + "," +
                            department.getDeptStatus());
                    writer.newLine();
                } else {
                    writer.write(UtilityMethod.seperateID(dept.getId()) + "," + dept.getDeptName() + "," + dept.getDeptDescription() + "," +
                            dept.getDeptStatus());
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
    public Boolean deleteDepartment(String deptId) {
        ObservableList<Department> departmentObservableList = loadAllDepartments();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Department department : departmentObservableList) {
                if (!department.getId().equals(UtilityMethod.seperateID(deptId))) {
                    writer.write(UtilityMethod.seperateID(department.getId()) + "," + department.getDeptName() + "," + department.getDeptDescription() + "," +
                            department.getDeptStatus());
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
    public SortedList<Department> searchTable(ObservableList<Department> departmentObservableList, TextField searchTextField) {
        FilteredList<Department> filteredData = new FilteredList<>(departmentObservableList, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(department -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (department.getId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (department.getDeptName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (department.getDeptDescription().contains(lowerCaseFilter)) {
                    return true;
                } else if (department.getDeptStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Department> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }
}
