package main.users;

import java.util.UUID;

public abstract class User {
    private final String userID;
    private final String userName;
    private String password;

    // Constructors
    public User(String userName, String password) {
        this.userID =  UUID.randomUUID().toString();
        this.userName = userName;
        this.password = password;
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    // Password Management
    private void setPassword(String newPassword) {
        this.password = newPassword;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }
        return false;
    }

    /**
     * Changes the user's password if the current password matches and the new password is valid.
     *
     * @param currentPassword The user's current password.
     * @param newPassword     The new password to set.
     * @return true if the password was successfully changed, false otherwise.
     */
    public boolean changePassword(String currentPassword, String newPassword) {
        if (!this.password.equals(currentPassword)) {
            System.out.println("Current password is incorrect.");
            return false;
        }

        if (!isValidPassword(newPassword)) {
            System.out.println("New password does not meet the required criteria:");
            System.out.println("Password must be at least 8 characters long and contain both letters and numbers.");
            return false;
        }

        setPassword(newPassword);
        System.out.println("Password changed successfully.");
        return true;
    }

    /**
     * Verifies if the provided password matches the stored password.
     *
     * @param password The password to verify.
     * @return true if the password matches, false otherwise.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // Overriding equals and hashCode based on userID
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return userID.equals(other.userID);
    }

    @Override
    public int hashCode() {
        return userID.hashCode();
    }

    // toString method
    @Override
    public String toString() {
        return "User [userID=" + userID + ", userName=" + userName + "]";
    }
}