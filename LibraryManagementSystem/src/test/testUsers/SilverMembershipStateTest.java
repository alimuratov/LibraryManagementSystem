package test.testUsers;

import main.users.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SilverMembershipStateTest {
    private SilverMembershipState state;
    private Membership membership;

    @Before
    public void setUp() {
        state = new SilverMembershipState();
        membership = new Membership();
        membership.setState(state, 0);
    }

    @Test
    public void testGetters() {
        assertEquals(5, state.getMaxRentBooks());
        assertEquals(21, state.getRentalDays());
        assertEquals(0.1, state.getPurchaseDiscount(), 0.001);
        assertEquals(2, state.getWaitlistPriority());
        assertEquals(250, state.getMaxXP());
        assertEquals("SILVER", state.getType());
        assertTrue(state.getPreviousState() instanceof BronzeMembershipState);
        assertTrue(state.getNextState() instanceof GoldMembershipState);
    }

    @Test
    public void testAddXPWithinLimit() {
        for (int purchase = 0; purchase < 10; purchase++) {
            state.addXP(membership, 20);
        }
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(200, membership.getCurrentXP());
    }

    @Test
    public void testAddXPExceedingLimit() {
        for (int purchase = 0; purchase < 13; purchase++) {
            state.addXP(membership, 20);
        }
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
    }

    @Test
    public void testDeductXPWithinLimit() {
        state.addXP(membership, 20);
        state.deductXP(membership, 10);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(10, membership.getCurrentXP());
    }

    @Test
    public void testDeductXPBelowZero() {
        state.deductXP(membership, 10);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(90, membership.getCurrentXP());
    }
}