package main.kocka;
import main.users.User;

public class SessionManagerStub{
    private SessionManagerStub(){

    }
    
    private static SessionManagerStub instance = new SessionManagerStub();
    
    public static SessionManagerStub getInstance(){
        return instance;
    }
    
    public void createSession(User user){
        return;
    }

    public boolean removeSession(User user){
        return false;
    }
}
