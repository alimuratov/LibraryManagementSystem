package main.users;

import java.util.*;

import main.authentication.Password;
import main.book.Book;
import main.book.Review;

public class Customer extends User {
    private Membership membership;
    private ArrayList<Book> rentedBooks;
    private ArrayList<Book> purchasedBooks;
    private ArrayList<Review> reviews;
    private Map<String, Double> profileVector;
    private LibraryManager libraryManager = LibraryManager.getInstance();

    // Constructor
    public Customer(String userName, Password password) {
        super(userName, password);
        this.membership = new Membership();
        this.rentedBooks = new ArrayList<>();
        this.purchasedBooks = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    // Getters
    public Membership getMembership() {
        return membership;
    }

    public ArrayList<Book> getRentedBooks() {
        return rentedBooks;
    }

    public ArrayList<Book> getPurchasedBooks() {
        return purchasedBooks;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public Map<String, Double> getProfileVector() {
        return this.profileVector;
    }

    // Setters
    public void setProfileVector(Map<String, Double> profileVector) {
        this.profileVector = profileVector;
    }

    // Methods to manage rented books (now called internally)
    public void addRentedBook(Book book) {
        rentedBooks.add(book);
    }

    public void removeRentedBook(Book book) {
        rentedBooks.remove(book);
    }

    // Methods to manage purchased books (now called internally)
    public void addPurchasedBook(Book book) {
        purchasedBooks.add(book);
    }

    public void removePurchasedBook(Book book) {
        purchasedBooks.remove(book);
    }

    // Public methods to interact with books via Librarian
    public void rentBook(Book book) {
        libraryManager.rentBook(this, book);
    }

    public void returnBook(Book book) {
        libraryManager.returnBook(this, book);
    }

    public void purchaseBook(Book book, String method) {
        libraryManager.purchaseBook(this, book, method);
    }

    public void refundBook(Book book) {
        libraryManager.refundBook(this, book);
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
}