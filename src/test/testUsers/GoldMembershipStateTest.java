package test.testUsers;

import main.users.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GoldMembershipStateTest {
    private GoldMembershipState state;
    private Membership membership;

    @Before
    public void setUp() {
        state = new GoldMembershipState();
        membership = new Membership();
        membership.setState(state, 250);
    }

    @Test
    public void testGetters() {
        assertEquals(10, state.getMaxRentBooks());
        assertEquals(30, state.getRentalDays());
        assertEquals(0.2, state.getPurchaseDiscount(), 0.001);
        assertEquals(1, state.getWaitlistPriority());
        assertEquals(250, state.getMaxXP());
        assertEquals("GOLD", state.getType());
        assertTrue(state.getPreviousState() instanceof SilverMembershipState);
        assertNull(state.getNextState());
    }

    @Test
    public void testAddXPExceedingLimit() {
        for (int purchase = 0; purchase < 5; purchase++) {
            state.addXP(membership, 20);
        }
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
    }

    @Test
    public void testDeductXPToSilver() {
        state.deductXP(membership, 20);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(230, membership.getCurrentXP());
    }
}