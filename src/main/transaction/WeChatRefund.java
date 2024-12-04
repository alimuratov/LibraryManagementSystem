package main.transaction;

public class WeChatRefund implements RefundMethod {
	@Override
	public void processRefund(double amount) {
		System.out.printf("Processing WeChat refund of: HK$%.2f.\n", amount);
	}
}
