package view.userManagement;

import bean.User;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import service.UserService;
import service.impl.UserFileWriterServiceImpl;
import utility.dataHandler.DataValidation;
import utility.dataHandler.MasterData;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> fNameColumn;

    @FXML
    private TableColumn<User, String> lNameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> typeColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField fNameTextField;

    @FXML
    private PasswordField pwPasswordField;

    @FXML
    private PasswordField confirmPWPasswordField;

    @FXML
    private TextField lNameTextField;

    @FXML
    private Label emailLabel;

    @FXML
    private Label fNameLabel;

    @FXML
    private Label lNameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton updateButton;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    private static User selectedUser = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData() {
        // Load user data from a service
        UserService userService = new UserFileWriterServiceImpl();
        ObservableList<User> userObservableList = userService.loadAllUserData();

        // Set cell value factories for table columns
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("uEmail"));
        fNameColumn.setCellValueFactory(new PropertyValueFactory<>("uFName"));
        lNameColumn.setCellValueFactory(new PropertyValueFactory<>("uLName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("uPassword"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("uType"));

        // Clear and set the user table with loaded data
        userTable.setItems(null);
        userTable.setItems(userObservableList);

        // Set dropdown ChoiceBox data
        typeChoiceBox.setValue(MasterData.USER_TYPE_OBSERVABLE_LIST.get(0));
        typeChoiceBox.setItems(MasterData.USER_TYPE_OBSERVABLE_LIST);

        // Search the table based on user input
        searchTable(userObservableList);
    }

    public void searchTable(ObservableList<User> userObservableList) {
        // Search the user table based on the given input
        UserService userService = new UserFileWriterServiceImpl();

        SortedList<User> sortedData = userService.searchTable(userObservableList, searchTextField);

        // Bind the SortedList to the TableView
        sortedData.comparatorProperty().bind(userTable.comparatorProperty());

        // Set the sorted and filtered data to the table
        userTable.setItems(sortedData);
    }

    @FXML
    void addUser(ActionEvent event) {
        // Add a new user to the table
        ObservableList<User> modelList = userTable.getItems();
        ArrayList<String> userList = new ArrayList<>();
        for (User user : modelList) {
            userList.add(user.getuEmail().toLowerCase());
        }

        clearLabels();

        if (userValidation() && !UtilityMethod.checkDataAvailability(userList, emailTextField.getText().toLowerCase())) {
            User user = new User();
            UserService userService = new UserFileWriterServiceImpl();

            // Set user data from input fields
            user.setuEmail(emailTextField.getText());
            user.setuFName(fNameTextField.getText());
            user.setuLName(lNameTextField.getText());
            user.setuPassword(pwPasswordField.getText());
            user.setuType(typeChoiceBox.getValue());

            if (userService.insertUserData(user)) {
                AlertPopUp.insertSuccessfully("User");
                loadData();
                clearFields(event);
            } else {
                AlertPopUp.insertionFailed("User");
            }

        } else {
            userValidationMessage();
        }
    }

    @FXML
    void updateUser(ActionEvent event) {
        // Update an existing user in the table
        clearLabels();

        if (userValidation()) {
            User user = new User();
            UserService userService = new UserFileWriterServiceImpl();

            // Set user data from input fields
            user.setuEmail(selectedUser.getuEmail());
            user.setuFName(fNameTextField.getText());
            user.setuLName(lNameTextField.getText());
            user.setuPassword(pwPasswordField.getText());
            user.setuType(typeChoiceBox.getValue());

            if (userService.updateUserData(user)) {
                AlertPopUp.updateSuccessfully("User");
                loadData();
                clearFields(event);
            } else {
                AlertPopUp.updateFailed("User");
            }
        } else {
            userValidationMessage();
        }
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        // Delete a user from the table
        if (selectedUser != null) {
            UserService userService = new UserFileWriterServiceImpl();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("User");

            if (action.get() == ButtonType.OK) {
                if (userService.deleteUserData(selectedUser.getuEmail())) {
                    AlertPopUp.deleteSuccessfull("User");
                    loadData();
                    clearFields(event);
                } else {
                    AlertPopUp.deleteFailed("User");
                }
            } else {
                loadData();
            }
        } else {
            AlertPopUp.selectRowToDelete("User");
        }
    }

    @FXML
    void setSelectedUserDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);
            emailTextField.setEditable(false);
            selectedUser = userTable.getSelectionModel().getSelectedItem();

            // Set selected user data to input fields
            emailTextField.setText(selectedUser.getuEmail());
            fNameTextField.setText(selectedUser.getuFName());
            lNameTextField.setText(selectedUser.getuLName());
            typeChoiceBox.setValue(selectedUser.getuType());
        } catch (NullPointerException exception) {
            // Handle null pointer exception silently
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        // Clear input fields and reset components
        emailTextField.setText("");
        fNameTextField.setText("");
        lNameTextField.setText("");
        pwPasswordField.setText("");
        confirmPWPasswordField.setText("");
        typeChoiceBox.setValue(MasterData.USER_TYPE_OBSERVABLE_LIST.get(0));
        resetComponents();
        clearLabels();
        selectedUser = null;
    }

    private void clearLabels() {
        // Clear validation labels
        emailLabel.setText("");
        passwordLabel.setText("");
        fNameLabel.setText("");
        lNameLabel.setText("");
    }

    private boolean userValidation() {
        // Validate user input data
        return DataValidation.TextFieldNotEmpty(emailTextField.getText())
                && DataValidation.TextFieldNotEmpty(fNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(lNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(pwPasswordField.getText())
                && DataValidation.TextFieldNotEmpty(pwPasswordField.getText())

                && DataValidation.isValidMaximumLength(emailTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(fNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(lNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(pwPasswordField.getText(), 20)
                && DataValidation.isValidMaximumLength(confirmPWPasswordField.getText(), 20)

                && DataValidation.isValidEmail(emailTextField.getText())
                && DataValidation.PasswordFieldMatch(pwPasswordField, confirmPWPasswordField);
    }

    private void userValidationMessage() {
        // Display validation messages for user input
        ObservableList<User> modelList = userTable.getItems();
        ArrayList<String> userList = new ArrayList<>();
        for (User user : modelList) {
            userList.add(user.getuEmail().toLowerCase());
        }

        if (!(DataValidation.TextFieldNotEmpty(emailTextField.getText())
                && DataValidation.TextFieldNotEmpty(fNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(lNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(pwPasswordField.getText())
                && DataValidation.TextFieldNotEmpty(confirmPWPasswordField.getText()))) {

            // Validate and show required field messages
            DataValidation.TextFieldNotEmpty(emailTextField.getText(), emailLabel, "User Email Required!");
            DataValidation.TextFieldNotEmpty(fNameTextField.getText(), fNameLabel, "User First Name Required!");
            DataValidation.TextFieldNotEmpty(lNameTextField.getText(), lNameLabel, "User Last Name Required!");
            DataValidation.TextFieldNotEmpty(confirmPWPasswordField.getText(), passwordLabel, "User Confirm Password Required!");
            DataValidation.TextFieldNotEmpty(pwPasswordField.getText(), passwordLabel, "User Password Required!");
        }

        if (!(DataValidation.isValidMaximumLength(emailTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(fNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(lNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(pwPasswordField.getText(), 20)
                && DataValidation.isValidMaximumLength(confirmPWPasswordField.getText(), 20))) {

            // Validate and show maximum length exceeded messages
            DataValidation.isValidMaximumLength(emailTextField.getText(), 45, emailLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(fNameTextField.getText(), 45, fNameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(lNameTextField.getText(), 45, lNameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(pwPasswordField.getText(), 20, passwordLabel, "Field Limit 20 Exceeded!");
            DataValidation.isValidMaximumLength(confirmPWPasswordField.getText(), 20, passwordLabel, "Field Limit 20 Exceeded!");
        }

        if (!(DataValidation.isValidEmail(emailTextField.getText())
                && DataValidation.PasswordFieldMatch(pwPasswordField, confirmPWPasswordField))) {

            // Validate and show email and password match messages
            DataValidation.isValidEmail(emailTextField.getText(), emailLabel, "Invalid Email Address");
            DataValidation.PasswordFieldMatch(pwPasswordField, confirmPWPasswordField, passwordLabel, passwordLabel, "Password doesn't match");
        }

        if (!UtilityMethod.checkDataAvailability(userList, emailTextField.getText().toLowerCase())) {
            checkUserEmailAvailability();
        }
    }

    @FXML
    private void checkUserEmailAvailability() {
        ObservableList<User> modelList = userTable.getItems();
        ArrayList<String> userList = new ArrayList<>();
        for (User user : modelList) {
            userList.add(user.getuEmail().toLowerCase());
        }

        if (emailTextField.getText().isEmpty()) {
            emailLabel.setStyle("-fx-text-fill: #ff0000 ");
            emailLabel.setText("Email Cannot be empty");
        } else if (UtilityMethod.checkDataAvailability(userList, emailTextField.getText().toLowerCase()) && DataValidation.isValidEmail(emailTextField.getText())) {
            emailLabel.setStyle("-fx-text-fill: #ff0000 ");
            emailLabel.setText("Email Already Taken!!");
        } else {
            if (DataValidation.isValidEmail(emailTextField.getText())) {
                emailLabel.setStyle("-fx-text-fill: #00B605 ");
                emailLabel.setText("Email Available");
            } else {
                emailLabel.setStyle("-fx-text-fill: #ff0000 ");
                emailLabel.setText("Invalid Email Address!!");
            }
        }
    }

    private void resetComponents() {
        // Reset visibility and editability of components
        addButton.setVisible(true);
        updateButton.setVisible(false);
        emailTextField.setEditable(true);
    }


}
