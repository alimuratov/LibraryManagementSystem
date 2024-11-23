package main.users;

public class Membership {
    private MembershipType type;
    private int maxRentBooks;
    private int rentalDays;
    private double purchaseDiscount;
    private int waitlistPriority;
    private int currentXP;
    private int maxXP;

    // Constructor
    public Membership() {
        this.type = MembershipType.BRONZE;
        this.maxRentBooks = 2;
        this.rentalDays = 14;
        this.purchaseDiscount = 0.0;
        this.waitlistPriority = 3;
        this.currentXP = 0;
        this.maxXP = 100;
    }

    // Getters
    public MembershipType getType() {
        return type;
    }

    public int getMaxRentBooks() {
        return maxRentBooks;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public int getWaitlistPriority() {
        return waitlistPriority;
    }

    public double calculateDiscountedPrice(double originalPrice) {
        return originalPrice * (1 - this.purchaseDiscount);
    }

    private void upgradeMembership() {
        switch (type) {
            case BRONZE:
                type = MembershipType.SILVER;
                currentXP = 0;
                maxXP = 250;
                System.out.println("Membership upgraded to SILVER. XP reset to " + currentXP + "/" + maxXP);

                this.maxRentBooks = 5;
                this.rentalDays = 21;
                this.purchaseDiscount = 0.10;
                this.waitlistPriority = 2;
                break;
            case SILVER:
                type = MembershipType.GOLD;
                currentXP = 250;
                maxXP = 250;
                System.out.println("Membership upgraded to GOLD. XP set to " + currentXP + "/" + maxXP);

                this.maxRentBooks = 10;
                this.rentalDays = 30;
                this.purchaseDiscount = 0.20;
                this.waitlistPriority = 1;
                break;
            default:
                System.out.println("Cannot upgrade from the current membership type: " + type);
        }
    }

    // Check if an upgrade is possible
    private void checkForUpgrade() {
        if (currentXP >= maxXP) {
            upgradeMembership();
        }
    }

    public void addXP(int points) {
        if (type == MembershipType.GOLD) {
            System.out.println("Already at maximum membership level (GOLD). XP cannot be increased further.");
            return;
        }

        currentXP += points;
        System.out.println("Added " + points + " XP. Current XP: " + currentXP + "/" + maxXP);
        checkForUpgrade();
    }

    public void displayMembershipInfo() {
        System.out.println("Membership Type: " + type);
        System.out.println("Current XP: " + currentXP + "/" + maxXP);
        System.out.println("Max Rentable Books: " + maxRentBooks);
        System.out.println("Rental Days: " + rentalDays);
        System.out.println("Purchase Discount: " + (purchaseDiscount * 100) + "%");
        System.out.println("Waitlist Priority: " + waitlistPriority);
    }
}
