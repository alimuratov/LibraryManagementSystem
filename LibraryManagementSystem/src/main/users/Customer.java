package main.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import main.book.*;
import main.system.*;

public class Customer extends User {
    private Set<Book> rentedBooks = new HashSet<>();
    private Set<Book> purchasedBooks = new HashSet<>();
    private Set<Review> reviews = new HashSet<>();

    private Map<String, Double> profileVector;

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

    public Map<String, Double> getProfileVector() {
        return this.profileVector;
    }

    // Setter
    public void setProfileVector(Map<String, Double> profileVector) {
        this.profileVector = profileVector;
    }

    // Methods to manage rented books
    public void addRentedBook(Book book) {
        rentedBooks.add(book);
    }

    public void removeRentedBook(Book book) {
        rentedBooks.remove(book);
    }

    // Methods to manage purchased books (if needed)
    public void addPurchasedBook(Book book) {
        purchasedBooks.add(book);
    }

    public void removePurchasedBook(Book book) {
        purchasedBooks.remove(book);
    }

    // Review Methods
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