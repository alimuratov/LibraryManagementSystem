package main.users;

public class GoldMembership implements MembershipState {
    @Override
    public void addXP(Membership membership, int points) {
        System.out.println("Already at maximum membership level (GOLD). XP cannot be increased further.");
    }

    @Override
    public void displayMembershipInfo(Membership membership) {
        System.out.println("Membership Type: GOLD");
        System.out.println("Max Rentable Books: 10");
        System.out.println("Rental Days: 30");
        System.out.println("Purchase Discount: 20.0%");
        System.out.println("Waitlist Priority: 1");
        System.out.println("Current XP: " + membership.getCurrentXP() + "/∞");
    }
}