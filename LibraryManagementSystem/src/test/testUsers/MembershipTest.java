package test.testUsers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.users.BronzeMembershipState;
import main.users.GoldMembershipState;
import main.users.Membership;
import main.users.SilverMembershipState;

class MembershipTest {
    private Membership membership;
    
    @BeforeEach
    void setUp() {
        membership = new Membership();
    }
    
    // Bronze Membership Tests
    @Test
    void testInitialState_Bronze() {
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
        assertEquals(2, membership.getMaxRentBooks());
        assertEquals(14, membership.getRentalDays());
        assertEquals(0, membership.getState().getPurchaseDiscount());
        assertEquals(3, membership.getWaitlistPriority());
        assertEquals(100, membership.getState().getMaxXP());
        assertEquals("BRONZE", membership.getType());
        assertTrue(membership.getState().getNextState() instanceof SilverMembershipState);
        assertEquals(null, membership.getState().getPreviousState());
    }
    
    @Test
    void testAddXPWithinLimit_Bronze() {
        membership.addXP(50);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(50, membership.getCurrentXP());
    }
    
    @Test
    void testAddXPCausingTransition_BronzeToSilver() {
        membership.addXP(100);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    @Test
    void testAddXPCausingTransition_BronzeToSilver_WithExcess() {
        membership.addXP(150);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(50, membership.getCurrentXP());
    }
    
    @Test
    void testAddXPMultipleTransitions_BronzeToSilverToGold() {
        membership.addXP(350);
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
    }
    
    @Test
    void testAddXPMultipleTransitions_BronzeToSilverToGold_WithExcess() {
        membership.addXP(400);
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
    }
    
    @Test
    void testAddXPExactLimit_BronzeToSilver() {
        membership.addXP(100);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPWithinLimit_Bronze() {
        membership.addXP(50);
        membership.deductXP(20);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(30, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPToZero_Bronze() {
        membership.addXP(50);
        membership.deductXP(50);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPBelowZero_Bronze() {
        membership.addXP(50);
        membership.deductXP(60);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    // Silver Membership Tests
    @Test
    void testInitialState_Silver() {
        membership.addXP(150);
        SilverMembershipState silverState = (SilverMembershipState) membership.getState();
        assertEquals(5, silverState.getMaxRentBooks());
        assertEquals(21, silverState.getRentalDays());
        assertEquals(0.10, silverState.getPurchaseDiscount());
        assertEquals(2, silverState.getWaitlistPriority());
        assertEquals(250, silverState.getMaxXP());
        assertEquals("SILVER", silverState.getType());
        assertTrue(silverState.getNextState() instanceof GoldMembershipState);
        assertTrue(silverState.getPreviousState() instanceof BronzeMembershipState);
    }
    
    @Test
    void testAddXPWithinLimit_Silver() {
        membership.addXP(150);
        membership.addXP(50);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(100, membership.getCurrentXP());
    }
    
    @Test
    void testAddXPExactLimit_Silver() {
        membership.addXP(150);
        membership.addXP(200);
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
    }
    
    @Test
    void testAddXPExceedingLimit_Silver() {
        membership.addXP(150);
        membership.addXP(400);
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPWithinLimit_Silver() {
        membership.addXP(150);
        membership.deductXP(20);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(30, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPToZero_Silver() {
        membership.addXP(150);
        membership.deductXP(50);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPBelowSilver() {
        membership.addXP(150);
        membership.deductXP(130);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(20, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPBelowBronze() {
        membership.addXP(150);
        membership.deductXP(200);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    // Gold Membership Tests
    @Test
    void testInitialState_Gold() {
        membership.addXP(350);
        GoldMembershipState goldState = (GoldMembershipState) membership.getState();
        assertEquals(10, goldState.getMaxRentBooks());
        assertEquals(30, goldState.getRentalDays());
        assertEquals(0.20, goldState.getPurchaseDiscount());
        assertEquals(1, goldState.getWaitlistPriority());
        assertEquals(250, goldState.getMaxXP());
        assertEquals("GOLD", goldState.getType());
        assertNull(goldState.getNextState());
        assertTrue(goldState.getPreviousState() instanceof SilverMembershipState);
    }
    
    @Test
    void testAddXPExceedingMax_Gold() {
        membership.addXP(350);
        membership.addXP(500);
        assertTrue(membership.getState() instanceof GoldMembershipState);
        assertEquals(250, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPExactSilver_Gold() {
        membership.addXP(350);
        membership.deductXP(250);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPWithinSilver_Gold() {
        membership.addXP(350);
        membership.deductXP(50);
        assertTrue(membership.getState() instanceof SilverMembershipState);
        assertEquals(200, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPBelowSilver_Gold() {
        membership.addXP(350);
        membership.deductXP(300);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(50, membership.getCurrentXP());
        
        membership.deductXP(250);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPExactBronze_Gold() {
        membership.addXP(350);
        membership.deductXP(350);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    @Test
    void testDeductXPBelowBronze_Gold() {
        membership.addXP(350);
        membership.deductXP(400);
        assertTrue(membership.getState() instanceof BronzeMembershipState);
        assertEquals(0, membership.getCurrentXP());
    }
    
    // Discount Calculation Tests
    @Test
    void testCalculateDiscount_Bronze() {
        double originalPrice = 200.0;
        double discountedPrice = membership.calculateDiscountedPrice(originalPrice);
        assertEquals(200.0, discountedPrice, 0.001);
    }
    
    @Test
    void testCalculateDiscount_Silver() {
        membership.addXP(150);
        double originalPrice = 100.0;
        double discountedPrice = membership.calculateDiscountedPrice(originalPrice);
        assertEquals(90.0, discountedPrice, 0.001);
    }
    
    @Test
    void testCalculateDiscount_Gold() {
        membership.addXP(350);
        double originalPrice = 100.0;
        double discountedPrice = membership.calculateDiscountedPrice(originalPrice);
        assertEquals(80.0, discountedPrice, 0.001);
    }
}
