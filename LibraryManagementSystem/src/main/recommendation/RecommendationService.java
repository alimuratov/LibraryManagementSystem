package main.recommendation;

import main.book.*;
import main.users.*;

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
