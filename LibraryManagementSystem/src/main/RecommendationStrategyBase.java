package main;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public abstract class RecommendationStrategyBase implements RecommendationStrategy {
	protected Data data;
	protected Map<User, Map<Book, BigDecimal>> userRatings; 
	protected List<Book> books; 
	
	public RecommendationStrategyBase(Data data) {
		this.data = data;
		this.userRatings = data.getUserRatings();
		this.books = data.getBooks();
	}
	
	public Data getData() {
		return data;
	}
	
	public void setData(Data data) {
		this.data = data;
		this.userRatings = data.getUserRatings();
	}
	
	public Map<User, Map<Book, BigDecimal>> getUserRatings() {
        return userRatings;
    }
	
	@Override
    public abstract Book[] getRecommendations(User user, int numberOfRecommendations);
}
