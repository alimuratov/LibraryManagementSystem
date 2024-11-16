package main;

public class RecommendationService {
	private RecommendationStrategy strategy;
	
	public void setStrategy(RecommendationStrategy strategy) {
		this.strategy = strategy;
	}
	
	public Book[] getRecommendations(User user, int numberOfRecommendations) {
		if(strategy == null) {
			// do something
		}
		return strategy.getRecommendations(user, numberOfRecommendations);
	}
}
