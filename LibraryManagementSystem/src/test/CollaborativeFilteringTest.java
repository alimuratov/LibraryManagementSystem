package test;

import org.junit.jupiter.api.Test;

import main.Book;
import main.CollaborativeFiltering;
import main.User;
import main.UserRatingsNotFound;
import main.PredefinedStubData;
import main.RandomStubData;
import main.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CollaborativeFilteringTest {
	
	protected static List<Book> books;
	
	@BeforeEach
	void setup() {
			
	}
	
	@Test
	void testCalculateDifferences_emptyUserRatings() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<Book, Map<Book, BigDecimal>> expectedSimilarities = new HashMap<>(); 
		Map<Book, Map<Book, Integer>> expectedFrequencies = new HashMap<>(); 
		
		collaborativeFiltering.calculateDifferences();
		
		assertEquals(expectedSimilarities, collaborativeFiltering.getBookSimilarities());
		assertEquals(expectedFrequencies, collaborativeFiltering.getFrequencies());
	}
	@Test
	void testCalculateDifferences_userWithNoRatedBooks() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		User user = new User("User");
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		userRatings.put(user, user1Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<Book, Map<Book, BigDecimal>> expectedSimilarities = new HashMap<>(); 
		Map<Book, Map<Book, Integer>> expectedFrequencies = new HashMap<>(); 
		
		collaborativeFiltering.calculateDifferences();
		
		assertEquals(expectedSimilarities, collaborativeFiltering.getBookSimilarities());
		assertEquals(expectedFrequencies, collaborativeFiltering.getFrequencies());
	}
	@Test
	void testCalculateDifferences_userWithOneRatedBook() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		User user = new User("User");
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(6.0));
		userRatings.put(user, user1Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<Book, Map<Book, BigDecimal>> expectedSimilarities = new HashMap<>(); 
		Map<Book, Map<Book, Integer>> expectedFrequencies = new HashMap<>(); 
		
		collaborativeFiltering.calculateDifferences();
		
		assertEquals(expectedSimilarities, collaborativeFiltering.getBookSimilarities());
		assertEquals(expectedFrequencies, collaborativeFiltering.getFrequencies());
	}
	@Test
	void testCalculateDifferences_userWithTwoRatedBooks() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		User user = new User("User");
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(4.0));
		user1Ratings.put(book2, BigDecimal.valueOf(3.0));
		userRatings.put(user, user1Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<Book, Map<Book, BigDecimal>> expectedSimilarities = new HashMap<>(); 
		Map<Book, Map<Book, Integer>> expectedFrequencies = new HashMap<>(); 
		Map<Book, Integer> innerMap = new HashMap<>();
		innerMap.put(book2, 1);
		expectedFrequencies.put(book1, innerMap);
		
		Map<Book, Integer> innerMap1 = new HashMap<>();
		innerMap1.put(book1, 1);
		expectedFrequencies.put(book2, innerMap1);
		
		Map<Book, BigDecimal> innerMap2 = new HashMap<>();
		innerMap2.put(book2, BigDecimal.valueOf(1.0));
		expectedSimilarities.put(book1, innerMap2);

		Map<Book, BigDecimal> innerMap3 = new HashMap<>();
		innerMap3.put(book1, BigDecimal.valueOf(-1.0));
		expectedSimilarities.put(book2, innerMap3);
		
		collaborativeFiltering.calculateDifferences();
		
		assertEquals(expectedSimilarities, collaborativeFiltering.getBookSimilarities());
		assertEquals(expectedFrequencies, collaborativeFiltering.getFrequencies());
	}
	@Test
	void testCalculateDifferences_multipleUsersWithOverlappingRatedBooks() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		User user1 = new User("User1");
		User user2 = new User("User2");
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(4.0));
		user1Ratings.put(book2, BigDecimal.valueOf(3.0));
		Map<Book, BigDecimal> user2Ratings = new HashMap<>();
		user2Ratings.put(book1, BigDecimal.valueOf(5.0));
		user2Ratings.put(book2, BigDecimal.valueOf(2.0));
		userRatings.put(user1, user1Ratings);
		userRatings.put(user2, user2Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<Book, Map<Book, Integer>> expectedFrequencies = new HashMap<>(); 
		Map<Book, Integer> innerMap = new HashMap<>();
		innerMap.put(book2, 2);
		expectedFrequencies.put(book1, innerMap);
		
		Map<Book, Integer> innerMap1 = new HashMap<>();
		innerMap1.put(book1, 2);
		expectedFrequencies.put(book2, innerMap1);
		
		Map<Book, Map<Book, BigDecimal>> expectedSimilarities = new HashMap<>(); 
		
		Map<Book, BigDecimal> innerMap2 = new HashMap<>();
		innerMap2.put(book2, BigDecimal.valueOf(2.0));
		expectedSimilarities.put(book1, innerMap2);

		Map<Book, BigDecimal> innerMap3 = new HashMap<>();
		innerMap3.put(book1, BigDecimal.valueOf(-2.0));
		expectedSimilarities.put(book2, innerMap3);
		
		collaborativeFiltering.calculateDifferences();
		
		assertEquals(expectedSimilarities, collaborativeFiltering.getBookSimilarities());
		assertEquals(expectedFrequencies, collaborativeFiltering.getFrequencies());
	}	
	@Test
	void testCalculateDifferences_multipleUsersWithNonOverlappingRatedBooks() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		Book book3 = new Book("Book Three", "Description Three");
		Book book4 = new Book("Book Four", "Description Four");
		List<Book> books = Arrays.asList(book1, book2);
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		User user1 = new User("User1");
		User user2 = new User("User2");
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(4.0));
		user1Ratings.put(book2, BigDecimal.valueOf(3.0));
		Map<Book, BigDecimal> user2Ratings = new HashMap<>();
		user2Ratings.put(book3, BigDecimal.valueOf(5.0));
		user2Ratings.put(book4, BigDecimal.valueOf(2.0));
		userRatings.put(user1, user1Ratings);
		userRatings.put(user2, user2Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<Book, Map<Book, Integer>> expectedFrequencies = new HashMap<>(); 
		Map<Book, Integer> freq1 = new HashMap<>();
		freq1.put(book2, 1);
		expectedFrequencies.put(book1, freq1);
		
		Map<Book, Integer> freq2 = new HashMap<>();
		freq2.put(book1, 1);
		expectedFrequencies.put(book2, freq2);
		
		Map<Book, Integer> freq3 = new HashMap<>();
		freq3.put(book4, 1);
		expectedFrequencies.put(book3, freq3);
		
		Map<Book, Integer> freq4 = new HashMap<>();
		freq4.put(book3, 1);
		expectedFrequencies.put(book4, freq4);
		
		Map<Book, Map<Book, BigDecimal>> expectedSimilarities = new HashMap<>(); 
		
		Map<Book, BigDecimal> sim1 = new HashMap<>();
		sim1.put(book2, BigDecimal.valueOf(1.0));
		expectedSimilarities.put(book1, sim1);

		Map<Book, BigDecimal> sim2 = new HashMap<>();
		sim2.put(book1, BigDecimal.valueOf(-1.0));
		expectedSimilarities.put(book2, sim2);
		
		Map<Book, BigDecimal> sim3 = new HashMap<>();
		sim3.put(book3, BigDecimal.valueOf(-3.0));
		expectedSimilarities.put(book4, sim3);
		
		Map<Book, BigDecimal> sim4 = new HashMap<>();
		sim4.put(book4, BigDecimal.valueOf(3.0));
		expectedSimilarities.put(book3, sim4);
		
		collaborativeFiltering.calculateDifferences();
		
		assertEquals(expectedSimilarities, collaborativeFiltering.getBookSimilarities());
		assertEquals(expectedFrequencies, collaborativeFiltering.getFrequencies());
	}	
	@Test
	void testCalculateDifferences_oneUserTwoBooksSameRating() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		User user1 = new User("User1");
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(4.0));
		user1Ratings.put(book2, BigDecimal.valueOf(4.0));

		userRatings.put(user1, user1Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<Book, Map<Book, Integer>> expectedFrequencies = new HashMap<>(); 
		Map<Book, Integer> freq1 = new HashMap<>();
		freq1.put(book2, 1);
		expectedFrequencies.put(book1, freq1);
		
		Map<Book, Integer> freq2 = new HashMap<>();
		freq2.put(book1, 1);
		expectedFrequencies.put(book2, freq2);
		
		Map<Book, Map<Book, BigDecimal>> expectedSimilarities = new HashMap<>(); 
		
		Map<Book, BigDecimal> sim1 = new HashMap<>();
		sim1.put(book2, BigDecimal.valueOf(0.0));
		expectedSimilarities.put(book1, sim1);

		Map<Book, BigDecimal> sim2 = new HashMap<>();
		sim2.put(book1, BigDecimal.valueOf(0.0));
		expectedSimilarities.put(book2, sim2);
		
		collaborativeFiltering.calculateDifferences();
		
		assertEquals(expectedSimilarities, collaborativeFiltering.getBookSimilarities());
		assertEquals(expectedFrequencies, collaborativeFiltering.getFrequencies());
	}
	
	@Test
	void testPredictRatings_emptyUserRatings() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<User, Map<Book, BigDecimal>> expectedPredictedUserRatings = new HashMap<>(); 
				
		collaborativeFiltering.slopeOne();
		
		assertEquals(expectedPredictedUserRatings, collaborativeFiltering.getPredictedUserRatings());
	}
	@Test
	void testPredictRatings_userWithNoRating() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		User user = new User("User");
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		
		userRatings.put(user, user1Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<User, Map<Book, BigDecimal>> expectedPredictedUserRatings = new HashMap<>(); 
		
		user1Ratings.put(book1, BigDecimal.valueOf(0));
		user1Ratings.put(book2, BigDecimal.valueOf(0));
		expectedPredictedUserRatings.put(user, user1Ratings);
				
		collaborativeFiltering.slopeOne();
		
		assertEquals(expectedPredictedUserRatings, collaborativeFiltering.getPredictedUserRatings());
	}
	@Test
	void testPredictRatings_userWithAllBooksRated() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		User user = new User("User");
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(4.0));
		user1Ratings.put(book2, BigDecimal.valueOf(5.0));
		
		userRatings.put(user, user1Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<User, Map<Book, BigDecimal>> expectedPredictedUserRatings = new HashMap<>(); 
		Map<Book, BigDecimal> innerMap = new HashMap<>();
		innerMap.put(book1, BigDecimal.valueOf(4.0));
		innerMap.put(book2, BigDecimal.valueOf(5.0));
		expectedPredictedUserRatings.put(user, innerMap);
				
		collaborativeFiltering.slopeOne();
		
		assertEquals(expectedPredictedUserRatings, collaborativeFiltering.getPredictedUserRatings());
	}
	@Test
	void testPredictRatings_twoUsersTwoBooksRatedPartiallyByOneUser() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		User user1 = new User("User1");
		User user2 = new User("User2");
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(4.0));
		
		Map<Book, BigDecimal> user2Ratings = new HashMap<>();
		user2Ratings.put(book1, BigDecimal.valueOf(5.0));
		user2Ratings.put(book2, BigDecimal.valueOf(5.5));
		
		userRatings.put(user1, user1Ratings);
		userRatings.put(user2, user2Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<User, Map<Book, BigDecimal>> expectedPredictedUserRatings = new HashMap<>(); 
		Map<Book, BigDecimal> innerMap = new HashMap<>();
		innerMap.put(book1, BigDecimal.valueOf(4.0));
		innerMap.put(book2, new BigDecimal("4.5000000000"));
		expectedPredictedUserRatings.put(user1, innerMap);
		expectedPredictedUserRatings.put(user2, user2Ratings);
		
				
		collaborativeFiltering.slopeOne();
		
		assertEquals(expectedPredictedUserRatings, collaborativeFiltering.getPredictedUserRatings());
	}
	@Test
	void testPredictRatings_emptyFrequenciesAndSimilarity() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		List<Book> books = Arrays.asList(book1, book2);
		User user1 = new User("User1");
		User user2 = new User("User2");
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(4.0));
		
		Map<Book, BigDecimal> user2Ratings = new HashMap<>();
		
		userRatings.put(user1, user1Ratings);
		userRatings.put(user2, user2Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<User, Map<Book, BigDecimal>> expectedPredictedUserRatings = new HashMap<>(); 
		Map<Book, BigDecimal> innerMap = new HashMap<>();
		innerMap.put(book1, BigDecimal.valueOf(4.0));
		innerMap.put(book2, BigDecimal.ZERO);
		expectedPredictedUserRatings.put(user1, innerMap);
		Map<Book, BigDecimal> innerMap2 = new HashMap<>();
		innerMap2.put(book1, BigDecimal.ZERO);
		innerMap2.put(book2, BigDecimal.ZERO);
		expectedPredictedUserRatings.put(user2, innerMap2);

		collaborativeFiltering.slopeOne();
		
		assertEquals(expectedPredictedUserRatings, collaborativeFiltering.getPredictedUserRatings());
	}
	@Test
	void testPredictRatings_threeUsersThreeBooksPartiallyRated() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		Book book3 = new Book("Book Three", "Description Three");
		List<Book> books = Arrays.asList(book1, book2, book3);
		User user1 = new User("User1");
		User user2 = new User("User2");
		User user3 = new User("User3");
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book1, BigDecimal.valueOf(5.0));
		user1Ratings.put(book2, BigDecimal.valueOf(3.0));
		user1Ratings.put(book3, BigDecimal.valueOf(2.0));
		
		Map<Book, BigDecimal> user2Ratings = new HashMap<>();
		user2Ratings.put(book1, BigDecimal.valueOf(3.0));
		user2Ratings.put(book2, BigDecimal.valueOf(4.0));
		
		Map<Book, BigDecimal> user3Ratings = new HashMap<>();
		user3Ratings.put(book2, BigDecimal.valueOf(2.0));
		user3Ratings.put(book3, BigDecimal.valueOf(5.0));
		
		userRatings.put(user1, user1Ratings);
		userRatings.put(user2, user2Ratings);
		userRatings.put(user3, user3Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<User, Map<Book, BigDecimal>> expectedPredictedUserRatings = new HashMap<>(); 
		Map<Book, BigDecimal> innerMap1 = new HashMap<>();
		innerMap1.put(book1, BigDecimal.valueOf(5.0));
		innerMap1.put(book2, BigDecimal.valueOf(3.0));
		innerMap1.put(book3, BigDecimal.valueOf(2.0));
		expectedPredictedUserRatings.put(user1, innerMap1);
		Map<Book, BigDecimal> innerMap2 = new HashMap<>();
		innerMap2.put(book1, BigDecimal.valueOf(3.0));
		innerMap2.put(book2, BigDecimal.valueOf(4.0));
		innerMap2.put(book3, BigDecimal.valueOf(10.0).divide(BigDecimal.valueOf(3.0), 10, RoundingMode.HALF_UP));
		expectedPredictedUserRatings.put(user2, innerMap2);
		Map<Book, BigDecimal> innerMap3 = new HashMap<>();
		innerMap3.put(book1, BigDecimal.valueOf(13.0).divide(BigDecimal.valueOf(3.0), 10, RoundingMode.HALF_UP));
		innerMap3.put(book2, BigDecimal.valueOf(2.0));
		innerMap3.put(book3, BigDecimal.valueOf(5.0));
		expectedPredictedUserRatings.put(user3, innerMap3);
		
		collaborativeFiltering.slopeOne();
		
		assertEquals(expectedPredictedUserRatings, collaborativeFiltering.getPredictedUserRatings());
	}
	@Test
	void testPredictRatings_twoUsersThreeBooksNoOverlappedRatings() {
		Data data = new Data();
		Book book1 = new Book("Book One", "Description One");
		Book book2 = new Book("Book Two", "Description Two");
		Book book3 = new Book("Book Three", "Description Three");
		List<Book> books = Arrays.asList(book1, book2, book3);
		User user1 = new User("User1");
		User user2 = new User("User2");
		data.setBooks(books);
		
		Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
		Map<Book, BigDecimal> user1Ratings = new HashMap<>();
		user1Ratings.put(book2, BigDecimal.valueOf(3.0));
		
		Map<Book, BigDecimal> user2Ratings = new HashMap<>();
		user2Ratings.put(book1, BigDecimal.valueOf(3.0));
		user2Ratings.put(book3, BigDecimal.valueOf(5.0));
		
		userRatings.put(user1, user1Ratings);
		userRatings.put(user2, user2Ratings);
		
		data.setUserRatings(userRatings);
		
		CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);
		
		Map<User, Map<Book, BigDecimal>> expectedPredictedUserRatings = new HashMap<>(); 
		Map<Book, BigDecimal> innerMap1 = new HashMap<>();
		innerMap1.put(book1, BigDecimal.ZERO);
		innerMap1.put(book2, BigDecimal.valueOf(3.0));
		innerMap1.put(book3, BigDecimal.ZERO);
		expectedPredictedUserRatings.put(user1, innerMap1);
		Map<Book, BigDecimal> innerMap2 = new HashMap<>();
		innerMap2.put(book1, BigDecimal.valueOf(3.0));
		innerMap2.put(book2, BigDecimal.ZERO);
		innerMap2.put(book3, BigDecimal.valueOf(5.0));
		expectedPredictedUserRatings.put(user2, innerMap2);
		
		collaborativeFiltering.slopeOne();
		
		assertEquals(expectedPredictedUserRatings, collaborativeFiltering.getPredictedUserRatings());
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

	    User user = new User("User");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user, 2));

	    for (Book book: recommendations) {
	    	System.out.println(book.getBookName());
	    }
	    assertEquals(2, recommendations.size());
	    assertTrue(recommendations.contains(book3) || recommendations.contains(book4) || recommendations.contains(book5) || recommendations.contains(book6) || recommendations.contains(book7));
	    assertFalse(recommendations.contains(book1) || recommendations.contains(book2));
	}
	@Test
	void testGetRecommendations_BooksAvailableLessThanRequested() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description.two");
	    
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(books);

	    User user1 = new User("User1");
	    User user2 = new User("User2");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(4.0));
	    userRatings.put(user1, user1Ratings);
	    
	    Map<Book, BigDecimal> user2Ratings = new HashMap<>();
	    user2Ratings.put(book1, BigDecimal.valueOf(4.0));
	    user2Ratings.put(book2, BigDecimal.valueOf(8.0));
	    userRatings.put(user2, user2Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, 2));

	    assertEquals(1, recommendations.size());
	    assertTrue(recommendations.contains(book2));
	}
	@Test
	void testGetRecommendations_userRatedAllBooks() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description two");
	    Book book3 = new Book("Book Three", "Descripton three");
	    
	    List<Book> books = Arrays.asList(book1, book2, book3);
	    data.setBooks(books);

	    User user1 = new User("User1");
	    User user2 = new User("User2");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(4.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book3, BigDecimal.valueOf(7.0));
	    userRatings.put(user1, user1Ratings);
	    
	    Map<Book, BigDecimal> user2Ratings = new HashMap<>();
	    user2Ratings.put(book1, BigDecimal.valueOf(4.0));
	    user2Ratings.put(book2, BigDecimal.valueOf(8.0));
	    userRatings.put(user2, user2Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, 2));

	    assertTrue(recommendations.isEmpty()); 
	}
	@Test
	void testGetRecommendations_noBooksAvailable() {
	    Data data = new Data();
	    
	    List<Book> books = Collections.emptyList();
	    data.setBooks(books);

	    User user1 = new User("User1");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    userRatings.put(user1, user1Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, 2));

	    assertTrue(recommendations.isEmpty()); 
	}
	@Test
	void testGetRecommendations_ZeroNumberOfRecommendations() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description.two");
	    
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(books);

	    User user1 = new User("User1");
	    User user2 = new User("User2");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(4.0));
	    userRatings.put(user1, user1Ratings);
	    
	    Map<Book, BigDecimal> user2Ratings = new HashMap<>();
	    user2Ratings.put(book1, BigDecimal.valueOf(4.0));
	    user2Ratings.put(book2, BigDecimal.valueOf(8.0));
	    userRatings.put(user2, user2Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, 0));

	    assertTrue(recommendations.isEmpty()); 
	}
	@Test
	void testGetRecommendations_duplicateBooks() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description.two");
	    
	    List<Book> books = Arrays.asList(book1, book2, book2);
	    data.setBooks(books);

	    User user1 = new User("User1");
	    User user2 = new User("User2");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(4.0));
	    userRatings.put(user1, user1Ratings);
	    
	    Map<Book, BigDecimal> user2Ratings = new HashMap<>();
	    user2Ratings.put(book1, BigDecimal.valueOf(4.0));
	    user2Ratings.put(book2, BigDecimal.valueOf(8.0));
	    userRatings.put(user2, user2Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, 2));

	    assertEquals(recommendations.size(), 1); 
	    assertTrue(recommendations.contains(book2));
	}
	@Test
	void testGetRecommendations_userIsNull() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description.two");
	    
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(books);

	    User user1 = new User("User1");
	    User user2 = new User("User2");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(4.0));
	    userRatings.put(user1, user1Ratings);
	    
	    Map<Book, BigDecimal> user2Ratings = new HashMap<>();
	    user2Ratings.put(book1, BigDecimal.valueOf(4.0));
	    user2Ratings.put(book2, BigDecimal.valueOf(8.0));
	    userRatings.put(user2, user2Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);


	    Exception exception = assertThrows(NullPointerException.class, () -> {
		    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(null, 1));
		});
	}
	@Test
	void testGetRecommendations_userRatingsNotFound() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description two");
	    
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(books);

	    User user1 = new User("User1");
	    User user2 = new User("User2");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    
	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    Exception exception = assertThrows(UserRatingsNotFound.class, () -> {
		    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, 1));
		});

	}
	@Test
	void testGetRecommendations_twoCalls() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description two");
	    
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(books);

	    User user1 = new User("User1");
	    User user2 = new User("User2");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(4.0));
	    userRatings.put(user1, user1Ratings);
	    
	    Map<Book, BigDecimal> user2Ratings = new HashMap<>();
	    user2Ratings.put(book1, BigDecimal.valueOf(4.0));
	    user2Ratings.put(book2, BigDecimal.valueOf(8.0));
	    userRatings.put(user2, user2Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, 1));

	    assertEquals(recommendations.size(), 1); 
	    assertTrue(recommendations.contains(book2));
	    
	    recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, 1));
	    
	    assertEquals(recommendations.size(), 1); 
	    assertTrue(recommendations.contains(book2));
	}
	@Test
	void testGetRecommendations_negativeNumberOfRecommendations() {
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description two");
	    
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(books);

	    User user1 = new User("User1");
	    User user2 = new User("User2");

	    Map<User, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(4.0));
	    userRatings.put(user1, user1Ratings);
	    
	    Map<Book, BigDecimal> user2Ratings = new HashMap<>();
	    user2Ratings.put(book1, BigDecimal.valueOf(4.0));
	    user2Ratings.put(book2, BigDecimal.valueOf(8.0));
	    userRatings.put(user2, user2Ratings);

	    data.setUserRatings(userRatings);

	    CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(data);

	    List<Book> recommendations = Arrays.asList(collaborativeFiltering.getRecommendations(user1, -1));

	    assertTrue(recommendations.isEmpty()); 
	}
}
