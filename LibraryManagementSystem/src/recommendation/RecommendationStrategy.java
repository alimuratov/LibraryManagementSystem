package recommendation;

import book.*;
import users.*;

public interface RecommendationStrategy {
	Book[] getRecommendations(Customer customer, int numberOfRecommendations);
}
