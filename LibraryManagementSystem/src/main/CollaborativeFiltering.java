package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollaborativeFiltering {
    private Map<Book, Map<Book, BigDecimal>> bookSimilarities = new HashMap<>();
    private Map<Book, Map<Book, Integer>> frequencies = new HashMap<>();
    private Map<User, HashMap<Book, BigDecimal>> inputData;
    private Map<User, HashMap<Book, BigDecimal>> outputData = new HashMap<>();
    
    private Data data; 
    
    public CollaborativeFiltering(Data data) {
        this.data = data;
    }
    
    public void slopeOne() {
        inputData = data.getData();
        calculateDifferences();
        predictRatings();
    }
    
    private void calculateDifferences() {
        for (User user : inputData.keySet()) {
            for (Book book1 : inputData.get(user).keySet()) {
                for (Book book2 : inputData.get(user).keySet()) {
                    if (!book1.equals(book2)) {
                        BigDecimal rating1 = inputData.get(user).get(book1);
                        BigDecimal rating2 = inputData.get(user).get(book2);
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
        for (User user : inputData.keySet()) {
            HashMap<Book, BigDecimal> userRatings = new HashMap<>();
            Set<Book> ratedBooks = inputData.get(user).keySet();

            for (Book book : data.getBooks()) {
                if (ratedBooks.contains(book)) {
                    userRatings.put(book, inputData.get(user).get(book));
                    continue;
                }

                BigDecimal numerator = BigDecimal.ZERO;
                BigDecimal denominator = BigDecimal.ZERO;

                for (Book ratedBook : ratedBooks) {
                    if (frequencies.containsKey(book) && frequencies.get(book).containsKey(ratedBook)) {
                        BigDecimal similarity = bookSimilarities.get(book).get(ratedBook);
                        int frequency = frequencies.get(book).get(ratedBook);

                        numerator = numerator.add(
                            similarity.add(inputData.get(user).get(ratedBook))
                                    .multiply(BigDecimal.valueOf(frequency))
                        );
                        denominator = denominator.add(BigDecimal.valueOf(frequency));
                    }
                }
                
                // no user has rated `book` and any of the books the current user has rated
                // so for the current user the prediction is based on the
                if (denominator.equals(BigDecimal.ZERO)) {
                    userRatings.put(book, BigDecimal.ZERO);
                    continue;
                }

                BigDecimal prediction = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
                userRatings.put(book, prediction);
            }
            outputData.put(user, userRatings);
        }
    }

    public Map<User, HashMap<Book, BigDecimal>> getOutput() {
        return outputData;
    }
    
    public Map<User, HashMap<Book, BigDecimal>> getInput() {
    	return inputData;
    }
    
    public void setData(Data data) {
    	this.data = data;
    }
    
    public Data getData() {
    	return data; 
    }
}
