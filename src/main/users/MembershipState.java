package main.users;

public interface MembershipState {
    int getMaxRentBooks();
    int getRentalDays();
    double getPurchaseDiscount();
    int getWaitlistPriority();
    int getMaxXP();
    String getType();
    void addXP(Membership membership, int points);
    void deductXP(Membership membership, int points);
    MembershipState getPreviousState();
    MembershipState getNextState();
}
