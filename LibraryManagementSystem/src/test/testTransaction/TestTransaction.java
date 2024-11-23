package test.testTransaction;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import main.transaction.*;

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
    public void testCreditCardPayment() throws Exception {
        setOutput();
        String transactionID = Transaction.getNewTransactionID();
        Transaction transaction = PaymentFactory.createTransaction(transactionID, 100.00, "CreditCard", "CreditCard");
        
        assertFalse(transaction.isPaymentProcessed());
        transaction.processPayment();
        assertTrue(transaction.isPaymentProcessed());
        assertFalse(transaction.isPaymentRefunded());
        
        transaction.processRefund();
        assertTrue(transaction.isPaymentRefunded());
        
        String result = String.format("Processing credit card payment of: HK$%.2f.\n" +
                                       "Transaction ID: %s was processed successfully.\n" +
                                       "Processing credit card refund of: HK$%.2f.\n" +
                                       "Transaction ID: %s was refunded successfully.\n",
                                       100.00, transactionID, 100.00, transactionID);
        assertEquals(result, getOutput());
    }
    
    @Test
    public void testWeChatPayment() throws Exception {
        setOutput();
        String transactionID = Transaction.getNewTransactionID();
        Transaction transaction = PaymentFactory.createTransaction(transactionID, 100.00, "WeChat", "WeChat");
        
        assertFalse(transaction.isPaymentProcessed());
        transaction.processPayment();
        assertTrue(transaction.isPaymentProcessed());
        assertFalse(transaction.isPaymentRefunded());
        
        transaction.processRefund();
        assertTrue(transaction.isPaymentRefunded());
        
        String result = String.format("Processing WeChat payment of: HK$%.2f.\n" + 
                                       "Transaction ID: %s was processed successfully.\n" + 
                                       "Processing WeChat refund of: HK$%.2f.\n" + 
                                       "Transaction ID: %s was refunded successfully.\n",
                                       100.00, transactionID, 100.00, transactionID);
        assertEquals(result, getOutput());
    }
    
    @Test
    public void testMoreRefunds() throws Exception {
        setOutput();
        String transactionID = Transaction.getNewTransactionID();
        Transaction transaction = PaymentFactory.createTransaction(transactionID, 100.00, "WeChat", "WeChat");
        
        transaction.processPayment();
        transaction.processRefund();
        
        // Attempting a second refund
        transaction.processRefund();
        
        String result = String.format("Processing WeChat payment of: HK$%.2f.\n" +
                                       "Transaction ID: %s was processed successfully.\n" +
                                       "Processing WeChat refund of: HK$%.2f.\n" +
                                       "Transaction ID: %s was refunded successfully.\n" +
                                       "Payment has already been refunded.\n",
                                       100.00, transactionID, 100.00, transactionID);
        assertEquals(result, getOutput());
    }
    
    @Test
    public void testNotFoundTransaction() throws Exception {
        setOutput();
        String transactionID = Transaction.getNewTransactionID();
        Transaction transaction = PaymentFactory.createTransaction(transactionID, 100.00, "WeChat", "WeChat");
        
        // Refund before processing payment
        transaction.processRefund();
        
        String result = "Transaction not found.\n";
        assertEquals(result, getOutput());
    }
}