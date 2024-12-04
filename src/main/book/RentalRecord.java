package main.book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import main.users.*;

public class RentalRecord {
    private final Customer customer; 
    private final Book book;
    private final LocalDate rentalDate; 
    private final LocalDate returnDate; 
    private boolean isReturned;

    public RentalRecord(Customer customer, Book book) {
        if (customer == null || book == null) {
            throw new IllegalArgumentException("Customer and rental copy cannot be null.");
        }
        this.customer = customer;
        this.book = book;
        this.rentalDate = LocalDate.now(); 
        this.returnDate = rentalDate.plusDays(customer.getMembership().getRentalDays()); 
        this.isReturned = false;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void markAsReturned() {
        this.isReturned = true;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "RentalRecord {" +
                "Customer =" + customer +
                ", Book Name =" + book.getBookTitle() +
                ", Rental Date =" + rentalDate.format(formatter) +
                ", Return Date =" + (returnDate != null ? returnDate.format(formatter) : "Not Returned") +
                '}';
    }
}