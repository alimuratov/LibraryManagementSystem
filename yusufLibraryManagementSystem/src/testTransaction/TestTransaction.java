package testTransaction;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import transaction.*;

public class TestTransaction {
	
	PrintStream oldPrintStream;
    ByteArrayOutputStream bos;
    
    private void setOutput() throws Exception {
        oldPrintStream = System.out;
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
    }

    private String getOutput() {
        System.setOut(oldPrintStream);
        return bos.toString();
    }
    
    @Test
    public void testCreditCardPayment() throws Exception{
    	setOutput();
        PaymentMethod paymentMethod = new CreditCardPayment();
        RefundMethod refundMethod = new CreditCardRefund();
        double amount = 100.569;
        String transactionID = Transaction.getNewTransactionID();
        Transaction transaction = new Transaction(transactionID, amount, paymentMethod, refundMethod);
        assertEquals(transaction.getIsPaymentProcessed(), false);
        transaction.processPayment();
        assertEquals(transaction.getIsPaymentProcessed(), true);
        assertEquals(transaction.getIsPaymentRefunded(), false);
        transaction.processRefund();
        assertEquals(transaction.getIsPaymentRefunded(), true);
        assertEquals(Transaction.getNewTransactionID(), Integer.toString(Integer.parseInt(transactionID) + 1));
        String result = String.format("Processing credit card payment of: HK$%.2f.\n" +
                "Transaction ID: %s was processed successfully.\n" +
                "Processing credit card refund of: HK$%.2f.\n" +
                "Transaction ID: %s was refunded successfully.\n",
                amount, transactionID, amount, transactionID);
        assertEquals(result, getOutput());
    }
    
    @Test
	public void testWeChatPayment() throws Exception {
		setOutput();
		PaymentMethod paymentMethod = new WeChatPayment();
		RefundMethod refundMethod = new WeChatRefund();
		double amount = 100.569;
		String transactionID = "1";
		Transaction transaction = new Transaction(transactionID, amount, paymentMethod, refundMethod);
		transaction.processPayment();
		transaction.processRefund();

		String result = String.format("Processing WeChat payment of: HK$%.2f.\n" + 
				"Transaction ID: %s was processed successfully.\n" + 
				"Processing WeChat refund of: HK$%.2f.\n" + 
				"Transaction ID: %s was refunded successfully.\n",
				amount, transactionID, amount, transactionID);
		assertEquals(result, getOutput());
	}
    
    @Test
    public void testMoreRefund() throws Exception {
    	setOutput();
		PaymentMethod paymentMethod = new WeChatPayment();
		RefundMethod refundMethod = new WeChatRefund();
		double amount = 100.569;
		String transactionID = "1";
		Transaction transaction = new Transaction(transactionID, amount, paymentMethod, refundMethod);
		transaction.processPayment();
		transaction.processRefund();
		transaction.processRefund();

		String result = String.format("Processing WeChat payment of: HK$%.2f.\n" +
                "Transaction ID: %s was processed successfully.\n" +
                "Processing WeChat refund of: HK$%.2f.\n" +
                "Transaction ID: %s was refunded successfully.\n" +
                "Payment has already been refunded.\n",
                amount, transactionID, amount, transactionID);
        assertEquals(result, getOutput());
    }
    
    @Test
    public void testNotFoundTransaction() throws Exception {
    	setOutput();
		PaymentMethod paymentMethod = new WeChatPayment();
    	RefundMethod refundMethod = new WeChatRefund();
		double amount = 100.569;
		String transactionID = "1";
		Transaction transaction = new Transaction(transactionID, amount, paymentMethod, refundMethod);
		transaction.processRefund();
		
		String result = "Transaction not found.\n";
		assertEquals(result, getOutput());
    }
}
