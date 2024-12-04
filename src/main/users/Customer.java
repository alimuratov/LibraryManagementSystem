package main.users;

import java.util.*;

import main.authentication.Password;
import main.book.Book;
import main.book.Review;

public class Customer extends User {
    private Membership membership;
    private Set<Book> rentedBooks;
    private Set<Book> purchasedBooks;
    private Set<Review> reviews;
    private Map<String, Double> profileVector;
    private LibraryManager libraryManager = LibraryManager.getInstance();

    // Constructor
    public Customer(String userName, Password password) {
        super(userName, password);
        this.membership = new Membership();
        this.rentedBooks = new HashSet<>();
        this.purchasedBooks = new HashSet<>();
        this.reviews = new HashSet<>();
    }
    
    public Customer(String userName) {
        super(userName, new Password("defaultPassword"));
        this.membership = new Membership();
        this.rentedBooks = new HashSet<>();
        this.purchasedBooks = new HashSet<>();
        this.reviews = new HashSet<>();
    }

    public Customer(String userName, Integer id) {
        super(userName, id);
        this.membership = new Membership();
        this.rentedBooks = new HashSet<>();
        this.purchasedBooks = new HashSet<>();
        this.reviews = new HashSet<>();
    }


    // Getters
    public Membership getMembership() {
        return membership;
    }

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