package main;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import main.book.Book;
import main.users.User;

public class CollaborativeFiltering {
	private static Map<Book, Map<Book, BigDecimal>> bookSimilarities = new HashMap<>();
	private static Map<Book, Map<Book, Integer>> frequencies = new HashMap<>();
	private static Map<User, HashMap<Book, BigDecimal>> inputData;
	private static Map<User, HashMap<Book, BigDecimal>> outputData = new HashMap<>();
	
	public static void slopeOne(int numOfUsers) {
		inputData = Data.initializeData(numOfUsers);
		calculateDifferences();
		predictRatings();
	}
	
	public static void slopeOne(Map<User, HashMap<Book, BigDecimal>> data) {
		inputData = data;
		calculateDifferences();
		predictRatings();
	}
	
	private static void calculateDifferences() {
		for (User user : inputData.keySet()) {
			for (Book book1 : inputData.get(user).keySet()) {
				for (Book book2 : inputData.get(user).keySet()) {
					if (!book1.equals(book2)) {
						BigDecimal rating1 = inputData.get(user).get(book1);
						BigDecimal rating2 = inputData.get(user).get(book2);
						BigDecimal diff = rating1.subtract(rating2);
						if (!frequencies.containsKey(book1)) {
							frequencies.put(book1, new HashMap<>());
						}
						if (!frequencies.get(book1).containsKey(book2)) {
							frequencies.get(book1).put(book2, 0);
						}
						if (!bookSimilarities.containsKey(book1)) {
							bookSimilarities.put(book1, new HashMap<>());
						}
						if (!bookSimilarities.get(book1).containsKey(book2)) {
							bookSimilarities.get(book1).put(book2, new BigDecimal("0.0"));
						}
						int oldCount = frequencies.get(book1).get(book2);
						BigDecimal oldSimilarity = bookSimilarities.get(book1).get(book2);
						BigDecimal newSimilarity = (oldSimilarity.multiply(BigDecimal.valueOf(oldCount)).add(diff)).divide(BigDecimal.valueOf(oldCount+1));
						frequencies.get(book1).put(book2, oldCount + 1);
						bookSimilarities.get(book1).put(book2, newSimilarity);
					}
				}
			}
		}
	}
	
	private static void predictRatings() {
	    for (User user : inputData.keySet()) {
	        HashMap<Book, BigDecimal> userRatings = new HashMap<>();
	        
	        Set<Book> ratedBooks = inputData.get(user).keySet();

	        for (Book book : Data.books) {
	            if (ratedBooks.contains(book)) {
	            	userRatings.put(book, inputData.get(user).get(book));
	                continue;
	            }

	            BigDecimal numerator = BigDecimal.ZERO;
	            BigDecimal denominator = BigDecimal.ZERO;

	            for (Book ratedBook : ratedBooks) {
	                if (frequencies.get(book).containsKey(ratedBook)) {
	                    BigDecimal similarity = bookSimilarities.get(book).get(ratedBook);
	                    int frequency = frequencies.get(book).get(ratedBook);

	                    numerator = numerator.add(
	                        similarity.add(inputData.get(user).get(ratedBook))
	                            .multiply(BigDecimal.valueOf(frequency))
	                    );
	                    denominator = denominator.add(BigDecimal.valueOf(frequency));
	                }
	            }

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

	
	public static Map<User, HashMap<Book, BigDecimal>> getOutput() {
		return outputData;
	}
	
}
