package main.users;

public abstract class MembershipState {
    protected Membership membership;
    
    public abstract int getMaxRentBooks();
    public abstract int getRentalDays();
    public abstract double getPurchaseDiscount();
    public abstract int getWaitlistPriority();
    public abstract int getMaxXP();
    public abstract String getType();
    public abstract void addXP(int points);
    public abstract MembershipState getNextState();
    
    public void setMembership(Membership membership) {
        this.membership = membership;
    }
    
    public double calculateDiscountedPrice(double originalPrice) {
        return originalPrice * (1 - getPurchaseDiscount());
    }
    
    public void handleXPOverflow(int totalXP) {
        if (totalXP >= getMaxXP()) {
            int excessXP = totalXP - getMaxXP();
            MembershipState nextState = getNextState();
            membership.setState(nextState, excessXP);
        }
    }
}