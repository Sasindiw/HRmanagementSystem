package service;

import javafx.collections.ObservableList;
import bean.User;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
/**
 * This interface defines the contract for UserService implementations.
 */
public interface UserService {
    /**
     * Loads all user data from a data source and returns an observable list.
     *
     * @return The observable list of user data.
     */
    public ObservableList<User> loadAllUserData();

    /**
     * Loads a specific user from the data source based on the provided email.
     *
     * @param email The email of the user to load.
     * @return The loaded user object.
     */
    public User loadSpecificUser(String email);

    /**
     * Inserts user data into the data source.
     *
     * @param user The user data to insert.
     * @return True if the user data was successfully inserted, false otherwise.
     */
    public boolean insertUserData(User user);

    /**
     * Updates existing user data in the data source.
     *
     * @param user The updated user data.
     * @return True if the user data was successfully updated, false otherwise.
     */
    public boolean updateUserData(User user);

    /**
     * Deletes user data from the data source based on the provided email.
     *
     * @param email The email of the user to delete.
     * @return True if the user data was successfully deleted, false otherwise.
     */
    public boolean deleteUserData(String email);

    /**
     * Searches and filters the userObservableList based on the searchTextField input.
     *
     * @param userObservableList The original list of users.
     * @param searchTextField    The text field used for searching.
     * @return The sorted list of users after filtering.
     */
    public SortedList<User> searchTable(ObservableList<User> userObservableList, TextField searchTextField);

}
