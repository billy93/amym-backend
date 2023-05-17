package com.atibusinessgroup.amanyaman.web.rest.dto;

/**
 * View Model object for storing the user's key and password.
 */
public class KeyAndPasswordDTO {

    private String key;

    private String newPassword;
    private String passwordRetype;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordRetype() {
        return passwordRetype;
    }

    public void setPasswordRetype(String passwordRetype) {
        this.passwordRetype = passwordRetype;
    }

    @Override
    public String toString() {
        return "KeyAndPasswordDTO{" +
                "key='" + key + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", passwordRetype='" + passwordRetype + '\'' +
                '}';
    }
}
