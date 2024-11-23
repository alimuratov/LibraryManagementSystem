package main.users;

public class BronzeMembership implements MembershipState {
    @Override
    public void addXP(Membership membership, int points) {
        membership.setCurrentXP(membership.getCurrentXP() + points);
        System.out.println("Added " + points + " XP. Current XP: " + membership.getCurrentXP() + "/250");
        
        if (membership.getCurrentXP() >= 100) {
            membership.setCurrentState(new SilverMembership());
            membership.setCurrentXP(0); // Reset XP after upgrade
            membership.setMaxXP(250); // Set max XP for Silver
            System.out.println("Membership upgraded to SILVER.");
        }
    }

    @Override
    public void displayMembershipInfo(Membership membership) {
        System.out.println("Membership Type: BRONZE");
        System.out.println("Max Rentable Books: 2");
        System.out.println("Rental Days: 14");
        System.out.println("Purchase Discount: 0.0%");
        System.out.println("Waitlist Priority: 3");
        System.out.println("Current XP: " + membership.getCurrentXP() + "/250");
    }
}