package test.testUsers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.users.*;
import main.book.*;
import main.authentication.Password;
import java.util.*;

class CustomerTest {
    private Customer customer;
    private Book book1;
    private Book book2;
    private Review review;
    private Map<String, Double> profileVector;

    @BeforeEach
    void setUp() {
        // Initialize customer
        customer = new Customer("testUser", new Password("password123"));
        
        // Initialize books
        book1 = new Book(
            "123-4567890123",
            "Test Book 1",
            "Test Author",
            "Test Publisher",
            "2024-01-01",
            "Test Description",
            29.99,
            5,
            3
        );
        
        book2 = new Book(
            "123-4567890124",
            "Test Book 2",
            "Test Author",
            "Test Publisher",
            "2024-01-01",
            "Test Description",
            39.99,
            3,
            2
        );
        
        // Initialize review
        review = new Review("Good book", 5);
        
        // Initialize profile vector
        profileVector = new HashMap<>();
        profileVector.put("fiction", 0.8);
        profileVector.put("mystery", 0.6);
    }

    @Test
    void testConstructorAndGetters() {
        assertNotNull(customer.getMembership());
        assertTrue(customer.getRentedBooks().isEmpty());
        assertTrue(customer.getPurchasedBooks().isEmpty());
        assertTrue(customer.getReviews().isEmpty());
        assertNull(customer.getProfileVector());
    }

    @Test
    void testProfileVectorManagement() {
        customer.setProfileVector(profileVector);
        assertEquals(profileVector, customer.getProfileVector());
    }

    @Test
    void testProtectedBookManagement() {
        // Test rented books management
        customer.addRentedBook(book1);
        assertTrue(customer.getRentedBooks().contains(book1));
        
        customer.removeRentedBook(book1);
        assertFalse(customer.getRentedBooks().contains(book1));

        // Test purchased books management
        customer.addPurchasedBook(book2);
        assertTrue(customer.getPurchasedBooks().contains(book2));
        
        customer.removePurchasedBook(book2);
        assertFalse(customer.getPurchasedBooks().contains(book2));
    }

    @Test
    void testReviewManagement() {
        // Test adding review for rented book
        customer.addRentedBook(book1);
        customer.addReview(book1, review);
        assertTrue(customer.getReviews().contains(review));
        assertTrue(book1.getReviews().contains(review));

        // Test adding review for purchased book
        customer.addPurchasedBook(book2);
        Review review2 = new Review("Another good book", 4);
        customer.addReview(book2, review2);
        assertTrue(customer.getReviews().contains(review2));
        assertTrue(book2.getReviews().contains(review2));

        // Test adding review for unrented/unpurchased book
        Book book3 = new Book("Test Book 3", "Description");
        Review review3 = new Review("Unauthorized review", 3);
        assertThrows(IllegalArgumentException.class, 
            () -> customer.addReview(book3, review3));
    }

    @Test
    void testCollectionImmutability() {
        // Test that collections returned by getters are unmodifiable
        assertThrows(UnsupportedOperationException.class, 
            () -> customer.getRentedBooks().add(book1));
        
        assertThrows(UnsupportedOperationException.class, 
            () -> customer.getPurchasedBooks().add(book1));
        
        assertThrows(UnsupportedOperationException.class, 
            () -> customer.getReviews().add(review));
    }

    @Test
    void testLibraryInteractions() {
        // Test library interactions (these will delegate to LibraryManager)
        customer.rentBook(book1);
        customer.returnBook(book1);
        customer.purchaseBook(book2, "CreditCard");
        
        // Note: Actual effects of these operations depend on LibraryManager implementation
        // We're just testing that the methods can be called without throwing exceptions
    }
}