package transaction;

public class PaymentFactory {
    public static PaymentMethod createPaymentMethod(String type) {
        switch (type) {
            case "CreditCard":
                return new CreditCardPayment();
            case "WeChat":
                return new WeChatPayment();
            default:
                throw new IllegalArgumentException("Unknown payment method: " + type);
        }
    }

    public static RefundMethod createRefundMethod(String type) {
        switch (type) {
            case "CreditCard":
                return new CreditCardRefund();
            case "WeChat":
                return new WeChatRefund();
            default:
                throw new IllegalArgumentException("Unknown refund method: " + type);
        }
    }

    public static Transaction createTransaction(String transactionID, double amount, String paymentType, String refundType) {
        PaymentMethod paymentMethod = createPaymentMethod(paymentType);
        RefundMethod refundMethod = createRefundMethod(refundType);
        return new Transaction(transactionID, amount, paymentMethod, refundMethod);
    }
}