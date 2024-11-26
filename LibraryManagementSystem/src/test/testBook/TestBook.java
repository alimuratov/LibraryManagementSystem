package test.testBook;


import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import main.book.*;
import main.authentication.*;
import main.users.*;

class TestBook {
    private Book book;

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void tearDown() {
        Book.getAllBooks().clear();
    }

    @Test
    void constructor_WithAllParameters_InitializesCorrectly() {
        assertEquals("Test Book", book.getBookTitle());
        assertEquals(29.99, book.getBookPrice());
        assertEquals(5, book.getRentableCopies());
        assertEquals(3, book.getSaleableCopies());
        assertTrue(book.getReviews().isEmpty());
        assertNotNull(book.getRentingWaitList());
        assertNotNull(book.getSellingWaitList());
    }

    @Test
    void constructor_Simplified_InitializesWithDefaults() {
        Book simpleBook = new Book("Simple Book", "Simple Description");
        assertEquals("Simple Book", simpleBook.getBookTitle());
        assertEquals(0.0, simpleBook.getBookPrice());
        assertEquals(0, simpleBook.getRentableCopies());
        assertEquals(0, simpleBook.getSaleableCopies());
        assertEquals("Simple Book by Unknown Author ISBN: 000-0000000000 Publisher: Unknown Publisher Publication Date: 2024-01-01 Price: $0.0", simpleBook.getDisplayText());
        assertTrue(simpleBook.getReviews().isEmpty());
    }

    @Test
    void getAllBooks_InitiallyEmpty() {
        List<Book> allBooks = Book.getAllBooks();
        assertNotNull(allBooks);
        assertTrue(allBooks.isEmpty());
    }

    @Test
    void addBook_AddsToAllBooks() {
        Book.addBook(book);
        assertTrue(Book.getAllBooks().contains(book));
        assertEquals(1, Book.getAllBooks().size());
    }

    @Test
    void setters_UpdateValues() {
        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setRentableCopies(10);
        book.setSaleableCopies(5);

        assertEquals("New Title", book.getBookTitle());
        assertEquals(10, book.getRentableCopies());
        assertEquals(5, book.getSaleableCopies());
        assertTrue(book.getDisplayText().contains("New Author"));
    }

    @Test
    void isRentable_ReturnsCorrectValue() {
        assertTrue(book.isRentable());
        book.setRentableCopies(0);
        assertFalse(book.isRentable());
    }

    @Test
    void isSalable_ReturnsCorrectValue() {
        assertTrue(book.isSalable());
        book.setSaleableCopies(0);
        assertFalse(book.isSalable());
    }

    @Test
    void addReview_AddsToList() {
        Review review = new Review("Great book!", 5);
        book.addReview(review);
        
        assertEquals(1, book.getReviews().size());
        assertTrue(book.getReviews().contains(review));
    }

    @Test
    void addReview_WithNullReview_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            book.addReview(null);
        });
    }

    @Test
    void addMultipleReviews_AllReviewsAdded() {
        Review review1 = new Review("Great book!", 5);
        Review review2 = new Review("Good read", 4);
        
        book.addReview(review1);
        book.addReview(review2);
        
        assertEquals(2, book.getReviews().size());
        assertTrue(book.getReviews().contains(review1));
        assertTrue(book.getReviews().contains(review2));
    }

    @Test
    void waitlist_Management() {
        Customer customer1 = new Customer("User1", new Password("pass1"));
        Customer customer2 = new Customer("User2", new Password("pass2"));
        
        book.addRentingWaitList(customer1);
        book.addRentingWaitList(customer2);
        book.addSellingWaitList(customer1);
        
        assertEquals(2, book.getRentingWaitList().size());
        assertEquals(1, book.getSellingWaitList().size());
        assertEquals(customer1, book.getRentingWaitList().peek());
    }

    @Test
    void getDisplayText_ContainsAllInfo() {
        String display = book.getDisplayText();
        assertTrue(display.contains("Test Book"));
        assertTrue(display.contains("Test Author"));
        assertTrue(display.contains("123-4567890123"));
        assertTrue(display.contains("Test Publisher"));
        assertTrue(display.contains("2024-01-01"));
        assertTrue(display.contains("29.99"));
    }

    @Test
    void showAvailableCopies_DisplaysCorrectCount() {
        assertEquals("5 rentable copies available", book.showAvailableRentalCopies());
        assertEquals("3 saleable copies available", book.showAvailableSaleableCopies());
        
        book.setRentableCopies(0);
        book.setSaleableCopies(0);
        
        assertEquals("0 rentable copies available", book.showAvailableRentalCopies());
        assertEquals("0 saleable copies available", book.showAvailableSaleableCopies());
    }

    @Test
    void unmodifiableCollections_CannotBeModified() {
        Review review = new Review("Great book!", 5);
        book.addReview(review);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            book.getReviews().add(new Review("Another review", 4));
        });
    }

    @Test
    void multipleBooks_StaticListManagement() {
        Book book1 = new Book("Book 1", "Description 1");
        Book book2 = new Book("Book 2", "Description 2");
        
        Book.addBook(book1);
        Book.addBook(book2);
        
        assertEquals(2, Book.getAllBooks().size());
        assertTrue(Book.getAllBooks().contains(book1));
        assertTrue(Book.getAllBooks().contains(book2));
    }
    
    @Test
    void displayReviews_WithNoReviews() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            book.displayReviews();
            assertEquals("No reviews yet." + System.lineSeparator(), outputStream.toString());
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    void displayReviews_WithMultipleReviews() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Review review1 = new Review("Great book!", 5);
            Review review2 = new Review("Interesting read", 4);
            book.addReview(review1);
            book.addReview(review2);

            book.displayReviews();

            String output = outputStream.toString();
            assertTrue(output.contains("Book Rating: 5"));
            assertTrue(output.contains("Comment: Great book!"));
            assertTrue(output.contains("Book Rating: 4"));
            assertTrue(output.contains("Comment: Interesting read"));
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }
    
    @Test 
    void testSearch_BasicSearch() {
    	Book book1 = new Book("Book1", "The quick brown fox");
    	Book book2 = new Book("Book2", "Lazy dog jumps over");
    	Book book3 = new Book("Book3", "Quick movements in the wild");
    	
    	Book.addBook(book1);
    	Book.addBook(book2);
    	Book.addBook(book3);
    	
		List<Book> expected = Arrays.asList(book1, book3);
		assertEquals(expected, Book.search("quick"));
    }
    
    @Test
    void testSeach_CaseSensitivity() {
    	Book book1 = new Book("Book1", "The quick brown fox");
    	Book book2 = new Book("Book2", "Lazy dog jumps over");
    	Book book3 = new Book("Book3", "Quick movements in the wild");
    	
    	Book.addBook(book1);
    	Book.addBook(book2);
    	Book.addBook(book3);
    	
		List<Book> expected = Arrays.asList(book1, book3);
		assertEquals(expected, Book.search("QUiCk"));
    }
    
    @Test
    void testSearch_Punctuations() {
    	Book book1 = new Book("Book1", "The quick!!; brown fox");
    	Book book2 = new Book("Book2", "Lazy dog;; jumps. over");
    	Book book3 = new Book("Book3", "Quick movements, in; the! wild");
    	
    	Book.addBook(book1);
    	Book.addBook(book2);
    	Book.addBook(book3);
    	
		List<Book> expected = Arrays.asList(book1, book3);
		assertEquals(expected, Book.search("QUiCk"));
    }
    
    @Test
    void testSearch_noMatches() {
    	Book book1 = new Book("Book1", "The quick!!; brown fox");
    	Book book2 = new Book("Book2", "Lazy dog;; jumps. over");
    	Book book3 = new Book("Book3", "Quick movements, in; the! wild");
    	
    	Book.addBook(book1);
    	Book.addBook(book2);
    	Book.addBook(book3);
    	
		assertEquals(Collections.emptyList(), Book.search("wolf"));
    }
    
    @Test
    void testSearch_emptyString() {
    	Book book1 = new Book("Book1", "The quick!!; brown fox");
    	Book book2 = new Book("Book2", "Lazy dog;; jumps. over");
    	Book book3 = new Book("Book3", "Quick movements, in; the! wild");
    	
    	Book.addBook(book1);
    	Book.addBook(book2);
    	Book.addBook(book3);
    	
		List<Book> expected = Arrays.asList(book1, book2, book3);
		assertEquals(expected, Book.search(""));
    }
}