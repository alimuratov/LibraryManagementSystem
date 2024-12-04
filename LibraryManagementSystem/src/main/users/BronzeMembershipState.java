package main.users;

public class BronzeMembershipState extends MembershipState {
    @Override
    public int getMaxRentBooks() {
        return 2;
    }
    
    @Override
    public int getRentalDays() {
        return 14;
    }
    
    @Override
    public double getPurchaseDiscount() {
        return 0.0;
    }
    
    @Override
    public int getWaitlistPriority() {
        return 3;
    }
    
    @Override
    public int getMaxXP() {
        return 100;
    }
    
    @Override
    public String getType() {
        return "BRONZE";
    }

    @Override
    public MembershipState getPreviousState() {
        return null; 
    }
    
    @Override
    public MembershipState getNextState() {
        return new SilverMembershipState();
    }
    
    @Override
    public void addXP(int points) {
        int totalXP = membership.getCurrentXP() + points;
        System.out.print("Added " + points + " XP. ");
        
        if (totalXP >= getMaxXP()) {
        	handleXPOverflow(totalXP, this);
        } else {
        	membership.setCurrentXP(totalXP);
        	System.out.println("Current XP: " + membership.getCurrentXP() + "/" + getMaxXP() + ".");
        }
    }

    @Override
    public void deductXP(int points) {
        int totalXP = membership.getCurrentXP() - points;
        System.out.print("Deducted " + points + " XP. ");
        
        if (totalXP < 0) {
        	membership.setCurrentXP(0);
        	System.out.println("Already at minimal membership level (BRONZE) with 0 XP.");
        } else {
        	membership.setCurrentXP(totalXP);
        	System.out.println("Current XP: " + membership.getCurrentXP() + "/" + getMaxXP() + ".");
        }
    }
}