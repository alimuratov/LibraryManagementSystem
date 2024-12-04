package main.recommendation;


import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import main.users.*;
import main.book.*;

public class ContentFiltering extends RecommendationStrategyBase {
	
	private TfIdfProcessor tfIdfProcessor;
		
	public ContentFiltering(Data data) {
        super(data);
        this.tfIdfProcessor = new TfIdfProcessor(this.getBooks());
    }
	
	@Override
	protected boolean isUserReadyForRecommendation(Customer user) {
		return user.getProfileVector() != null;
	}
	
	@Override
	protected void preprocessForUser(Customer user) {
		tfIdfPreprocessing();
		Map<String, Double> profileVector = createUserProfile(user);
		user.setProfileVector(profileVector);
	}
	
	public void tfIdfPreprocessing() {
		tfIdfProcessor.createTfIdfSeparately();
		tfIdfProcessor.computeTfIdfVectors();
	}
	
	private Map<Book, Double> computeBookSimilarities(Customer user) {
		Map<Book, Double> bookSimilarities = new HashMap<>();
		Map<String, Double> profileVector = user.getProfileVector();
		
		for (Book book : books) {
			if (!data.isBookRatedByUser(book, user)) {
				Map<String, Double> bookVector = tfIdfProcessor.getTfIdfVectorForBook(book.getBookTitle());
				double similarity = tfIdfProcessor.cosineSimilarity(bookVector, profileVector);
				// Test
				// System.out.println("Book: " + book.getBookTitle() + "; Similarity: " + similarity);
				bookSimilarities.put(book, similarity);
			}
		}
		
		return bookSimilarities;
	}
	
	private int adjustNumberOfRecommendations(int availableRecommendations, int requestedNumber) {
		if (availableRecommendations < requestedNumber) {
            System.out.println("Requested number of recommendations exceeds the number of available books.");
            return availableRecommendations;
        }
        return requestedNumber;
	}
	
	private Book[] getTopRecommendedBooks(Map<Book, Double> bookSimilarities, int numberOfRecommendations) {
        return bookSimilarities.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(numberOfRecommendations)
                .map(Map.Entry::getKey)
                .toArray(Book[]::new);
    }
	
	@Override
	protected Book[] generateRecommendations(Customer user, int numberOfRecommendations) {	
		Map<Book, Double> bookSimilarities = computeBookSimilarities(user); 
		
		if (bookSimilarities.isEmpty()) {
			System.out.println("No recommendations available. You may have already rated all the books.");
			return new Book[0];
		} 
		
		numberOfRecommendations = adjustNumberOfRecommendations(bookSimilarities.size(), numberOfRecommendations);
		
		return getTopRecommendedBooks(bookSimilarities, numberOfRecommendations);
    }
	
	public double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
		return tfIdfProcessor.cosineSimilarity(vec1, vec2);
	}
	
	public Map<String, Double> createUserProfile(Customer user) {
		List<String> preferredBookNames = getPreferredBookNames(user);
		Map<String, Double> userProfile = tfIdfProcessor.aggregateTfIdfVectors(preferredBookNames);
		return tfIdfProcessor.normalizeVector(userProfile); 
	}
	
	public List<String> getPreferredBookNames(Customer user) {		
		if (userRatings == null || !userRatings.containsKey(user)) {
			return Collections.emptyList();
		}
		
		Map<Book, BigDecimal> currentUserRatings = userRatings.get(user);
		if(currentUserRatings == null) {
			return Collections.emptyList();
		}
		
		return currentUserRatings.entrySet().stream()
				.filter(entry -> entry.getValue().compareTo(BigDecimal.valueOf(4))>=0)
				.map(entry -> entry.getKey().getBookTitle())
				.collect(Collectors.toList());
	}
	
	public Map<String, Double> normalizeVector(Map<String, Double> vector) {
		return tfIdfProcessor.normalizeVector(vector);
	}
	
	// getters and setters
	
	public void setNewData(Data data) {
		this.data = data;
		this.userRatings = data.getUserRatings();
		this.books = data.getBooks();
	}
	
	public List<Book> getBooks() {
		return books;
	}
	
	// for testing
	public Map<String, Map<String, Double>> getTfIdfDict() {
		return tfIdfProcessor.getTfIdfDict();
	}
	
	public Set<String> getCorpus() {
		return tfIdfProcessor.getCorpus();
	}
	
	public Map<String, Integer> getTermDocumentFreq() {
		return tfIdfProcessor.getTermDocumentFreq();
	}
	
	public Map<String, Map<String, Double>> getTfDict() {
		return tfIdfProcessor.getTfDict();
	}	
	
	public Map<String, Double> getIdfDict() {
		return tfIdfProcessor.getIdfDict();
	}
	
	public void createTfIdfSeparately() {
		tfIdfProcessor.createTfIdfSeparately();
	}
	
	public void computeTfIdfVectors() {
		tfIdfProcessor.computeTfIdfVectors();
	}
	
}