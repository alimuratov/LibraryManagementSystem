package test.testRecommendation;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import main.book.Book;
import main.users.Customer;
import main.recommendation.*;

public class DataTest {

    private Data data;
    private Customer user1;
    private Customer user2;
    private Book book1;
    private Book book2;

    @Before
    public void setUp() {
        user1 = new Customer("User1");
        user2 = new Customer("User2");
        book1 = new Book("Book1", "Book1 Description");
        book2 = new Book("Book2", "Book2 Description");
    }

    @Test
    public void testIsBookRatedByUser_UserNotPresent() {
    	Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
        data = new Data(userRatings, Arrays.asList(book1, book2));
        assertFalse(data.isBookRatedByUser(book1, user1));
    }

    @Test
    public void testIsBookRatedByUser_UserPresent_BookNotRated() {
    	Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
    	Map<Book, BigDecimal> ratings = new HashMap<>();
    	userRatings.put(user1, ratings);
    	data = new Data(userRatings, Arrays.asList(book1, book2));
        assertFalse(data.isBookRatedByUser(book1, user1));
    }

    @Test
    public void testIsBookRatedByUser_UserAndBookPresent() {
    	
    	Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
    	Map<Book, BigDecimal> ratings = new HashMap<>();
    	ratings.put(book1, BigDecimal.valueOf(4.5));
    	userRatings.put(user1, ratings);
    	data = new Data(userRatings, Arrays.asList(book1, book2));
        assertTrue(data.isBookRatedByUser(book1, user1));
        assertFalse(data.isBookRatedByUser(book2, user1));
    }

    @Test
    public void testConstructorsAndGetters() {
        Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
        List<Book> books = Arrays.asList(book1, book2);
        data = new Data(userRatings, books);
        
        assertEquals(userRatings, data.getUserRatings());
        assertEquals(books, data.getBooks());
        
        Data defaultData = new Data();
        defaultData.setUserRatings(userRatings);
        defaultData.setBooks(books);
        
        assertEquals(userRatings, defaultData.getUserRatings());
        assertEquals(books, defaultData.getBooks());
    }
}
