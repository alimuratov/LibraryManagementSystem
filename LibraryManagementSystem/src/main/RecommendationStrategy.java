package main;

public interface RecommendationStrategy {
	Book[] getRecommendations(User user, int numberOfRecommendations) 
			throws UserRatingsNotFound,
				   BookNotFoundException;
}
