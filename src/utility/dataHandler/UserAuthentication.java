package utility.dataHandler;


import bean.User;
import service.UserService;
import service.impl.UserFileWriterServiceImpl;

public class UserAuthentication {
    private static User AuthenticatedUser = null;

    public static User getAuthenticatedUser() {
        return AuthenticatedUser;
    }

    public static void setAuthenticatedUser(User AuthenticatedUser) {
        UserAuthentication.AuthenticatedUser = AuthenticatedUser;
    }

    public static String authenticateUser(String userName, String password) {
        UserService userService = new UserFileWriterServiceImpl();
        User user = userService.loadSpecificUser(userName);
        if (user == null) {
            return "Invalid User Email!!";
        } else if (!user.getuPassword().equals(DataEncryption.passwordEncryption(password))) {
            return "Invalid Password!!";
        } else {
            setAuthenticatedUser(user);
            return "true";
        }
    }
}
