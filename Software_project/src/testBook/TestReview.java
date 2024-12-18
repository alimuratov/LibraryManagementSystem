package testBook;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import book.Review;

class TestReview {
	PrintStream oldPrintStream;
    ByteArrayOutputStream bos;
    
    private void setOutput() throws Exception {
        oldPrintStream = System.out;
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
    }

    private String getOutput() {
        System.setOut(oldPrintStream);
        return bos.toString();
    }
    
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
		setOutput();
		Review review1 = new Review("Great world-building, worth the read.", 10);
		assertEquals("Rating should be 0 to 8", getOutput());
	}

}
