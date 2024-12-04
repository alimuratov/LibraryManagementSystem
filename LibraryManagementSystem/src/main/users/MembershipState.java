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
    public abstract void deductXP(int points);
    public abstract MembershipState getPreviousState();
    public abstract MembershipState getNextState();
    
    public void setMembership(Membership membership) {
        this.membership = membership;
    }
    
    public double calculateDiscountedPrice(double originalPrice) {
        return originalPrice * (1 - getPurchaseDiscount());
    }
    
    public void handleXPOverflow(int totalXP, MembershipState currentState) {
    	MembershipState nextState = currentState.getNextState();
    	
    	if (nextState.getNextState() == null) { 
    		membership.setState(nextState, nextState.getMaxXP());
    		return;
    	}
    	
        int excessXP = totalXP - getMaxXP();
        
        if (excessXP >= nextState.getMaxXP()) {
            handleXPOverflow(excessXP, nextState);
        } else {
        	membership.setState(nextState, excessXP);
        }
    }

    public void handleXPUnderflow(int totalXP, MembershipState currentState) {
        MembershipState previousState = currentState.getPreviousState();
        
        if (previousState == null) {
    		membership.setState(currentState, 0);
    		return;
    	}
        
        int adjustedXP = previousState.getMaxXP() + totalXP;
        
        if (adjustedXP < 0) {
        	handleXPUnderflow(adjustedXP, previousState);
        } else {
        	membership.setState(previousState, adjustedXP);
        }
    }
}