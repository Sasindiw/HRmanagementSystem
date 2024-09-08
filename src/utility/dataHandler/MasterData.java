package utility.dataHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MasterData {
    public static String USER_TYPE_ADMIN = "ADMIN";
    public static String USER_TYPE_HR_MANAGER = "HR-MANAGER";
    public static String USER_TYPE_AST_HR_MANAGER = "AST-HR-MANAGER";
    public static ObservableList<String> USER_TYPE_OBSERVABLE_LIST = FXCollections.observableArrayList(USER_TYPE_ADMIN, USER_TYPE_HR_MANAGER, USER_TYPE_AST_HR_MANAGER);

    //Default User Data
    //        User defaultUser = new User("admin@gmail.com", "Default", "Admin", "defaultPassword", MasterData.USER_TYPE_ADMIN);

    public static String DEFAULT_USER_EMAIL = "admin@gmail.com";
    public static String DEFAULT_USER_FIRST_NAME = "Default";
    public static String DEFAULT_USER_LAST_NAME = "Admin";
    public static String DEFAULT_USER_PASSWORD = "defaultPassword";
    public static String DEFAULT_USER_TYPE = USER_TYPE_ADMIN;

    //Department Availability
    public static String DEPT_STATUS_AVAILABLE = "AVAILABLE";
    public static String DEPT_STATUS_NOT_AVAILABLE = "NOT-AVAILABLE";
    public static ObservableList<String> DEPT_STATUS_OBSERVABLE_LIST = FXCollections.observableArrayList(DEPT_STATUS_AVAILABLE, DEPT_STATUS_NOT_AVAILABLE);

    //Designation Availability
    public static String DSG_STATUS_AVAILABLE = "AVAILABLE";
    public static String DSG_STATUS_NOT_AVAILABLE = "NOT-AVAILABLE";
    public static ObservableList<String> DSG_STATUS_OBSERVABLE_LIST = FXCollections.observableArrayList(DSG_STATUS_AVAILABLE, DSG_STATUS_NOT_AVAILABLE);
}
