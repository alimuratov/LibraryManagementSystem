package test.testBook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import main.users.*;
import main.book.*;
import main.authentication.*;

class RentalRecordTest {
    private Customer customer;
    private Book book;
    private RentalRecord rentalRecord;

    @BeforeEach
    void setUp() {
        // Create test objects
        customer = new Customer("testUser", new Password("password123"));
        book = new Book("Test Book", "Test Description");
        rentalRecord = new RentalRecord(customer, book);
    }

    @Test
    void constructor_WithValidInputs_CreatesRecord() {
        assertNotNull(rentalRecord);
        assertEquals(customer, rentalRecord.getCustomer());
        assertEquals(book, rentalRecord.getBook());
        assertEquals(LocalDate.now(), rentalRecord.getRentalDate());
        assertFalse(rentalRecord.isReturned());
    }

    @Test
    void constructor_WithNullCustomer_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RentalRecord(null, book);
        });
    }

    @Test
    void constructor_WithNullBook_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RentalRecord(customer, null);
        });
    }

    @Test
    void getReturnDate_CalculatesCorrectly() {
        LocalDate expectedReturnDate = LocalDate.now().plusDays(
            customer.getMembership().getRentalDays()
        );
        assertEquals(expectedReturnDate, rentalRecord.getReturnDate());
    }

    @Test
    void markAsReturned_ChangesStatus() {
        assertFalse(rentalRecord.isReturned());
        rentalRecord.markAsReturned();
        assertTrue(rentalRecord.isReturned());
    }

    @Test
    void toString_ContainsAllInformation() {
        String result = rentalRecord.toString();
        assertTrue(result.contains(customer.toString()));
        assertTrue(result.contains(book.getBookTitle()));
        assertTrue(result.contains(LocalDate.now().toString()));
        assertTrue(result.contains(rentalRecord.getReturnDate().toString()));
    }
}