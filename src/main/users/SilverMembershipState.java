package main.users;

public class SilverMembershipState implements MembershipState {

    @Override
    public int getMaxRentBooks() {
        return 5;
    }

    @Override
    public int getRentalDays() {
        return 21;
    }

    @Override
    public double getPurchaseDiscount() {
        return 0.10;
    }

    @Override
    public int getWaitlistPriority() {
        return 2;
    }

    @Override
    public int getMaxXP() {
        return 250;
    }

    @Override
    public String getType() {
        return "SILVER";
    }

    @Override
    public MembershipState getPreviousState() {
        return new BronzeMembershipState();
    }

    @Override
    public MembershipState getNextState() {
        return new GoldMembershipState();
    }

    @Override
    public void addXP(Membership membership, int points) {
        int totalXP = membership.getCurrentXP() + points;
        System.out.print("Added " + points + " XP. ");

        if (totalXP >= getMaxXP()) {
            membership.setState(getNextState(), 250);
        } else {
            membership.setCurrentXP(totalXP);
            System.out.println("Current XP: " + membership.getCurrentXP() + "/" + getMaxXP() + ".");
        }
    }

    @Override
    public void deductXP(Membership membership, int points) {
        int totalXP = membership.getCurrentXP() - points;
        System.out.print("Deducted " + points + " XP. ");

        if (totalXP < 0) {
            int adjustedXP = getPreviousState().getMaxXP() + totalXP;
            membership.setState(getPreviousState(), adjustedXP);
        } else {
            membership.setCurrentXP(totalXP);
            System.out.println("Current XP: " + membership.getCurrentXP() + "/" + getMaxXP() + ".");
        }
    }
}
