package main.users;

import main.book.*;

import java.time.LocalDate;

public class Renting {
    private Customer customer;
    private Book book;
    private LocalDate rentalDate;
    private LocalDate returnDate;

    // Constructors
    public Renting(Customer customer, Book book) {
        this.customer = customer;
        this.book = book;
        this.rentalDate = LocalDate.now();
        this.returnDate = rentalDate.plusDays(14); // Assuming a 2-week rental period
    }

    // Rent a book
    public boolean rentBook() {
        if (book.getAvailableLendingCopy() != null) {
            book.Lend(customer);
            customer.addRentedBook(book);
            System.out.println("Book rented successfully: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("Book is not available for rent and you have been added to the waiting list: " + book.getDisplayText());
            return false;
        }
    }

    // Return a rented book
    public boolean returnBook() {
        if (customer.getRentedBooks().contains(book)) {
            customer.removeRentedBook(book);
            System.out.println("Book returned successfully: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("This book is not in your rented list: " + book.getDisplayText());
            return false;
        }
    }

    @Override
    public String toString() {
        return "Renting [customer=" + customer.getUserName() + ", book=" + book.getDisplayText() +
               ", rentalDate=" + rentalDate + ", returnDate=" + returnDate + "]";
    }

    // Additional methods as needed
}
