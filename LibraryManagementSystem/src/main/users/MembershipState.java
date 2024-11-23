package main.users;

public interface MembershipState {
    void addXP(Membership membership, int points);
    void displayMembershipInfo(Membership membership);
}