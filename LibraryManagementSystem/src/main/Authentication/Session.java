package main.kocka;
import main.users.*;
import java.util.UUID;

public class Session {
    User user;
    UUID sessionID;
    boolean isValid;
    
    public Session(User user){
        this.user = user;
        this.isValid = true;
        this.sessionID = UUID.randomUUID();
    }

    public boolean getIsValid(){
        return this.isValid;
    }

    public void setIsValid(boolean isValid){
        this.isValid = isValid;
    }

    public User getUser(){
        return user;
    }
}
