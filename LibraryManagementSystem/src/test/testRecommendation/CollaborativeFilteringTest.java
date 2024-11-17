package test;

import org.junit.jupiter.api.Test;

import main.book.*;
import main.recommendation.*;
import main.system.Password;
import main.users.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CollaborativeFilteringTest {
	
	protected static List<Book> books;
	
	public void printData(Data dataObj) {
		Map<Customer, Map<Book, BigDecimal>> data = dataObj.getCustomerRatings();
		for (Map.Entry<Customer, Map<Book, BigDecimal>> customerEntry : data.entrySet()) {
			Customer customer = customerEntry.getKey();
			System.out.println("Customer: " + customer.getUserName());
			
			Map<Book, BigDecimal> bookRatings = customerEntry.getValue();
			for (Map.Entry<Book, BigDecimal> bookEntry : bookRatings.entrySet()) {
				Book book = bookEntry.getKey();
				BigDecimal rating = bookEntry.getValue();
				System.out.println("	Book:" + book.getBookTitle() + ", Rating: " + rating);
			}
		}
	}
	
	@BeforeEach
	void setup() {
		books = Arrays.asList(
				new Book("Book1", "desciprtion1"),
				new Book("Book2", "desciprtion2"), 
				new Book("Book3", "desciprtion3") );
	}
	
	@Disabled
	@Test
	void testWikipedia() {
		Map<Customer, Map<Book, BigDecimal>> data = new HashMap<>();
		
		Customer customer1 = new Customer("John", new Password("JohnPassword"));
		Map<Book, BigDecimal> bookRatings1 = new HashMap<>();
		bookRatings1.put(books.get(0), BigDecimal.valueOf(5.0));
		bookRatings1.put(books.get(1), BigDecimal.valueOf(3.0));
		bookRatings1.put(books.get(2), BigDecimal.valueOf(2.0));
		data.put(customer1, bookRatings1);
		
		Customer customer2 = new Customer("Mark", new Password("MarkPassword"));
		Map<Book, BigDecimal> bookRatings2 = new HashMap<>();
		bookRatings2.put(books.get(0), BigDecimal.valueOf(3.0));
		bookRatings2.put(books.get(1), BigDecimal.valueOf(4.0));
		data.put(customer2, bookRatings2);
		
		Customer customer3 = new Customer("Lucy", new Password("LucyPassword"));
		Map<Book, BigDecimal> bookRatings3 = new HashMap<>();
		bookRatings3.put(books.get(1), BigDecimal.valueOf(2.0));
		bookRatings3.put(books.get(2), BigDecimal.valueOf(5.0));
		data.put(customer3, bookRatings3);
		
		PredefinedStubData stubData = new PredefinedStubData(data, books);
		CollaborativeFiltering collFilter = new CollaborativeFiltering(stubData);
		collFilter.slopeOne();
		Map<Customer, Map<Book, BigDecimal>> actualOutput = collFilter.getOutput();
		
		Map<Customer, Map<Book, BigDecimal>> expectedOutput = data;
		expectedOutput.get(customer2).put(books.get(2), BigDecimal.valueOf(10).divide(BigDecimal.valueOf(3), 10, RoundingMode.HALF_UP));
		expectedOutput.get(customer3).put(books.get(0), (new BigDecimal("13").divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP)));
		
		assertEquals(expectedOutput, actualOutput);
	}

}
