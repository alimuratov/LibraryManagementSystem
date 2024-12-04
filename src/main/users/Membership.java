package main.users;

public class Membership {
    private MembershipState state;
    private int currentXP;
    
    // Constructor
    public Membership() {
        this.state = new BronzeMembershipState();
        this.state.setMembership(this);
        this.currentXP = 0;
    }
    
    // State management
    public void setState(MembershipState newState, int startingXP) {
        this.state = newState;
        this.state.setMembership(this);
        this.currentXP = startingXP;
        
        // Only print XP message if not Gold state
        if (!(newState instanceof GoldMembershipState)) {
            System.out.println("Membership upgraded to " + state.getType() + ". Starting with XP: " + currentXP + "/" + state.getMaxXP());
        } else {
            System.out.println("Membership upgraded to " + state.getType() + ".");
        }
    }
    
    public MembershipState getState() {
        return state;
    }
    
    // Getters that delegate to state
    public String getType() {
        return state.getType();
    }
    
    public int getMaxRentBooks() {
        return state.getMaxRentBooks();
    }
    
    public int getRentalDays() {
        return state.getRentalDays();
    }
    
    public int getWaitlistPriority() {
        return state.getWaitlistPriority();
    }
    
    public double calculateDiscountedPrice(double originalPrice) {
        return state.calculateDiscountedPrice(originalPrice);
    }
    
    // XP management
    public void addXP(int points) {
        state.addXP(points);
    }
    
    public int getCurrentXP() {
        return currentXP;
    }
    
    protected void setCurrentXP(int xp) { // protected
        this.currentXP = xp;
    }
}