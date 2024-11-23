package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.users.Membership;
import main.users.MembershipType;

public class MembershipTest {

    @Test
    public void testMembershipBronze() {
        Membership membership = new Membership(MembershipType.BRONZE);
        assertEquals(MembershipType.BRONZE, membership.getType());
        assertEquals(2, membership.getMaxRentBooks());
        assertEquals(14, membership.getRentalPeriodDays());
        assertEquals(0.0, membership.getPurchaseDiscount());
        assertEquals(1, membership.getWaitlistPriority());
    }

    @Test
    public void testMembershipSilver() {
        Membership membership = new Membership(MembershipType.SILVER);
        assertEquals(MembershipType.SILVER, membership.getType());
        assertEquals(5, membership.getMaxRentBooks());
        assertEquals(21, membership.getRentalPeriodDays());
        assertEquals(0.10, membership.getPurchaseDiscount(), 0.001);
        assertEquals(2, membership.getWaitlistPriority());
    }

    @Test
    public void testMembershipGold() {
        Membership membership = new Membership(MembershipType.GOLD);
        assertEquals(MembershipType.GOLD, membership.getType());
        assertEquals(10, membership.getMaxRentBooks());
        assertEquals(30, membership.getRentalPeriodDays());
        assertEquals(0.20, membership.getPurchaseDiscount());
        assertEquals(3, membership.getWaitlistPriority());
    }

    @Test
    public void testCalculateDiscountedPrice() {
        Membership bronze = new Membership(MembershipType.BRONZE);
        Membership silver = new Membership(MembershipType.SILVER);
        Membership gold = new Membership(MembershipType.GOLD);

        double originalPrice = 100.0;

        assertEquals(100.0, bronze.calculateDiscountedPrice(originalPrice));
        assertEquals(90.0, silver.calculateDiscountedPrice(originalPrice));
        assertEquals(80.0, gold.calculateDiscountedPrice(originalPrice));
    }
}
