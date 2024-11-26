package test.testAuthentication;

import org.junit.Test;
import static org.junit.Assert.*;

import main.authentication.*;
import main.exceptions.ExInvalidPassword;

public class TestPassword {
    @Test
    public void testValidPassword_1(){
        Password password = new Password("AStrongPa55word");

        try{
            Password.validPassword(password);
        } catch (Exception e){
            fail("The password checker should not throw an excpetion, but it threw: " + e.getMessage());
        }
    }

    @Test
    public void testValidPassword_2(){
        Password password = new Password("short");
        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            Password.validPassword(password);
        });
        assertEquals("Password must contain at least 6 characters\n", e.getMessage());
    }
    
    @Test
    public void testValidPassword_3(){ //invalid password (No uppercase letters)
        Password password = new Password("123456137b");

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            Password.validPassword(password);
        });

        assertEquals("Password must contain at least one uppercase letter\n", e.getMessage());
    }

    @Test
    public void testValidPassword_4(){ //invalid password (no lowercase letters)
        Password password = new Password("1234567123D");

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            Password.validPassword(password);
        });
        assertEquals("Password must contain at least one lowercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_5(){ //invalid password (no digits)
        String parole = "abcdEFGHI";
        Password password = new Password(parole);

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            Password.validPassword(password);
        });

        assertEquals("Password must contain at least one digit\n", e.getMessage());
    }

    @Test
    public void testRegister_6(){ //invalid password (only digits)
        String parole = "123456789";
        Password password = new Password(parole);

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            Password.validPassword(password);
        });

        assertEquals("Password must contain at least one uppercase letter and at least one lowercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_7(){ //invalid password (only lowercase)
        String parole = "abcdefghijkl";
        Password password = new Password(parole);

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            Password.validPassword(password);
        });

        assertEquals("Password must contain at least one digit and at least one uppercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_8(){ //invalid password (only uppercase)
        String parole = "ABCDEFGHIJKL";
        Password password = new Password(parole);

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            Password.validPassword(password);
        });

        assertEquals("Password must contain at least one digit and at least one lowercase letter\n", e.getMessage());
    }

    @Test
    public void testRegister_9(){ //invalid password (completely invalid)
        String parole = "+-+-+-+-+-+-+";
        Password password = new Password(parole);

        ExInvalidPassword e = assertThrows(ExInvalidPassword.class, () -> {
            Password.validPassword(password);
        });

        assertEquals("Password must contain at least one digit, at least one uppercase letter, and at least one lowercase letter\n", e.getMessage());
    }
}
