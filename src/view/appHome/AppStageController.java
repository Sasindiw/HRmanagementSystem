package view.appHome;

import bean.User;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import utility.dataHandler.MasterData;
import utility.dataHandler.UserAuthentication;
import utility.navigation.MenuBarHandler;
import utility.navigation.Navigation;

import java.net.URL;
import java.util.ResourceBundle;

public class AppStageController implements Initializable {
    @FXML
    private AnchorPane baseAnchorPane;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton departmentButton;

    @FXML
    private JFXButton designationButton;

    @FXML
    private JFXButton employeeButton;

    @FXML
    private JFXButton userButton;

    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private Label userLabel;

    private Navigation navigation;

    private User loggedUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loggedUser = UserAuthentication.getAuthenticatedUser();
        userLabel.setText(loggedUser.getuFName() + " " + loggedUser.getuLName());

        navigation = new Navigation();
        loadHomeDetail();
        managePanelPermission();
    }

    private void loadHomeDetail() {
        navigation.loadHomeDetail(detailAnchorPane);
    }

    @FXML
    private void loadHomeDetail(ActionEvent actionEvent) {
        navigation.loadHomeDetail(detailAnchorPane);
    }

    @FXML
    private void logoutAccount(ActionEvent actionEvent) {
        navigation.logoutAccount(baseAnchorPane);
    }

    @FXML
    private void loadMenuDetail(ActionEvent actionEvent) {
        navigation.loadMenuDetail(detailAnchorPane);
    }
    @FXML
    private void loadOrderDetail(ActionEvent actionEvent) {
        navigation.loadOrderDetail(detailAnchorPane);
    }
    @FXML
    private void loadEmployeeDetail(ActionEvent actionEvent) {
        navigation.loadEmployeeDetail(detailAnchorPane);
    }
    @FXML
    private void loadUserDetail(ActionEvent actionEvent) {
        navigation.loadUserDetail(detailAnchorPane);
    }

    /**
     * Sets the selection based on the pressed button in the menu bar and updates the menu number.
     * Also calls the setStyle() method to update the style of the buttons.
     */
    @FXML
    private void setSelection() {
        if (homeButton.isPressed())
            MenuBarHandler.setMenuNumber(0);
        else if (departmentButton.isPressed())
            MenuBarHandler.setMenuNumber(1);
        else if (designationButton.isPressed())
            MenuBarHandler.setMenuNumber(2);
        else if (employeeButton.isPressed())
            MenuBarHandler.setMenuNumber(3);
        else if (userButton.isPressed())
            MenuBarHandler.setMenuNumber(4);
        else
            MenuBarHandler.setMenuNumber(0);
        setStyle();
    }

    /**
     * Manages the visibility of buttons based on the user's type.
     * Sets the visibility and managed property of the departmentButton, designationButton,
     * employeeButton, and userButton based on the logged-in user's type.
     */
    private void managePanelPermission(){
        if(loggedUser.getuType().equals(MasterData.USER_TYPE_ADMIN)){
            departmentButton.setVisible(false);
            designationButton.setVisible(false);
            employeeButton.setVisible(false);
            userButton.setVisible(true);
        }else if(loggedUser.getuType().equals(MasterData.USER_TYPE_HR_MANAGER)){
            departmentButton.setVisible(true);
            designationButton.setVisible(true);
            employeeButton.setVisible(true);
            userButton.setVisible(false);
        }else if(loggedUser.getuType().equals(MasterData.USER_TYPE_AST_HR_MANAGER)){
            departmentButton.setVisible(false);
            designationButton.setVisible(false);
            employeeButton.setVisible(true);
            userButton.setVisible(false);
        }else{
            departmentButton.setVisible(true);
            designationButton.setVisible(true);
            employeeButton.setVisible(true);
            userButton.setVisible(true);
        }
        departmentButton.managedProperty().bind(departmentButton.visibleProperty());
        designationButton.managedProperty().bind(designationButton.visibleProperty());
        employeeButton.managedProperty().bind(employeeButton.visibleProperty());
        userButton.managedProperty().bind(userButton.visibleProperty());
    }

    /**
     * Sets the style of the buttons based on the menu number.
     * Uses a switch statement to determine the style to be applied to the button.
     * The selectionColor represents the background color for the selected button.
     */
    public void setStyle() {
        String selectionColor = "-fx-background-color:   #4e678e; ";
        resetButtons();
        switch (MenuBarHandler.getMenuNumber()) {
            case 0:
                homeButton.setStyle(selectionColor);
                break;
            case 1:
                departmentButton.setStyle(selectionColor);
                break;
            case 2:
                designationButton.setStyle(selectionColor);
                break;
            case 3:
                employeeButton.setStyle(selectionColor);
                break;
            case 4:
                userButton.setStyle(selectionColor);
                break;
            default:
                homeButton.setStyle(selectionColor);
        }
    }

    /**
     * Resets the style of all the buttons to the default color.
     * Uses a defaultColor string to represent the default background color of the buttons.
     */
    private void resetButtons() {
        String defaultColor = "-fx-background-color:    #1f2c41; ";
        homeButton.setStyle(defaultColor);
        departmentButton.setStyle(defaultColor);
        designationButton.setStyle(defaultColor);
        employeeButton.setStyle(defaultColor);
        userButton.setStyle(defaultColor);
    }



}
