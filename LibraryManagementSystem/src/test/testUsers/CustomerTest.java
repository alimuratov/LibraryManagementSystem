package test.testUsers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.authentication.Password;
import main.book.Book;
import main.book.Review;
import main.users.Customer;
import main.users.Membership;

public class CustomerTest {

    private Password password;
    private Customer customer;
    private Book book;
    private Review review;

    @BeforeEach
    public void setUp() {
        password = new Password("password123");
        customer = new Customer("testuser", password);
        book = new Book("123-4567890123", "Test Book", "Author", "Publisher", "2024-01-01", "Description", 100.0, 5, 5);
        review = new Review("Great book!", 5);
    }

    @Test
    public void testConstructor() {
        assertNotNull(customer.getMembership());
        assertTrue(customer.getRentedBooks().isEmpty());
        assertTrue(customer.getPurchasedBooks().isEmpty());
        assertTrue(customer.getReviews().isEmpty());
        assertNull(customer.getProfileVector());
    }

    @Test
    public void testGetMembership() {
        Membership membership = customer.getMembership();
        assertNotNull(membership);
        assertEquals("BRONZE", membership.getType());
    }

    @Test
    public void testGetSetProfileVector() {
        Map<String, Double> profileVector = new HashMap<>();
        profileVector.put("Fiction", 0.7);
        profileVector.put("Science", 0.3);
        customer.setProfileVector(profileVector);
        assertEquals(profileVector, customer.getProfileVector());
    }

    @Test
    public void testAddRemoveRentedBook() {
        customer.addRentedBook(book);
        assertTrue(customer.getRentedBooks().contains(book));

        customer.removeRentedBook(book);
        assertFalse(customer.getRentedBooks().contains(book));
    }

    @Test
    public void testAddRemovePurchasedBook() {
        customer.addPurchasedBook(book);
        assertTrue(customer.getPurchasedBooks().contains(book));

        customer.removePurchasedBook(book);
        assertFalse(customer.getPurchasedBooks().contains(book));
    }

    @Test
    public void testAddReview_Success_PurchasedBook() {
        customer.addPurchasedBook(book);
        customer.addReview(book, review);

        assertTrue(customer.getReviews().contains(review));
        assertTrue(book.getReviews().contains(review));
    }

    @Test
    public void testAddReview_Success_RentedBook() {
        customer.addRentedBook(book);
        customer.addReview(book, review);

        assertTrue(customer.getReviews().contains(review));
        assertTrue(book.getReviews().contains(review));
    }

    @Test
    public void testAddReview_Failure() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customer.addReview(book, review);
        });

        String expectedMessage = "User has not rented or purchased this book.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRentBook() {
        customer.rentBook(book);
        assertTrue(customer.getRentedBooks().contains(book));
    }

    @Test
    public void testReturnBook() {
        customer.returnBook(book);
        assertFalse(customer.getRentedBooks().contains(book));
    }

    @Test
    public void testPurchaseBook() {
        customer.purchaseBook(book, "CreditCard");
        assertTrue(customer.getPurchasedBooks().contains(book));
    }

    @Test
    public void testRefundBook() {
        customer.refundBook(book);
        assertFalse(customer.getPurchasedBooks().contains(book));
    }
}
