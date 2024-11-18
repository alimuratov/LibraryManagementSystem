package transaction;

public interface PaymentMethod {

    /**
     * @param amount
     * Payment Strategy for different transaction options
     */

    public void processPayment(double amount);
}
