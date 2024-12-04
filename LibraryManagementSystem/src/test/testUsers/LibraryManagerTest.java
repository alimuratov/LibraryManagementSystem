package test.testUsers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import main.authentication.Password;
import main.book.*;
import main.transaction.Transaction;
import main.users.*;

public class LibraryManagerTest {
    private LibraryManager libraryManager;
    private Customer customer;
    private Customer customer2;
    private Book book;
    private Book book2;
    private Password password;
    
    private Customer bronzeCustomer;
    private Customer silverCustomer;
    private Customer goldCustomer;
    private Book rentableBook;
    private Book salableBook;

    @Before
    public void setUp() throws Exception {
        libraryManager = LibraryManager.getInstance();

        TreeMap<LocalDate, List<RentalRecord>> activeRentals = libraryManager.getActiveRentals();
        activeRentals.clear();

        TreeMap<LocalDate, List<PurchaseRecord>> refundablePurchases = libraryManager.getRefundablePurchases();
        refundablePurchases.clear();

        password = new Password("test123");
        customer = new Customer("TestUser", password);
        customer2 = new Customer("TestUser2", password);
        book = new Book("ISBN123", "Test Book", "Test Author", "Test Publisher", 
                       "2024-01-01", "Test Description", 100.0, 1, 1);
        book2 = new Book("ISBN456", "Another Book", "Another Author", "Another Publisher", 
                        "2024-06-01", "Another Description", 150.0, 2, 2);
    }

    @Test
    public void testRentBookNullValues() {
        libraryManager.rentBook(null, book);
        libraryManager.rentBook(customer, null);
        
        assertTrue(customer.getRentedBooks().isEmpty());
    }

    @Test
    public void testRentBookMaxLimit() {
        Book book1 = new Book("ISBN1", "Book1", "Author", "Publisher", "2024-01-01", "Description", 100.0, 1, 1);
        Book book2 = new Book("ISBN2", "Book2", "Author", "Publisher", "2024-01-01", "Description", 100.0, 1, 1);
        Book book3 = new Book("ISBN3", "Book3", "Author", "Publisher", "2024-01-01", "Description", 100.0, 1, 1);
        
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
        assertFalse(customer2.getRentedBooks().contains(rentBook));
        
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

        
        TreeMap<LocalDate, List<RentalRecord>> activeRentals = libraryManager.getActiveRentals();

        // Add an entry with an empty list
        activeRentals.put(dateWithEmptyRecords, new ArrayList<>());

        // Now call processReturns with the date that has an empty list
        libraryManager.processReturns(dateWithEmptyRecords);

        // Assert that no exceptions occur and the method handles empty records
        // Optionally, you can check if the entry has been removed after processing
        assertFalse(activeRentals.containsKey(dateWithEmptyRecords));
    }

    @Test
    public void testRefundBookNullValues() {
        libraryManager.refundBook(null, book);
        libraryManager.refundBook(customer, null);
        assertTrue(customer.getPurchasedBooks().isEmpty());
    }


    @Test
    public void testRefundBookRefundPeriodExpired() throws Exception {
        libraryManager.purchaseBook(customer, book, "CreditCard");
        
        TreeMap<LocalDate, List<PurchaseRecord>> refundablePurchases = libraryManager.getRefundablePurchases();

        LocalDate purchaseDate = LocalDate.now().minusDays(8);
        LocalDate refundExpiryDate = purchaseDate.plusDays(7);

        // Manually set the purchaseRecord's purchaseDate and refundExpiryDate
        List<PurchaseRecord> records = refundablePurchases.getOrDefault(refundExpiryDate, new ArrayList<>());
        PurchaseRecord expiredRecord = null;
        for (PurchaseRecord pr : records) {
            if (pr.getCustomer().equals(customer) && pr.getBook().equals(book)) {
                expiredRecord = pr;
                break;
            }
        }
        if (expiredRecord == null) {
            Transaction transaction = new Transaction("txn-expired", 100.0, new main.transaction.CreditCardPayment(), new main.transaction.CreditCardRefund());
            expiredRecord = new PurchaseRecord(customer, book, transaction, "CreditCard");
            // Normally, the constructor sets the purchaseDate to now, so we need to adjust it
            Field purchaseDateField = PurchaseRecord.class.getDeclaredField("purchaseDate");
            purchaseDateField.setAccessible(true);
            purchaseDateField.set(expiredRecord, purchaseDate);

            Field refundExpiryDateField = PurchaseRecord.class.getDeclaredField("refundExpiryDate");
            refundExpiryDateField.setAccessible(true);
            refundExpiryDateField.set(expiredRecord, refundExpiryDate);

            records.add(expiredRecord);
            refundablePurchases.put(refundExpiryDate, records);
        }

        // Attempt to refund after the refund period
        libraryManager.refundBook(customer, book);

        // Assertions
        assertTrue(customer.getPurchasedBooks().contains(book));
        // Assuming that the refund should not have been processed, check if the purchase is still present
        assertTrue(refundablePurchases.containsKey(refundExpiryDate));
    }

    /**
     * Test refundBook when the refund has already been processed.
     */
    @Test
    public void testRefundBookAlreadyRefunded() throws Exception {
        // Simulate purchasing a book
        libraryManager.purchaseBook(customer, book, "CreditCard");

        TreeMap<LocalDate, List<PurchaseRecord>> refundablePurchases = libraryManager.getRefundablePurchases();

        LocalDate refundExpiryDate = LocalDate.now().plusDays(7);
        List<PurchaseRecord> records = refundablePurchases.get(refundExpiryDate);
        assertNotNull(records);
        assertFalse(records.isEmpty());

        PurchaseRecord record = records.get(0);
        libraryManager.refundBook(customer, book);
        libraryManager.refundBook(customer, book);
        assertFalse(customer.getPurchasedBooks().contains(book));
        assertFalse(records.contains(record));
    }

    
    /**
     * Test processRefunds with expired refunds.
     */
    @Test
    public void testProcessRefundsWithExpired() throws Exception {
        // Simulate purchasing a book
        libraryManager.purchaseBook(customer, book, "CreditCard");

        TreeMap<LocalDate, List<PurchaseRecord>> refundablePurchases = libraryManager.getRefundablePurchases();

        // Assume purchase date was 8 days ago to expire the refund period (7 days)
        LocalDate purchaseDate = LocalDate.now().minusDays(8);
        LocalDate refundExpiryDate = purchaseDate.plusDays(7);

        // Manually set the purchaseRecord's purchaseDate and refundExpiryDate
        List<PurchaseRecord> records = refundablePurchases.getOrDefault(refundExpiryDate, new ArrayList<>());
        PurchaseRecord expiredRecord = null;
        for (PurchaseRecord pr : records) {
            if (pr.getCustomer().equals(customer) && pr.getBook().equals(book)) {
                expiredRecord = pr;
                break;
            }
        }
        if (expiredRecord == null) {
            // Create a new expired PurchaseRecord
            Transaction transaction = new Transaction("txn-expired", 100.0, new main.transaction.CreditCardPayment(), new main.transaction.CreditCardRefund());
            expiredRecord = new PurchaseRecord(customer, book, transaction, "CreditCard");
            // Normally, the constructor sets the purchaseDate to now, so we need to adjust it
            Field purchaseDateField = PurchaseRecord.class.getDeclaredField("purchaseDate");
            purchaseDateField.setAccessible(true);
            purchaseDateField.set(expiredRecord, purchaseDate);

            Field refundExpiryDateField = PurchaseRecord.class.getDeclaredField("refundExpiryDate");
            refundExpiryDateField.setAccessible(true);
            refundExpiryDateField.set(expiredRecord, refundExpiryDate);

            records.add(expiredRecord);
            refundablePurchases.put(refundExpiryDate, records);
        }

        // Process refunds with currentDate after the refundExpiryDate
        LocalDate currentDate = refundExpiryDate.plusDays(1);
        libraryManager.processRefunds(currentDate);

        // Assertions
        // The expired refund should have been processed (in this case, just logged)
        // Since the refund period has expired, no action should be taken
        // Check that the record is cleared from refundablePurchases
        assertFalse(refundablePurchases.containsKey(refundExpiryDate));
    }

    /**
     * Test refundBook with multiple customers and books.
     */
    @Test
    public void testRefundBookMultipleCustomersBooks() {
        // Simulate purchases
        libraryManager.purchaseBook(customer, book, "CreditCard");
        libraryManager.purchaseBook(customer2, book, "WeChat");
        libraryManager.purchaseBook(customer, book2, "CreditCard");
        libraryManager.purchaseBook(customer2, book2, "WeChat");

        // Refund for customer
        libraryManager.refundBook(customer, book);
        libraryManager.refundBook(customer, book2);

        // Refund for customer2
        libraryManager.refundBook(customer2, book);
        libraryManager.refundBook(customer2, book2);

        // Assertions
        assertFalse(customer.getPurchasedBooks().contains(book));
        assertFalse(customer.getPurchasedBooks().contains(book2));
        assertFalse(customer2.getPurchasedBooks().contains(book));
        assertFalse(customer2.getPurchasedBooks().contains(book2));
    }

    @Test
    public void testRefundBookCustomerEqualsFalse() throws Exception {
        libraryManager.purchaseBook(customer, book, "CreditCard");
        libraryManager.refundBook(customer2, book);

        assertTrue(customer.getPurchasedBooks().contains(book));
        assertFalse(customer2.getPurchasedBooks().contains(book));

        PurchaseRecord record = null;
        
		TreeMap<LocalDate, List<PurchaseRecord>> refundablePurchases = libraryManager.getRefundablePurchases();
        for (Map.Entry<LocalDate, List<PurchaseRecord>> entry : refundablePurchases.entrySet()) {
            for (PurchaseRecord pr : entry.getValue()) {
                if (pr.getCustomer().equals(customer) && pr.getBook().equals(book)) {
                	record = pr;
                }
            }
        }

        assertNotNull(record);
    }
    
    @Test
    public void testBronzeToSilver() {
        Membership membership = customer.getMembership();
        assertEquals("BRONZE", membership.getState().getType());
        assertEquals(0, membership.getCurrentXP());

        // Upwards
        book = new Book("ISBN123", "Test Book", "Test Author", "Test Publisher", 
                "2024-01-01", "Test Description", 100.0, 1, 8);
        libraryManager.rentBook(customer, book);
        libraryManager.returnBook(customer, book); 
        libraryManager.purchaseBook(customer, book2, "CreditCard"); 

        assertEquals(0, customer.getRentedBooks().size());
        assertEquals(1, customer.getPurchasedBooks().size());
        assertEquals(30, membership.getCurrentXP());
        assertEquals("BRONZE", membership.getState().getType());

        
        for (int i = 0; i < 7; i++) {
            libraryManager.purchaseBook(customer, book, "CreditCard"); 
        }
        assertEquals(8, customer.getPurchasedBooks().size());
        assertEquals(70, membership.getCurrentXP());
        assertEquals("SILVER", membership.getState().getType());
        
        // Downwards
        for (int i = 0; i < 5; i++) {
            libraryManager.refundBook(customer, book); 
        }
        assertEquals(70, membership.getCurrentXP());
        assertEquals("BRONZE", membership.getState().getType());
    }
    
    // Membership Simulation
    @Test
    public void testBronzeToGold() {
        Membership membership = customer.getMembership();
        assertEquals("BRONZE", membership.getState().getType());
        assertEquals(0, membership.getCurrentXP());

        // Upwards
        book = new Book("ISBN123", "Test Book", "Test Author", "Test Publisher", 
                "2024-01-01", "Test Description", 100.0, 0, 5);
        for (int i = 0; i < 5; i++) {
            libraryManager.purchaseBook(customer, book, "CreditCard");
        }

        assertEquals("SILVER", membership.getState().getType());
        assertEquals(0, membership.getCurrentXP());

        book2 = new Book("ISBN456", "Another Book", "Another Author", "Another Publisher", 
                "2024-06-01", "Another Description", 150.0, 0, 14);
        for (int i = 0; i < 13; i++) { 
            libraryManager.purchaseBook(customer, book2, "CreditCard");
        }

        assertEquals("GOLD", membership.getState().getType());
        assertEquals(250, membership.getCurrentXP());
        
        libraryManager.purchaseBook(customer, book2, "CreditCard");
        assertEquals("GOLD", membership.getState().getType());
        assertEquals(250, membership.getCurrentXP());
        
     // Downwards
        for (int i = 0; i < 12; i++) {
            libraryManager.refundBook(customer, book2); 
        }
        
        assertEquals("SILVER", membership.getState().getType());
        assertEquals(10, membership.getCurrentXP());
        

        libraryManager.refundBook(customer, book2); 
        assertEquals("BRONZE", membership.getState().getType());
        assertEquals(90, membership.getCurrentXP());
        
        for (int i = 0; i < 2; i++) {
            libraryManager.refundBook(customer, book); 
        }
        assertEquals("BRONZE", membership.getState().getType());
        assertEquals(50, membership.getCurrentXP());
    }
    
    @Test
    public void testRentingWaitlistAdditionAndPriority() {
    	password = new Password("test123");
	    bronzeCustomer = new Customer("BronzeUser", password);   // Bronze
	    silverCustomer = new Customer("SilverUser", password);   // Silver
	    goldCustomer = new Customer("GoldUser", password);       // Gold

	    for (int purchase = 0; purchase < 10; purchase++) {
	    	silverCustomer.getMembership().addXP(20);
	    }
	    
	    for (int purchase = 0; purchase < 18; purchase++) {
	    	goldCustomer.getMembership().addXP(20);
	    }
	    
        rentableBook = new Book("ISBN-R", "Rentable Book", "Author A", "Publisher A", 
                "2024-01-01", "Description A", 50.0, 1, 5);
		salableBook = new Book("ISBN-S", "Salable Book", "Author B", "Publisher B", 
		               "2024-06-01", "Description B", 80.0, 3, 1);
		
        assertTrue(rentableBook.isRentable());

        libraryManager.rentBook(bronzeCustomer, rentableBook);
        assertTrue(bronzeCustomer.getRentedBooks().contains(rentableBook));
        assertEquals(0, rentableBook.getRentableCopies());


        libraryManager.rentBook(silverCustomer, rentableBook);
        libraryManager.rentBook(goldCustomer, rentableBook);

        assertFalse(silverCustomer.getRentedBooks().contains(rentableBook));
        assertFalse(goldCustomer.getRentedBooks().contains(rentableBook));

        List<Customer> expectedWaitlistOrder = new ArrayList<>();
        expectedWaitlistOrder.add(goldCustomer);
        expectedWaitlistOrder.add(silverCustomer);
        assertEquals(expectedWaitlistOrder, new ArrayList<>(rentableBook.getRentingWaitList()));
    }

    @Test
    public void testRentingWaitlistNotificationAndProcessing() {
    	password = new Password("test123");
	    bronzeCustomer = new Customer("BronzeUser", password);   // Bronze
	    silverCustomer = new Customer("SilverUser", password);   // Silver
	    goldCustomer = new Customer("GoldUser", password);       // Gold

	    for (int purchase = 0; purchase < 10; purchase++) {
	    	silverCustomer.getMembership().addXP(20);
	    }
	    
	    for (int purchase = 0; purchase < 18; purchase++) {
	    	goldCustomer.getMembership().addXP(20);
	    }
        rentableBook = new Book("ISBN-R", "Rentable Book", "Author A", "Publisher A", 
                "2024-01-01", "Description A", 50.0, 1, 5);
		salableBook = new Book("ISBN-S", "Salable Book", "Author B", "Publisher B", 
		               "2024-06-01", "Description B", 80.0, 3, 1);
    	
        libraryManager.rentBook(bronzeCustomer, rentableBook);
        assertTrue(bronzeCustomer.getRentedBooks().contains(rentableBook));
        assertEquals(0, rentableBook.getRentableCopies());

        libraryManager.rentBook(silverCustomer, rentableBook);
        libraryManager.rentBook(goldCustomer, rentableBook);

        libraryManager.returnBook(bronzeCustomer, rentableBook);
        assertTrue(goldCustomer.getRentedBooks().contains(rentableBook));
        assertEquals(0, rentableBook.getRentableCopies());

        libraryManager.returnBook(goldCustomer, rentableBook);
        assertTrue(silverCustomer.getRentedBooks().contains(rentableBook));
        assertEquals(0, rentableBook.getRentableCopies());

        assertTrue(rentableBook.getRentingWaitList().isEmpty());
    }

    @Test
    public void testSellingWaitlistAdditionAndPriority() {
    	password = new Password("test123");
	    bronzeCustomer = new Customer("BronzeUser", password);   // Bronze
	    silverCustomer = new Customer("SilverUser", password);   // Silver
	    goldCustomer = new Customer("GoldUser", password);       // Gold

	    silverCustomer.getMembership().addXP(100);
	    goldCustomer.getMembership().addXP(350);
        rentableBook = new Book("ISBN-R", "Rentable Book", "Author A", "Publisher A", 
                "2024-01-01", "Description A", 50.0, 1, 5);
		salableBook = new Book("ISBN-S", "Salable Book", "Author B", "Publisher B", 
		               "2024-06-01", "Description B", 80.0, 3, 1);
		
        assertTrue(salableBook.isSalable());

        libraryManager.purchaseBook(goldCustomer, salableBook, "CreditCard");
        assertTrue(goldCustomer.getPurchasedBooks().contains(salableBook));
        assertEquals(0, salableBook.getSaleableCopies());

        libraryManager.purchaseBook(silverCustomer, salableBook, "WeChat");
        libraryManager.purchaseBook(bronzeCustomer, salableBook, "CreditCard");


        assertFalse(silverCustomer.getPurchasedBooks().contains(salableBook));
        assertFalse(bronzeCustomer.getPurchasedBooks().contains(salableBook));

        List<Customer> expectedWaitlistOrder = new ArrayList<>();
        expectedWaitlistOrder.add(silverCustomer);
        expectedWaitlistOrder.add(bronzeCustomer);
        assertEquals(expectedWaitlistOrder, new ArrayList<>(salableBook.getSellingWaitList()));
    }

    @Test
    public void testSellingWaitlistNotificationAndProcessing() {
    	password = new Password("test123");
	    bronzeCustomer = new Customer("BronzeUser", password);   // Bronze
	    silverCustomer = new Customer("SilverUser", password);   // Silver
	    goldCustomer = new Customer("GoldUser", password);       // Gold

	    silverCustomer.getMembership().addXP(100);
	    goldCustomer.getMembership().addXP(350);
        rentableBook = new Book("ISBN-R", "Rentable Book", "Author A", "Publisher A", 
                "2024-01-01", "Description A", 50.0, 1, 5);
		salableBook = new Book("ISBN-S", "Salable Book", "Author B", "Publisher B", 
		               "2024-06-01", "Description B", 80.0, 3, 1);
		
        libraryManager.purchaseBook(goldCustomer, salableBook, "CreditCard");
        assertTrue(goldCustomer.getPurchasedBooks().contains(salableBook));
        assertEquals(0, salableBook.getSaleableCopies());

        libraryManager.purchaseBook(silverCustomer, salableBook, "WeChat");
        libraryManager.purchaseBook(bronzeCustomer, salableBook, "CreditCard");

        libraryManager.refundBook(goldCustomer, salableBook);
        assertTrue(silverCustomer.getPurchasedBooks().contains(salableBook));
        assertEquals(0, salableBook.getSaleableCopies());

        libraryManager.refundBook(silverCustomer, salableBook);
        assertTrue(bronzeCustomer.getPurchasedBooks().contains(salableBook));
        assertEquals(0, salableBook.getSaleableCopies());

        assertTrue(salableBook.getSellingWaitList().isEmpty());
    }

    @Test
    public void testMultipleCopiesWaitlistHandling() {
    	password = new Password("test123");
	    bronzeCustomer = new Customer("BronzeUser", password);   // Bronze
	    silverCustomer = new Customer("SilverUser", password);   // Silver
	    goldCustomer = new Customer("GoldUser", password);       // Gold

	    silverCustomer.getMembership().addXP(100);
	    goldCustomer.getMembership().addXP(350);
        rentableBook = new Book("ISBN-R", "Rentable Book", "Author A", "Publisher A", 
                "2024-01-01", "Description A", 50.0, 1, 5);
		salableBook = new Book("ISBN-S", "Salable Book", "Author B", "Publisher B", 
		               "2024-06-01", "Description B", 80.0, 3, 1);
		
        rentableBook.setRentableCopies(2);
        assertTrue(rentableBook.isRentable());

        libraryManager.rentBook(bronzeCustomer, rentableBook);
        libraryManager.rentBook(silverCustomer, rentableBook);
        assertTrue(bronzeCustomer.getRentedBooks().contains(rentableBook));
        assertTrue(silverCustomer.getRentedBooks().contains(rentableBook));
        assertEquals(0, rentableBook.getRentableCopies());

        libraryManager.rentBook(goldCustomer, rentableBook);
        assertFalse(goldCustomer.getRentedBooks().contains(rentableBook));
        assertEquals(1, rentableBook.getRentingWaitList().size());

        libraryManager.returnBook(bronzeCustomer, rentableBook);
        assertTrue(goldCustomer.getRentedBooks().contains(rentableBook));
        assertEquals(0, rentableBook.getRentableCopies());

        assertTrue(rentableBook.getRentingWaitList().isEmpty());
    }
}