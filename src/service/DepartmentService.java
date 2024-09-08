package service;

import bean.Department;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
/**
 * This interface defines the contract for DepartmentService implementations.
 */
public interface DepartmentService {
    /**
     * Loads all departments from a data source and returns an observable list.
     *
     * @return The observable list of departments.
     */
    public ObservableList<Department> loadAllDepartments();

    /**
     * Loads a specific department from the data source based on the provided department ID.
     *
     * @param departmentId The ID of the department to load.
     * @return The loaded department object.
     */
    public Department loadSpecificDepartment(String departmentId);

    /**
     * Adds a new department to the data source.
     *
     * @param department The department to add.
     * @return True if the department was successfully added, false otherwise.
     */
    public Boolean addDepartment(Department department);

    /**
     * Updates an existing department in the data source.
     *
     * @param department The department to update.
     * @return True if the department was successfully updated, false otherwise.
     */
    public Boolean updateDepartment(Department department);

    /**
     * Deletes a department from the data source based on the provided department ID.
     *
     * @param deptId The ID of the department to delete.
     * @return True if the department was successfully deleted, false otherwise.
     */
    public Boolean deleteDepartment(String deptId);

    /**
     * Searches and filters the departmentObservableList based on the searchTextField input.
     *
     * @param departmentObservableList The original list of departments.
     * @param searchTextField         The text field used for searching.
     * @return The sorted list of departments after filtering.
     */
    public SortedList<Department> searchTable(ObservableList<Department> departmentObservableList, TextField searchTextField);
}
