package main;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

	private List<Book> books;
	private Map<User, Map<Book, BigDecimal>> userRatings;
	
	public Data() {}

	public Data(Map<User, Map<Book, BigDecimal>> userRatings, List<Book> books) {
		this.userRatings = userRatings;
		this.books = books;
	}
	
	public List<Book> getBooks() {
		return books;
	}

	public Map<User, Map<Book, BigDecimal>> getUserRatings() {
		return userRatings;
	}

	public void setUserRatings(Map<User, Map<Book, BigDecimal>> userRatings) {
		this.userRatings = userRatings;
		
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	public boolean isBookRatedByUser(Book book, User user) {
		if((userRatings.containsKey(user) && userRatings.get(user).containsKey(book))) return true;
		return false;
	}
}
