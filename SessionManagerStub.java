
public class SessionManagerStub{
    private SessionManagerStub(){

    }
    
    private static SessionManagerStub instance = new SessionManagerStub();
    
    public static SessionManagerStub getInstance(){
        return instance;
    }
    
    public void createSession(UserStub user){
        return;
    }

    public boolean removeSession(UserStub user){
        return false;
    }
}
