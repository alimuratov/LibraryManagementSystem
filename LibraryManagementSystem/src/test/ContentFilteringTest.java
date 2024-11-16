package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.*;

import java.math.BigDecimal;
import java.util.*;

public class ContentFilteringTest {
	private ContentFiltering contentFiltering;
	private Data testData;
	
	private User user1;
	private Book book1, book2, book3, book4;
	
	@BeforeEach
	public void setUp() {
		book1 = new Book("Book One", "An adventure in the mountains");
        book2 = new Book("Book Two", "Romantic tales by the sea");
        book3 = new Book("Book Three", "Romantic tales by the sea");
        book4 = new Book("Book Four", "Science fiction and space exploration");
        
        List<Book> books = Arrays.asList(book1, book2, book3, book4);
        
        user1 = new User("user1");
        
        Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
        Map<Book, BigDecimal> user1Ratings = new HashMap<>();
        user1Ratings.put(book1, BigDecimal.valueOf(5));
        user1Ratings.put(book3, BigDecimal.valueOf(4));
        userRatings.put(user1, user1Ratings);
        
        testData = new Data();
        testData.setBooks(books);
        testData.setUserRatings(userRatings);
        
        contentFiltering = new ContentFiltering(testData);
	}
	
	@Test
	public void test_1() {
		Book[] recommendations = contentFiltering.getRecommendations(user1, 2);
		
		List<Book> ratedBooks = Arrays.asList(book1, book3);
		
		for (Book book : recommendations) {
			assertFalse(ratedBooks.contains(book));
		}
		
		assertEquals(2, recommendations.length);
	}
	
	@Test
	
	
}
