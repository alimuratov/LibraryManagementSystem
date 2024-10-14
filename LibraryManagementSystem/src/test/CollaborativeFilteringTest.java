package test;

import org.junit.jupiter.api.Test;

import main.Book;
import main.CollaborativeFiltering;
import main.User;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CollaborativeFilteringTest {
	
	protected static List<Book> books;
	
	@BeforeEach
	void setup() {
		books = Arrays.asList(
				new Book("Book1"),
				new Book("Book2"), 
				new Book("Book3") );
	}
	
	@Test
	void testWikipedia() {
		Map<User, HashMap<Book, BigDecimal>> data = new HashMap<>();
		
		User user1 = new User("John");
		HashMap<Book, BigDecimal> bookRatings1 = new HashMap<>();
		bookRatings1.put(books.get(0), BigDecimal.valueOf(5.0));
		bookRatings1.put(books.get(1), BigDecimal.valueOf(3.0));
		bookRatings1.put(books.get(2), BigDecimal.valueOf(2.0));
		data.put(user1, bookRatings1);
		
		User user2 = new User("Mark");
		HashMap<Book, BigDecimal> bookRatings2 = new HashMap<>();
		bookRatings2.put(books.get(0), BigDecimal.valueOf(3.0));
		bookRatings2.put(books.get(1), BigDecimal.valueOf(4.0));
		data.put(user2, bookRatings2);
		
		User user3 = new User("Lucy");
		HashMap<Book, BigDecimal> bookRatings3 = new HashMap<>();
		bookRatings3.put(books.get(1), BigDecimal.valueOf(2.0));
		bookRatings3.put(books.get(2), BigDecimal.valueOf(5.0));
		data.put(user3, bookRatings3);
		
		CollaborativeFiltering.slopeOne(data);
		Map<User, HashMap<Book, BigDecimal>> actualOutput = CollaborativeFiltering.getOutput();
		
		Map<User, HashMap<Book, BigDecimal>> expectedOutput = data;
		expectedOutput.get(user2).put(books.get(2), BigDecimal.valueOf(10).divide(BigDecimal.valueOf(3), 10, RoundingMode.HALF_UP));
		expectedOutput.get(user3).put(books.get(0), (new BigDecimal("13").divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP)));
		
		assertEquals(expectedOutput, actualOutput);
	}

}
