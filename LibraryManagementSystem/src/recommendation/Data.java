package recommendation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import book.*;
import users.*;

public class Data {

	private List<Book> books;
	private Map<Customer, Map<Book, BigDecimal>> customerRatings;
	
	public Data() {}

	public Data(Map<Customer, Map<Book, BigDecimal>> customerRatings, List<Book> books) {
		this.customerRatings = customerRatings;
		this.books = books;
	}
	
	public List<Book> getBooks() {
		return books;
	}

	public Map<Customer, Map<Book, BigDecimal>> getCustomerRatings() {
		return customerRatings;
	}

	public void setCustomerRatings(Map<Customer, Map<Book, BigDecimal>> customerRatings) {
		this.customerRatings = customerRatings;
		
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	public boolean isBookRatedByCustomer(Book book, Customer customer) {
		if((customerRatings.containsKey(customer) && customerRatings.get(customer).containsKey(book))) return true;
		return false;
	}
}
