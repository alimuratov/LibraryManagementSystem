package test.testSystem;

import org.junit.Test;
import static org.junit.Assert.*;

import main.system.*;
import main.users.*;

public class AuthenticationTest {
    @Test
    public void testLogin_1(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        Password password = new Password(parole);
        String userName = "JohnDoe";
        User user = new User(userName, password);
        
        authenticationService.addUser(user);

        User alreadyExistingUser = authenticationService.login(userName, parole);
        assertNotNull(alreadyExistingUser);

        assertEquals(alreadyExistingUser.getUserID(), user.getUserID());
        assertEquals(alreadyExistingUser.getUserName(), user.getUserName());
        assertEquals(alreadyExistingUser.getPassword(), user.getPassword());

    }

    @Test
    public void testLogin_2(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        String userName = "JohnDoe";

        User nonExistingUser = authenticationService.login(userName, parole);

        assertNull(nonExistingUser);
    }

    @Test
    public void testLogin_3(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        String wrongParole = "987";
        Password password = new Password(parole);

        String userName = "JohnDoe";
        User user = new User(userName, password);
        
        authenticationService.addUser(user);

        User wrongPasswordUser = authenticationService.login(userName, wrongParole);
        assertNull(wrongPasswordUser);
    }

    @Test
    public void testRegister_1(){ // valid password
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        String userName = "JohnDoe";

        Password password = new Password(parole);

        User user = authenticationService.register(userName, parole);

        assertNotNull(user);
        assertEquals(userName, user.getUserName());
        assertTrue(user.getPassword().equals(password));
    }

    @Test
    public void testRegister_2(){ //invalid password (too short)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1Ab";
        String userName = "JohnDoe";

        User user = authenticationService.register(userName, parole);

        assertNull(user);
    }

    @Test
    public void testRegister_3(){ //invalid password (No uppercase letters)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "123456137b";
        String userName = "JohnDoe";

        User user = authenticationService.register(userName, parole);

        assertNull(user);
    }

    @Test
    public void testRegister_4(){ //invalid password (no lowercase letters)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234567123a";
        String userName = "JohnDoe";

        User user = authenticationService.register(userName, parole);

        assertNull(user);
    }

    @Test
    public void testLogout_1(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        User user = new User("Abc", "1234ABCDefg");
        boolean testBool = authenticationService.logout(user);

        assertFalse(testBool);
    }

    @Test
    public void testLogout_2(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        User user = new User("Abc", "1234ABCDefg");
        authenticationService.addUser(user);
        boolean testBool = authenticationService.logout(user);

        assertTrue(testBool);
    }
}