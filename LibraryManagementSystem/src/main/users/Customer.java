package main.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import book.Book;
import book.Review;
import book.SalableBookCopy;
import main.kocka.Password;
import book.RentalBookCopy;
import book.Customer_stub;

public class Customer extends User {
    private Set<Book> rentedBooks = new HashSet<>();
    private Set<Book> purchasedBooks = new HashSet<>();
    private Set<Review> reviews = new HashSet<>();

    // Constructor
    public Customer(String userName, Password password) {
        super(userName, password);
    }

    // Getters
    public Set<Book> getRentedBooks() {
        return Collections.unmodifiableSet(rentedBooks);
    }

    public Set<Book> getPurchasedBooks() {
        return Collections.unmodifiableSet(purchasedBooks);
    }

    public Set<Review> getReviews() {
        return Collections.unmodifiableSet(reviews);
    }

    // Book Interaction Methods

    /**
     * Rents a book if it's available.
     *
     * @param book The book to rent.
     * @return true if the book was successfully rented, false otherwise.
     */
    public boolean rentBook(Book book) {
        if (book.isAvailableForRent()) {
            // Assuming Customer_stub is an interface or superclass that Customer implements
            Customer_stub customerStub = this; // Casting to Customer_stub if necessary
            book.Lend(customerStub);
            rentedBooks.add(book);
            System.out.println("Book rented successfully: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("Book is not available for rent: " + book.getDisplayText());
            return false;
        }
    }

    /**
     * Returns a rented book.
     *
     * @param book The book to return.
     * @return true if the book was successfully returned, false otherwise.
     */
    public boolean returnBook(Book book) {
        if (rentedBooks.contains(book)) {
            book.returnBook(this); // Assuming returnBook handles the logic
            rentedBooks.remove(book);
            System.out.println("Book returned successfully: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("This book is not in your rented list: " + book.getDisplayText());
            return false;
        }
    }

    /**
     * Purchases a book if it's saleable.
     *
     * @param book The book to purchase.
     * @return true if the book was successfully purchased, false otherwise.
     */
    public boolean purchaseBook(Book book) {
        if (book.isSalable()) {
            Customer_stub customerStub = this; // Casting to Customer_stub if necessary
            book.Buy(customerStub);
            purchasedBooks.add(book);
            System.out.println("Book purchased successfully: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("Book is not available for purchase: " + book.getDisplayText());
            return false;
        }
    }

    // Review Methods

    /**
     * Adds a review for a book.
     *
     * @param book   The book being reviewed.
     * @param review The review content.
     */
    public void addReview(Book book, Review review) {
        if (purchasedBooks.contains(book) || rentedBooks.contains(book)) {
            book.addReview(review);
            reviews.add(review);
            System.out.println("Review added for book: " + book.getDisplayText());
        } else {
            System.out.println("Cannot review a book you haven't rented or purchased: " + book.getDisplayText());
            throw new IllegalArgumentException("User has not rented or purchased this book.");
        }
    }

    // Additional methods will be added here
}
