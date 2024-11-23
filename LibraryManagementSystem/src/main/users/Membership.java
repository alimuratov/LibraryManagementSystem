package main.users;

public class Membership {
    private MembershipState currentState;
    private int currentXP;
    private int maxXP;

    // Constructor
    public Membership() {
        this.currentState = new BronzeMembership();
        this.currentXP = 0;
        this.maxXP = 100;
    }

    // Getters and Setters
    public int getCurrentXP() {
        return currentXP;
    }

    public void setCurrentXP(int currentXP) {
        this.currentXP = currentXP;
    }

    public int getMaxXP() {
        return maxXP;
    }

    public void setMaxXP(int maxXP) {
        this.maxXP = maxXP;
    }

    public void setCurrentState(MembershipState state) {
        this.currentState = state;
    }

    public void addXP(int points) {
        currentState.addXP(this, points);
    }

    public void displayMembershipInfo() {
        currentState.displayMembershipInfo(this);
    }
}