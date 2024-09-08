package service.impl;

import bean.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import service.UserService;
import utility.dataHandler.DataEncryption;
import utility.dataHandler.FileHandler;
import utility.dataHandler.MasterData;
import utility.popUp.AlertPopUp;

import java.io.*;

public class UserFileWriterServiceImpl implements UserService {
    private String filePath;
    private final String fileName = "user_data.txt";

    public UserFileWriterServiceImpl() {
        initializeService();
        loadAllUserData();
    }

    private void initializeService(){
        this.filePath = FileHandler.initializeService(fileName);
    }

    public static void startFileWriterService(){
        new UserFileWriterServiceImpl();
    }

    @Override
    public ObservableList<User> loadAllUserData() {
        ObservableList<User> userObservableList = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                userObservableList.add(new User(userData[0], userData[1], userData[2], userData[3], userData[4]));
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }

        // If no records found, add a default user
        if (userObservableList.isEmpty()) {
            User defaultUser = createDefaultUser();
            userObservableList.add(defaultUser);
        }

        return userObservableList;
    }

    private User createDefaultUser() {
        User defaultUser = new User(MasterData.DEFAULT_USER_EMAIL, MasterData.DEFAULT_USER_FIRST_NAME, MasterData.DEFAULT_USER_LAST_NAME, MasterData.DEFAULT_USER_PASSWORD, MasterData.USER_TYPE_ADMIN);
        insertUserData(defaultUser);
        System.out.println("Default User Created");
        return defaultUser;
    }

    @Override
    public User loadSpecificUser(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].toLowerCase().equals(email.toLowerCase())) {
                    return new User(userData[0], userData[1], userData[2], userData[3], userData[4]);
                }
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }
        return null;
    }

    @Override
    public boolean insertUserData(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(user.getuEmail() + "," + user.getuFName() + "," + user.getuLName() + "," +
                    DataEncryption.passwordEncryption(user.getuPassword()) + "," + user.getuType());
            writer.newLine();
            FileHandler.removeEmptyLines(filePath);
            return true;
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }
        return false;
    }

    @Override
    public boolean updateUserData(User user) {
        ObservableList<User> users = loadAllUserData();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User u : users) {
                if (u.getuEmail().equals(user.getuEmail())) {
                    writer.write(user.getuEmail() + "," + user.getuFName() + "," + user.getuLName() + "," +
                            DataEncryption.passwordEncryption(user.getuPassword()) + "," + user.getuType());
                    writer.newLine();
                } else {
                    writer.write(u.getuEmail() + "," + u.getuFName() + "," + u.getuLName() + "," +
                            u.getuPassword() + "," + u.getuType());
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
    public boolean deleteUserData(String email) {
        ObservableList<User> users = loadAllUserData();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users) {
                if (!user.getuEmail().equals(email)) {
                    writer.write(user.getuEmail() + "," + user.getuFName() + "," + user.getuLName() + "," +
                            DataEncryption.passwordEncryption(user.getuPassword()) + "," + user.getuType());
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
    public SortedList<User> searchTable(ObservableList<User> userObservableList, TextField searchTextField) {

        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<User> filteredData = new FilteredList<>(userObservableList, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getuFName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (user.getuLName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<User> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }
}

