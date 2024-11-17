package transaction;

public class WeChatPayment implements PaymentMethod {
	@Override
	public void processPayment(double amount) {
		System.out.printf("Processing WeChat payment of: HK$%.2f.\n", amount);
	}
}
