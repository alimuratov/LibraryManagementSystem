package test.testBook;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.book.Book;
import main.book.PurchaseRecord;
import main.transaction.PaymentFactory;
import main.transaction.Transaction;
import main.users.Customer;
import main.authentication.Password;

public class TestPurchaseRecord {

    private Customer customer;
    private Book book;
    private Transaction transaction;
    private PurchaseRecord purchaseRecord;
    private String refundMethod;

    @BeforeEach
    public void setUp() {
        // Initialize a Customer
        Password password = new Password("securePassword123");
        customer = new Customer("john_doe", password);
        
        // Initialize a Book
        book = new Book("123-4567890123", "Effective Java", "Joshua Bloch", "Addison-Wesley", 
                        "2018-01-06", "A comprehensive guide to programming in Java.", 
                        45.00, 5, 10);
        
        // Initialize a Transaction
        String transactionID = "txn-001";
        refundMethod = "CreditCard";
        transaction = PaymentFactory.createTransaction(transactionID, book.getBookPrice(), "CreditCard", refundMethod);
        
        // Initialize a PurchaseRecord
        purchaseRecord = new PurchaseRecord(customer, book, transaction, refundMethod);
    }

    @Test
    public void testPurchaseRecordCreation() {
        assertEquals(customer, purchaseRecord.getCustomer());
        assertEquals(book, purchaseRecord.getBook());
        assertEquals(LocalDate.now(), purchaseRecord.getPurchaseDate());
        assertEquals(LocalDate.now().plusDays(7), purchaseRecord.getRefundExpiryDate());
        assertEquals(transaction, purchaseRecord.getTransaction());
        assertEquals(refundMethod, purchaseRecord.getRefundMethod());
        assertFalse(purchaseRecord.isRefunded());
    }

    @Test
    public void testMarkAsRefunded() {
        assertFalse(purchaseRecord.isRefunded());
        purchaseRecord.markAsRefunded();
        assertTrue(purchaseRecord.isRefunded());
    }

    @Test
    public void testToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expected = "PurchaseRecord {Customer =" + customer.getUserName() +
                          ", Book =" + book.getBookTitle() +
                          ", Purchase Date =" + LocalDate.now().format(formatter) +
                          ", Refund Expiry Date =" + LocalDate.now().plusDays(7).format(formatter) +
                          ", Is Refunded =" + purchaseRecord.isRefunded() +
                          '}';
        assertEquals(expected, purchaseRecord.toString(), "toString method should return the correct string representation.");
    }
    
    @Test
    public void testConstructorThrowsExceptionWhenCustomerIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new PurchaseRecord(null, book, transaction, refundMethod);
        });
        
        assertEquals("Customer, book, and transaction cannot be null.", exception.getMessage());
    }

    @Test
    public void testConstructorThrowsExceptionWhenBookIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new PurchaseRecord(customer, null, transaction, refundMethod);
        });
        
        assertEquals("Customer, book, and transaction cannot be null.", exception.getMessage());
    }

    @Test
    public void testConstructorThrowsExceptionWhenRefundMethodIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new PurchaseRecord(customer, book, transaction, null);
        });
        
        assertEquals("Customer, book, and transaction cannot be null.", exception.getMessage());
    }
}
