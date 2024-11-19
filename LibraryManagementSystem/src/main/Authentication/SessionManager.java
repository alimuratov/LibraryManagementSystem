package main.kocka;
import java.util.ArrayList;

import main.users.*;

public class SessionManager {
    private ArrayList<Session> sessions;
    private static SessionManager instance = new SessionManager();

    private SessionManager(){
        sessions = new ArrayList<Session>();
    }

    public static SessionManager getInstance(){
        return instance;
    }

    public void addSession(Session session){
        sessions.add(session);
    }

    public int getSize(){
        return sessions.size();
    }

    public void createSession(User user){
        Session session = new Session(user);
        sessions.add(session);
    }

    public boolean removeSession(User user){
        for(Session session: sessions)
            if(session.getUser().equals(user) && session.getIsValid()){
                session.setIsValid(false);
                sessions.remove(session);
                return true;
            }
        
        return false;
            
    }
}
