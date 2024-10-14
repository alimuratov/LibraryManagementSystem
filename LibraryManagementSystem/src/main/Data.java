package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.math.BigDecimal;

public class Data {
	protected static List<Book> books = Arrays.asList(
											new Book("Book1"),
											new Book("Book2"), 
											new Book("Book3") );
	
	public static Map<User, HashMap<Book, BigDecimal>> initializeData(int numOfUsers) {
		Map<User, HashMap<Book, BigDecimal>> data = new HashMap<>();
	    Random rand = new Random();
	    int booksToRate = Math.min(4, books.size());
		for (int i = 0; i < numOfUsers; i++) {
			User user = new User("User" + i);
			HashMap<Book, BigDecimal> bookRatings = new HashMap<>();
			Set<Book> ratedBooks = new HashSet<>();
			while (ratedBooks.size() < booksToRate) {
				Book book = books.get(rand.nextInt(books.size()));
				if (!ratedBooks.contains(book)) {
					bookRatings.put(book, BigDecimal.valueOf(rand.nextDouble() * 5));
					ratedBooks.add(book);
				}
			}	
			data.put(user, bookRatings);
		}
		return data;
	} 
	
}
