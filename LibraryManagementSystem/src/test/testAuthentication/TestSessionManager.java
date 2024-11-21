package test.testAuthentication;

import org.junit.Test;
import static org.junit.Assert.*;

import main.authentication.*;
import main.exceptions.ExUserDoesNotExist;
import main.users.*;


public class TestSessionManager {
    @Test
    public void testCreateSession_1(){
        SessionManager sessionManager = SessionManager.getInstance();
        Password password = new Password("AStrongPa55word");
        User user = new Customer("Kazakhstan", password);

        sessionManager.createSession(user);
        assertEquals(sessionManager.getSize(), 1);
    }

    @Test
    public void testCreateSession_2(){
        SessionManager sessionManager = SessionManager.getInstance();
        assertEquals(sessionManager.getSize(), 0);      
    }

    @Test
    public void testRemoveSession_1(){
        SessionManager sessionManager = SessionManager.getInstance();
        Password password = new Password("AStrongPa55word");
        User user = new Customer("Kazakhstan", password);

        sessionManager.createSession(user);
        try {
            sessionManager.removeSession(user);
            assertEquals(sessionManager.getSize(), 0);
        } catch (Exception e){
            fail("Remove session should not throw an exception, but it threw: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveSession_2(){
        SessionManager sessionManager = SessionManager.getInstance();
        Password password = new Password("AStrongPa55word");
        User user = new Customer("Kazakhstan", password);
        User newUser = new Customer("ThisUserDoesNotExist", password);

        sessionManager.createSession(user);

        assertThrows(ExUserDoesNotExist.class, () -> {
            sessionManager.removeSession(newUser);
        });
    }
}
