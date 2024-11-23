package testAuthentication;

import org.junit.Test;
import static org.junit.Assert.*;

import authentication.*;
import users.*;

public class TestSession {
    @Test
    public void testValid_1(){
        Password password = new Password("AStrongPa55word");
        Customer costumer = new Customer("FirstNameLastName", password);

        Session session = new Session(costumer);

        assertTrue(session.getIsValid());
    }

    @Test
    public void testValid_2(){
        Password password = new Password("AStrongPa55word");
        Customer costumer = new Customer("LastNameFirstName", password);

        Session session = new Session(costumer);
        session.setIsValid(false);
        assertFalse(session.getIsValid());
    }

    @Test
    public void testUser_1(){
        Password password = new Password("AStrongPa55word");
        Customer costumer = new Customer("NameMiddleName", password);

        Session session = new Session(costumer);
        assertEquals(costumer, session.getUser());
    }
}
