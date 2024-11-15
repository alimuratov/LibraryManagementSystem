package main.users;

import main.book.Book;
import java.time.LocalDate;

public class Renting {
    private Customer customer;
    private Book book;
    private LocalDate rentalDate;
    private LocalDate returnDate;

    public Renting() { }

    public Renting(Customer customer, Book book, LocalDate rentalDate, LocalDate returnDate) {
        this.customer = customer;
        this.book = book;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Renting [customer=" + customer + ", book=" + book +
               ", rentalDate=" + rentalDate + ", returnDate=" + returnDate + "]";
    }

    // Additional methods as needed
}
