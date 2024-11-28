package main.transaction;

public class CreditCardRefund implements RefundMethod {
	@Override
	public void processRefund(double amount) {
		System.out.printf("Processing credit card refund of: HK$%.2f.\n", amount);
	}
}
