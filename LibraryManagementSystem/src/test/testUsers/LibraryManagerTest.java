package test.testUsers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import main.authentication.Password;
import main.book.*;
import main.users.*;


public class LibraryManagerTest {
    private LibraryManager libraryManager;
    private Customer customer;
    private Book book;
    private Password password;

    @Before
    public void setUp() {
        // Reset singleton instance before each test
        libraryManager = new LibraryManager();
        password = new Password("test123");
        customer = new Customer("TestUser", password);
        book = new Book("ISBN123", "Test Book", "Test Author", "Test Publisher", 
                       "2024-01-01", "Test Description", 100.0, 1, 1);
    }

    @Test
    public void testRentBookNullValues() {
        libraryManager.rentBook(null, book);
        libraryManager.rentBook(customer, null);
        
        assertTrue(customer.getRentedBooks().isEmpty());
    }

    @Test
    public void testRentBookMaxLimit() {
        // Set up books and rentals to hit membership limit
        Book book1 = new Book("ISBN1", "Book1", "Author", "Publisher", "2024-01-01", "Description", 100.0, 1, 1);
        Book book2 = new Book("ISBN2", "Book2", "Author", "Publisher", "2024-01-01", "Description", 100.0, 1, 1);
        Book book3 = new Book("ISBN3", "Book3", "Author", "Publisher", "2024-01-01", "Description", 100.0, 1, 1);
        
        // Bronze membership allows 2 books
        libraryManager.rentBook(customer, book1);
        libraryManager.rentBook(customer, book2);
        libraryManager.rentBook(customer, book3); // Should fail
        
        assertEquals(2, customer.getRentedBooks().size());
    }

    @Test
    public void testReturnBookBasicFlow() {
        libraryManager.rentBook(customer, book);
        int initialRentableCopies = book.getRentableCopies();
        
        libraryManager.returnBook(customer, book);
        
        assertFalse(customer.getRentedBooks().contains(book));
        assertEquals(initialRentableCopies + 1, book.getRentableCopies());
    }

    @Test
    public void testReturnBookNullValues() {
        libraryManager.returnBook(null, book);
        libraryManager.returnBook(customer, null);
        // Should not throw exceptions
    }
    
    @Test
    public void testProcessReturnsEmpty() {
        LocalDate currentDate = LocalDate.now();
        libraryManager.processReturns(currentDate);
        // Should handle empty returns without exceptions
    }

    @Test
    public void testProcessReturnsWithBooks() {
        libraryManager.rentBook(customer, book);
        LocalDate returnDate = LocalDate.now().plusDays(customer.getMembership().getRentalDays());
        
        libraryManager.processReturns(returnDate);
        
        assertFalse(customer.getRentedBooks().contains(book));
        assertTrue(book.isRentable());
    }

    @Test
    public void testPurchaseBookNullValues() {
        libraryManager.purchaseBook(null, book, "CreditCard");
        libraryManager.purchaseBook(customer, null, "CreditCard");
        
        assertTrue(customer.getPurchasedBooks().isEmpty());
    }

    @Test
    public void testWaitlistProcessing() {
        // Test renting waitlist
        Book rentBook = new Book("ISBN-R", "RentBook", "Author", "Publisher", 
                               "2024-01-01", "Description", 100.0, 0, 1);
        Customer customer2 = new Customer("TestUser2", password);
        
        libraryManager.rentBook(customer, rentBook);
        libraryManager.rentBook(customer2, rentBook);
        
        rentBook.setRentableCopies(1);
        libraryManager.processRentingWaitlist(rentBook);
        
        assertTrue(customer.getRentedBooks().contains(rentBook));
        
        // Test selling waitlist
        Book saleBook = new Book("ISBN-S", "SaleBook", "Author", "Publisher", 
                                "2024-01-01", "Description", 100.0, 1, 0);
        
        libraryManager.purchaseBook(customer, saleBook, "CreditCard");
        libraryManager.purchaseBook(customer2, saleBook, "CreditCard");
        
        saleBook.setSaleableCopies(1);
        libraryManager.processSellingWaitlist(saleBook, "CreditCard");
        
        assertTrue(customer.getPurchasedBooks().contains(saleBook));
    }

    @Test
    public void testReturnBookMissedBranchesComplex() {
        // Setup multiple customers and books
        Customer customer2 = new Customer("TestUser2", password);
        Book book1 = new Book("ISBN1", "Book1", "Author", "Publisher", 
                             "2024-01-01", "Description", 100.0, 2, 1);
        Book book2 = new Book("ISBN2", "Book2", "Author", "Publisher", 
                             "2024-01-01", "Description", 100.0, 2, 1);

        // Create multiple rental records on different dates
        libraryManager.rentBook(customer, book1);  // First rental
        libraryManager.rentBook(customer2, book2); // Second rental with different customer
        libraryManager.rentBook(customer, book2);  // Third rental

        // Now try different return scenarios
        libraryManager.returnBook(customer2, book1);  // Try returning a book customer2 hasn't rented
        libraryManager.returnBook(customer, book2);   // Return a valid rental
        libraryManager.returnBook(customer, book2);   // Try returning the same book again

        // Verify final states
        assertFalse(customer.getRentedBooks().contains(book2));
        assertTrue(customer.getRentedBooks().contains(book1));
        assertTrue(customer2.getRentedBooks().contains(book2));
    }
    
    @Test
    public void testProcessSellingWaitlistSalableButEmptyWaitlist() {
        Book saleBook = new Book("ISBN-S", "SaleBook", "Author", "Publisher", 
                                "2024-01-01", "Description", 100.0, 1, 1); // Salable copies > 0
        // Ensure the selling waitlist is empty
        assertTrue(saleBook.getSellingWaitList().isEmpty());
        libraryManager.processSellingWaitlist(saleBook, "CreditCard");
        // Assert that no exceptions are thrown and the method handles the empty waitlist
    }

    @Test
    public void testProcessReturnsWithEmptyRecords() throws Exception {
        LocalDate dateWithEmptyRecords = LocalDate.now().plusDays(1);

        // Use reflection to access the private 'activeRentals' field
        Field field = LibraryManager.class.getDeclaredField("activeRentals");
        field.setAccessible(true);

        @SuppressWarnings("unchecked")
        TreeMap<LocalDate, List<RentalRecord>> activeRentals = (TreeMap<LocalDate, List<RentalRecord>>) field.get(libraryManager);

        // Add an entry with an empty list
        activeRentals.put(dateWithEmptyRecords, new ArrayList<>());

        // Now call processReturns with the date that has an empty list
        libraryManager.processReturns(dateWithEmptyRecords);

        // Assert that no exceptions occur and the method handles empty records
        // Optionally, you can check if the entry has been removed after processing
        assertFalse(activeRentals.containsKey(dateWithEmptyRecords));
    }
    
    @Test
    public void testReturnBookAlreadyReturnedRecordStillInActiveRentals() throws Exception {
        // Rent the book
        libraryManager.rentBook(customer, book);

        Field field = LibraryManager.class.getDeclaredField("activeRentals");
        field.setAccessible(true);

        @SuppressWarnings("unchecked")
        TreeMap<LocalDate, List<RentalRecord>> activeRentals = (TreeMap<LocalDate, List<RentalRecord>>) field.get(libraryManager);

        // Find the rental record for our customer and book
        RentalRecord rentalRecord = null;
        for (List<RentalRecord> records : activeRentals.values()) {
            for (RentalRecord record : records) {
                if (record.getCustomer().equals(customer) && record.getBook().equals(book)) {
                    rentalRecord = record;
                    break;
                }
            }
            if (rentalRecord != null) {
                break;
            }
        }

     // Ensure the rental record was found
        assertNotNull("Rental record should not be null", rentalRecord);

        // Mark the rental record as returned
        rentalRecord.markAsReturned();

        // Manually update the customer's rented books and the book's rentable copies to reflect the return
        customer.removeRentedBook(book);
        book.setRentableCopies(book.getRentableCopies() + 1);

        // Now, attempt to return the book again
        libraryManager.returnBook(customer, book);

        // Assertions
        // The customer's rented books should still not contain the book
        assertFalse(customer.getRentedBooks().contains(book));

        // The rentable copies should remain the same as before the second return attempt
        assertEquals(1, book.getRentableCopies());
    }

    
}