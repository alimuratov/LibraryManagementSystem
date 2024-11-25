package test.testBook;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.book.*;
import main.users.*;
import main.authentication.Password;

class TestBook {
    private Book fullBook;
    private Book simpleBook;
    private Customer bronzeCustomer;
    private Customer silverCustomer;
    private Customer goldCustomer;
    private Review validReview;
    private Review invalidRatingReview;

    @BeforeEach
    void setUp() {
        // Initialize books
        fullBook = new Book(
            "123-4567890123",
            "Test Book",
            "Test Author",
            "Test Publisher",
            "2024-01-01",
            "Test Description",
            29.99,
            5,
            3
        );
        
        simpleBook = new Book("Simple Book", "Simple Description");
        
        // Initialize customers with different membership levels
        bronzeCustomer = new Customer("bronze", new Password("pass123"));
        silverCustomer = new Customer("silver", new Password("pass123"));
        goldCustomer = new Customer("gold", new Password("pass123"));
        
        // Set up membership levels by adding XP
        silverCustomer.getMembership().addXP(100); // Upgrade to Silver
        goldCustomer.getMembership().addXP(100);   // To Silver first
        goldCustomer.getMembership().addXP(250);   // Then to Gold
        
        // Initialize reviews
        validReview = new Review("Good book", 5);
        invalidRatingReview = new Review("Bad rating", 9); // Should print warning
    }

    @Test
    void testConstructors() {
        // Test full constructor
        assertEquals("Test Book", fullBook.getBookTitle());
        assertEquals(29.99, fullBook.getBookPrice());
        assertEquals(5, fullBook.getRentableCopies());
        assertEquals(3, fullBook.getSaleableCopies());
        
        // Test simple constructor
        assertEquals("Simple Book", simpleBook.getBookTitle());
        assertEquals(0.0, simpleBook.getBookPrice());
        assertEquals(0, simpleBook.getRentableCopies());
        assertEquals(0, simpleBook.getSaleableCopies());
    }

    @Test
    void testStaticMethods() {
        // Test getAllBooks and addBook
        Book.addBook(fullBook);
        assertTrue(Book.getAllBooks().contains(fullBook));
        
        // Test getting allBooks multiple times (coverage for null check)
        assertNotNull(Book.getAllBooks());
        assertSame(Book.getAllBooks(), Book.getAllBooks());
    }

    @Test
    void testSetters() {
        fullBook.setTitle("New Title");
        fullBook.setAuthor("New Author");
        fullBook.setRentableCopies(10);
        fullBook.setSaleableCopies(5);
        
        assertEquals("New Title", fullBook.getBookTitle());
        assertEquals("New Title by New Author", fullBook.getDisplayText().split("ISBN")[0].trim());
        assertEquals(10, fullBook.getRentableCopies());
        assertEquals(5, fullBook.getSaleableCopies());
    }

    @Test
    void testAvailabilityChecks() {
        assertTrue(fullBook.isRentable());
        assertTrue(fullBook.isSalable());
        
        fullBook.setRentableCopies(0);
        fullBook.setSaleableCopies(0);
        
        assertFalse(fullBook.isRentable());
        assertFalse(fullBook.isSalable());
    }

    @Test
    void testWaitlistIntegration() {
        // Test waitlist with different membership priorities
        fullBook.addRentingWaitList(bronzeCustomer);
        fullBook.addRentingWaitList(silverCustomer);
        fullBook.addRentingWaitList(goldCustomer);
        
        // Verify priority queue ordering based on membership levels
        assertEquals(goldCustomer, fullBook.getRentingWaitList().poll());
        assertEquals(silverCustomer, fullBook.getRentingWaitList().poll());
        assertEquals(bronzeCustomer, fullBook.getRentingWaitList().poll());
        
        // Test selling waitlist
        fullBook.addSellingWaitList(bronzeCustomer);
        fullBook.addSellingWaitList(goldCustomer);
        fullBook.addSellingWaitList(silverCustomer);
        
        assertEquals(goldCustomer, fullBook.getSellingWaitList().poll());
        assertEquals(silverCustomer, fullBook.getSellingWaitList().poll());
        assertEquals(bronzeCustomer, fullBook.getSellingWaitList().poll());
    }

    @Test
    void testReviewIntegration() {
        // Test adding valid review
        fullBook.addReview(validReview);
        assertEquals(1, fullBook.getReviews().size());
        assertTrue(fullBook.getReviews().contains(validReview));
        assertEquals(5, validReview.getBookRating());
        assertEquals("Good book", validReview.getComments());
        
        // Test adding invalid rating review
        fullBook.addReview(invalidRatingReview);
        assertEquals(0, invalidRatingReview.getBookRating()); // Rating should not be set
        
        // Test adding null review
        assertThrows(IllegalArgumentException.class, () -> fullBook.addReview(null));
        
        // Test displaying reviews
        fullBook.displayReviews(); // With reviews
        
        Book emptyBook = new Book("Empty Book", "No reviews");
        emptyBook.displayReviews(); // Without reviews
    }

    @Test
    void testDisplayMethods() {
        String displayText = fullBook.getDisplayText();
        assertTrue(displayText.contains("Test Book"));
        assertTrue(displayText.contains("Test Author"));
        assertTrue(displayText.contains("123-4567890123"));
        assertTrue(displayText.contains("Test Publisher"));
        assertTrue(displayText.contains("2024-01-01"));
        assertTrue(displayText.contains("29.99"));
        
        assertEquals("5 rentable copies available", fullBook.showAvailableRentalCopies());
        assertEquals("3 saleable copies available", fullBook.showAvailableSaleableCopies());
    }

    @Test
    void testCollectionImmutability() {
        fullBook.addReview(validReview);
        assertThrows(UnsupportedOperationException.class, 
            () -> fullBook.getReviews().add(validReview));
    }
}