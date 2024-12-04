package test.testUsers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.users.*;

class MembershipTest {
    
    @Test
    void testInitialState() {
        Membership membership = new Membership();
        assertEquals("BRONZE", membership.getType());
        assertEquals(0, membership.getCurrentXP());
    }
    
    @Test
    void testStateTransitions() {
        Membership membership = new Membership();
        
        // Test Bronze state getters
        assertEquals(2, membership.getMaxRentBooks());
        assertEquals(14, membership.getRentalDays());
        assertEquals(3, membership.getWaitlistPriority());
        
        // Upgrade to Silver
        membership.addXP(100);
        assertEquals("SILVER", membership.getType());
        
        // Test Silver state getters
        assertEquals(5, membership.getMaxRentBooks());
        assertEquals(21, membership.getRentalDays());
        assertEquals(2, membership.getWaitlistPriority());
        
        // Upgrade to Gold
        membership.addXP(250);
        assertEquals("GOLD", membership.getType());
        
        // Test Gold state getters
        assertEquals(10, membership.getMaxRentBooks());
        assertEquals(30, membership.getRentalDays());
        assertEquals(1, membership.getWaitlistPriority());
    }
    
    @Test
    void testXPManagement() {
        Membership membership = new Membership();
        
        // Test adding XP below threshold
        membership.addXP(50);
        assertEquals(50, membership.getCurrentXP());
        
        // Test protected setter through reflection
        try {
            java.lang.reflect.Method setCurrentXP = 
                Membership.class.getDeclaredMethod("setCurrentXP", int.class);
            setCurrentXP.setAccessible(true);
            setCurrentXP.invoke(membership, 75);
            assertEquals(75, membership.getCurrentXP());
        } catch (Exception e) {
            fail("Failed to test protected setCurrentXP method");
        }
        
        // Test XP overflow and state transition
        membership.addXP(50);
        assertEquals("SILVER", membership.getType());
    }
    
    @Test
    void testDiscountCalculations() {
        Membership membership = new Membership();
        double originalPrice = 100.0;
        
        // Test Bronze discount (0%)
        assertEquals(100.0, membership.calculateDiscountedPrice(originalPrice));
        
        // Test Silver discount (10%)
        membership.addXP(100);
        assertEquals(90.0, membership.calculateDiscountedPrice(originalPrice));
        
        // Test Gold discount (20%)
        membership.addXP(250);
        assertEquals(80.0, membership.calculateDiscountedPrice(originalPrice));
    }
}