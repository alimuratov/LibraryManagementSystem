package test.testUsers;

import main.users.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MembershipTest {
    private Membership membership;

    @Before
    public void setUp() {
        membership = new Membership();
    }

    @Test
    public void testInitialState() {
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
        assertEquals("BRONZE", membership.getType());
        assertEquals(2, membership.getMaxRentBooks());
        assertEquals(14, membership.getRentalDays());
        assertEquals(3, membership.getWaitlistPriority());
        assertEquals(100.0, membership.calculateDiscountedPrice(100.0), 0.001);
    }

    @Test
    public void testStateTransitionsProgression() {
        for (int purchase = 0; purchase < 6; purchase++) {
            membership.addXP(20);
        }
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(20, membership.getCurrentXP());
        assertEquals(5, membership.getMaxRentBooks());
        assertEquals(21, membership.getRentalDays());
        assertEquals(2, membership.getWaitlistPriority());
        assertEquals(90.0, membership.calculateDiscountedPrice(100.0), 0.001);

        
        for (int purchase = 0; purchase < 12; purchase++) {
            membership.addXP(20);
        }
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
        assertEquals(10, membership.getMaxRentBooks());
        assertEquals(30, membership.getRentalDays());
        assertEquals(1, membership.getWaitlistPriority());
        assertEquals(80.0, membership.calculateDiscountedPrice(100.0), 0.001);
        
        membership.addXP(20);
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
    }

    @Test
    public void testStateTransitionsDowngrade() {
        membership.setState(new GoldMembershipState(), 250);
        membership.deductXP(20);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(230, membership.getCurrentXP());
        
        
        for (int refund = 0; refund < 12; refund++) {
            membership.deductXP(20);
        }
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(90, membership.getCurrentXP());
        
        
        for (int refund = 0; refund < 5; refund++) {
            membership.deductXP(20);
        }
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
}