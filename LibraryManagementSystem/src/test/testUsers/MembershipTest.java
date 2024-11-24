package test.testUsers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.users.*;

class MembershipTest {
    @Test
    void testNewMembership() {
        Membership membership = new Membership();
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
        assertEquals("BRONZE", membership.getType());
    }
    
    @Test
    void testMembershipProgression() {
        Membership membership = new Membership();
        
        // Test Bronze to Silver progression
        membership.addXP(110);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        
        // Test Silver to Gold progression
        membership.addXP(260);
        assertTrue(membership.getState() instanceof GoldMembershipState);
    }
    
    @Test
    void testDiscountCalculation() {
        Membership membership = new Membership();
        assertEquals(100.0, membership.calculateDiscountedPrice(100.0)); // Bronze - no discount
        
        membership.setState(new SilverMembershipState(), 0);
        assertEquals(90.0, membership.calculateDiscountedPrice(100.0)); // Silver - 10% discount
        
        membership.setState(new GoldMembershipState(), 0);
        assertEquals(80.0, membership.calculateDiscountedPrice(100.0)); // Gold - 20% discount
    }
}
