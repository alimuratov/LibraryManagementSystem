package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.users.MembershipType;

public class MembershipTypeTest {

    @Test
    public void testEnumValues() {
        MembershipType[] expected = {MembershipType.BRONZE, MembershipType.SILVER, MembershipType.GOLD};
        assertArrayEquals(expected, MembershipType.values());
    }

    @Test
    public void testValueOf() {
        assertEquals(MembershipType.BRONZE, MembershipType.valueOf("BRONZE"));
        assertEquals(MembershipType.SILVER, MembershipType.valueOf("SILVER"));
        assertEquals(MembershipType.GOLD, MembershipType.valueOf("GOLD"));
    }

    @Test
    public void testToString() {
        assertEquals("BRONZE", MembershipType.BRONZE.toString());
        assertEquals("SILVER", MembershipType.SILVER.toString());
        assertEquals("GOLD", MembershipType.GOLD.toString());
    }
}
