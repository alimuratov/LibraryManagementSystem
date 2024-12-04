package test.testBook;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.book.*;
import main.users.*;
import main.authentication.Password;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.lang.reflect.Field;

class TestRentalRecord {
    private Customer bronzeCustomer;
    private Customer silverCustomer;
    private Customer goldCustomer;
    private Book book;
    private RentalRecord bronzeRecord;
    private RentalRecord silverRecord;
    private RentalRecord goldRecord;
    
    @BeforeEach
    void setUp() {
        // Initialize customers with different membership levels
        bronzeCustomer = new Customer("bronze", new Password("pass123"));
        silverCustomer = new Customer("silver", new Password("pass123"));
        goldCustomer = new Customer("gold", new Password("pass123"));
        
        // Set up membership levels
        silverCustomer.getMembership().addXP(100); // Upgrade to Silver
        goldCustomer.getMembership().addXP(100);   // To Silver first
        goldCustomer.getMembership().addXP(250);   // Then to Gold
        
        // Initialize book
        book = new Book(
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
        
        // Create rental records for different membership levels
        bronzeRecord = new RentalRecord(bronzeCustomer, book);
        silverRecord = new RentalRecord(silverCustomer, book);
        goldRecord = new RentalRecord(goldCustomer, book);
    }
    
    @Test
    void testConstructorValidation() {
        // Test null customer
        assertThrows(IllegalArgumentException.class, 
            () -> new RentalRecord(null, book));
        
        // Test null book
        assertThrows(IllegalArgumentException.class, 
            () -> new RentalRecord(bronzeCustomer, null));
    }
    
    @Test
    void testRentalDurationsByMembership() {
        // Test Bronze membership (14 days)
        assertEquals(14, ChronoUnit.DAYS.between(bronzeRecord.getRentalDate(), bronzeRecord.getReturnDate()));
        
        // Test Silver membership (21 days)
        assertEquals(21, ChronoUnit.DAYS.between(silverRecord.getRentalDate(), silverRecord.getReturnDate()));
        
        // Test Gold membership (30 days)
        assertEquals(30, ChronoUnit.DAYS.between(goldRecord.getRentalDate(), goldRecord.getReturnDate()));
    }
    
    @Test
    void testGetters() {
        // Test bronzeRecord getters
        assertEquals(bronzeCustomer, bronzeRecord.getCustomer());
        assertEquals(book, bronzeRecord.getBook());
        assertEquals(LocalDate.now(), bronzeRecord.getRentalDate());
        assertFalse(bronzeRecord.isReturned());
        
        // Test return status management
        bronzeRecord.markAsReturned();
        assertTrue(bronzeRecord.isReturned());
    }
    
    @Test
    void testToString() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Test normal case with return date
        String normalString = bronzeRecord.toString();
        assertTrue(normalString.contains(bronzeCustomer.toString()));
        assertTrue(normalString.contains(book.getBookTitle()));
        assertTrue(normalString.contains(LocalDate.now().format(formatter)));
        assertTrue(normalString.contains(bronzeRecord.getReturnDate().format(formatter)));
        
        // Test case with null return date using reflection
        Field returnDateField = RentalRecord.class.getDeclaredField("returnDate");
        returnDateField.setAccessible(true);
        returnDateField.set(bronzeRecord, null);
        
        String nullReturnString = bronzeRecord.toString();
        assertTrue(nullReturnString.contains("Not Returned"));
    }
    
    @Test
    void testRentalRecordIntegration() {
        // Test integration with Book
        assertEquals("Test Book", bronzeRecord.getBook().getBookTitle());
        assertEquals(29.99, bronzeRecord.getBook().getBookPrice());
        
        // Test integration with Customer
        assertEquals("bronze", bronzeRecord.getCustomer().getUserName());
        assertEquals(14, bronzeRecord.getCustomer().getMembership().getRentalDays());
        
        // Test integration with Membership through Customer
        assertEquals(14, ChronoUnit.DAYS.between(bronzeRecord.getRentalDate(), bronzeRecord.getReturnDate()));
        assertEquals(21, ChronoUnit.DAYS.between(silverRecord.getRentalDate(), silverRecord.getReturnDate()));
        assertEquals(30, ChronoUnit.DAYS.between(goldRecord.getRentalDate(), goldRecord.getReturnDate()));
    }
}