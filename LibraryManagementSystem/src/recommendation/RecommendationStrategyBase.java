package recommendation;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import book.*;
import users.*;

public abstract class RecommendationStrategyBase implements RecommendationStrategy {
	protected Data data;
	protected Map<Customer, Map<Book, BigDecimal>> customerRatings; 
	protected List<Book> books; 
	
	public RecommendationStrategyBase(Data data) {
		this.data = data;
		this.customerRatings = data.getCustomerRatings();
		this.books = data.getBooks();
	}
	
	public Data getData() {
		return data;
	}
	
	public void setData(Data data) {
		this.data = data;
		this.customerRatings = data.getCustomerRatings();
	}
	
	public Map<Customer, Map<Book, BigDecimal>> getCustomerRatings() {
        return customerRatings;
    }
	
	@Override
    public abstract Book[] getRecommendations(Customer customer, int numberOfRecommendations);
}
