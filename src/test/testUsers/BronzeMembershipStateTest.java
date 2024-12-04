package test.testUsers;

import main.users.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BronzeMembershipStateTest {
    private BronzeMembershipState state;
    private Membership membership;

    @Before
    public void setUp() {
        state = new BronzeMembershipState();
        membership = new Membership();
    }

    @Test
    public void testGetters() {
        assertEquals(2, state.getMaxRentBooks());
        assertEquals(14, state.getRentalDays());
        assertEquals(0.0, state.getPurchaseDiscount(), 0.001);
        assertEquals(3, state.getWaitlistPriority());
        assertEquals(100, state.getMaxXP());
        assertEquals("BRONZE", state.getType());
        assertNull(state.getPreviousState());
        assertTrue(state.getNextState() instanceof SilverMembershipState);
    }

    @Test
    public void testAddXPWithinLimit() {
    	for (int purchase = 0; purchase < 2; purchase++) {
    		state.addXP(membership, 20);
    	}
    	assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(40, membership.getCurrentXP());
    }

    @Test
    public void testAddXPExceedingLimit() {
    	for (int purchase = 0; purchase < 6; purchase++) {
    		state.addXP(membership, 20);
    	}
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(20, membership.getCurrentXP());
    }

    @Test
    public void testDeductXPWithinLimit() {
        membership.addXP(20);
        state.deductXP(membership, 10);
        assertEquals(10, membership.getCurrentXP());
    }

    @Test
    public void testDeductXPBelowZero() {
    	state.addXP(membership, 10);
    	state.deductXP(membership, 20);
        assertEquals(0, membership.getCurrentXP());
    }
}
