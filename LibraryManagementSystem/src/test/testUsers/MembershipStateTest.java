package test.testUsers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.users.*;

class MembershipStateTest {
    
    // BronzeMembershipState Tests
    @Test
    void testBronzeMembershipState() {
        Membership membership = new Membership();
        BronzeMembershipState state = (BronzeMembershipState) membership.getState();
        
        assertEquals(2, state.getMaxRentBooks());
        assertEquals(14, state.getRentalDays());
        assertEquals(0.0, state.getPurchaseDiscount());
        assertEquals(3, state.getWaitlistPriority());
        assertEquals(100, state.getMaxXP());
        assertEquals("BRONZE", state.getType());
        assertTrue(state.getNextState() instanceof SilverMembershipState);
        
        // Test XP addition below threshold
        membership.addXP(50);
        assertEquals(50, membership.getCurrentXP());
        
        // Test XP addition causing upgrade
        membership.addXP(60);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        
        // Test price calculation
        assertEquals(100.0, state.calculateDiscountedPrice(100.0));
    }
    
    // SilverMembershipState Tests
    @Test
    void testSilverMembershipState() {
        Membership membership = new Membership();
        membership.addXP(100); // Upgrade to Silver
        
        SilverMembershipState state = (SilverMembershipState) membership.getState();
        
        assertEquals(5, state.getMaxRentBooks());
        assertEquals(21, state.getRentalDays());
        assertEquals(0.10, state.getPurchaseDiscount());
        assertEquals(2, state.getWaitlistPriority());
        assertEquals(250, state.getMaxXP());
        assertEquals("SILVER", state.getType());
        assertTrue(state.getNextState() instanceof GoldMembershipState);
        
        // Test price calculation for Silver (10% discount)
        assertEquals(90.0, state.calculateDiscountedPrice(100.0));
        
        // Test XP addition below threshold
        membership.addXP(150);
        assertEquals(150, membership.getCurrentXP());
        assertEquals(90.0, state.calculateDiscountedPrice(100.0));
        
        // Test XP addition causing upgrade
        membership.addXP(150);
        assertTrue(membership.getState() instanceof GoldMembershipState);
        
        // Test price calculation after upgrade to Gold (20% discount)
        assertEquals(80.0, membership.getState().calculateDiscountedPrice(100.0));
    }
    
    // GoldMembershipState Tests
    @Test
    void testGoldMembershipState() {
        Membership membership = new Membership();
        membership.addXP(100); // Upgrade to Silver
        membership.addXP(250); // Upgrade to Gold
        
        GoldMembershipState state = (GoldMembershipState) membership.getState();
        
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