package main.recommendation;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import main.users.*;
import main.book.*;
import main.exceptions.*;

public abstract class RecommendationStrategyBase implements RecommendationStrategy {
	protected Data data;
	protected Map<Customer, Map<Book, BigDecimal>> userRatings; 
	protected List<Book> books; 
	
	public RecommendationStrategyBase(Data data) {
		this.data = data;
		this.userRatings = data.getUserRatings();
		this.books = data.getBooks();
	}
	
	public Data getData() {
		return data;
	}
	
	public void setData(Data data) {
		this.data = data;
		this.userRatings = data.getUserRatings();
	}
	
	public Map<Customer, Map<Book, BigDecimal>> getUserRatings() {
        return userRatings;
    }
	
	@Override
    public Book[] getRecommendations(Customer user, int numberOfRecommendations) throws UserRatingsNotFound {
		if (numberOfRecommendations <= 0) {
			System.out.println("Number of recommendations cannot be less than one.");
			return new Book[0];
		}
		if (user == null) {
			throw new NullPointerException("User cannot be null");
		}
		
		if(!isUserReadyForRecommendation(user)) {
			preprocessForUser(user);
			if(!isUserReadyForRecommendation(user)) {
				throw new UserRatingsNotFound("User's rating records are not found in the database.");
			}
		}
		
		Book[] recommendations = generateRecommendations(user, numberOfRecommendations);
		
		if (recommendations.length == 0) {
            System.out.println("No recommendations available.");
            return new Book[0];
		} else if (recommendations.length < numberOfRecommendations) {
            System.out.println("Requested number of recommendations exceeds the number of available books.");
		}
		
		return recommendations;
	}
	
	protected abstract boolean isUserReadyForRecommendation(Customer user);
	
	protected abstract void preprocessForUser(Customer user);
	
	protected abstract Book[] generateRecommendations(Customer user, int numberOfRecommendations);
}
