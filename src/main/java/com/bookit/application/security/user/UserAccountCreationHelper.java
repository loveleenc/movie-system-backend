package com.bookit.application.security.user;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserAccountCreationHelper {
    public static final String accountActivationEmailSubject = "Account activation: welcome to Book Show!";
    static final String emailRegex = "^([\\w-]|(?<!\\.)\\.)+[a-zA-Z0-9]@[a-zA-Z0-9]([a-zA-Z0-9\\-]+)((\\.([a-zA-Z]){2,9}){0,2})$";

    public UserAccountCreationHelper(){}

    public Boolean passwordCriteriaFulfilled(String password) {
        Boolean containsUppercaseCharacter = false;
        Boolean containsLowercaseCharacter = false;
        Boolean containsDigit = false;
        Boolean containsSpecialCharacter = false;
        Boolean lengthIsInRange = password.length() >= 8 && password.length() < 20;
        List<Character> specialCharacters = Arrays.asList('!', '*', '@', '#', '$', '%', '^', '*', '(', ')');
        if (!lengthIsInRange) {
            return false;
        }
        for (int i = 0; i < password.length(); i++) {
            if (containsDigit && containsLowercaseCharacter && containsUppercaseCharacter && containsSpecialCharacter) {
                return true;
            }
            if (Character.isDigit(password.charAt(i))) {
                containsDigit = true;
            } else if (Character.isUpperCase(password.charAt(i))) {
                containsUppercaseCharacter = true;
            } else if (Character.isLowerCase(password.charAt(i))) {
                containsLowercaseCharacter = true;
            } else if (specialCharacters.contains(password.charAt(i))) {
                containsSpecialCharacter = true;
            }
        }
        return containsUppercaseCharacter && containsLowercaseCharacter && containsDigit && containsSpecialCharacter;
    }

    public Boolean usernameCriteriaFulfilled(String username) {
        Boolean lengthIsInRange = username.length() >= 8 && username.length() < 15;
        if (!lengthIsInRange) {
            return false;
        }
        for (int i = 0; i < username.length(); i++) {
            if (!(Character.isDigit(username.charAt(i)) ||
                    Character.isUpperCase(username.charAt(i)) ||
                    Character.isLowerCase(username.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public Boolean emailIsValid(String email){
        return emailRegex.matches(email);
    }

    public String createAccountActivationEmailMessage(String username, UrlResource accountActivationUrl){
        return String.format("Hello %s,\nClick the link below to activate your account-\n%s", username, accountActivationUrl.getURL().toString());
    }

    public String createAccountActivationEmailMessage(String username){
        return String.format("Hello %s,\nYou will be contacted soon by our team to verify and activate your account.", username);
    }
}
