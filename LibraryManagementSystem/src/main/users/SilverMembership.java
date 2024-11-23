package main.users;

public class SilverMembership implements MembershipState {
    @Override
    public void addXP(Membership membership, int points) {
        membership.setCurrentXP(membership.getCurrentXP() + points);
        System.out.println("Added " + points + " XP. Current XP: " + membership.getCurrentXP() + "/250");
        
        if (membership.getCurrentXP() >= 250) {
            membership.setCurrentState(new GoldMembership());
            membership.setCurrentXP(0); // Reset XP after upgrade
            membership.setMaxXP(Integer.MAX_VALUE); // Gold has no max XP limit
            System.out.println("Membership upgraded to GOLD.");
        }
    }

    @Override
    public void displayMembershipInfo(Membership membership) {
        System.out.println("Membership Type: SILVER");
        System.out.println("Max Rentable Books: 5");
        System.out.println("Rental Days: 21");
        System.out.println("Purchase Discount: 10.0%");
        System.out.println("Waitlist Priority: 2");
        System.out.println("Current XP: " + membership.getCurrentXP() + "/250");
    }
}