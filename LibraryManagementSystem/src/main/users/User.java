package main.users;

import main.kocka.Password;

import java.util.UUID;

public class User {
    private final String userID;
    private final String userName;
    private Password password;

    // Constructors
    public User(String userName, Password password) {
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

    public Password getPassword() {
        return password;
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