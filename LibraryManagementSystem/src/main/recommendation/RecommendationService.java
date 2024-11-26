package main.recommendation;

import main.book.*;
import main.users.*;
import main.recommendation.*;

public class RecommendationService {
	private RecommendationStrategy strategy;
	
	public void setStrategy(String strategyType, Data data) {
        this.strategy = RecommendationStrategyFactory.createStrategy(strategyType, data);
    }
	
	public Book[] getRecommendations(Customer user, int numberOfRecommendations) {
        if (strategy == null) {
            System.out.println("Set the recommendation strategy.");
            return new Book[0]; 
        }
        return strategy.getRecommendations(user, numberOfRecommendations);
    }
	
}
