package main.recommendation;

import main.book.*;
import main.users.*;
import main.exceptions.*;

public interface RecommendationStrategy {
	Book[] getRecommendations(Customer user, int numberOfRecommendations) 
			throws UserRatingsNotFound,
				   BookNotFoundException;
	
	Data getData(); 
}