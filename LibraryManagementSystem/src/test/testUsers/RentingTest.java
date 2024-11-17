package test.testUsers;

import static org.junit.Assert.*;
import org.junit.Test;
import main.users.*;
import main.book.*;
import main.system.Password;
import java.time.LocalDate;

public class RentingTest {

    // Test when a book is available for rent
    @Test
    public void testRentBook_AvailableCopy() {
        // Arrange
        Password password = new Password("password123");
        Customer customer = new Customer("Alice", password);
        Book book = new Book("Effective Java", "Joshua Bloch");
        RentalBookCopy copy = new RentalBookCopy();
        book.addRentalCopy(copy);

        Renting renting = new Renting(customer, book);

        // Act
        boolean result = renting.rentBook();

        // Assert
        assertTrue(result);
        assertTrue(customer.getRentedBooks().contains(book));
    }

    // Test when a book is not available for rent
    @Test
    public void testRentBook_NoAvailableCopy() {
        // Arrange
        Password password = new Password("password123");
        Customer customer = new Customer("Bob", password);
        Book book = new Book("Clean Code", "Robert C. Martin");
        // No rental copies added

        Renting renting = new Renting(customer, book);

        // Act
        boolean result = renting.rentBook();

        // Assert
        assertFalse(result);
        assertFalse(customer.getRentedBooks().contains(book));
    }

    // Test returning a book that is currently rented by the customer
    @Test
    public void testReturnBook_BookRented() {
        // Arrange
        Password password = new Password("password123");
        Customer customer = new Customer("Charlie", password);
        Book book = new Book("Design Patterns", "Erich Gamma");
        customer.addRentedBook(book);

        Renting renting = new Renting(customer, book);

        // Act
        boolean result = renting.returnBook();

        // Assert
        assertTrue(result);
        assertFalse(customer.getRentedBooks().contains(book));
    }

    // Test returning a book that is not rented by the customer
    @Test
    public void testReturnBook_BookNotRented() {
        // Arrange
        Password password = new Password("password123");
        Customer customer = new Customer("Dave", password);
        Book book = new Book("Refactoring", "Martin Fowler");
        // Book not added to rentedBooks

        Renting renting = new Renting(customer, book);

        // Act
        boolean result = renting.returnBook();

        // Assert
        assertFalse(result);
    }
}
