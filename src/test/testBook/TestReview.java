package test.testBook;

import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

import main.book.*;

// Review Tests
class TestReview {
    @Test
    void testValidReviewCreation() {
        Review review = new Review("Great book", 5);
        assertEquals("Great book", review.getComments());
        assertEquals(5, review.getBookRating());
    }
    
    @Test
    void testInvalidRatingBelowZero() {
        Review review = new Review("Bad book", -1);
        assertEquals(0, review.getBookRating());
    }
    
    @Test
    void testInvalidRatingAboveEight() {
        Review review = new Review("Amazing book", 9);
        assertEquals(0, review.getBookRating());
    }
    
    @Test
    void testToString() {
        Review review = new Review("Good read", 7);
        String expected = "Book Review\nBook Rating: 7\nComment: Good read";
        assertEquals(expected, review.toString());
    }
}
