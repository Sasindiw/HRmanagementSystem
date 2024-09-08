package utility.dataHandler;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {
    /**
     * Checks if a string field is not empty.
     * Returns true if the string field is not null or empty.
     * @param stringField The string field to check.
     * @return True if the string field is not empty, false otherwise.
     */
    public static boolean TextFieldNotEmpty(String stringField){
        //returning string fields empty as default value
        boolean returnVal = false;
        if(stringField != null  && !stringField.isEmpty()){
            returnVal = true;
        }
        return returnVal;
    }


    /**
     * Checks if a text field is not empty and sets the validation text to a label if it is empty.
     * @param stringField The string field to check.
     * @param label The label to set the validation text.
     * @param validationText The text to set in the label if the field is empty.
     */
    public static void TextFieldNotEmpty(String stringField, Label label, String validationText){

        if(!TextFieldNotEmpty(stringField)){
            label.setText(validationText);
        }

    }

    /**
     * Checks if two password fields match.
     * Returns true if the password fields have the same value.
     * @param passwordField The first password field.
     * @param ConfirmPasswordField The second password field to compare.
     * @return True if the password fields match, false otherwise.
     */
    public static boolean PasswordFieldMatch(PasswordField passwordField, PasswordField ConfirmPasswordField){
        //returning integer fields empty as default value
        boolean returnVal = false;
        if(passwordField.getText().equals(ConfirmPasswordField.getText())){
            returnVal = true;
        }
        return returnVal;
    }

    /**
     * Checks if two password fields match and sets the validation text to respective labels if they don't match.
     * @param passwordField The first password field.
     * @param ConfirmPasswordField The second password field to compare.
     * @param passwordLabel The label to set the validation text for the password field.
     * @param confirmPasswordLabel The label to set the validation text for the confirm password field.
     * @param validationText The text to set in the labels if the password fields don't match.
     */
    public static void PasswordFieldMatch(PasswordField passwordField, PasswordField ConfirmPasswordField, Label passwordLabel, Label confirmPasswordLabel,String validationText){

        if(!PasswordFieldMatch(passwordField, ConfirmPasswordField)){
            passwordLabel.setText(validationText);
            confirmPasswordLabel.setText(validationText);
        }

    }

    /**
     * Checks if an image field is not empty.
     * Returns true if the image field has a valid image.
     * @param imageView The image view to check.
     * @return True if the image field is not empty, false otherwise.
     */
    public static boolean ImageFieldNotEmpty(ImageView imageView){
        boolean returnVal = false;
        try{
            if(!(imageView.getImage() == null) || !(imageView.getImage().isError())){
                returnVal = true;
            }
        }catch (NullPointerException ex){

        }

        return returnVal;
    }

    /**
     * Checks if an image field is not empty and sets the validation text to a label if it is empty.
     * @param imageView The image view to check.
     * @param label The label to set the validation text.
     * @param validationText The text to set in the label if the field is empty.
     */
    public static void ImageFieldNotEmpty(ImageView imageView, Label label, String validationText){

        if(!ImageFieldNotEmpty(imageView)){
            label.setText(validationText);
        }
    }
    //email validation
    public static final Pattern VALIDEMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Checks if an email address is valid using a regular expression pattern.
     * Returns true if the email address is valid.
     * @param emailStr The email address to check.
     * @return True if the email address is valid, false otherwise.
     */
    public static boolean isValidEmail(String emailStr) {
        boolean returnVal = false;
        Matcher matcher = VALIDEMAIL.matcher(emailStr);

        if(matcher.find()){
            returnVal = true;
        }
        return returnVal;
    }

    /**
     * Checks if an email address is valid and sets the validation text to a label if it is invalid.
     * @param emailStr The email address to check.
     * @param label The label to set the validation text.
     * @param validationText The text to set in the label if the email address is invalid.
     */
    public static void isValidEmail(String emailStr, Label label, String validationText) {

        if((!isValidEmail(emailStr))&& (!emailStr.isEmpty())){
            label.setText(validationText);
        }
    }
    //phone number validation for 10 digits, start with zero, next value from 1-9 and rest 8 digits from 0-9
    public static  final Pattern VALIDPHONENO = Pattern.compile("^[0][1-9][0-9]{8}$");

    /**
     * Checks if a phone number is valid using a regular expression pattern.
     * Returns true if the phone number is valid.
     * @param phone The phone number to check.
     * @return True if the phone number is valid, false otherwise.
     */
    public static boolean isValidPhoneNo(String phone){

        Matcher matcher = VALIDPHONENO.matcher(phone);
        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Checks if a phone number is valid and sets the validation text to a label if it is invalid.
     * @param phone The phone number to check.
     * @param label The label to set the validation text.
     * @param validationText The text to set in the label if the phone number is invalid.
     */
    public static void isValidPhoneNo(String phone, Label label, String validationText){

        if((!isValidPhoneNo(phone))&& (!phone.isEmpty())){
            label.setText(validationText);
        }
    }
    //checking for integer number
    public static  final Pattern VALIDINTEGER = Pattern.compile("\\d+$");
    public static  final Pattern VALIDFLOAT = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+$");
    public static boolean isValidNumberFormat(String number) {
        int numberCheck = 0;
        try{
            Integer.parseInt(number);
            numberCheck = 1;
        }catch(NumberFormatException ex){

        }
        try{
            Float.parseFloat(number);
            numberCheck = 2;
        }catch(NumberFormatException ex){

        }
        if(numberCheck == 1){
            Matcher matcher = VALIDINTEGER.matcher(number);
            if(matcher.matches()){
                return true;
            }else{
                return false;
            }
        }else if(numberCheck == 2){
            Matcher matcher = VALIDFLOAT.matcher(number);
            if(matcher.matches()){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    /**
     * Checks if a number is valid (integer or float).
     * Returns true if the number is a valid integer or float.
     * @param number The number to check.
     * @return True if the number is a valid integer or float, false otherwise.
     */
    public static void isValidNumberFormat(String number, Label label, String validationText) {
        if((!isValidNumberFormat(number)) && (!number.isEmpty())){
            label.setText(validationText);
        }
    }

    /**
     * Checks if a string has a maximum length.
     * Returns true if the string length is less than or equal to the maximum length.
     * @param data The string to check.
     * @param maxLength The maximum length allowed.
     * @return True if the string length is less than or equal to the maximum length, false otherwise.
     */
    public static boolean isValidMaximumLength(String data, int maxLength){
        boolean returnVal = true;
        if(data.length() > maxLength){
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * Checks if a string has a maximum length and sets the validation text to a label if it exceeds the maximum length.
     * @param data The string to check.
     * @param maxLength The maximum length allowed.
     * @param label The label to set the validation text.
     * @param validationText The text to set in the label if the string length exceeds the maximum length.
     */
    public static void isValidMaximumLength(String data, int maxLength, Label label, String validationText){
        if(!isValidMaximumLength(data,maxLength)){
            label.setText(validationText);
        }
    }


    public static  final Pattern VALIDOLDNIC = Pattern.compile("^[0-9]{9}[vVxX]$");
    public static  final Pattern VALIDNEWNIC = Pattern.compile("^[1-2]{1}[0-9]{11}$");

    /**
     * Checks if a National Identity Card (NIC) number is valid.
     * Returns true if the NIC number is valid (matches either the old or new NIC pattern).
     * @param textField The text field containing the NIC number to check.
     * @return True if the NIC number is valid, false otherwise.
     */
    public static boolean isValidNIC(TextField textField){
        if(isValidNumberFormat(textField.getText())){
            Matcher matcher = VALIDNEWNIC.matcher(textField.getText());
            if(matcher.matches()){
                return true;
            }else{
                return false;
            }
        }else{
            Matcher matcher = VALIDOLDNIC.matcher(textField.getText());
            if(matcher.matches()){
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * Checks if a National Identity Card (NIC) number is valid and sets the validation text to a label if it is invalid.
     * @param textField The text field containing the NIC number to check.
     * @param label The label to set the validation text.
     * @param validationText The text to set in the label if the NIC number is invalid.
     */
    public static void isValidNIC(TextField textField, Label label, String validationText){
        if(!(isValidNIC(textField))){
            label.setText(validationText);
        }
    }
}
