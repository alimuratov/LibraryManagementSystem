package transaction;

import java.util.Dictionary;
import java.util.Hashtable;

public class Transaction {
	private double amount;
	private String transactionID;
	private boolean isPaymentProcessed = false;
	private boolean isPaymentRefunded = false;
	private PaymentMethod paymentMethod;
	private RefundMethod refundMethod;
	private static Dictionary<String, Boolean> transactions = new Hashtable<String, Boolean>();
	// transaction x is set to true if it was processed and not refunded 
	
	public Transaction(String aTransactionID, double anAmount, PaymentMethod aPaymentMethod, RefundMethod aRefundMethod) {
		this.transactionID = aTransactionID;
		this.amount = anAmount;
		this.paymentMethod = aPaymentMethod;
		this.refundMethod = aRefundMethod;
	}

	public void processPayment() {
		paymentMethod.processPayment(amount);
		transactions.put(transactionID, true);
		isPaymentProcessed = true;
        System.out.printf("Transaction ID: %s was processed successfully.\n", transactionID);
	}

	public void processRefund() {
		if(transactions.get(transactionID) != null && transactions.get(transactionID) == true) {
			refundMethod.processRefund(amount);
            transactions.put(transactionID, false);
			isPaymentRefunded = true;
	        System.out.printf("Transaction ID: %s was refunded successfully.\n", transactionID);
		} 
		else if (isPaymentRefunded) {
            System.out.printf("Payment has already been refunded.\n");
        } 
        else {
            System.out.printf("Transaction not found.\n");
        }
	}

	public boolean getIsPaymentProcessed(){
		return this.isPaymentProcessed;
	}
	
	public boolean getIsPaymentRefunded() {
		return this.isPaymentRefunded;
	}
	
	public static String getNewTransactionID() {
        return Integer.toString(Transaction.transactions.size() + 1);
    }
}