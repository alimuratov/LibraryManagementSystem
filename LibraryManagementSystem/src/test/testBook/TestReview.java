package test.testBook;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import main.book.*;

class TestReview {
	@Test
	public void testReview_01() {
		Review review1 = new Review("Great world-building, worth the read.", 5);
		assertEquals("Great world-building, worth the read.", review1.getComments());
	}
	
	@Test
	public void testReview_02() {
		Review review1 = new Review("Great world-building, worth the read.", 5);
		assertEquals(5, review1.getBookRating());
		
	}
	
	@Test
	public void testReview_03() {
		Review review1 = new Review("Great world-building, worth the read.", 5);
		assertEquals("Book Review\nBook Rating: 5\nComment: Great world-building, worth the read.", review1.toString());
		
	}
	
	@Test
	public void testReview_04() throws Exception{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outputStream));
		Review review1 = new Review("Great world-building, worth the read.", 10);
		String consoleOutput = outputStream.toString();
	    System.setOut(System.out);
		assertEquals("Rating should be 0 to 8", consoleOutput);
	}

}
