package transaction;

public class Transaction {
    private double amount;
    private PaymentMethod paymentMethod;

    public Transaction(double amount, PaymentMethod paymentMethod) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public void process() {
        paymentMethod.processPayment(amount);
    }
}