package test.testUsers;

import static org.junit.Assert.*;
import org.junit.Test;
import main.users.*;
import main.book.*;
import main.system.Password;
import java.util.List;

public class AdminTest {

    // Test viewing reviews for a book managed by admin, including reviews with invalid ratings
    @Test
    public void testViewBookReviews_BookManaged() {
        // Arrange
        Password password = new Password("adminPass");
        Admin admin = new Admin("AdminUser", password);
        Book book = new Book("Test-Driven Development", "Kent Beck");
        admin.addBook(book);

        // Create reviews with valid and invalid ratings
        Review validReview = new Review("Excellent book on TDD.", 5);
        Review invalidReviewLow = new Review("Not helpful.", -2);   // Invalid rating (below 0)
        Review invalidReviewHigh = new Review("Outstanding!", 10);  // Invalid rating (above 8)
        Review edgeCaseReviewZero = new Review("Average read.", 0); // Edge case (rating at lower bound)
        Review edgeCaseReviewEight = new Review("Masterpiece!", 8); // Edge case (rating at upper bound)

        // Add reviews to the book
        book.addReview(validReview);
        book.addReview(invalidReviewLow);
        book.addReview(invalidReviewHigh);
        book.addReview(edgeCaseReviewZero);
        book.addReview(edgeCaseReviewEight);

        // Act
        admin.viewBookReviews(book);

        // Assert
        // Assuming the Book class has a method to get the list of reviews
        List<Review> reviews = book.getReviews();
        assertEquals(5, reviews.size());

        // Check the ratings of each review
        assertEquals(5, reviews.get(0).getBookRating());  // Valid rating
        assertEquals(0, reviews.get(1).getBookRating());  // Invalid rating defaults to 0
        assertEquals(0, reviews.get(2).getBookRating());  // Invalid rating defaults to 0
        assertEquals(0, reviews.get(3).getBookRating());  // Edge case at lower bound
        assertEquals(8, reviews.get(4).getBookRating());  // Edge case at upper bound
    }

    // Test viewing reviews for a book not managed by admin
    @Test
    public void testViewBookReviews_BookNotManaged() {
        // Arrange
        Password password = new Password("adminPass");
        Admin admin = new Admin("AdminUser", password);
        Book book = new Book("The Phoenix Project", "Gene Kim");

        // Create a review
        Review review = new Review("Great insights into DevOps.", 7);
        book.addReview(review);

        // Act
        admin.viewBookReviews(book);

        // Since the book is not managed by the admin, the method should print a message and return without displaying reviews.
        // In a real test, we might capture the console output to assert the correct message is printed.
    }
}