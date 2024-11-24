package test.testUsers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.users.*;

class MembershipStateTest {
    
    // BronzeMembershipState Tests
    @Test
    void testBronzeMembershipState() {
        BronzeMembershipState state = new BronzeMembershipState();
        Membership membership = new Membership();
        state.setMembership(membership);
        
        assertEquals(2, state.getMaxRentBooks());
        assertEquals(14, state.getRentalDays());
        assertEquals(0.0, state.getPurchaseDiscount());
        assertEquals(3, state.getWaitlistPriority());
        assertEquals(100, state.getMaxXP());
        assertEquals("BRONZE", state.getType());
        assertTrue(state.getNextState() instanceof SilverMembershipState);
        
        // Test XP addition below threshold
        state.addXP(50);
        assertEquals(50, membership.getCurrentXP());
        
        // Test XP addition causing upgrade
        state.addXP(60);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        
     // Test price calculation
        assertEquals(100.0, state.calculateDiscountedPrice(100.0));
    }
    
    // SilverMembershipState Tests
    @Test
    void testSilverMembershipState() {
        SilverMembershipState state = new SilverMembershipState();
        Membership membership = new Membership();
        state.setMembership(membership);
        
        assertEquals(5, state.getMaxRentBooks());
        assertEquals(21, state.getRentalDays());
        assertEquals(0.10, state.getPurchaseDiscount());
        assertEquals(2, state.getWaitlistPriority());
        assertEquals(250, state.getMaxXP());
        assertEquals("SILVER", state.getType());
        assertTrue(state.getNextState() instanceof GoldMembershipState);
        
        // Test XP addition below threshold
        state.addXP(150);
        assertEquals(0, membership.getCurrentXP());
        
        // Test XP addition causing upgrade
        state.addXP(150);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        
        // Test price calculation
        assertEquals(80.0, state.calculateDiscountedPrice(100.0));
    }
    
    // GoldMembershipState Tests
    @Test
    void testGoldMembershipState() {
        GoldMembershipState state = new GoldMembershipState();
        
        assertEquals(10, state.getMaxRentBooks());
        assertEquals(30, state.getRentalDays());
        assertEquals(0.20, state.getPurchaseDiscount());
        assertEquals(1, state.getWaitlistPriority());
        assertEquals(250, state.getMaxXP());
        assertEquals("GOLD", state.getType());
        assertNull(state.getNextState());
        
        // Test XP addition at max level
        state.addXP(50); // Should just print message

        
        // Test price calculation
        assertEquals(80.0, state.calculateDiscountedPrice(100.0));
    }
}
