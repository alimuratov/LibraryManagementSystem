package test.testRecommendation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import main.recommendation.*;
import main.book.*;
import main.users.*;
import main.exceptions.*;

import java.math.BigDecimal;
import java.util.*;

public class ContentFilteringTest {
	private ContentFiltering contentFiltering;
	private Data testData;
	
	private Customer user1;
	private Book book1, book2, book3, book4;
	
	
	@BeforeEach
	void setUp() {
		book1 = new Book("Book One", "An adventure in the mountains");
        book2 = new Book("Book Two", "A thrilling mystery in the city");
        book3 = new Book("Book Three", "Romantic tales by the sea");
        book4 = new Book("Book Four", "Science fiction and space exploration");
                
        List<Book> books = Arrays.asList(book1, book2, book3, book4);
        
        user1 = new Customer("user1");
        
        Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
        Map<Book, BigDecimal> user1Ratings = new HashMap<>();
        user1Ratings.put(book1, BigDecimal.valueOf(5));
        user1Ratings.put(book2, BigDecimal.valueOf(3));
        user1Ratings.put(book3, BigDecimal.valueOf(4));
        user1Ratings.put(book4, BigDecimal.valueOf(2));
        userRatings.put(user1, user1Ratings);
        
        testData = new Data();
        testData.setBooks(books);
        testData.setUserRatings(userRatings);
        
        contentFiltering = new ContentFiltering(testData);
	}
	
	@Disabled
	@Test
	void test_1() {
		Book[] recommendations = contentFiltering.getRecommendations(user1, 2);
		
		List<Book> ratedBooks = Arrays.asList(book1, book3);
		
		for (Book book : recommendations) {
			assertFalse(ratedBooks.contains(book));
		}
		
		assertEquals(2, recommendations.length);
	}
	
	@Test
	void testCreateTfIdfSeparately_NormalDescriptions() {
		List<Book> books = Arrays.asList(
		            new Book("Book A", "The quick brown fox"),
		            new Book("Book B", "Jumped over the lazy dog"),
		            new Book("Book C", "The fox was quick and brown")
		        );

        Data data = new Data();
        data.setBooks(books);

        contentFiltering = new ContentFiltering(data);
        contentFiltering.createTfIdfSeparately();
        
        Set<String> expectedCorpus = new HashSet<>(Arrays.asList(
                "the", "quick", "brown", "fox", "jumped", "over", "lazy", "dog", "was", "and"
        ));
        
        // Check corpus correctness
        assertEquals(expectedCorpus, contentFiltering.getCorpus());
        
        // Check termDocumentFreq correctness
        Map<String, Integer> expectedTermDocFreq = new HashMap<>();
        expectedTermDocFreq.put("the", 3);
        expectedTermDocFreq.put("quick", 2);
        expectedTermDocFreq.put("brown", 2);
        expectedTermDocFreq.put("fox", 2);
        expectedTermDocFreq.put("jumped", 1);
        expectedTermDocFreq.put("over", 1);
        expectedTermDocFreq.put("lazy", 1);
        expectedTermDocFreq.put("dog", 1);
        expectedTermDocFreq.put("was", 1);
        expectedTermDocFreq.put("and", 1);
        assertEquals(expectedTermDocFreq, contentFiltering.getTermDocumentFreq());
        
        // Check tfDict correctness for each book
        Map<String, Double> expectedTfBookA = new HashMap<>();
        expectedTfBookA.put("the", 1.0 / 4);
        expectedTfBookA.put("quick", 1.0 / 4);
        expectedTfBookA.put("brown", 1.0 / 4);
        expectedTfBookA.put("fox", 1.0 / 4);
        assertEquals(expectedTfBookA, contentFiltering.getTfDict().get("Book A"));
        
        Map<String, Double> expectedTfBookB = new HashMap<>();
        expectedTfBookB.put("jumped", 1.0 / 5);
        expectedTfBookB.put("over", 1.0 / 5);
        expectedTfBookB.put("the", 1.0 / 5);
        expectedTfBookB.put("lazy", 1.0 / 5);
        expectedTfBookB.put("dog", 1.0 / 5);
        assertEquals(expectedTfBookB, contentFiltering.getTfDict().get("Book B"));
        
        Map<String, Double> expectedTfBookC = new HashMap<>();
        expectedTfBookC.put("the", 1.0 / 6); // "The", "fox", "was", "quick", "and", "brown" => 6 terms
        expectedTfBookC.put("fox", 1.0 / 6);
        expectedTfBookC.put("was", 1.0 / 6);
        expectedTfBookC.put("quick", 1.0 / 6);
        expectedTfBookC.put("and", 1.0 / 6);
        expectedTfBookC.put("brown", 1.0 / 6);
        assertEquals(expectedTfBookC, contentFiltering.getTfDict().get("Book C"));
        
        Map<String, Double> expectedIdfDict = new HashMap<>();
        int totalDocuments = 3;
        expectedIdfDict.put("the", (double) totalDocuments / 3); 
        expectedIdfDict.put("quick", (double) totalDocuments / 2); 
        expectedIdfDict.put("brown", (double) totalDocuments / 2); 
        expectedIdfDict.put("fox", (double) totalDocuments / 2); 
        expectedIdfDict.put("jumped", (double) totalDocuments / 1); 
        expectedIdfDict.put("over", (double) totalDocuments / 1); 
        expectedIdfDict.put("lazy", (double) totalDocuments / 1); 
        expectedIdfDict.put("dog", (double) totalDocuments / 1); 
        expectedIdfDict.put("was", (double) totalDocuments / 1); 
        expectedIdfDict.put("and", (double) totalDocuments / 1); 
        assertEquals(expectedIdfDict, contentFiltering.getIdfDict());

	}
	@Test
	void testCreateTfIdfSeparately_EmptyDescription() {
	    List<Book> books = Arrays.asList(
	        new Book("Book D", ""),
	        new Book("Book E", "Silent night")
	    );

	    Data data = new Data();
	    data.setBooks(books);
	    contentFiltering = new ContentFiltering(data);

	    contentFiltering.createTfIdfSeparately();

	    // Confirm corpus correctness
	    Set<String> expectedCorpus = new HashSet<>(Arrays.asList("silent", "night"));
	    assertEquals(expectedCorpus, contentFiltering.getCorpus());

	    // Confirm termDocumentFreq correctness
	    Map<String, Integer> expectedTermDocFreq = new HashMap<>();
	    expectedTermDocFreq.put("silent", 1);
	    expectedTermDocFreq.put("night", 1);
	    assertEquals(expectedTermDocFreq, contentFiltering.getTermDocumentFreq());

	    // Confirm tfDict for each book
	    Map<String, Double> expectedTfBookD = new HashMap<>();
	    assertEquals(expectedTfBookD, contentFiltering.getTfDict().get("Book D"));

	    Map<String, Double> expectedTfBookE = new HashMap<>();
	    expectedTfBookE.put("silent", 1.0 / 2); 
	    expectedTfBookE.put("night", 1.0 / 2);
	    assertEquals(expectedTfBookE, contentFiltering.getTfDict().get("Book E"));

	    // Verify idfDict
	    Map<String, Double> expectedIdfDict = new HashMap<>();
	    expectedIdfDict.put("silent", 2.0 / 1); 
	    expectedIdfDict.put("night", 2.0 / 1); 
	    assertEquals(expectedIdfDict, contentFiltering.getIdfDict(), "IDF dictionary does not match.");
	}
	@Test
	void testCreateTfIdfSeparately_OnlyPunctuation() {
	    List<Book> books = Arrays.asList(
	        new Book("Book F", "!!! ??? ,,,"),
	        new Book("Book G", "Hello, world!")
	    );

	    Data data = new Data();
	    data.setBooks(books);
	    contentFiltering = new ContentFiltering(data);

	    contentFiltering.createTfIdfSeparately();

	    Set<String> expectedCorpus = new HashSet<>(Arrays.asList("hello", "world"));
	    assertEquals(expectedCorpus, contentFiltering.getCorpus());

	    Map<String, Integer> expectedTermDocFreq = new HashMap<>();
	    expectedTermDocFreq.put("hello", 1);
	    expectedTermDocFreq.put("world", 1);
	    assertEquals(expectedTermDocFreq, contentFiltering.getTermDocumentFreq());

	    Map<String, Double> expectedTfBookF = new HashMap<>();
	    assertEquals(expectedTfBookF, contentFiltering.getTfDict().get("Book F"));

	    Map<String, Double> expectedTfBookG = new HashMap<>();
	    expectedTfBookG.put("hello", 1.0 / 2);
	    expectedTfBookG.put("world", 1.0 / 2);
	    assertEquals(expectedTfBookG, contentFiltering.getTfDict().get("Book G"));

	    // Verify idfDict
	    Map<String, Double> expectedIdfDict = new HashMap<>();
	    expectedIdfDict.put("hello", 2.0 / 1); 
	    expectedIdfDict.put("world", 2.0 / 1);
	    assertEquals(expectedIdfDict, contentFiltering.getIdfDict());
	}
	@Test
	void testCreateTfIdfSeparately_DuplicateTerms() {
	    List<Book> books = Arrays.asList(
	        new Book("Book H", "Data science is fun. Data science is challenging."),
	        new Book("Book I", "Machine learning and data science go hand in hand.")
	    );

	    Data data = new Data();
	    data.setBooks(books);
	    contentFiltering = new ContentFiltering(data);

	    contentFiltering.createTfIdfSeparately();

	    Set<String> expectedCorpus = new HashSet<>(Arrays.asList(
	        "data", "science", "is", "fun", "challenging", "machine",
	        "learning", "and", "go", "hand", "in"
	    ));
	    assertEquals(expectedCorpus, contentFiltering.getCorpus());

	    Map<String, Integer> expectedTermDocFreq = new HashMap<>();
	    expectedTermDocFreq.put("data", 2);
	    expectedTermDocFreq.put("science", 2);
	    expectedTermDocFreq.put("is", 1);
	    expectedTermDocFreq.put("fun", 1);
	    expectedTermDocFreq.put("challenging", 1);
	    expectedTermDocFreq.put("machine", 1);
	    expectedTermDocFreq.put("learning", 1);
	    expectedTermDocFreq.put("and", 1);
	    expectedTermDocFreq.put("go", 1);
	    expectedTermDocFreq.put("hand", 1);
	    expectedTermDocFreq.put("in", 1);
	    assertEquals(expectedTermDocFreq, contentFiltering.getTermDocumentFreq());

	    Map<String, Double> expectedTfBookH = new HashMap<>();
	    expectedTfBookH.put("data", 2.0 / 8); 
	    expectedTfBookH.put("science", 2.0 / 8);
	    expectedTfBookH.put("is", 2.0 / 8);
	    expectedTfBookH.put("fun", 1.0 / 8);
	    expectedTfBookH.put("challenging", 1.0 / 8);
	    assertEquals(expectedTfBookH, contentFiltering.getTfDict().get("Book H"));

	    Map<String, Double> expectedTfBookI = new HashMap<>();
	    expectedTfBookI.put("machine", 1.0 / 9);
	    expectedTfBookI.put("learning", 1.0 / 9);
	    expectedTfBookI.put("and", 1.0 / 9);
	    expectedTfBookI.put("data", 1.0 / 9);
	    expectedTfBookI.put("science", 1.0 / 9);
	    expectedTfBookI.put("go", 1.0 / 9);
	    expectedTfBookI.put("hand", 2.0 / 9);
	    expectedTfBookI.put("in", 1.0 / 9);
	    assertEquals(expectedTfBookI, contentFiltering.getTfDict().get("Book I"));

	    Map<String, Double> expectedIdfDict = new HashMap<>();
	    int totalDocuments = 2;
	    expectedIdfDict.put("data", (double) totalDocuments / 2);
	    expectedIdfDict.put("science", (double) totalDocuments / 2); 
	    expectedIdfDict.put("is", (double) totalDocuments / 1); 
	    expectedIdfDict.put("fun", (double) totalDocuments / 1); 
	    expectedIdfDict.put("challenging", (double) totalDocuments / 1); 
	    expectedIdfDict.put("machine", (double) totalDocuments / 1); 
	    expectedIdfDict.put("learning", (double) totalDocuments / 1); 
	    expectedIdfDict.put("and", (double) totalDocuments / 1); 
	    expectedIdfDict.put("go", (double) totalDocuments / 1); 
	    expectedIdfDict.put("hand", (double) totalDocuments / 1); 
	    expectedIdfDict.put("in", (double) totalDocuments / 1); 
	    assertEquals(expectedIdfDict, contentFiltering.getIdfDict());
	}
	
	@Test
	void testCreateTfIdfSeparately_CaseSensitivity() {
	    List<Book> books = Arrays.asList(
	        new Book("Book J", "Science is fascinating."),
	        new Book("Book K", "science drives innovation.")
	    );

	    Data data = new Data();
	    data.setBooks(books);
	    contentFiltering = new ContentFiltering(data);

	    contentFiltering.createTfIdfSeparately();

	    Set<String> expectedCorpus = new HashSet<>(Arrays.asList(
	        "science", "is", "fascinating", "drives", "innovation"
	    ));
	    assertEquals(expectedCorpus, contentFiltering.getCorpus());

	    Map<String, Integer> expectedTermDocFreq = new HashMap<>();
	    expectedTermDocFreq.put("science", 2);
	    expectedTermDocFreq.put("is", 1);
	    expectedTermDocFreq.put("fascinating", 1);
	    expectedTermDocFreq.put("drives", 1);
	    expectedTermDocFreq.put("innovation", 1);
	    assertEquals(expectedTermDocFreq, contentFiltering.getTermDocumentFreq());

	    Map<String, Double> expectedTfBookJ = new HashMap<>();
	    expectedTfBookJ.put("science", 1.0 / 3);
	    expectedTfBookJ.put("is", 1.0 / 3);
	    expectedTfBookJ.put("fascinating", 1.0 / 3);
	    assertEquals(expectedTfBookJ, contentFiltering.getTfDict().get("Book J"));

	    Map<String, Double> expectedTfBookK = new HashMap<>();
	    expectedTfBookK.put("science", 1.0 / 3);
	    expectedTfBookK.put("drives", 1.0 / 3);
	    expectedTfBookK.put("innovation", 1.0 / 3);
	    assertEquals(expectedTfBookK, contentFiltering.getTfDict().get("Book K"));

	    Map<String, Double> expectedIdfDict = new HashMap<>();
	    int totalDocuments = 2;
	    expectedIdfDict.put("science", (double) totalDocuments / 2); 
	    expectedIdfDict.put("is", (double) totalDocuments / 1); 
	    expectedIdfDict.put("fascinating", (double) totalDocuments / 1); 
	    expectedIdfDict.put("drives", (double) totalDocuments / 1); 
	    expectedIdfDict.put("innovation", (double) totalDocuments / 1); 
	    assertEquals(expectedIdfDict, contentFiltering.getIdfDict());
	}
	@Test
	void testCreateTfIdfSeparately_IdenticalDescriptions() {
	    List<Book> books = Arrays.asList(
	        new Book("Book L", "AI and machine learning."),
	        new Book("Book M", "AI and machine learning.")
	    );

	    Data data = new Data();
	    data.setBooks(books);
	    contentFiltering = new ContentFiltering(data);

	    contentFiltering.createTfIdfSeparately();

	    Set<String> expectedCorpus = new HashSet<>(Arrays.asList("ai", "and", "machine", "learning"));
	    assertEquals(expectedCorpus, contentFiltering.getCorpus());

	    Map<String, Integer> expectedTermDocFreq = new HashMap<>();
	    expectedTermDocFreq.put("ai", 2);
	    expectedTermDocFreq.put("and", 2);
	    expectedTermDocFreq.put("machine", 2);
	    expectedTermDocFreq.put("learning", 2);
	    assertEquals(expectedTermDocFreq, contentFiltering.getTermDocumentFreq());

	    Map<String, Double> expectedTfBookL = new HashMap<>();
	    expectedTfBookL.put("ai", 1.0 / 4);
	    expectedTfBookL.put("and", 1.0 / 4);
	    expectedTfBookL.put("machine", 1.0 / 4);
	    expectedTfBookL.put("learning", 1.0 / 4);
	    assertEquals(expectedTfBookL, contentFiltering.getTfDict().get("Book L"));

	    Map<String, Double> expectedTfBookM = new HashMap<>();
	    expectedTfBookM.put("ai", 1.0 / 4);
	    expectedTfBookM.put("and", 1.0 / 4);
	    expectedTfBookM.put("machine", 1.0 / 4);
	    expectedTfBookM.put("learning", 1.0 / 4);
	    assertEquals(expectedTfBookM, contentFiltering.getTfDict().get("Book M"));

	    Map<String, Double> expectedIdfDict = new HashMap<>();
	    int totalDocuments = 2;
	    expectedIdfDict.put("ai", (double) totalDocuments / 2); 
	    expectedIdfDict.put("and", (double) totalDocuments / 2); 
	    expectedIdfDict.put("machine", (double) totalDocuments / 2);
	    expectedIdfDict.put("learning", (double) totalDocuments / 2);
	    assertEquals(expectedIdfDict, contentFiltering.getIdfDict());
	}
	@Test
	void testCreateTfIdfSeparately_LargeVocabulary() {
	    StringBuilder descriptionS = new StringBuilder();
	    for (int i = 1; i <= 100; i++) {
	        descriptionS.append("Term").append(i).append(" ");
	    }

	    StringBuilder descriptionT = new StringBuilder();
	    for (int i = 101; i <= 200; i++) {
	        descriptionT.append("Term").append(i).append(" ");
	    }
	    
	    List<Book> books = Arrays.asList(
	        new Book("Book S", descriptionS.toString()),
	        new Book("Book T", descriptionT.toString())
	    );

	    Data data = new Data();
	    data.setBooks(books);
	    contentFiltering = new ContentFiltering(data);

	    contentFiltering.createTfIdfSeparately();

	    assertEquals(200, contentFiltering.getCorpus().size());

	    for (int i = 1; i <= 200; i++) {
	        String term = "term" + i;
	        assertEquals(1, contentFiltering.getTermDocumentFreq().get(term).intValue());
	    }

	    Map<String, Double> tfBookS = contentFiltering.getTfDict().get("Book S");
	    assertEquals(100, tfBookS.size());
	    for (int i = 1; i <= 100; i++) {
	        String term = "term" + i;
	        assertEquals(1.0 / 100, tfBookS.get(term), 1e-6);
	    }

	    Map<String, Double> tfBookT = contentFiltering.getTfDict().get("Book T");
	    assertEquals(100, tfBookT.size());
	    for (int i = 101; i <= 200; i++) {
	        String term = "term" + i;
	        assertEquals(1.0 / 100, tfBookT.get(term), 1e-6);
	    }

	    for (int i = 1; i <= 200; i++) {
	        String term = "term" + i;
	        assertEquals(2.0 / 1, contentFiltering.getIdfDict().get(term), 1e-6);
	    }
	}
	
	@Test
	void testCosineSimilarity_BothZeroVectors() {
		ContentFiltering contentFiltering = new ContentFiltering(new Data());
		Double similarity = contentFiltering.cosineSimilarity(new HashMap<String, Double>(), new HashMap<String, Double>()); 
		assertEquals(similarity, 0.0);
	}
	@Test
	void testCosineSimilarity_FirstZeroVector() {
		ContentFiltering contentFiltering = new ContentFiltering(new Data());
		Map<String, Double> vec2 = new HashMap<String, Double>();
		vec2.put("One", 1.0);
		Double similarity = contentFiltering.cosineSimilarity(new HashMap<String, Double>(), vec2); 
		assertEquals(similarity, 0.0);
	}
	@Test
	void testCosineSimilarity_SecondZeroVector() {
		ContentFiltering contentFiltering = new ContentFiltering(new Data());
		Map<String, Double> vec2 = new HashMap<String, Double>();
		vec2.put("One", 1.0);
		Double similarity = contentFiltering.cosineSimilarity(vec2, new HashMap<String, Double>()); 
		assertEquals(similarity, 0.0);
	}
	@Test
	void testNormalizeVector_StandardPositiveValues() {
        Map<String, Double> input = new HashMap<>();
        input.put("a", 3.0);
        input.put("b", 4.0);

        Map<String, Double> expected = new HashMap<>();
        expected.put("a", 0.6);
        expected.put("b", 0.8);
        
        Map<String, Double> actual = contentFiltering.normalizeVector(input);

        assertEquals(expected, actual);
	}
	@Test
	void testNormalizeVector_NegativeValuesException() {
	    Map<String, Double> vector = new HashMap<>();
	    vector.put("x", -1.0);
	    vector.put("y", 2.0);
	    vector.put("z", -2.0);

	    assertThrows(IllegalArgumentException.class, () -> {
            contentFiltering.normalizeVector(vector);
        });
	}
	@Test
	void testNormalizeVector_EmptyVector() {
	    Map<String, Double> input = new HashMap<>();

	    Map<String, Double> actual = contentFiltering.normalizeVector(input);
	    
	    assertTrue(actual.isEmpty());
	}
	@Test
	void testNormalizeVector_AllZeroValues() {
	    Map<String, Double> input = new HashMap<>();
	    input.put("a", 0.0);
	    input.put("b", 0.0);
	    input.put("c", 0.0);

	    Map<String, Double> actual = contentFiltering.normalizeVector(input);

	    assertEquals(input, actual);
	}
	@Test
	void testNormalizeVector_SingleElementPositive() {
	    Map<String, Double> input = new HashMap<>();
	    input.put("only", 5.0);

	    Map<String, Double> expected = new HashMap<>();
	    expected.put("only", 1.0);

	    Map<String, Double> actual = contentFiltering.normalizeVector(input);

	    assertEquals(expected, actual);
	}
	@Test
	void testNormalizeVector_VeryLargeValues() {
	    Map<String, Double> input = new HashMap<>();
	    input.put("a", 1e9);
	    input.put("b", 1e9);

	    double norm = Math.sqrt(1e18 + 1e18);

	    Map<String, Double> expected = new HashMap<>();
	    expected.put("a", 1e9 / norm); 
	    expected.put("b", 1e9 / norm);

	    Map<String, Double> actual = contentFiltering.normalizeVector(input);

	    assertEquals(expected, actual);
	}
	@Test
	void testNormalizeVector_VerySmallValues() {
	    Map<String, Double> input = new HashMap<>();
	    input.put("a", 1e-9);
	    input.put("b", 2e-9);

	    double norm = Math.sqrt(1e-18 + 4e-18); 

	    Map<String, Double> expected = new HashMap<>();
	    expected.put("a", 1e-9 / norm); 
	    expected.put("b", 2e-9 / norm); 

	    Map<String, Double> actual = contentFiltering.normalizeVector(input);

	    assertEquals(expected, actual);
	}
	@Test
	void testNormalizeVector_MixedZeroAndNonZeroValues() {
	    Map<String, Double> input = new HashMap<>();
	    input.put("a", 0.0);
	    input.put("b", 3.0);
	    input.put("c", 4.0);

	    Map<String, Double> expected = new HashMap<>();
	    expected.put("a", 0.0);       
	    expected.put("b", 3.0 / 5.0); 
	    expected.put("c", 4.0 / 5.0); 

	    Map<String, Double> actual = contentFiltering.normalizeVector(input);

	    assertEquals(expected, actual);
	}
	@Test
	void testNormalizeVector_NegativeZero() {
	    Map<String, Double> input = new HashMap<>();
	    input.put("a", -0.0);
	    input.put("b", 1.0);

	    Map<String, Double> expected = new HashMap<>();
	    expected.put("a", -0.0); 
	    expected.put("b", 1.0); 

	    Map<String, Double> actual = contentFiltering.normalizeVector(input);

	    assertEquals(expected, actual);
	}
	@Test
	void testNormalizeVector_NaNAndInfinityValues() {
	    Map<String, Double> input = new HashMap<>();
	    input.put("a", Double.NaN);
	    input.put("b", Double.POSITIVE_INFINITY);
	    input.put("c", 1.0);

	    assertThrows(IllegalArgumentException.class, () -> {
            contentFiltering.normalizeVector(input);
        });
	}
	@Test
	void testNormalizeVector_NullInput() {
	    Map<String, Double> input = null;

	    assertThrows(NullPointerException.class, () -> {
	        contentFiltering.normalizeVector(input);
	    });
	}
	
	@Test
    void testGetPreferredBookNames_MultipleRatings() {
	    List<Book> books = Arrays.asList(
	    		new Book("Book One", "An adventure in the mountains"),
	    		new Book("Book Two", "A thrilling mystery in the city"),
	    		new Book("Book Three", "Romantic tales by the sea"),
	    		new Book("Book Four", "Science fiction and space exploration")
		);
		    
        List<String> expected = Arrays.asList("Book One", "Book Three");
        List<String> actual = contentFiltering.getPreferredBookNames(user1);
        
        assertEquals(expected, actual);
    } 
	@Test
	void testGetPreferredBookNames_UserRatingsNull() {
		Data data = new Data();
	    // Set userRatings to null
	    contentFiltering.setNewData(data);
	    
	    List<String> expected = Collections.emptyList();
	    List<String> actual = contentFiltering.getPreferredBookNames(user1);
	    
	    assertEquals(expected, actual);
	}
	@Test
	void testGetPreferredBookNames_UserNotInUserRatings() {
	    Customer user2 = new Customer("user2"); // New user not in userRatings

	    List<String> expected = Collections.emptyList();
	    List<String> actual = contentFiltering.getPreferredBookNames(user2);
	    
	    assertEquals(expected, actual);
	}
	@Test
	void testGetPreferredBookNames_UserRatingsMapNull() {
	    Customer user3 = new Customer("user3");
	    Data data = new Data();
	    data.setBooks(null);
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    userRatings.put(user3, null); 
	    
	    data.setUserRatings(userRatings);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    List<String> expected = Collections.emptyList();
	    List<String> actual = contentFiltering.getPreferredBookNames(user3);
	    
	    assertEquals(expected, actual);
	}
	@Test
	void testGetPreferredBookNames_AllRatingsBelowThreshold() {
		Data data = new Data();
	    Customer user5 = new Customer("user5");
	    Book book5 = new Book("Book Five", "An insightful biography");
	    Book book6 = new Book("Book Six", "Mystery of the ancient world");
	    
	    Map<Book, BigDecimal> user5Ratings = new HashMap<>();
	    user5Ratings.put(book5, BigDecimal.valueOf(2));
	    user5Ratings.put(book6, BigDecimal.valueOf(3.5));
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    userRatings.put(user5, user5Ratings);
	    data.setBooks(Arrays.asList(book5, book6));
	    data.setUserRatings(userRatings);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    List<String> expected = Collections.emptyList();
	    List<String> actual = contentFiltering.getPreferredBookNames(user5);
	    
	    assertEquals(expected, actual);
	}
	@Test
	void testGetPreferredBookNames_AllRatingsAtThreshold() {
		Data data = new Data();
	    Customer user6 = new Customer("user6");
	    Book book7 = new Book("Book Seven", "Historical events in depth");
	    Book book8 = new Book("Book Eight", "Exploring the unknown");
	    
	    Map<Book, BigDecimal> user6Ratings = new HashMap<>();
	    user6Ratings.put(book7, BigDecimal.valueOf(4));
	    user6Ratings.put(book8, BigDecimal.valueOf(4));
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    userRatings.put(user6, user6Ratings);
	    
	    data.setBooks(Arrays.asList(book7, book8));
	    data.setUserRatings(userRatings);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    List<String> expected = Arrays.asList("Book Seven", "Book Eight");
	    List<String> actual = contentFiltering.getPreferredBookNames(user6);
	    
	    assertEquals(expected, actual);
	}
	@Test
	void testGetPreferredBookNames_RatingsJustBelowAndAboveThreshold() {
		Data data = new Data();
	    Customer user7 = new Customer("user7");
	    Book book9 = new Book("Book Nine", "Thrilling suspense story");
	    Book book10 = new Book("Book Ten", "Innovative scientific discoveries");
	    
	    Map<Book, BigDecimal> user7Ratings = new HashMap<>();
	    user7Ratings.put(book9, BigDecimal.valueOf(3.99));
	    user7Ratings.put(book10, BigDecimal.valueOf(4.01));
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    userRatings.put(user7, user7Ratings);
	    
	    data.setBooks(Arrays.asList(book9, book10));
	    data.setUserRatings(userRatings);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    List<String> expected = Arrays.asList("Book Ten");
	    List<String> actual = contentFiltering.getPreferredBookNames(user7);
	    
	    assertEquals(expected, actual);
	}
	@Test
	void testGetPreferredBookNames_BooksWithIdenticalNames() {
		Data data = new Data();
	    Customer user10 = new Customer("user10");
	    Book book11a = new Book("Book Eleven", "Detailed analysis of quantum physics", 1);
	    Book book11b = new Book("Book Eleven", "Comprehensive guide to astrophysics", 2);
	    
	    Map<Book, BigDecimal> user10Ratings = new HashMap<>();
	    user10Ratings.put(book11a, BigDecimal.valueOf(5));
	    user10Ratings.put(book11b, BigDecimal.valueOf(4));
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    userRatings.put(user10, user10Ratings);
	    
	    data.setBooks(Arrays.asList(book11a, book11b));
	    data.setUserRatings(userRatings);
	    	
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    List<String> expected = Arrays.asList("Book Eleven", "Book Eleven");
	    List<String> actual = contentFiltering.getPreferredBookNames(user10);
	    
	    assertEquals(expected, actual);
	}
	@Test
	void testGetPreferredBookNames_UsersWithIdenticalNames() {
		Data data = new Data();
	    Customer user11a = new Customer("user11", 1);
	    Customer user11b = new Customer("user11", 2); 
	    Book book14 = new Book("Book Fourteen", "Exploring deep space");
	    Book book15 = new Book("Book Fifteen", "Advanced robotics");
	    
	    Map<Book, BigDecimal> user11aRatings = new HashMap<>();
	    user11aRatings.put(book14, BigDecimal.valueOf(4));
	    
	    
	    Map<Book, BigDecimal> user11bRatings = new HashMap<>();
	    user11bRatings.put(book15, BigDecimal.valueOf(5));
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    userRatings.put(user11a, user11aRatings);
	    userRatings.put(user11b, user11bRatings);
	    
	    data.setBooks(Arrays.asList(book14, book15));
	    data.setUserRatings(userRatings);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    List<String> expectedUser11a = Arrays.asList("Book Fourteen");
	    List<String> actualUser11a = contentFiltering.getPreferredBookNames(user11a);
	    
	    assertEquals(expectedUser11a, actualUser11a);
	    
	    List<String> expectedUser11b = Arrays.asList("Book Fifteen");
	    List<String> actualUser11b = contentFiltering.getPreferredBookNames(user11b);
	    
	    assertEquals(expectedUser11b, actualUser11b);
	}

	void testComputeTfIdfVectors_BooksAreNotSet() {
		Data data = new Data();
		ContentFiltering contentFiltering = new ContentFiltering(data);
		contentFiltering.createTfIdfSeparately();
		assertTrue(contentFiltering.getTfIdfDict().isEmpty());
	}
	@Test
	void testComputeTfIdfVectors_NonEmptyBooksWithValidTfAndIdf() {
		Data data = new Data();
		
	    Book book1 = new Book("Book One", "Description One");
	    Book book2 = new Book("Book Two", "Description Two");
	    List<Book> books = Arrays.asList(book1, book2);
	    
	    data.setBooks(books);
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    contentFiltering.createTfIdfSeparately();
	    contentFiltering.computeTfIdfVectors();

	    Map<String, Map<String, Double>> tfIdfDict = contentFiltering.getTfIdfDict();

	    Map<String, Double> expectedTfIdfBook1 = new HashMap<>();
	    expectedTfIdfBook1.put("description", 0.5); 
	    expectedTfIdfBook1.put("one", 1.0); 
	    expectedTfIdfBook1.put("two", 0.0); 
	    Map<String, Double> normalizedTfIdfBook1 = contentFiltering.normalizeVector(expectedTfIdfBook1);

	    assertEquals(normalizedTfIdfBook1, tfIdfDict.get("Book One"));

	    Map<String, Double> expectedTfIdfBook2 = new HashMap<>();
	    expectedTfIdfBook2.put("description", 0.5); 
	    expectedTfIdfBook2.put("one", 0.0); 
	    expectedTfIdfBook2.put("two", 1.0);
	    Map<String, Double> normalizedTfIdfBook2 = contentFiltering.normalizeVector(expectedTfIdfBook2);

	    assertEquals(normalizedTfIdfBook2, tfIdfDict.get("Book Two"));
	}
	@Test
	void testComputeTfIdfVectors_EmptyBooksList() {
		Data data = new Data();
		
	    List<Book> books = new ArrayList<>();
	    
	    data.setBooks(books);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    contentFiltering.createTfIdfSeparately();
	    contentFiltering.computeTfIdfVectors();
	    
	    Map<String, Map<String, Double>> tfIdfDict = contentFiltering.getTfIdfDict();
	    
	    assertTrue(tfIdfDict.isEmpty());
	}
	@Test
	void testComputeTfIdfVectors_OneBook() {
		Data data = new Data();
		
	    Book book = new Book("Book One", "Description One");
	    List<Book> books = Arrays.asList(book);
	    
	    data.setBooks(books);
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    contentFiltering.createTfIdfSeparately();
	    contentFiltering.computeTfIdfVectors();

	    Map<String, Map<String, Double>> tfIdfDict = contentFiltering.getTfIdfDict();

	    Map<String, Double> expectedTfIdfBook = new HashMap<>();
	    expectedTfIdfBook.put("description", 0.5); 
	    expectedTfIdfBook.put("one", 0.5); 
	    Map<String, Double> normalizedTfIdfBook1 = contentFiltering.normalizeVector(expectedTfIdfBook);

	    assertEquals(normalizedTfIdfBook1, tfIdfDict.get("Book One"));
	}
	
	@Test
	void testCreateUserProfile_OneRating() {
	    Customer user1 = new Customer("user");
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description two");;
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(books);
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(5.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(1.0));
	    userRatings.put(user1, user1Ratings);
	    
	    data.setUserRatings(userRatings);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    Map<String, Double> expected = new HashMap<>();
	    expected.put("description", 0.5);
	    expected.put("one", 1.0);
	    expected.put("two", 0.0);
	    expected = contentFiltering.normalizeVector(expected);
	    
	    contentFiltering.tfIdfPreprocessing();
	    Map<String, Double> actual = contentFiltering.createUserProfile(user1);
	    
	    for (String key : expected.keySet()) {
	    	assertTrue(actual.containsKey(key));
	    	assertEquals(expected.get(key), actual.get(key), 1e-6);
	    }	
	}
	@Test
	void testCreateUserProfile_NoPreferredBooks() {
	    Customer user1 = new Customer("user");
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description two");;
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(books);
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(2.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(1.0));
	    userRatings.put(user1, user1Ratings);
	    
	    data.setUserRatings(userRatings);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    Map<String, Double> expected = new HashMap<>();
	    expected.put("description", 0.0);
	    expected.put("one", 0.0);
	    expected.put("two", 0.0);
	    
	    contentFiltering.tfIdfPreprocessing();
	    Map<String, Double> actual = contentFiltering.createUserProfile(user1);
	    
	    for (String key : expected.keySet()) {
	    	assertTrue(actual.containsKey(key));
	    	assertEquals(expected.get(key), actual.get(key), 1e-6);
	    }	
	}
	@Test
	void testCreateUserProfile_PreferredBookNotInTheTfIdfDict() {
	    Customer user1 = new Customer("user");
	    Data data = new Data();
	    Book book1 = new Book("Book One", "Description one");
	    Book book2 = new Book("Book Two", "Description two");;
	    List<Book> books = Arrays.asList(book1, book2);
	    data.setBooks(Collections.emptyList());
	    
	    Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(1.0));
	    userRatings.put(user1, user1Ratings);
	    
	    data.setUserRatings(userRatings);
	    
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    
	    Map<String, Double> expected = new HashMap<>();
	    expected.put("description", 0.5);
	    expected.put("one", 1.0);
	    expected.put("two", 0.0);
	    expected = contentFiltering.normalizeVector(expected);
	    
	    contentFiltering.tfIdfPreprocessing();
	    
	    Exception exception = assertThrows(BookNotFoundException.class, () -> {
	    	Map<String, Double> actual = contentFiltering.createUserProfile(user1);
	    });
	    
	    String expectedMessage = "Book not found in TfIdfDict: " + book1.getBookTitle();
	    String actualMessage = exception.getMessage();
	    assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	void testGetRecommendations_Normal() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("Book Four", "Underwater archaeology: uncovering marine biology history and oceanic mysteries.");
		
		Book book5 = new Book("Book five", "An introduction to dance and choreography for aspiring performers.");
		Book book6 = new Book("Book six", "The history of ballet and the evolution of modern dance styles.");
		Book book7 = new Book("Book seven", "Dance as an art form: exploring rhythm, movement, and creative expression.");
		List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6, book7);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    	    
	    List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 2));
	    
	    assertTrue(recommendations.contains(book3));
	    assertTrue(recommendations.contains(book4));
	}
	@Test
	void testGetRecommendations_BooksAvailableLessThanTheNumberOfRequestedRecommendations() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		
		List<Book> books = Arrays.asList(book1, book2, book3);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    	    
	    List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 2));
	    
	    assertTrue(recommendations.contains(book3));
	}
	@Test
	void testGetRecommendations_AllBooksRated() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		
		List<Book> books = Arrays.asList(book1, book2, book3);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    user1Ratings.put(book3, BigDecimal.valueOf(2.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    	    
	    List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 2));
	    
	    assertTrue(recommendations.isEmpty());
	}
	@Test 
	void testGetRecommendations_NoBooks() {
		Data data = new Data();
		
		List<Book> books = Arrays.asList();
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    	    
	    List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 2));
	    
	    assertTrue(recommendations.isEmpty());
	}
	@Test 
	void testGetRecommendations_NumberOfRecommendationsIsZero() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("Book Four", "Underwater archaeology: uncovering marine biology history and oceanic mysteries.");
		
		Book book5 = new Book("Book five", "An introduction to dance and choreography for aspiring performers.");
		Book book6 = new Book("Book six", "The history of ballet and the evolution of modern dance styles.");
		Book book7 = new Book("Book seven", "Dance as an art form: exploring rhythm, movement, and creative expression.");
		List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6, book7);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    	    
	    List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 0));
	    
	    assertTrue(recommendations.isEmpty());
	}
	@Test 
	void testGetRecommendations_NumberOfRecommendationsExceedsAvailableBooks() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("Book Four", "Underwater archaeology: uncovering marine biology history and oceanic mysteries.");
		
		List<Book> books = Arrays.asList(book1, book2, book3, book4);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	    	    
	    List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 3));
	    
	    assertTrue(recommendations.contains(book3));
	    assertTrue(recommendations.contains(book4));
	}
	@Test 
	void testGetRecommendations_TfIdfDictMissingBook() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("Book Four", "Underwater archaeology: uncovering marine biology history and oceanic mysteries.");
		
		List<Book> books = Arrays.asList(book1, book2, book3, book4);
		data.setBooks(Collections.emptyList());
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
	       	    
    	Exception exception = assertThrows(BookNotFoundException.class, () -> {
    		List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 3));
 	    });
	}
	@Test 
	void testGetRecommendations_DuplicateBooksRecommendOnlyOne() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("Book Four", "Underwater archaeology: uncovering marine biology history and oceanic mysteries.");
		
		List<Book> books = Arrays.asList(book1, book2, book3, book4, book4);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
    	    	    
		List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 3));
		
		assertEquals(recommendations.size(), 2); 
		assertTrue(recommendations.contains(book3));
		assertTrue(recommendations.contains(book4));
	}
	@Test
	void testGetRecommendations_UserNull() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("Book Four", "Underwater archaeology: uncovering marine biology history and oceanic mysteries.");
		
		List<Book> books = Arrays.asList(book1, book2, book3, book4);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
    	    	    		
		Exception exception = assertThrows(NullPointerException.class, () -> {
			List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(null, 3));
		});
	}
	@Test
	void testGetRecommendations_NegativeNumberOfRecommendations() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book Three", "The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("Book Four", "Underwater archaeology: uncovering marine biology history and oceanic mysteries.");
		
		List<Book> books = Arrays.asList(book1, book2, book3, book4);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
    	    	    		
		List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, -1));
		
		assertTrue(recommendations.isEmpty());
	}
	@Test
	void testGetRecommendations_allBooksHaveTheSameSimilarityScore() {
		Data data = new Data();
		Book book1 = new Book("Book One", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book Two", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book3 = new Book("Book Three", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book4 = new Book("Book Four", "A comprehensive guide to marine biology and underwater research techniques.");
		
		List<Book> books = Arrays.asList(book1, book2, book3, book4);
		data.setBooks(books);
		
		Customer user = new Customer("Customer");
		
		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();
	    Map<Book, BigDecimal> user1Ratings = new HashMap<>();
	    user1Ratings.put(book1, BigDecimal.valueOf(6.0));
	    user1Ratings.put(book2, BigDecimal.valueOf(5.0));
	    userRatings.put(user, user1Ratings);
	    
	    data.setUserRatings(userRatings);
		
	    ContentFiltering contentFiltering = new ContentFiltering(data);
    	    	    		
		List<Book> recommendations = Arrays.asList(contentFiltering.getRecommendations(user, 1));
		
		assertTrue(recommendations.contains(book1) || recommendations.contains(book2) || recommendations.contains(book3));
	}
}