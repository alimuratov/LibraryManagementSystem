package main.book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import main.users.Customer;
import main.transaction.Transaction;

public class PurchaseRecord {
    private final Customer customer;
    private final Book book;
    private final LocalDate purchaseDate;
    private final LocalDate refundExpiryDate;
    private final Transaction transaction;
    private final String refundMethod;

    public PurchaseRecord(Customer customer, Book book, Transaction transaction, String refundMethod) {
        if (customer == null || book == null || refundMethod == null) {
            throw new IllegalArgumentException("Customer, book, and transaction cannot be null.");
        }
        this.customer = customer;
        this.book = book;
        this.purchaseDate = LocalDate.now();
        this.refundExpiryDate = purchaseDate.plusDays(7);
        this.transaction = transaction;
        this.refundMethod = refundMethod;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public LocalDate getRefundExpiryDate() {
        return refundExpiryDate;
    }

    public String getRefundMethod() {
        return refundMethod;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "PurchaseRecord {" +
                "Customer =" + customer.getUserName() +
                ", Book =" + book.getBookTitle() +
                ", Purchase Date =" + purchaseDate.format(formatter) +
                ", Refund Expiry Date =" + refundExpiryDate.format(formatter) +
                '}';
    }
}
