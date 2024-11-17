package test.testUsers;

import static org.junit.Assert.*;
import org.junit.Test;
import main.users.*;
import main.book.*;
import main.system.Password;

public class CustomerTest {

    // Test adding a rented book
    @Test
    public void testAddRentedBook() {
        // Arrange
        Password password = new Password("password123");
        Customer customer = new Customer("Eve", password);
        Book book = new Book("The Pragmatic Programmer", "Andrew Hunt");

        // Act
        customer.addRentedBook(book);

        // Assert
        assertTrue(customer.getRentedBooks().contains(book));
    }

    // Test removing a rented book
    @Test
    public void testRemoveRentedBook() {
        // Arrange
        Password password = new Password("password123");
        Customer customer = new Customer("Frank", password);
        Book book = new Book("Head First Design Patterns", "Eric Freeman");
        customer.addRentedBook(book);

        // Act
        customer.removeRentedBook(book);

        // Assert
        assertFalse(customer.getRentedBooks().contains(book));
    }

    // Test adding a review for a book the customer has rented
    @Test
    public void testAddReview_RentedBook() {
        // Arrange
        Password password = new Password("password123");
        Customer customer = new Customer("Grace", password);
        Book book = new Book("Test-Driven Development", "Kent Beck");
        Review review = new Review(customer, "Great book!", 5);
        customer.addRentedBook(book);

        // Act
        customer.addReview(book, review);

        // Assert
        assertTrue(customer.getReviews().contains(review));
    }

    // Test adding a review for a book the customer has not rented or purchased
    @Test(expected = IllegalArgumentException.class)
    public void testAddReview_NotOwnedBook() {
        // Arrange
        Password password = new Password("password123");
        Customer customer = new Customer("Heidi", password);
        Book book = new Book("Domain-Driven Design", "Eric Evans");
        Review review = new Review(customer, "Informative read.", 4);

        // Act
        customer.addReview(book, review);

        // Assert is handled by the expected exception
    }
}