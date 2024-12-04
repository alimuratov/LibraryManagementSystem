package main.book;

import java.util.*;
import main.users.Customer;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String publisher; 
    private String publicationDate; 
    private String bookDescription;
    private double bookPrice;
    private int rentableCopies;
    private int saleableCopies;
    private List<Review> reviews;
    private Queue<Customer> rentingWaitList;
    private Queue<Customer> sellingWaitList;
    private static List<Book> allBooks;

    // Constructor with all details
    public Book(String isbn, String title, String author, String publisher, String publicationDate, String bookDescription, double price, int rentableCopies, int saleableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.bookDescription = bookDescription;
        this.bookPrice = price;
        this.rentableCopies = rentableCopies;
        this.saleableCopies = saleableCopies;
        this.reviews = new ArrayList<>();
        this.rentingWaitList = new PriorityQueue<>(Comparator.comparingInt(c -> c.getMembership().getWaitlistPriority()));
        this.sellingWaitList = new PriorityQueue<>(Comparator.comparingInt(c -> c.getMembership().getWaitlistPriority()));
    }

    // Simplified constructor
    public Book(String title, String bookDescription) {
        this("000-0000000000",
             title, 
             "Unknown Author",
             "Unknown Publisher",
             "2024-01-01",
             bookDescription, 
             0.0, // Default price
             0,   // Default rentable copies
             0    // Default saleable copies
        );
    }
    
    public Book(String title, String bookDescription, Integer id) {
        this(String.valueOf(id),
             title, 
             "Unknown Author",
             "Unknown Publisher",
             "2024-01-01",
             bookDescription, 
             0.0, // Default price
             0,   // Default rentable copies
             0    // Default saleable copies
        );
    }


    // Static method to get all books
    public static List<Book> getAllBooks() {
        if (allBooks == null) {
            allBooks = new ArrayList<>();
        }
        return allBooks;
    }

    // Method to add a book to the list of all books
    public static void addBook(Book book) {
        getAllBooks().add(book);
    }

    // Getters
    public String getBookTitle() {
        return title;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public int getRentableCopies() {
        return rentableCopies;
    }

    public int getSaleableCopies() {
        return saleableCopies;
    }

    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }

    public Queue<Customer> getRentingWaitList() {
        return rentingWaitList;
    }

    public Queue<Customer> getSellingWaitList() {
        return sellingWaitList;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public void setRentableCopies(int rentableCopies) {
        this.rentableCopies = rentableCopies;
    }

    public void setSaleableCopies(int saleableCopies) {
        this.saleableCopies = saleableCopies;
    }


    // Methods to check availability
    public boolean isRentable() {
        return rentableCopies > 0;
    }

    public boolean isSalable() {
        return saleableCopies > 0;
    }

    // Methods to handle waitlists
    public void addRentingWaitList(Customer customer) {
        rentingWaitList.add(customer);
    }

    public void addSellingWaitList(Customer customer) {
        sellingWaitList.add(customer);
    }

    // Methods for reviews
    public void addReview(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null.");
        }
        reviews.add(review);
    }

    public void displayReviews() {
        if (reviews.isEmpty()) {
            System.out.println("No reviews yet.");
        } else {
            for (Review review : reviews) {
                System.out.println(review);
            }
        }
    }

    // Display book details
    public String getDisplayText() {
        return title + " by " + author + " ISBN: " + isbn + " Publisher: " + publisher + " Publication Date: " + publicationDate + " Price: $" + bookPrice;
    }

    // Methods to show available copies
    public String showAvailableRentalCopies() {
        return rentableCopies + " rentable copies available";
    }

    public String showAvailableSaleableCopies() {
        return saleableCopies + " saleable copies available";
    }
    
    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }
    
    public static List<Book> search(String searchString) {
        String[] tokens = searchString.split("[\\p{Punct}\\s]+"); // Regex to split by punctuation and whitespace
        
        List<Book> result = new ArrayList<>();
        
        for (Book book : allBooks) {
            String bookDescription = book.getBookDescription().toLowerCase();
            for (String token : tokens) {
                if (bookDescription.contains(token.toLowerCase())) {
                    result.add(book);
                    break;
                }
            }
        }
        
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Book))
            return false;
        Book other = (Book) obj;
        return title.equals(other.title);
    }

    @Override
    public int hashCode() {
        if (isbn == null) {
            return title.hashCode();
        }
        return isbn.hashCode();
    }

    @Override
    public String toString() {
        return "Book{" +
               ", Title='" + title + '\'' +
               ", Author='" + author + '\'' +
               ", Publisher='" + publisher + '\'' +
               ", Publication Date='" + publicationDate + '\'' +
               ", Description='" + bookDescription + '\'' +
               ", Price=$" + bookPrice +
               ", Rentable Copies=" + rentableCopies +
               ", Saleable Copies=" + saleableCopies +
               ", Renting Waitlist Size=" + rentingWaitList.size() +
               ", Selling Waitlist Size=" + sellingWaitList.size() +
               '}';
    }
    
    public static void removeAllBooks() {
    	if (allBooks != null && !allBooks.isEmpty()) allBooks.clear();
    }
}