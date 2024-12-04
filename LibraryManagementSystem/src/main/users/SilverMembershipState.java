package main.users;

public class SilverMembershipState extends MembershipState {
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
    public void addXP(int points) {
        int totalXP = membership.getCurrentXP() + points;
        System.out.println("Added " + points + " XP. ");
        
        if (totalXP >= getMaxXP()) {
        	handleXPOverflow(totalXP, this);
        } else {
        	membership.setCurrentXP(totalXP);
        	System.out.print("Current XP: " + membership.getCurrentXP() + "/" + getMaxXP() + ".");
        }
    }

    @Override
    public void deductXP(int points) {
        int totalXP = membership.getCurrentXP() - points;
        System.out.print("Deducted " + points + " XP. ");
        
        if (totalXP < 0) {
        	handleXPUnderflow(totalXP, this);
        } else {
        	membership.setCurrentXP(totalXP);
        	System.out.println("Current XP: " + membership.getCurrentXP() + "/" + getMaxXP() + ".");
        }
    }
}