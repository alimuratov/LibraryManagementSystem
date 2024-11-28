package test.testAuthentication;

import org.junit.Test;
import static org.junit.Assert.*;

import main.authentication.*;
import main.exceptions.ExIncorrectPassword;
import main.exceptions.ExInvalidPassword;
import main.exceptions.ExNonExistingUsesrname;
import main.exceptions.ExTakenUsername;
import main.exceptions.ExUserDoesNotExist;
import main.users.*;

public class TestAuthentication {
    @Test
    public void testLogin_1(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        Password password = new Password(parole);
        String userName = "LucasDoe";
        Customer customer = new Customer(userName, password);
        
        authenticationService.addUser(customer);
        
        try {
            User alreadyExistingUser = authenticationService.login(userName, parole);
            assertNotNull(alreadyExistingUser);
            assertEquals(alreadyExistingUser.getUserID(), customer.getUserID());
            assertEquals(alreadyExistingUser.getUserName(), customer.getUserName());
            assertEquals(alreadyExistingUser.getPassword(), customer.getPassword());
        } catch (Exception e) {
            fail("Login should not throw an exception, but it threw: " + e.getMessage());
        }

    }

    @Test
    public void testLogin_2(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        String userName = "TheoJoe";
        
        assertThrows(ExNonExistingUsesrname.class, () -> {
            authenticationService.login(userName, parole);
        });

    }

    @Test
    public void testLogin_3(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        String wrongParole = "987";
        Password password = new Password(parole);

        String userName = "PhoebeDoe";
        Customer customer = new Customer(userName, password);
        
        authenticationService.addUser(customer);

        assertThrows(ExIncorrectPassword.class, () -> {
            authenticationService.login(userName, wrongParole);
        });
    }

    @Test
    public void testLoginAdmin_1() {
    	AuthenticationService authenticationService = AuthenticationService.getInstance();
    	String parole = "Admin123";
    	Password password = new Password(parole);
    	
    	String userName = "admin";
    	
    	Admin admin = Admin.getInstance(), admin2 = null;
    	
    	try {
    		admin = authenticationService.loginAdmin(parole);
    		admin2 = authenticationService.getAdmin(password);
    	} catch (Exception e) {
            fail("Login should not throw an exception, but it threw: " + e.getMessage());
    	}
    	assertEquals(userName, admin.getUserName());
    	assertEquals(admin, admin2);
    }
    
    @Test
    public void testLoginAdmin_2() {
    	AuthenticationService authenticationService = AuthenticationService.getInstance();
    	String parole = "Admin124";
    	Password password = new Password(parole);
    	
    	String userName = "admin";
    	
    	Admin admin = Admin.getInstance();
    	
    	assertThrows(ExIncorrectPassword.class, ()-> {
    		authenticationService.loginAdmin(parole);
    	});
    }
    
    @Test
    public void testRegister_1(){ // valid password
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        String userName = "TonyDoe";

        Password password = new Password(parole);

        Customer customer;
        try {
        	customer = authenticationService.registerCustomer(userName, parole);
            assertNotNull(customer);
            assertEquals(userName, customer.getUserName());
            assertTrue(customer.getPassword().equals(password));
        } catch (Exception e) {
            fail("Register should not throw an exception, but it threw " + e.getMessage());
        }
    }

    @Test
    public void testRegister_2(){ //invalid password (too short)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1Ab";
        String userName = "IbrahimDoe";


        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });

        assertEquals("Password must contain at least 6 characters\n", e.getMessage());
    }

    @Test
    public void testRegister_3(){ //invalid password (No uppercase letters)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "123456137b";
        String userName = "NicholasDoe";

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });

        assertEquals("Password must contain at least one uppercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_4(){ //invalid password (no lowercase letters)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234567123A";
        String userName = "StevenDoe";

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });

        assertEquals("Password must contain at least one lowercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_5(){ //invalid password (no digits)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "abcdEFGHI";
        String userName = "PhilipDoe";

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });

        assertEquals("Password must contain at least one digit\n", e.getMessage());
    }

    @Test
    public void testRegister_6(){ //invalid password (only digits)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "123456789";
        String userName = "BradDoe";

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });

        assertEquals("Password must contain at least one uppercase letter and at least one lowercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_7(){ //invalid password (only lowercase)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "abcdefghijkl";
        String userName = "JohnMichaelDoe";

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });

        assertEquals("Password must contain at least one digit and at least one uppercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_8(){ //invalid password (only uppercase)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "ABCDEFGHIJKL";
        String userName = "AlexDoe";

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });

        assertEquals("Password must contain at least one digit and at least one lowercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_9(){ //invalid password (completely invalid)
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "+-+-+-+-+-+-+";
        String userName = "JDoe";

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });

        assertEquals("Password must contain at least one digit, at least one uppercase letter, and at least one lowercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_10(){ //taken username
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        Password password = new Password(parole);
        String userName = "SebastianDoe";
        Customer customer = new Customer(userName, password);
        authenticationService.addUser(customer);

        assertThrows(ExTakenUsername.class, () -> {
            authenticationService.registerCustomer(userName, parole);
        });
    }

    @Test
    public void testLogout_1(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        Password password = new Password(parole);
        String userName = "Abc";
        User customer = new Customer(userName, password);
        authenticationService.addUser(customer);
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.createSession(customer);
        try{
            authenticationService.logout(customer);
        } catch (Exception e) {
            fail("Logout should not throw an exception, but it threw: " + e.getMessage());
        }
    }

    @Test
    public void testLogout_2(){
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        String parole = "1234ABCDefg";
        Password password = new Password(parole);
        String userName = "Abc";
        Customer customer = new Customer(userName, password);
        authenticationService.addUser(customer);
        
        assertThrows(ExUserDoesNotExist.class, () ->{
            authenticationService.logout(customer);
        });
    }
}
