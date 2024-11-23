package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.authentication.Password;
import main.book.Book;
import main.book.Review;
import main.users.Customer;
import main.users.Membership;
import main.users.MembershipType;

public class CustomerTest {

    private Password password;
    private Customer customer;
    private Book book1;
    private Book book2;
    private Review review1;
    private Review review2;

    @BeforeEach
    public void setUp() {
        password = new Password("securePassword123");
        customer = new Customer("Alice", password);
        book1 = new Book("Effective Java", "A comprehensive guide to programming in Java.");
        book2 = new Book("Clean Code", "A handbook of agile software craftsmanship.");
        review1 = new Review("Great book!", 8);
        review2 = new Review("Very informative.", 7);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(new Membership(MembershipType.BRONZE), customer.getMembership());
        assertTrue(customer.getRentedBooks().isEmpty(), "Rented books should be empty");
        assertTrue(customer.getPurchasedBooks().isEmpty(), "Purchased books should be empty");
        assertTrue(customer.getReviews().isEmpty(), "Reviews should be empty");
        assertNull(customer.getProfileVector(), "Profile vector should be null initially");
    }

    @Test
    public void testUpgradeMembership() {
        customer.upgradeMembership(MembershipType.SILVER);
        assertEquals(new Membership(MembershipType.SILVER), customer.getMembership());

        customer.upgradeMembership(MembershipType.GOLD);
        assertEquals(new Membership(MembershipType.GOLD), customer.getMembership());
    }

    @Test
    public void testSetAndGetProfileVector() {
        Map<String, Double> profile = new HashMap<>();
        profile.put("fiction", 0.8);
        profile.put("science", 0.6);

        customer.setProfileVector(profile);
        assertEquals(profile, customer.getProfileVector());
    }

    @Test
    public void testAddRentedBook_UnderLimit() {
        customer.addRentedBook(book1);
        assertTrue(customer.getRentedBooks().contains(book1), "Rented books should contain book1");
    }

    @Test
    public void testAddRentedBook_AtLimit() {
        // Default BRONZE membership allows max 2 rented books
        customer.addRentedBook(book1);
        customer.addRentedBook(book2);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            customer.addRentedBook(new Book("Design Patterns", "Elements of Reusable Object-Oriented Software"));
        }, "Adding a rented book beyond the limit should throw IllegalStateException");

        assertEquals("Cannot rent more books.", exception.getMessage(),
                "Exception message should match");
    }

    @Test
    public void testAddRentedBook_AfterUpgrade() {
        customer.upgradeMembership(MembershipType.SILVER); // SILVER allows 5 rented books

        for (int i = 1; i <= 5; i++) {
            customer.addRentedBook(new Book("Book " + i, "Description " + i));
        }

        assertEquals(5, customer.getRentedBooks().size(), "Rented books should have 5 books");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            customer.addRentedBook(new Book("Book 6", "Description 6"));
        }, "Adding a rented book beyond the SILVER limit should throw IllegalStateException");

        assertEquals("Cannot rent more books.", exception.getMessage(),
                "Exception message should match");
    }

    @Test
    public void testRemoveRentedBook_Present() {
        customer.addRentedBook(book1);
        assertTrue(customer.getRentedBooks().contains(book1), "Rented books should contain book1");

        customer.removeRentedBook(book1);
        assertFalse(customer.getRentedBooks().contains(book1), "Rented books should not contain book1 after removal");
    }

    @Test
    public void testRemoveRentedBook_NotPresent() {
        // Attempting to remove a book that was never rented
        assertDoesNotThrow(() -> {
            customer.removeRentedBook(book1);
        }, "Removing a non-existent rented book should not throw an exception");
    }

    @Test
    public void testAddPurchasedBook() {
        customer.addPurchasedBook(book1);
        assertTrue(customer.getPurchasedBooks().contains(book1), "Purchased books should contain book1");
    }

    @Test
    public void testRemovePurchasedBook_Present() {
        customer.addPurchasedBook(book1);
        assertTrue(customer.getPurchasedBooks().contains(book1), "Purchased books should contain book1");

        customer.removePurchasedBook(book1);
        assertFalse(customer.getPurchasedBooks().contains(book1), "Purchased books should not contain book1 after removal");
    }

    @Test
    public void testRemovePurchasedBook_NotPresent() {
        // Attempting to remove a book that was never purchased
        assertDoesNotThrow(() -> {
            customer.removePurchasedBook(book1);
        }, "Removing a non-existent purchased book should not throw an exception");
    }

    @Test
    public void testAddReview_PurchasedBook() {
        customer.addPurchasedBook(book1);
        customer.addReview(book1, review1);
        assertTrue(customer.getReviews().contains(review1), "Reviews should contain review1");
    }

    @Test
    public void testAddReview_RentedBook() {
        customer.addRentedBook(book2);
        customer.addReview(book2, review2);
        assertTrue(customer.getReviews().contains(review2), "Reviews should contain review2");
    }

    @Test
    public void testAddReview_NotPurchasedOrRented() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customer.addReview(book1, review1);});

        assertEquals("User has not rented or purchased this book.", exception.getMessage(),
                "Exception message should match");
    }

    @Test
    public void testAddReview_BothPurchasedAndRented() {
        customer.addPurchasedBook(book1);
        customer.addRentedBook(book1); // Book is both purchased and rented
        customer.addReview(book1, review1);
        assertTrue(customer.getReviews().contains(review1), "Reviews should contain review1");
    }
}