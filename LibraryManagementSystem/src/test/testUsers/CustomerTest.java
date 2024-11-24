package test.testUsers;

import main.users.*;
import main.book.*;
import main.authentication.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerTest {
    private Customer customer;
    private Book book;
    private Password password;

    @Before
    public void setUp() {
        password = new Password("testPass123");
        customer = new Customer("testUser", password);
        book = new Book("Test Book", "Test Description");
    }

    @Test
    public void testAddReview_ValidRentedBook() {
        customer.addRentedBook(book);
        Review review = new Review("Good book", 5);
        customer.addReview(book, review);
        assertTrue(customer.getReviews().contains(review));
    }

    @Test
    public void testAddReview_ValidPurchasedBook() {
        customer.addPurchasedBook(book);
        Review review = new Review("Great book", 4);
        customer.addReview(book, review);
        assertTrue(customer.getReviews().contains(review));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddReview_UnownedBook() {
        Review review = new Review("Haven't read", 1);
        customer.addReview(book, review);
    }
}