package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.authentication.Password;
import main.users.User;

public class UserTest {
    private static class TestUser extends User {
        public TestUser(String userID, String userName, Password password) {
            super(userName, password);
            
	        try {
	            java.lang.reflect.Field userIDField = User.class.getDeclaredField("userID");
	            userIDField.setAccessible(true);
	            userIDField.set(this, userID);
	        } catch (NoSuchFieldException | IllegalAccessException e) {
	            throw new RuntimeException("Failed to set userID via reflection", e);
	        }
        }
    }

    private Password password;
    private TestUser user1;
    private TestUser user2;
    private TestUser user3;

    @BeforeEach
    public void setUp() {
        password = new Password("securePassword123");
        
        // Initialize TestUser instances with controlled userIDs
        user1 = new TestUser("user-1", "Alice", password);
        user2 = new TestUser("user-1", "Alice", password); // Same userID as user1
        user3 = new TestUser("user-2", "Bob", password);   // Different userID
    }

    @Test
    public void testConstructorAndGetters() {
        assertNotNull(user1.getUserID());
        assertEquals("Alice", user1.getUserName());
        assertEquals(password, user1.getPassword());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(user1.equals(user1));
    }

    @Test
    public void testEquals_NullObject() {
        assertFalse(user1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(user1.equals("Some String"));
    }

    @Test
    public void testEquals_DifferentUserID() {
        assertFalse(user1.equals(user3));
    }

    @Test
    public void testEquals_SameUserID() {
        assertTrue(user1.equals(user2));
    }

    // Test hashCode consistency with equals
    @Test
    public void testHashCode() {
        assertEquals(user1.hashCode(), user1.hashCode());

        assertEquals(user1.hashCode(), user2.hashCode());

        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "User [userID=user-1, userName=Alice]";
        assertEquals(expected, user1.toString());
    }
}
