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
        double amount = 100.569;
        Transaction transaction = new Transaction(amount, paymentMethod);
        transaction.process();
        
        String result = String.format("Processing credit card payment of: HK$%.2f.\n", amount);
        assertEquals(result, getOutput());
    }
}
