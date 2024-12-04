package test.testRecommendation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import main.recommendation.*;
import main.book.*;

public class RecommendationStrategyBaseTest {

	@Test
	void testGetter() {
		Data data = new Data();
		data.setBooks(Arrays.asList(new Book("book1", "description1")));
		RecommendationStrategyBase recommendationSystem = new ContentFiltering(data);
		
		assertEquals(data, recommendationSystem.getData());
	}
	
	@Test
	void testSetter() {
		Data data1 = new Data();
		RecommendationStrategyBase recommendationSystem = new ContentFiltering(data1);
		
		Data data2 = new Data();
		data2.setBooks(Arrays.asList(new Book("book1", "description1")));

		recommendationSystem.setData(data2);
		
		assertEquals(data2, recommendationSystem.getData());
		assertEquals(1, recommendationSystem.getData().getBooks().size()); // new data contains just one book

	}
}
