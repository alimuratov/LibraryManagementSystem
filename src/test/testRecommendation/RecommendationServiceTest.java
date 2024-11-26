package test.testRecommendation;

import main.recommendation.*;
import main.users.*;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.book.*;



public class RecommendationServiceTest {
	
	@Test
	public void testGetRecommendations_Content() {        
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("Book Four", "Underwater archaeology: uncovering marine biology history and oceanic mysteries.");
		
		Book book5 = new Book("Book five", "An introduction to dance and choreography for aspiring performers.");
		Book book6 = new Book("Book six", "The history of ballet and the evolution of modern dance styles.");
		Book book7 = new Book("Book seven", "Dance as an art form: exploring rhythm, movement, and creative expression.");
		List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6, book7);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
	    
        RecommendationService service = new RecommendationService();
        
        service.setStrategy("content", data);
        
        List<Book> recommendations = Arrays.asList(service.getRecommendations(user, 5));
        
        assertTrue(recommendations.contains(book3));
	    assertTrue(recommendations.contains(book4));	
	}
	
	@Test
	void testGetRecommendations_Normal() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description two");
	    Book book3 = new Book("Book Three", "Description three");
	    Book book4 = new Book("Book Four", "Description four");
	    Book book5 = new Book("Book Five", "Description five");
	    Book book6 = new Book("Book Six", "Description six");
	    Book book7 = new Book("Book Seven", "Description seven");
	    List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6, book7);
	    data.setBooks(books);

	    Customer user = new Customer("Customer");

	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);

	    data.setUserRatings(userRatings);

        RecommendationService service = new RecommendationService();
        
        service.setStrategy("collaborative", data);

	    List<Book> recommendations = Arrays.asList(service.getRecommendations(user, 2));

	    assertEquals(2, recommendations.size());
	    assertTrue(recommendations.contains(book3) || recommendations.contains(book4) || recommendations.contains(book5) || recommendations.contains(book6) || recommendations.contains(book7));
	    assertFalse(recommendations.contains(book1) || recommendations.contains(book2));
	}
	
	@Test
	void testGetRecommendations_NullStrategy() {
		RecommendationService service = new RecommendationService();
        Data data = new Data();
        Customer user = new Customer("User");
        
        Book[] recommendations = service.getRecommendations(user, 5);
        
        assertNotNull(recommendations);
        assertEquals(0, recommendations.length);

	}
	
	@Test
	void testGetRecommendations_UnknownStrategy() {
		RecommendationService service = new RecommendationService();
        Data data = new Data();
        Customer user = new Customer("User");
        
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            service.setStrategy("unknown", data);
        });
        
        assertEquals("Unknown strategy type", exception.getMessage());
	}
}
