package service;

import bean.Employee;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
/**
 * This interface defines the contract for EmployeeService implementations.
 */
public interface EmployeeService {
    /**
     * Loads all employee data from a data source and returns an observable list.
     *
     * @return The observable list of employee data.
     */
    public ObservableList<Employee> loadAllEmployeeData();

    /**
     * Loads a specific employee from the data source based on the provided employee ID.
     *
     * @param employeeId The ID of the employee to load.
     * @return The loaded employee object.
     */
    public Employee loadSpecificEmployee(String employeeId);

    /**
     * Inserts employee data into the data source.
     *
     * @param employee The employee data to insert.
     * @return True if the employee data was successfully inserted, false otherwise.
     */
    public boolean insertEmployeeData(Employee employee);

    /**
     * Updates existing employee data in the data source.
     *
     * @param employee The updated employee data.
     * @return True if the employee data was successfully updated, false otherwise.
     */
    public boolean updateEmployeeData(Employee employee);

    /**
     * Deletes employee data from the data source based on the provided employee ID.
     *
     * @param employeeId The ID of the employee to delete.
     * @return True if the employee data was successfully deleted, false otherwise.
     */
    public boolean deleteEmployeeData(String employeeId);

    /**
     * Searches and filters the employeeObservableList based on the searchTextField input.
     *
     * @param employeeObservableList The original list of employees.
     * @param searchTextField        The text field used for searching.
     * @return The sorted list of employees after filtering.
     */
    public SortedList<Employee> searchTable(ObservableList<Employee> employeeObservableList, TextField searchTextField);

}
