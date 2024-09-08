package service;

import bean.Designation;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
/**
 * This interface defines the contract for DesignationService implementations.
 */
public interface DesignationService {
    /**
     * Loads all designations from a data source and returns an observable list.
     *
     * @return The observable list of designations.
     */
    public ObservableList<Designation> loadAllDesignations();

    /**
     * Loads a specific designation from the data source based on the provided designation ID.
     *
     * @param designationId The ID of the designation to load.
     * @return The loaded designation object.
     */
    public Designation loadSpecificDesignation(String designationId);

    /**
     * Adds a new designation to the data source.
     *
     * @param designation The designation to add.
     * @return True if the designation was successfully added, false otherwise.
     */
    public Boolean addDesignation(Designation designation);

    /**
     * Updates an existing designation in the data source.
     *
     * @param designation The designation to update.
     * @return True if the designation was successfully updated, false otherwise.
     */
    public Boolean updateDesignation(Designation designation);

    /**
     * Deletes a designation from the data source based on the provided designation ID.
     *
     * @param designationId The ID of the designation to delete.
     * @return True if the designation was successfully deleted, false otherwise.
     */
    public Boolean deleteDesignation(String designationId);

    /**
     * Searches and filters the designationObservableList based on the searchTextField input.
     *
     * @param designationObservableList The original list of designations.
     * @param searchTextField          The text field used for searching.
     * @return The sorted list of designations after filtering.
     */
    public SortedList<Designation> searchTable(ObservableList<Designation> designationObservableList, TextField searchTextField);
}
