package view.appHome;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utility.dataHandler.DataValidation;
import utility.dataHandler.MasterData;
import utility.dataHandler.UserAuthentication;
import utility.navigation.Navigation;


public class AppLoginController{

    @FXML
    private AnchorPane baseAnchorPane;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label validationLabel;

    /**
     * Performs the login action when the login button is pressed.
     * Validates the fields, authenticates the user using the provided email and password,
     * and handles the navigation based on the authentication result.
     * Displays the validation message or navigates to the application stage.
     */
    @FXML
    void login(ActionEvent event) {
        if (fieldValidation()) {
            String authenticateMessage = UserAuthentication.authenticateUser(emailTextField.getText(), passwordTextField.getText());

            if (authenticateMessage.equals("true")){
                Navigation navigation = new Navigation();
                navigation.loadAppStage(baseAnchorPane);
            } else {
                validationLabel.setText(authenticateMessage);
            }
        } else {
            fieldValidationMessage();
        }
    }

    /**
     * Validates the fields for login.
     * Checks if the email and password fields are not empty and if the email is in a valid format.
     * Returns true if all conditions are met, false otherwise.
     */
    private boolean fieldValidation() {
        return DataValidation.TextFieldNotEmpty(emailTextField.getText())
                && DataValidation.TextFieldNotEmpty(passwordTextField.getText())
                && DataValidation.isValidEmail(emailTextField.getText());
    }

    /**
     * Displays the field validation message.
     * Checks each field for empty values and an invalid email format,
     * and sets the corresponding error message in the validation label.
     */
    private void fieldValidationMessage() {
        if (!(DataValidation.TextFieldNotEmpty(emailTextField.getText())
                && DataValidation.TextFieldNotEmpty(passwordTextField.getText()))) {
            DataValidation.TextFieldNotEmpty(emailTextField.getText(), validationLabel, "User Name/Password Fields Cannot be Empty");
            DataValidation.TextFieldNotEmpty(passwordTextField.getText(), validationLabel, "User Name/Password Fields Cannot be Empty");
            DataValidation.isValidEmail(emailTextField.getText(), validationLabel, "Invalid Email Address ");
        }
    }

    /**
     * Clears the validation label.
     * Sets the text of the validation label to an empty string.
     */
    private void clearLabel() {
        validationLabel.setText("");
    }

    /**
     * Clears the login fields.
     * Sets the email and password text fields to empty strings
     * and clears the validation label.
     */
    @FXML
    private void clearFields(ActionEvent event) {
        emailTextField.setText("");
        passwordTextField.setText("");
        clearLabel();
    }

    /**
     * Fills the login fields with default data.
     * Sets the email and password text fields to the default user email and password.
     */
    @FXML
    private void fillData(ActionEvent event){
        emailTextField.setText(MasterData.DEFAULT_USER_EMAIL);
        passwordTextField.setText(MasterData.DEFAULT_USER_PASSWORD);
    }


}
