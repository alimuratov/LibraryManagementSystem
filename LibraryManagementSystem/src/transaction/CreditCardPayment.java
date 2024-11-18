package transaction;

public class CreditCardPayment implements PaymentMethod {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of: HK$%.2f.\n", amount);
    }
}