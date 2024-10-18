package main;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class PredefinedStubData implements Data {
	
	private List<Book> books;
	private Map<User, HashMap<Book, BigDecimal>> data;

	public PredefinedStubData(Map<User, HashMap<Book, BigDecimal>> data, List<Book> books) {
		this.data = data;
		this.books = books;
	}
	
	@Override
	public List<Book> getBooks() {
		return books;
	}
	
	@Override
	public Map<User, HashMap<Book, BigDecimal>> getData() {
		return data; 
	}
	
}
