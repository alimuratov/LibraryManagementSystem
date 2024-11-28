package test.testRecommendation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import main.recommendation.*;

public class RecommendationStrategyFactoryTest {
	@Test
    public void testCreate_ContentFiltering() {
        Data data = new Data();
        RecommendationStrategy strategy = RecommendationStrategyFactory.createStrategy("content", data);
        assertNotNull(strategy);
        assertTrue(strategy instanceof ContentFiltering);
    }
	
	@Test
	public void testCreate_CollaborativeFiltering() {
		Data data = new Data();
        RecommendationStrategy strategy = RecommendationStrategyFactory.createStrategy("collaborative", data);
        assertNotNull(strategy);
        assertTrue(strategy instanceof CollaborativeFiltering);
	}
	
	@Test
    public void testCreate_CaseInsensitive() {
        Data data = new Data();
        RecommendationStrategy strategy = RecommendationStrategyFactory.createStrategy("CoNtEnT", data);
        assertNotNull(strategy);
        assertTrue(strategy instanceof ContentFiltering);
    }
	
	@Test
    public void testCreate_NullStrategy() {
        Data data = new Data();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> { 
        	RecommendationStrategy strategy = RecommendationStrategyFactory.createStrategy(null, data);
        });
        assertEquals("Strategy type cannot be null", exception.getMessage());
    }
	
	@Test
	public void testCreate_UnkownStrategy() {
        Data data = new Data();
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> { 
        	RecommendationStrategy strategy = RecommendationStrategyFactory.createStrategy("rating", data);
        });
        assertEquals("Unknown strategy type", exception.getMessage());
    }
	
	@Test
	public void testCreate_NullData() {
        Data data = new Data();
        Exception exception = assertThrows(NullPointerException.class, () -> { 
        	RecommendationStrategy strategy = RecommendationStrategyFactory.createStrategy("Content", null);
        });
    }
}