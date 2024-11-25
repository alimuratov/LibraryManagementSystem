package test.testUsers;

import org.junit.Test;
import static org.junit.Assert.*;
import main.book.Book;
import java.util.List;

import main.users.Admin;

public class AdminTest {

    @Test
    public void testGetInstance() {
        // Reset the instance variable to null using reflection
        try {
            java.lang.reflect.Field instanceField = Admin.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            fail("Failed to reset Admin.instance");
        }

        // First call to getInstance() when instance is null
        Admin admin1 = Admin.getInstance();
        assertNotNull(admin1);

        // Second call to getInstance() when instance is not null
        Admin admin2 = Admin.getInstance();
        assertSame(admin1, admin2);
    }

    @Test
    public void testAddBook() {
        Admin admin = Admin.getInstance();

        // Clear all books before testing
        List<Book> allBooks = Book.getAllBooks();
        allBooks.clear();

        Book book = new Book("Test Book", "Test Description");

        // Capture System.out output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Test adding a book not in the library
        admin.addBook(book);
        assertTrue(allBooks.contains(book));
        String expectedOutput1 = "Book added to the library: " + book.getDisplayText() + System.lineSeparator();
        assertEquals(expectedOutput1, outContent.toString());

        // Reset output stream
        outContent.reset();

        // Test adding a book already in the library
        admin.addBook(book);
        String expectedOutput2 = "Book already exists in the library: " + book.getDisplayText() + System.lineSeparator();
        assertEquals(expectedOutput2, outContent.toString());
    }

    @Test
    public void testRemoveBook() {
        Admin admin = Admin.getInstance();

        // Clear all books before testing
        List<Book> allBooks = Book.getAllBooks();
        allBooks.clear();

        Book book = new Book("Test Book", "Test Description");
        allBooks.add(book); // Add book to library

        // Capture System.out output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Test removing a book that exists
        admin.removeBook(book);
        assertFalse(allBooks.contains(book));
        String expectedOutput1 = "Book removed from the library: " + book.getDisplayText() + System.lineSeparator();
        assertEquals(expectedOutput1, outContent.toString());

        // Reset output stream
        outContent.reset();

        // Test removing a book that does not exist
        admin.removeBook(book);
        String expectedOutput2 = "Book not found in the library: " + book.getDisplayText() + System.lineSeparator();
        assertEquals(expectedOutput2, outContent.toString());
    }
}
