
package service.impl;

import bean.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import service.DesignationService;
import service.EmployeeService;
import utility.dataHandler.FileHandler;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.io.*;

public class EmployeeFileWriterServiceImpl implements EmployeeService {
    private String filePath;
    private final String fileName = "employee_data.txt";

    public EmployeeFileWriterServiceImpl() {
        initializeService();
    }

    private void initializeService(){
        this.filePath = FileHandler.initializeService(fileName);
    }

    public static void startFileWriterService(){
        new EmployeeFileWriterServiceImpl();
    }

    @Override
    public ObservableList<Employee> loadAllEmployeeData() {
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();
        DesignationService designationService = new DesignationFileWriterServiceImpl();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    employeeObservableList.add(new Employee(data[0], data[1], data[2], Integer.parseInt(data[3]), designationService.loadSpecificDesignation(data[4])));
                }
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }

        return employeeObservableList;
    }

    @Override
    public Employee loadSpecificEmployee(String eID) {
        DesignationService designationService = new DesignationFileWriterServiceImpl();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5 && data[0].equals(eID)) {
                    return new Employee(data[0], data[1], data[2], Integer.parseInt(data[3]), designationService.loadSpecificDesignation(data[4]));
                }
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }

        return null;
    }

    @Override
    public boolean insertEmployeeData(Employee employee) {
        employee.seteID(FileHandler.generateId(filePath, 5).toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(employee.geteID() + "," + employee.geteName() + "," + employee.geteNIC() + "," +
                    employee.geteContactNo() + "," + UtilityMethod.seperateID(employee.geteDesignation().getId()));
            writer.newLine();
            return true;
        } catch (IOException e) {
            AlertPopUp.generalError(e);
            return false;
        }
    }

    @Override
    public boolean updateEmployeeData(Employee employee) {
        ObservableList<Employee> employeeObservableList = loadAllEmployeeData();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Employee emp : employeeObservableList) {
                if (emp.geteID().equals(employee.geteID())) {
                    writer.write(UtilityMethod.seperateID(employee.geteID()) + "," + employee.geteName() + "," + employee.geteNIC() + "," +
                            employee.geteContactNo() + "," + UtilityMethod.seperateID(employee.geteDesignation().getId()));
                    writer.newLine();
                } else {
                    writer.write(UtilityMethod.seperateID(emp.geteID()) + "," + emp.geteName() + "," + emp.geteNIC() + "," +
                            emp.geteContactNo() + "," + UtilityMethod.seperateID(emp.geteDesignation().getId()));
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
    public boolean deleteEmployeeData(String eID) {
        ObservableList<Employee> employeeObservableList = loadAllEmployeeData();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Employee employee : employeeObservableList) {
                if (!employee.geteID().equals(UtilityMethod.seperateID(eID))) {
                    writer.write(UtilityMethod.seperateID(employee.geteID()) + "," + employee.geteName() + "," + employee.geteNIC() + "," +
                            employee.geteContactNo() + "," + UtilityMethod.seperateID(employee.geteDesignation().getId()));
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
    public SortedList<Employee> searchTable(ObservableList<Employee> employeeObservableList, TextField searchTextField) {
        FilteredList<Employee> filteredData = new FilteredList<>(employeeObservableList, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (employee.geteName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.geteNIC().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(employee.geteContactNo()).contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.geteDesignation().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Employee> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }
}

