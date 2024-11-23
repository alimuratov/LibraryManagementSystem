package recommendation;

import book.*;
import users.*;

public class RecommendationService {
	private RecommendationStrategy strategy;
	
	public void setStrategy(RecommendationStrategy strategy) {
		this.strategy = strategy;
	}
	
	public Book[] getRecommendations(Customer customer, int numberOfRecommendations) {
		if(strategy == null) {
			// do something
		}
		return strategy.getRecommendations(customer, numberOfRecommendations);
	}
}
