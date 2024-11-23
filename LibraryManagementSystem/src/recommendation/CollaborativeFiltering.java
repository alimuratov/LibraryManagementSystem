package recommendation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import book.*;
import users.*;


public class CollaborativeFiltering extends RecommendationStrategyBase {
    private Map<Book, Map<Book, BigDecimal>> bookSimilarities = new HashMap<>();
    private Map<Book, Map<Book, Integer>> frequencies = new HashMap<>();
    private Map<Customer, Map<Book, BigDecimal>> predictedCustomerRatings = new HashMap<>();    
    
    public CollaborativeFiltering(Data data) {
        super(data);
    }
    
    @Override
    public Book[] getRecommendations(Customer customer, int numberOfRecommendations) {
    	// if no predictions have been made for the customer, run slopeOne() algorithm
    	if(!predictedCustomerRatings.containsKey(customer)) {
			slopeOne();
    	}
    	
    	Map<Book, BigDecimal> ratedBooks = predictedCustomerRatings.get(customer);
    	
    	Book[] sortedBooks = ratedBooks.entrySet() // retrieves a set of Map.Entry<Book, BigDecimal>; this is necessary in order to perform operations that involve both keys and values (e.g., sorting)
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
    
    private void calculateDifferences() {
        for (Customer customer : customerRatings.keySet()) {
            for (Book book1 : customerRatings.get(customer).keySet()) {
                for (Book book2 : customerRatings.get(customer).keySet()) {
                    if (!book1.equals(book2)) {
                        BigDecimal rating1 = customerRatings.get(customer).get(book1);
                        BigDecimal rating2 = customerRatings.get(customer).get(book2);
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
        for (Customer customer : customerRatings.keySet()) {
            HashMap<Book, BigDecimal> currentCustomerRatings = new HashMap<>();
            Set<Book> ratedBooks = customerRatings.get(customer).keySet();

            for (Book book : books) {
                if (ratedBooks.contains(book)) {
                    currentCustomerRatings.put(book, customerRatings.get(customer).get(book));
                    continue;
                }

                BigDecimal numerator = BigDecimal.ZERO;
                BigDecimal denominator = BigDecimal.ZERO;

                for (Book ratedBook : ratedBooks) {
                    if (frequencies.containsKey(book) && frequencies.get(book).containsKey(ratedBook)) {
                        BigDecimal similarity = bookSimilarities.get(book).get(ratedBook);
                        int frequency = frequencies.get(book).get(ratedBook);

                        numerator = numerator.add(
                            similarity.add(customerRatings.get(customer).get(ratedBook))
                                    .multiply(BigDecimal.valueOf(frequency))
                        );
                        denominator = denominator.add(BigDecimal.valueOf(frequency));
                    }
                }
                
                // no customer has rated `book` and any of the books the current customer has rated
                // so for the current customer the prediction is based on the
                if (denominator.equals(BigDecimal.ZERO)) {
                    currentCustomerRatings.put(book, BigDecimal.ZERO);
                    continue;
                }

                BigDecimal prediction = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
                currentCustomerRatings.put(book, prediction);
            }
            predictedCustomerRatings.put(customer, currentCustomerRatings);
        }
    }

    public Map<Customer, Map<Book, BigDecimal>> getPredictedCustomerRatings() {
        return predictedCustomerRatings;
    }
}
