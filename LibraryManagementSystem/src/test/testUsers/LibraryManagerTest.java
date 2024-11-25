package test.testUsers;

import main.users.*;
import main.book.Book;
import main.book.RentalRecord;
import main.transaction.PaymentFactory;
import main.transaction.PaymentMethod;
import main.transaction.Transaction;
import main.authentication.Password;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

public class LibraryManagerTest {

    private LibraryManager libraryManager;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @Before
    public void setUp() throws Exception {
        // Reset the singleton instance before each test
        Field instance = LibraryManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        libraryManager = LibraryManager.getInstance();

        // Capture System.out output
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    public void testRentBook_NullCustomerOrBook() {
        Customer customer = null;
        Book book = null;

        libraryManager.rentBook(customer, book);

        String expectedOutput = "Customer or Book cannot be null.\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRentBook_CustomerAlreadyRentedBook() {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);
        Book book = new Book("1234567890", "Test Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        customer.addRentedBook(book);

        libraryManager.rentBook(customer, book);

        String expectedOutput = "You already have this book rented: " + book.getDisplayText() + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRentBook_CustomerReachedRentalLimit() {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);

        // Add rented books to reach the limit
        Book book1 = new Book("ISBN1", "Book 1", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);
        Book book2 = new Book("ISBN2", "Book 2", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        customer.addRentedBook(book1);
        customer.addRentedBook(book2);

        Book newBook = new Book("ISBN3", "Book 3", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        libraryManager.rentBook(customer, newBook);

        String expectedOutput = "You have reached your rental limit.\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRentBook_BookIsNotRentable() {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);

        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 0, 1); // 0 rentable copies

        libraryManager.rentBook(customer, book);

        String expectedOutput = "Book is not available for rent. You have been added to the waiting list: " + book.getDisplayText() + "\n";
        assertEquals(expectedOutput, outContent.toString());
        assertTrue(book.getRentingWaitList().contains(customer));
    }

    @Test
    public void testRentBook_SuccessfulRent() {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);

        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1); // 1 rentable copy

        libraryManager.rentBook(customer, book);

        String output = outContent.toString();
        assertTrue(output.contains("Book rented successfully: " + book.getDisplayText()));
        assertTrue(output.contains("Return Date: "));
        assertEquals(0, book.getRentableCopies());
        assertTrue(customer.getRentedBooks().contains(book));
    }

    @Test
    public void testReturnBook_NullCustomerOrBook() {
        Customer customer = null;
        Book book = null;

        libraryManager.returnBook(customer, book);

        String expectedOutput = "Customer or Book cannot be null.\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testReturnBook_RecordNotFound() {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);
        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        libraryManager.returnBook(customer, book);

        String expectedOutput = "This book is not in your rented list: " + book.getDisplayText() + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testReturnBook_SuccessfulReturn() {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);
        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        // Rent the book first
        libraryManager.rentBook(customer, book);

        // Clear output
        outContent.reset();

        libraryManager.returnBook(customer, book);

        String output = outContent.toString();
        assertTrue(output.contains("Book returned successfully: " + book.getDisplayText()));
        assertEquals(1, book.getRentableCopies());
        assertFalse(customer.getRentedBooks().contains(book));
    }

    @Test
    public void testProcessReturns_NoRentals() {
        LocalDate currentDate = LocalDate.now();

        libraryManager.processReturns(currentDate);

        String expectedOutput = "No rentals to return on " + currentDate + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testProcessReturns_WithRentals() throws Exception {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);
        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        // Rent the book
        libraryManager.rentBook(customer, book);

        // Manually adjust the return date to today
        Field activeRentalsField = LibraryManager.class.getDeclaredField("activeRentals");
        activeRentalsField.setAccessible(true);
        TreeMap<LocalDate, List<RentalRecord>> activeRentals = (TreeMap<LocalDate, List<RentalRecord>>) activeRentalsField.get(libraryManager);

        LocalDate today = LocalDate.now();
        List<RentalRecord> records = activeRentals.values().iterator().next();
        RentalRecord rentalRecord = records.get(0);

        // Remove the old date entry
        activeRentals.clear();
        activeRentals.put(today, Arrays.asList(rentalRecord));

        // Clear output
        outContent.reset();

        libraryManager.processReturns(today);

        String output = outContent.toString();
        assertTrue(output.contains("Book returned automatically: " + book.getDisplayText() + " by " + customer.getUserName()));
        assertEquals(1, book.getRentableCopies());
        assertFalse(customer.getRentedBooks().contains(book));
    }

    @Test
    public void testPurchaseBook_NullCustomerOrBook() {
        Customer customer = null;
        Book book = null;

        libraryManager.purchaseBook(customer, book, "CreditCard");

        String expectedOutput = "Customer or Book cannot be null.\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testPurchaseBook_BookIsNotSalable() {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);
        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 0); // 0 saleable copies

        libraryManager.purchaseBook(customer, book, "CreditCard");

        String expectedOutput = "Book is not available for purchase. You have been added to the waiting list: " + book.getDisplayText() + "\n";
        assertEquals(expectedOutput, outContent.toString());
        assertTrue(book.getSellingWaitList().contains(customer));
    }

    @Test
    public void testPurchaseBook_SuccessfulPurchase() {
        Password password = new Password("password");
        Customer customer = new Customer("John", password);
        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1); // 1 saleable copy

        libraryManager.purchaseBook(customer, book, "CreditCard");

        String output = outContent.toString();
        assertTrue(output.contains("Processing credit card payment of: HK$100.00."));
        assertTrue(output.contains("Transaction ID: "));
        assertTrue(output.contains("Book purchased successfully: " + book.getDisplayText()));
        assertTrue(output.contains("Discounted Price: HK$100.00"));
        assertEquals(0, book.getSaleableCopies());
        assertTrue(customer.getPurchasedBooks().contains(book));
    }

    @Test
    public void testProcessRentingWaitlist() throws Exception {
        Password password = new Password("password");
        Customer customer1 = new Customer("Alice", password);
        Customer customer2 = new Customer("Bob", password);
        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 0, 1); // 0 rentable copies

        // Both customers attempt to rent the book
        libraryManager.rentBook(customer1, book);
        libraryManager.rentBook(customer2, book);

        // Now make the book rentable
        book.setRentableCopies(1);

        // Invoke processRentingWaitlist
        Method method = LibraryManager.class.getDeclaredMethod("processRentingWaitlist", Book.class);
        method.setAccessible(true);
        method.invoke(libraryManager, book);

        String output = outContent.toString();
        assertTrue(output.contains("Notifying Alice about the availability of the book: " + book.getDisplayText()));
        assertTrue(output.contains("Book rented successfully: " + book.getDisplayText()));
        assertEquals(0, book.getRentableCopies());
        assertTrue(customer1.getRentedBooks().contains(book));
        assertFalse(customer2.getRentedBooks().contains(book));
    }

    @Test
    public void testProcessSellingWaitlist() throws Exception {
        Password password = new Password("password");
        Customer customer1 = new Customer("Alice", password);
        Customer customer2 = new Customer("Bob", password);
        Book book = new Book("ISBN", "Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 0); // 0 saleable copies

        // Both customers attempt to purchase the book
        libraryManager.purchaseBook(customer1, book, "CreditCard");
        libraryManager.purchaseBook(customer2, book, "CreditCard");

        // Now make the book salable
        book.setSaleableCopies(1);

        // Invoke processSellingWaitlist
        Method method = LibraryManager.class.getDeclaredMethod("processSellingWaitlist", Book.class, String.class);
        method.setAccessible(true);
        method.invoke(libraryManager, book, "CreditCard");

        String output = outContent.toString();
        assertTrue(output.contains("Notifying Alice about the availability of the book for purchase: " + book.getDisplayText()));
        assertTrue(output.contains("Book purchased successfully: " + book.getDisplayText()));
        assertEquals(0, book.getSaleableCopies());
        assertTrue(customer1.getPurchasedBooks().contains(book));
        assertFalse(customer2.getPurchasedBooks().contains(book));
    }
    
    @Test
    public void testReturnBook_RecordAlreadyReturned() throws Exception {
        // Set up
        Password password = new Password("password");
        Customer customer = new Customer("John", password);
        Book book = new Book("1234567890", "Test Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        // Rent the book
        libraryManager.rentBook(customer, book);

        // Manually mark the rental record as returned
        Field activeRentalsField = LibraryManager.class.getDeclaredField("activeRentals");
        activeRentalsField.setAccessible(true);
        TreeMap<LocalDate, List<RentalRecord>> activeRentals =
                (TreeMap<LocalDate, List<RentalRecord>>) activeRentalsField.get(libraryManager);
        RentalRecord rentalRecord = activeRentals.firstEntry().getValue().get(0);
        rentalRecord.markAsReturned();

        // Attempt to return the book again
        outContent.reset();
        libraryManager.returnBook(customer, book);

        // Verify output
        String expectedOutput = "This book is not in your rented list: " + book.getDisplayText() + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testReturnBook_RecordListBecomesEmpty() throws Exception {
        // Set up
        Password password = new Password("password");
        Customer customer = new Customer("John", password);
        Book book = new Book("1234567890", "Test Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        // Rent and return the book
        libraryManager.rentBook(customer, book);
        outContent.reset();
        libraryManager.returnBook(customer, book);

        // Verify that activeRentals is empty
        Field activeRentalsField = LibraryManager.class.getDeclaredField("activeRentals");
        activeRentalsField.setAccessible(true);
        TreeMap<LocalDate, List<RentalRecord>> activeRentals =
                (TreeMap<LocalDate, List<RentalRecord>>) activeRentalsField.get(libraryManager);

        assertTrue(activeRentals.isEmpty());

        // Verify output
        assertTrue(outContent.toString().contains("Book returned successfully: " + book.getDisplayText()));
    }

    @Test
    public void testReturnBook_MultipleRecordsSameDate() throws Exception {
        // Set up
        Password password = new Password("password");
        Customer customer1 = new Customer("John", password);
        Customer customer2 = new Customer("Jane", password);
        Book book = new Book("1234567890", "Test Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 2, 2);

        // Both customers rent the book
        libraryManager.rentBook(customer1, book);
        libraryManager.rentBook(customer2, book);

        // Return book for customer1
        outContent.reset();
        libraryManager.returnBook(customer1, book);

        // Verify that activeRentals is not empty
        Field activeRentalsField = LibraryManager.class.getDeclaredField("activeRentals");
        activeRentalsField.setAccessible(true);
        TreeMap<LocalDate, List<RentalRecord>> activeRentals =
                (TreeMap<LocalDate, List<RentalRecord>>) activeRentalsField.get(libraryManager);

        assertFalse(activeRentals.isEmpty());

        // Verify output
        assertTrue(outContent.toString().contains("Book returned successfully: " + book.getDisplayText()));
    }

    // purchaseBook(Customer, Book, String)

    @Test
    public void testPurchaseBook_PaymentFails() throws Exception {
        // Set up
        Password password = new Password("password");
        Customer customer = new Customer("Jane", password);
        Book book = new Book("1234567890", "Test Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        // Mock PaymentMethod to simulate payment failure
        PaymentMethod failingPaymentMethod = new PaymentMethod() {
            @Override
            public void processPayment(double amount) {
                System.out.printf("Processing payment of: HK$%.2f.\n", amount);
                // Do not set isPaymentProcessed to true
            }
        };

        // Create a failing transaction
        Transaction failingTransaction = new Transaction(UUID.randomUUID().toString(), 100.0, failingPaymentMethod, null) {
            @Override
            public void processPayment() {
                paymentMethod.processPayment(amount);
                // Do not set isPaymentProcessed to true to simulate failure
                System.out.printf("Transaction ID: %s failed to process.\n", transactionID);
            }
        };

        // Mock PaymentFactory to return the failing transaction
        PaymentFactory paymentFactory = new PaymentFactory() {
            @Override
            public Transaction createTransaction(String transactionID, double amount, String paymentType, String refundType) {
                return failingTransaction;
            }
        };

        // Since we cannot easily inject our mock PaymentFactory without external libraries, simulate payment failure by using an invalid payment method
        outContent.reset();
        libraryManager.purchaseBook(customer, book, "InvalidMethod");

        // Verify output
        String expectedOutput = "Unknown payment method: InvalidMethod";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    // processReturns(LocalDate)

    @Test
    public void testProcessReturns_EmptyRecords() throws Exception {
        // Set up
        LocalDate currentDate = LocalDate.now();

        // Manually add an empty list to activeRentals
        Field activeRentalsField = LibraryManager.class.getDeclaredField("activeRentals");
        activeRentalsField.setAccessible(true);
        TreeMap<LocalDate, List<RentalRecord>> activeRentals =
                (TreeMap<LocalDate, List<RentalRecord>>) activeRentalsField.get(libraryManager);
        activeRentals.put(currentDate, new ArrayList<>());

        // Invoke processReturns
        outContent.reset();
        libraryManager.processReturns(currentDate);

        // Verify output
        String expectedOutput = "No rentals to return on " + currentDate + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    // processSellingWaitlist(Book, String)

    @Test
    public void testProcessSellingWaitlist_EmptyWaitlist() throws Exception {
        // Set up
        Book book = new Book("1234567890", "Test Book", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        // Ensure waitlist is empty
        assertTrue(book.getSellingWaitList().isEmpty());

        // Make the book salable
        book.setSaleableCopies(1);

        // Invoke processSellingWaitlist
        Method method = LibraryManager.class.getDeclaredMethod("processSellingWaitlist", Book.class, String.class);
        method.setAccessible(true);
        outContent.reset();
        method.invoke(libraryManager, book, "CreditCard");

        // Verify that nothing happens
        assertEquals("", outContent.toString());
    }

    // rentBook(Customer, Book)

    @Test
    public void testRentBook_MembershipUpgrade() {
        // Set up
        Password password = new Password("password");
        Customer customer = new Customer("Jane", password);

        // Upgrade membership to Silver
        customer.getMembership().addXP(100); // Should upgrade to Silver

        // Add rented books up to Silver limit
        for (int i = 0; i < 5; i++) {
            Book book = new Book("ISBN" + i, "Book" + i, "Author", "Publisher",
                    "2020-01-01", "Description", 100.0, 1, 1);
            customer.addRentedBook(book);
        }

        // Attempt to rent another book
        Book newBook = new Book("ISBN_New", "Book New", "Author", "Publisher",
                "2020-01-01", "Description", 100.0, 1, 1);

        outContent.reset();
        libraryManager.rentBook(customer, newBook);

        // Verify output
        String expectedOutput = "You have reached your rental limit.\n";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
}
