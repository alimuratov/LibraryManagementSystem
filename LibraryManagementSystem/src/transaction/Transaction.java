package transaction;

import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private double amount;
    private String transactionID;
    private boolean isPaymentProcessed = false;
    private boolean isPaymentRefunded = false;
    private PaymentMethod paymentMethod;
    private RefundMethod refundMethod;

    // Static map to store transaction statuses
    private static Map<String, Boolean> transactions = new HashMap<>();

    public Transaction(String transactionID, double amount, PaymentMethod paymentMethod, RefundMethod refundMethod) {
        this.transactionID = transactionID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.refundMethod = refundMethod;
    }

    public void processPayment() {
        paymentMethod.processPayment(amount);
        transactions.put(transactionID, true);
        isPaymentProcessed = true;
        System.out.printf("Transaction ID: %s was processed successfully.\n", transactionID);
    }

    public void processRefund() {
        if (transactions.get(transactionID) != null && transactions.get(transactionID)) {
            refundMethod.processRefund(amount);
            transactions.put(transactionID, false);
            isPaymentRefunded = true;
            System.out.printf("Transaction ID: %s was refunded successfully.\n", transactionID);
        } else if (isPaymentRefunded) {
            System.out.printf("Payment has already been refunded.\n");
        } else {
            System.out.printf("Transaction not found.\n");
        }
    }

    public boolean isPaymentProcessed() {
        return isPaymentProcessed;
    }

    public boolean isPaymentRefunded() {
        return isPaymentRefunded;
    }
}