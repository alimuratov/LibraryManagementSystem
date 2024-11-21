package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.*;


public class CollaborativeFiltering extends RecommendationStrategyBase {
    private Map<Book, Map<Book, BigDecimal>> bookSimilarities = new HashMap<>();
    private Map<Book, Map<Book, Integer>> frequencies = new HashMap<>();
    private Map<User, Map<Book, BigDecimal>> predictedUserRatings = new HashMap<>();    
    
    public CollaborativeFiltering(Data data) {
        super(data);
    }
    
    @Override
    public Book[] getRecommendations(User user, int numberOfRecommendations) throws UserRatingsNotFound {
    	if (numberOfRecommendations <= 0) {
			System.out.println("Number of recommendations cannot be less than one.");
			return new Book[0];
		}
		if (user == null) {
			throw new NullPointerException("User cannot be null");
		}
    	// if no predictions have been made for the user, run slopeOne() algorithm
		if (!predictedUserRatings.containsKey(user)) {
		    slopeOne();
		    if (!predictedUserRatings.containsKey(user)) {
		        throw new UserRatingsNotFound("User's rating records are not found in the database.");
		    }
		}

    	
    	Map<Book, BigDecimal> initialRatings = userRatings.get(user);
    	Map<Book, BigDecimal> predictedRatings = predictedUserRatings.get(user);
    	
    	if(initialRatings != null && predictedRatings != null) {
    		predictedRatings.keySet().removeAll(initialRatings.keySet());
    	}
    	
    	if (predictedRatings.size() == 0) {
    		System.out.println("No recommendations available. You may have already rated all the books.");
			return new Book[0]; 
    	}
    	
    	if (predictedRatings.size() < numberOfRecommendations) {
    		System.out.println("Requested number of recommendations exceeds the number of available books.");
    		numberOfRecommendations = predictedRatings.size();
    	}
    	
    	Book[] sortedBooks = predictedRatings.entrySet() // retrieves a set of Map.Entry<Book, BigDecimal>; this is necessary in order to perform operations that involve both keys and values (e.g., sorting)
    			.stream() // since we converted Map into a collection of entries, we can stream this collection and use  
    			.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // sorting books by their ratings in descending order 
    			.limit(numberOfRecommendations)
    			.map(Map.Entry::getKey) // extracting keys (Book objects) 
    			.toArray(Book[]::new); // storing those extracted keys in an array
    	
    	return sortedBooks; 
    }
    
    public void slopeOne() {
        calculateDifferences();
        predictRatings();
    }
    
    public void calculateDifferences() {
        for (User user : userRatings.keySet()) {
            for (Book book1 : userRatings.get(user).keySet()) {
                for (Book book2 : userRatings.get(user).keySet()) {
                    if (!book1.equals(book2)) {
                        BigDecimal rating1 = userRatings.get(user).get(book1);
                        BigDecimal rating2 = userRatings.get(user).get(book2);
                        BigDecimal diff = rating1.subtract(rating2);
                        frequencies.computeIfAbsent(book1, k -> new HashMap<>())
                                   .merge(book2, 1, Integer::sum);
                        bookSimilarities.computeIfAbsent(book1, k -> new HashMap<>())
                                        .merge(book2, diff, (oldVal, newVal) -> 
                                            oldVal.multiply(BigDecimal.valueOf(frequencies.get(book1).get(book2) - 1))
                                                   .add(newVal)
                                                   .divide(BigDecimal.valueOf(frequencies.get(book1).get(book2)), RoundingMode.HALF_UP)
                                        );
                    }
                }
            }
        }
    }
    
    private void predictRatings() {
        for (User user : userRatings.keySet()) {
            HashMap<Book, BigDecimal> currentUserRatings = new HashMap<>();
            Set<Book> ratedBooks = userRatings.get(user).keySet();

            for (Book book : books) {
                if (ratedBooks.contains(book)) {
                    currentUserRatings.put(book, userRatings.get(user).get(book));
                    continue;
                }

                BigDecimal numerator = BigDecimal.ZERO;
                BigDecimal denominator = BigDecimal.ZERO;

                for (Book ratedBook : ratedBooks) {
                    if (frequencies.containsKey(book) && frequencies.get(book).containsKey(ratedBook)) {
                        BigDecimal similarity = bookSimilarities.get(book).get(ratedBook);
                        int frequency = frequencies.get(book).get(ratedBook);

                        numerator = numerator.add(
                            similarity.add(userRatings.get(user).get(ratedBook))
                                    .multiply(BigDecimal.valueOf(frequency))
                        );
                        denominator = denominator.add(BigDecimal.valueOf(frequency));
                    }
                }
                
                if (denominator.equals(BigDecimal.ZERO)) {
                    currentUserRatings.put(book, BigDecimal.ZERO);
                    continue;
                }

                BigDecimal prediction = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
                currentUserRatings.put(book, prediction);
            }
            predictedUserRatings.put(user, currentUserRatings);
        }
    }

    public Map<User, Map<Book, BigDecimal>> getPredictedUserRatings() {
        return predictedUserRatings;
    }
    
    public Map<Book, Map<Book, BigDecimal>> getBookSimilarities() {
    	return bookSimilarities;
    }
    
    public Map<Book, Map<Book, Integer>> getFrequencies() {
    	return frequencies;
    }
}
