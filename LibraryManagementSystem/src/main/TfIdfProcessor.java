package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TfIdfProcessor {
	
	private Map<String, Map<String, Double>> tfDict = new HashMap<>();
	private Map<String, Double> idfDict = new HashMap<>();
	private Map<String, Map<String, Double>> tfIdfDict = new HashMap<>();
	private Set<String> corpus = new HashSet<>();
	private Map<String, Integer> termDocumentFreq = new HashMap<>();
	private List<Book> books;
	
	public TfIdfProcessor(List<Book> books) {
		this.books = books;
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
	
	public Map<String, Double> aggregateTfIdfVectors(List<String> bookNames) {
		Map<String, Double> aggregatedProfile = new HashMap<>();
		
		for (String term : corpus) {
			aggregatedProfile.put(term, 0.0);
		}
		
		for (String bookName : bookNames) {
			if (!tfIdfDict.containsKey(bookName)) {
				throw new BookNotFoundException("Book not found in TfIdfDict: " + bookName);
			}
			Map<String, Double> tfIdfEntry = tfIdfDict.get(bookName);
			
			for (String term : tfIdfEntry.keySet()) {
				aggregatedProfile.put(term, aggregatedProfile.get(term) + tfIdfEntry.get(term));
			}
		}
		
		return aggregatedProfile;
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
	
	public Map<String, Double> getTfIdfVectorForBook(String bookName) {
	    if (!tfIdfDict.containsKey(bookName)) {
	        throw new BookNotFoundException("Book not found in TfIdfDict: " + bookName);
	    }
	    return tfIdfDict.get(bookName);
	}

	
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
	
}
