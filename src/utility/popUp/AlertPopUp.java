package utility.popUp;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertPopUp {

    /**
     * Shows an error message dialog with the provided exception details.
     * @param ex The exception that occurred.
     */
    public static void generalError(Exception ex) {
        // Get the name of the method where the error occurred
        String classMethod = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Create and show an error alert dialog
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setTitle("Error Occurred!..");
        msg.setHeaderText(null);
        msg.setContentText("An error occurred in method " + classMethod + ". Exception details: " + ex);
        msg.showAndWait();
    }

    /**
     * Shows an information message dialog indicating a successful insertion.
     * @param text The additional text to display in the message.
     */
    public static void insertSuccessfully(String text) {
        // Create and show an information alert dialog
        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setTitle("Successful..");
        msg.setHeaderText(null);
        msg.setContentText(text + " added successfully.");
        msg.showAndWait();
    }

    /**
     * Shows an error message dialog indicating a failed insertion.
     * @param text The additional text to display in the message.
     */
    public static void insertionFailed(String text) {
        // Create and show an error alert dialog
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setTitle("Error Occurred!..");
        msg.setHeaderText(null);
        msg.setContentText("Failed to add " + text);
        msg.showAndWait();
    }

    /**
     * Shows an information message dialog indicating a successful update.
     * @param text The additional text to display in the message.
     */
    public static void updateSuccessfully(String text) {
        // Create and show an information alert dialog
        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setTitle("Successful..");
        msg.setHeaderText(null);
        msg.setContentText(text + " updated successfully.");
        msg.showAndWait();
    }

    /**
     * Shows an error message dialog indicating a failed update.
     * @param text The additional text to display in the message.
     */
    public static void updateFailed(String text) {
        // Create and show an error alert dialog
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setTitle("Error Occurred!..");
        msg.setHeaderText(null);
        msg.setContentText("Failed to update " + text + ", please try again.");
        msg.showAndWait();
    }

    /**
     * Shows an information message dialog indicating that a row should be selected.
     * @param text The additional text to display in the message.
     */
    public static void selectRow(String text) {
        // Create and show an information alert dialog
        Alert successMsg = new Alert(Alert.AlertType.INFORMATION);
        successMsg.setTitle("Please Select..");
        successMsg.setHeaderText(null);
        successMsg.setContentText("Please select a " + text);
        successMsg.showAndWait();
    }

    /**
     * Shows an information message dialog indicating a successful deletion.
     * @param text The additional text to display in the message.
     */
    public static void deleteSuccessfull(String text) {
        // Create and show an information alert dialog
        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setTitle("Successful..");
        msg.setHeaderText(null);
        msg.setContentText(text + " deleted successfully.");
        msg.showAndWait();
    }

    /**
     * Shows an error message dialog indicating a failed deletion.
     * @param text The additional text to display in the message.
     */
    public static void deleteFailed(String text) {
        // Create and show an error alert dialog
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setTitle("Error Occurred!..");
        msg.setHeaderText(null);
        msg.setContentText(text);
        msg.showAndWait();
    }

    /**
     * Shows a confirmation dialog for deleting an item.
     * @param text The additional text to display in the confirmation message.
     * @return Optional containing the user's button choice (OK or Cancel).
     */
    public static Optional<ButtonType> deleteConfirmation(String text) {
        // Create and show a confirmation alert dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Your Request to Delete");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete the selected " + text + " ?");
        Optional<ButtonType> action = alert.showAndWait();
        return action;
    }

    /**
     * Shows an information message dialog indicating that a row should be selected for deletion.
     * @param text The additional text to display in the message.
     */
    public static void selectRowToDelete(String text) {
        // Create and show an information alert dialog
        Alert successMsg = new Alert(Alert.AlertType.INFORMATION);
        successMsg.setTitle("Please Select..");
        successMsg.setHeaderText(null);
        successMsg.setContentText("Please select a " + text + " record to delete.");
        successMsg.showAndWait();
    }

    /**
     * Shows a confirmation dialog for logging out.
     * @return Optional containing the user's button choice (OK or Cancel).
     */
    public static Optional<ButtonType> logoutConfirmation() {
        // Create and show a confirmation alert dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Your Logout Request");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to log out now?");
        Optional<ButtonType> action = alert.showAndWait();
        return action;
    }

}
