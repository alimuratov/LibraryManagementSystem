package main.recommendation;

import main.book.*;
import main.users.*;

public interface RecommendationStrategy {
	Book[] getRecommendations(Customer customer, int numberOfRecommendations);
}
