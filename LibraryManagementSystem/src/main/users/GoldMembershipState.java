package main.users;

public class GoldMembershipState extends MembershipState {
    @Override
    public int getMaxRentBooks() {
        return 10;
    }
    
    @Override
    public int getRentalDays() {
        return 30;
    }
    
    @Override
    public double getPurchaseDiscount() {
        return 0.20;
    }
    
    @Override
    public int getWaitlistPriority() {
        return 1;
    }
    
    @Override
    public int getMaxXP() {
        return 250;
    }
    
    @Override
    public String getType() {
        return "GOLD";
    }

    @Override
    public MembershipState getPreviousState() {
        return new SilverMembershipState();
    }
    
    @Override
    public MembershipState getNextState() {
        return null;
    }
    
    @Override
    public void addXP(int points) {
        System.out.println("Already at maximum membership level (GOLD).");
    }

    @Override
    public void deductXP(int points) {
        handleXPUnderflow(-points, this);
    }
}