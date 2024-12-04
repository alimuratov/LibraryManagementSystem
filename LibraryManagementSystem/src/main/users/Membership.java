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
    	String previousStateType = state.getType();
        this.state = newState;
        this.state.setMembership(this);
        this.currentXP = startingXP;
        
        MembershipState prevState = newState.getPreviousState();
        MembershipState nextState = newState.getNextState();
        
        // Upgrade from Bronze to Silver or Silver to Gold
        if (prevState != null && prevState.getType().equals(previousStateType)) {
            System.out.println("Membership upgraded to " + state.getType() + ". Starting with XP: " + currentXP + "/" + state.getMaxXP() + ".");	
        } 
        
        // Downgrade from Gold to Silver or Silver to Bronze
        else if (nextState != null && nextState.getType().equals(previousStateType)) {
            System.out.println("Membership downgraded to " + state.getType() + ". Starting with XP: " + currentXP + "/" + state.getMaxXP() + ".");
        }

        // Upgrade from Bronze to Gold and Downgrade from Gold to Bronze instantly
        else if (nextState == null) {
            System.out.println("Membership upgraded to " + state.getType() + ". Current XP: " + currentXP + "/" + state.getMaxXP() + ".");
        } else {
        	System.out.println("Membership downgraded to " + state.getType() + ". Current XP: " + currentXP + "/" + state.getMaxXP() + ".");
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

    public void deductXP(int points) {
        state.deductXP(points);
    }
    
    public int getCurrentXP() {
        return currentXP;
    }
    
    protected void setCurrentXP(int xp) { // protected
        this.currentXP = xp;
    }
}