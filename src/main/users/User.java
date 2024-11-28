package main.users;

import java.util.UUID;

import main.authentication.Password;

public abstract class User {
    private final String userID;
    private final String userName;
    private Password password;

    // Constructors
    public User(String userName, Password password) {
        this.userID =  UUID.randomUUID().toString();
        this.userName = userName;
        this.password = password;
    }
    
    public User(String userName, Integer id) {
        this.userName = userName;
        this.userID = String.valueOf(id);
        this.password = new Password("defaultPass"); 
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
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof User)) return false;
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