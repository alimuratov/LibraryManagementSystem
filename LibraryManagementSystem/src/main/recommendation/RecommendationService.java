package main.recommendation;

import main.book.*;
import main.users.*;

public class RecommendationService {
	private RecommendationStrategy strategy;
	
	public void setStrategy(RecommendationStrategy strategy) {
		this.strategy = strategy;
	}
	
	public Book[] getRecommendations(Customer user, int numberOfRecommendations) {
		if(strategy == null) {
			System.out.println("Set the recommendation strategy.");
		}
		return strategy.getRecommendations(user, numberOfRecommendations);
	}
}
