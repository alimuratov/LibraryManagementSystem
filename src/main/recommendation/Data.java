package main.recommendation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.book.*;
import main.users.*;

public class Data {

	private List<Book> books;
	private Map<Customer, Map<Book, BigDecimal>> userRatings;
	
	public Data() {}

	public Data(Map<Customer, Map<Book, BigDecimal>> userRatings, List<Book> books) {
		this.userRatings = userRatings;
		this.books = books;
	}
	
	public List<Book> getBooks() {
		return books;
	}

	public Map<Customer, Map<Book, BigDecimal>> getUserRatings() {
		return userRatings;
	}

	public void setUserRatings(Map<Customer, Map<Book, BigDecimal>> userRatings) {
		this.userRatings = userRatings;
		
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	public boolean isBookRatedByUser(Book book, Customer user) {
		if((userRatings.containsKey(user) && userRatings.get(user).containsKey(book))) {
			return true;
		}
		return false;
	}
}
