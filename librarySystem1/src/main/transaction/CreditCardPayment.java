package main.transaction;

public class CreditCardPayment implements PaymentMethod {
    @Override
    public void processPayment(double amount) {
        System.out.printf("Processing credit card payment of: HK$%.2f.\n", amount);
    }
}