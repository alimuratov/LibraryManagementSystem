package main;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RandomStubData implements Data {
	
	private List<Book> books;
	private Map<User, HashMap<Book, BigDecimal>> data = new HashMap<User, HashMap<Book, BigDecimal>>(); 
	private long seed;
	
	public RandomStubData(int numOfUsers, List<Book> books, long seed) {
		this.books = books;
		this.seed = seed;
		initializeData(numOfUsers); 
	}

	public Map<User, HashMap<Book, BigDecimal>> initializeData(int numOfUsers) {
	    Random rand = new Random(seed);
		for (int i = 1; i <= numOfUsers; i++) {
			User user = new User("User" + i);
			HashMap<Book, BigDecimal> bookRatings = new HashMap<>();
			
			for (Book book : books) {
				// 50% chance of rating this book
				if(rand.nextBoolean()) {
					BigDecimal rating = BigDecimal.valueOf(1+rand.nextInt(5));
					bookRatings.put(book, rating);
				}
			}
			data.put(user, bookRatings);
		}
		return data;
	}
	
	@Override
	public Map<User, HashMap<Book, BigDecimal>> getData() {
		return data;
	}

	@Override
	public List<Book> getBooks() {
		return books;
	}
	

}
