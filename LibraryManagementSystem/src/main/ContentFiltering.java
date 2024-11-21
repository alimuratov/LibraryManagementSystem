package main;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class ContentFiltering extends RecommendationStrategyBase {
		
	// shared data structures
	private Map<String, Map<String, Double>> tfDict = new HashMap<>();
	private Map<String, Double> idfDict = new HashMap<>();
	private Map<String, Map<String, Double>> tfIdfDict = new HashMap<>();
	private Set<String> corpus = new HashSet<>();
	private Map<String, Integer> termDocumentFreq = new HashMap<>();
	
	/*
	public static void printTfIdfDict(Map<String, Map<String, Double>> tfIdfDict) {
	    for (Map.Entry<String, Map<String, Double>> outerEntry : tfIdfDict.entrySet()) {
	        String key = outerEntry.getKey();
	        Map<String, Double> innerMap = outerEntry.getValue();

	        System.out.printf("Key: %s%n", key);
	        System.out.println("Values:");
	        for (Map.Entry<String, Double> innerEntry : innerMap.entrySet()) {
	            String innerKey = innerEntry.getKey();
	            Double value = innerEntry.getValue();
	            System.out.printf("  %s: %.4f%n", innerKey, value);
	        }
	        System.out.println();
	    }
	}
	
	public static void printProfileVector(Map<String, Double> profileVector) {
		
		System.out.println("_______________"); 
		
		if (profileVector.isEmpty()) {
		    System.out.println("The profile vector is empty.");
		} else {
		    System.out.println("The profile vector is not empty.");
		}

		
		for(Map.Entry<String, Double> entry : profileVector.entrySet()) {
			String term = entry.getKey();
			Double tfIdfVal = entry.getValue();
			System.out.printf("  %s: %.4f%n", term, tfIdfVal);
		}
		System.out.println();
	} 
	*/
		
	public ContentFiltering(Data data) {
        super(data);
    }
	
	public void tfIdfPreprocessing() {
		createTfIdfSeparately();
		computeTfIdfVectors();
	}
	
	public Book[] getBooksSimilarToUserProfile(User user, int numberOfRecommendations) {
		Map<Book, Double> bookSimilarities = new HashMap<>();
		Map<String, Double> profileVector = user.getProfileVector();
		
		// printProfileVector(profileVector);
		
		for (Book book : books) {
			if (!data.isBookRatedByUser(book, user)) {
				if (!tfIdfDict.containsKey(book.getBookName())) {
					throw new BookNotFoundException("Book not found in TfIdfDict: " + book.getBookName());
				}
				
				double similarity = cosineSimilarity(tfIdfDict.get(book.getBookName()), profileVector);
				bookSimilarities.put(book, similarity);
				
				// System.out.printf("Book: %s, Similarity with user profile: %f", book, similarity);
				// System.out.println();
			}
		}
		
		if (bookSimilarities.size() == 0) {
			System.out.println("No recommendations available. You may have already rated all the books.");
			return new Book[0]; 
		} else if (bookSimilarities.size() < numberOfRecommendations) {
			System.out.println("Requested number of recommendations exceeds the number of available books.");
			numberOfRecommendations = bookSimilarities.size();
		}
		
		Book[] recommendations = bookSimilarities.entrySet() 
    			.stream() 
    			.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
    			.limit(numberOfRecommendations)
    			.map(Map.Entry::getKey) 
    			.toArray(Book[]::new); 
		
		/* for (Book book: recommendations) {
			System.out.println(book);
		} */
		
		return recommendations;
	}
	
	@Override
	public Book[] getRecommendations(User user, int numberOfRecommendations) {	
		if (numberOfRecommendations <= 0) {
			System.out.println("Number of recommendations cannot be less than one.");
			return new Book[0];
		}
		if (user == null) {
			throw new NullPointerException("User cannot be null");
		}
		if (user.getProfileVector() == null) {
			tfIdfPreprocessing();
			Map<String, Double> profileVector = createUserProfile(user);
			user.setProfileVector(profileVector);
		}
		
		// printTfIdfDict(tfIdfDict);
		
		return getBooksSimilarToUserProfile(user, numberOfRecommendations);
		
    }
	
	public void createTfIdfSeparately() {
		for (Book book : books) {
			Map<String, Integer> termCounter = new HashMap<>();
			String bookDescription = book.getBookDescription();
			
			// remove punctuation marks
			String cleanedBookDescription = bookDescription.replaceAll("[^\\w\\s]", "");
			
			// split descriptions into words
			String[] rawTerms = cleanedBookDescription.split("\\s+");
			
			List<String> filteredTerms = new ArrayList<>();
			
			for (String term : rawTerms) {
			    if (!term.isEmpty()) {
			        filteredTerms.add(term.toLowerCase());
			    }
			}
			
			String[] terms = filteredTerms.toArray(new String[0]);
			
			int totalTermsInDoc = terms.length; 
			
			Set<String> seenTerms = new HashSet<>();
			
			for (String term : terms) {
				termCounter.put(term, termCounter.getOrDefault(term, 0) + 1);
				corpus.add(term);
				
				if (!seenTerms.contains(term)) {
					termDocumentFreq.put(term, termDocumentFreq.getOrDefault(term, 0) + 1);
					seenTerms.add(term);
				}
				
			}
			
			Map<String, Double> tf_entry = new HashMap<>();
			for (Map.Entry<String, Integer> termEntry : termCounter.entrySet()) {
				double tf = (double) termEntry.getValue() / totalTermsInDoc;
				tf_entry.put(termEntry.getKey(), tf);
			}
			tfDict.put(book.getBookName(), tf_entry);

		}
		
		int totalDocuments = books.size();
		for (String term : corpus) {
			int docFreq = termDocumentFreq.get(term);
			double idf = (double) totalDocuments / docFreq;
			idfDict.put(term, idf);
		}
		
	}
	
	// computes tfIdf vectors of each book
	public void computeTfIdfVectors() {
		for (Book book : books) {
			Map<String, Double> tfEntry = tfDict.get(book.getBookName());
			Map<String, Double> tfIdfEntry = new HashMap<>();
			
			for (String term : corpus) {
				double tf = tfEntry.getOrDefault(term, 0.0);
				double idf = idfDict.get(term);
				tfIdfEntry.put(term, tf*idf);
			}
			
			tfIdfDict.put(book.getBookName(), normalizeVector(tfIdfEntry));
		}
	}
	
	public double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
		Set<String> intersection = new HashSet<>(vec1.keySet());
		intersection.retainAll(vec2.keySet());
		
		double dotProduct = 0.0;
		double vec1Norm = 0.0;
		double vec2Norm = 0.0;
		
		for (String term : intersection) {
			double val1 = vec1.get(term);
			double val2 = vec2.get(term);
			
			dotProduct += val1 * val2;
		}
		
		for (double value : vec1.values()) {
			vec1Norm += value * value;
		}
		vec1Norm = Math.sqrt(vec1Norm);
		
		for (double value : vec2.values()) {
			vec2Norm += value * value; 
		}
		vec2Norm = Math.sqrt(vec2Norm);
		
		if (vec1Norm == 0 || vec2Norm == 0) {
			return 0.0;
		}
		
		return dotProduct / (vec1Norm * vec2Norm);
	}
	
	public Map<String, Double> createUserProfile(User user) {
		List<String> preferredBookNames = getPreferredBookNames(user);
		
		Map<String, Double> userProfile = new HashMap<>();
		
		for (String term : corpus) {
			userProfile.put(term, 0.0);
		}
		
		for (String bookName : preferredBookNames) {
			if (!tfIdfDict.containsKey(bookName)) {
				throw new BookNotFoundException("Book not found in TfIdfDict: " + bookName);
			}
			Map<String, Double> tfIdfEntry = tfIdfDict.get(bookName);
			
			for (String term : tfIdfEntry.keySet()) {
				userProfile.put(term, userProfile.get(term) + tfIdfEntry.get(term));
			}
		}
		
		return normalizeVector(userProfile); 
	}
	
	public List<String> getPreferredBookNames(User user) {
		List<String> preferredBookNames = new ArrayList<>();
		
		if (userRatings == null || !userRatings.containsKey(user)) {
			return preferredBookNames;
		}
		
		Map<Book, BigDecimal> currentUserRatings = userRatings.get(user);
		if(currentUserRatings == null) {
			return preferredBookNames;
		}
		
		for (Map.Entry<Book, BigDecimal> bookRatingEntry : currentUserRatings.entrySet()) {
			if (bookRatingEntry.getValue().compareTo(BigDecimal.valueOf(4)) >= 0) {
				preferredBookNames.add(bookRatingEntry.getKey().getBookName());
			}		
		}
		
		return preferredBookNames;
	}
	
	public Map<String, Double> normalizeVector(Map<String, Double> vector) {
		double norm = 0.0;
		
		for (double value : vector.values()) {
			if (value < 0) {
				throw new IllegalArgumentException("TF-IDF values cannot be negative.");
			} else if (Double.isNaN(value)) {
				throw new IllegalArgumentException("TF-IDF values cannot be NaN.");
			}
			norm += value * value;
		}
		
		norm = Math.sqrt(norm);
		
		if (norm == 0) { 
			return vector;
		}
		
		Map<String, Double> normalized = new HashMap<>();
		
		for (Map.Entry<String, Double> entry : vector.entrySet()) {
			normalized.put(entry.getKey(), entry.getValue() / norm);
		}
		
		return normalized;
	}
	
	// for testing
	public Map<String, Map<String, Double>> getTfIdfDict() {
		return tfIdfDict;
	}
	
	public Set<String> getCorpus() {
		return corpus;
	}
	
	public Map<String, Integer> getTermDocumentFreq() {
		return termDocumentFreq;
	}
	
	public Map<String, Map<String, Double>> getTfDict() {
		return tfDict;
	}	
	
	public Map<String, Double> getIdfDict() {
		return idfDict;
	}
	
	public void setNewData(Data data) {
		this.data = data;
		this.userRatings = data.getUserRatings();
		this.books = data.getBooks();
	}
}
