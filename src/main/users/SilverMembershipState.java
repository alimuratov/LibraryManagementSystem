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
    public MembershipState getNextState() {
        return new GoldMembershipState();
    }
    
    @Override
    public void addXP(int points) {
        int totalXP = membership.getCurrentXP() + points;
        System.out.println("Added " + points + " XP. Total XP: " + totalXP + "/" + getMaxXP());
        handleXPOverflow(totalXP);
        
        if (membership.getState() instanceof SilverMembershipState) {
            membership.setCurrentXP(totalXP);
        }
    }
}