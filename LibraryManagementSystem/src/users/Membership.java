package users;

public class Membership {
    private MembershipType type;
    private int maxRentBooks;
    private int rentalPeriodDays;
    private double purchaseDiscount;
    private int waitlistPriority;

    // Constructor
    public Membership(MembershipType type) {
        this.type = type;
        switch (type) {
            case BRONZE:
                this.maxRentBooks = 2;
                this.rentalPeriodDays = 14;
                this.purchaseDiscount = 0.0;
                this.waitlistPriority = 3;
                break;
            case SILVER:
                this.maxRentBooks = 5;
                this.rentalPeriodDays = 21;
                this.purchaseDiscount = 0.10;
                this.waitlistPriority = 2;
                break;
            case GOLD:
                this.maxRentBooks = 10;
                this.rentalPeriodDays = 30;
                this.purchaseDiscount = 0.20;
                this.waitlistPriority = 1;
                break;
        }
    }

    // Getters
    public MembershipType getType() {
        return type;
    }

    public int getMaxRentBooks() {
        return maxRentBooks;
    }

    public int getRentalPeriodDays() {
        return rentalPeriodDays;
    }

    public double getPurchaseDiscount() {
        return purchaseDiscount;
    }

    public int getWaitlistPriority() {
        return waitlistPriority;
    }

    public double calculateDiscountedPrice(double originalPrice) {
        double discount = getPurchaseDiscount();
        return originalPrice * (1 - discount);
    }
}
