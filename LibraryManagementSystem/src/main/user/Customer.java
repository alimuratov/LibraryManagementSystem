package main.user;

public class Customer extends User {
    private String customerId;

    public Customer() {
        super();
    }

    public Customer(String userName, String customerId) {
        super(userName);
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public void performRole() {
        // Implement customer-specific actions
        System.out.println("Customer " + getUserName() + " is renting books.");
    }

    @Override
    public String toString() {
        return "Customer [userName=" + getUserName() + ", customerId=" + customerId + "]";
    }
}
