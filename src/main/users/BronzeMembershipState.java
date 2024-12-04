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
    public MembershipState getNextState() {
        return new SilverMembershipState();
    }
    
    @Override
    public void addXP(int points) {
        int totalXP = membership.getCurrentXP() + points;
        System.out.println("Added " + points + " XP. Total XP: " + totalXP + "/" + getMaxXP());
        handleXPOverflow(totalXP);
        
        if (membership.getState() instanceof BronzeMembershipState) { 
            membership.setCurrentXP(totalXP);
        }
    }
}