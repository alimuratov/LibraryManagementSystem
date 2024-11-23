package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    protected boolean isUserReadyForRecommendation(User user) {
    	return predictedUserRatings.containsKey(user);
    }
    
    @Override
    protected void preprocessForUser(User user) {
    	slopeOne();
    }
    
    
    @Override
    protected Book[] generateRecommendations(User user, int numberOfRecommendations) {

    	Map<Book, BigDecimal> initialRatings = userRatings.get(user);
    	Map<Book, BigDecimal> predictedRatings = predictedUserRatings.get(user);
    	
    	if(hasInitialAndPredictedRatings(initialRatings, predictedRatings)) {
    		removeAlreadyRatedBooks(predictedRatings, initialRatings);
    	}
    	
    	if (predictedRatings.isEmpty()) {
    		System.out.println("No recommendations available. You may have already rated all the books.");
			return new Book[0]; 
    	}
    	
    	numberOfRecommendations = adjustNumberOfRecommendations(predictedRatings.size(), numberOfRecommendations);
    	
    	return getTopRecommendations(predictedRatings, numberOfRecommendations);
    }
    
    private boolean hasInitialAndPredictedRatings(Map<Book, BigDecimal> initialRatings, Map<Book, BigDecimal> predictedRatings) {
        return initialRatings != null && predictedRatings != null;
    }
    
    public void slopeOne() {
        calculateDifferences();
        predictRatings();
    }
    
    /* 
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
    } */
    
    public void calculateDifferences() {
    	userRatings.forEach((user, ratings) -> {
    		List<Book> booksRated = new ArrayList<>(ratings.keySet());
    		int size = booksRated.size();
    		
    		for (int i = 0; i < size; i++) {
    			Book book1 = booksRated.get(i);
    			BigDecimal rating1 = ratings.get(book1);
    			
    			for (int j = i + 1; j < size; j++) {
    				Book book2 = booksRated.get(j);
    				BigDecimal rating2 = ratings.get(book2);
    				
    				BigDecimal diff = rating1.subtract(rating2);
    				frequencies.computeIfAbsent(book1, k -> new HashMap<>())
    						   .merge(book2, 1, Integer::sum);
    				frequencies.computeIfAbsent(book2, k -> new HashMap<>())
    						   .merge(book1, 1, Integer::sum);
    				
    				bookSimilarities.computeIfAbsent(book1, k -> new HashMap<>())
                    			    .merge(book2, diff, BigDecimal::add);
    				
                    bookSimilarities.computeIfAbsent(book2, k -> new HashMap<>())
                    			    .merge(book1, diff.negate(), BigDecimal::add);
    			}
    		}
    	});
    	
        bookSimilarities.forEach((book1, map) -> {
            map.replaceAll((book2, sumDiff) -> {
                int count = frequencies.get(book1).get(book2);
                return sumDiff.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
            });
        });
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
    
    private void removeAlreadyRatedBooks(Map<Book, BigDecimal> predictedRatings, Map<Book, BigDecimal> initialRatings) {
    	predictedRatings.keySet().removeAll(initialRatings.keySet());
    }
    
    private int adjustNumberOfRecommendations(int availableRecommendations, int requestedNumber) {
    	if (availableRecommendations < requestedNumber) {
    		System.out.println("Requested number of recommendations exceeds the number of available books.");
            return availableRecommendations;
    	}
    	return requestedNumber;
    }
    
    private Book[] getTopRecommendations(Map<Book, BigDecimal> predictedRatings, int numberOfRecommendations) {
    	return predictedRatings.entrySet() // retrieves a set of Map.Entry<Book, BigDecimal>; this is necessary in order to perform operations that involve both keys and values (e.g., sorting)
    			.stream() // since we converted Map into a collection of entries, we can stream this collection and use  
    			.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // sorting books by their ratings in descending order 
    			.limit(numberOfRecommendations)
    			.map(Map.Entry::getKey) // extracting keys (Book objects) 
    			.toArray(Book[]::new);
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
