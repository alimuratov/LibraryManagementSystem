import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.Book;

public class User {
    private String userName;
    private String password;
    private Membership membership;
    private Set<Book> rentedBooks = new HashSet<>();
    private Set<Book> purchasedBooks = new HashSet<>();
    private Map<Book, Review> reviews = new HashMap<>();

    // Constructors
    public User(String userName, String password, Membership membership) {
        this.userName = userName;
        this.password = password;
        this.membership = membership;
    }


    // Getter
    public Set<Book> getRentedBooks() {
        return Collections.unmodifiableSet(rentedBooks);
    }

    public Set<Book> getPurchasedBooks() {
        return Collections.unmodifiableSet(purchasedBooks);
    }

    public List<Review> getReviews() {
        return new ArrayList<>(reviews.values());
    }


    // Password 
    private void setPassword(String newPassword) {
        this.password = newPassword;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }
        return false;
    }

    public boolean changePassword(String currentPassword, String newPassword) {
        if (!this.password.equals(currentPassword)) {
            System.out.println("Current password is incorrect.");
            return false;
        }
    
        if (!isValidPassword(newPassword)) {
            System.out.println("New password does not meet the required criteria: ");
            System.out.println("Password must be at least 8 characters long and contain both letters and numbers.");
            return false;
        }
    
        setPassword(newPassword);
        System.out.println("Password changed successfully.");
        return true;
    }


    // Membership
    public void upgradeMembership(Membership newMembership) {
        this.membership = newMembership;
    }

    public Membership getMembership() {
        return this.membership;
    }

    // Book
    public boolean rentBook(Book book) {
        if (membership.canRentMoreBooks() && book.isAvailableForRent()) {
            if (book.lend(this)) {
                rentedBooks.add(book);
                return true;
            }
        }
        return false;
    }

    public boolean returnBook(Book book) {
        if (rentedBooks.contains(book)) {
            if (book.returnBook(this)) {
                rentedBooks.remove(book);
                return true;
            }
        }
        return false;
    }

    public boolean purchaseBook(Book book) {
        if (membership.canPurchaseBooks() && book.isSaleable()) {
            if (book.buy(this)) {
                purchasedBooks.add(book);
                return true;
            }
        }
        return false;
    }


    // Review
    public void addReview(Book book, Review review) {
        if (purchasedBooks.contains(book) || rentedBooks.contains(book)) {
            reviews.put(book, review);
            book.addReview(review);
        } else {
            throw new IllegalArgumentException("User has not rented or purchased this book.");
        }
    }

    // Default
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return userName.equals(other.userName);
    }
    
    @Override
    public int hashCode() {
        return userName.hashCode();
    }
}
