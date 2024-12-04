package main.users;

public class Membership {
    private MembershipState state;
    private int currentXP;

    // Constructor
    public Membership() {
        this.state = new BronzeMembershipState();
        this.currentXP = 0;
    }

    // State management
    public void setState(MembershipState setState, int startingXP) {
    	MembershipState prevState = state;
        this.state = setState;
        this.currentXP = startingXP;

        MembershipState nextState = prevState.getNextState();
        if (nextState != null && setState.getType().equals(prevState.getNextState().getType())) {
            System.out.println("Membership upgraded to " + state.getType() + ". Starting with XP: " + currentXP + "/" + state.getMaxXP() + ".");
        } else {
            System.out.println("Membership downgraded to " + state.getType() + ". Starting with XP: " + currentXP + "/" + state.getMaxXP() + ".");
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
        return originalPrice * (1 - state.getPurchaseDiscount());
    }

    // XP management
    public void addXP(int points) {
        state.addXP(this, points);
    }

    public void deductXP(int points) {
        state.deductXP(this, points);
    }

    public int getCurrentXP() {
        return currentXP;
    }

    protected void setCurrentXP(int xp) { // protected
        this.currentXP = xp;
    }
}