package main.users;

import java.util.ArrayList;
import java.util.List;

import book.Book;
import book.Review;

public class Admin extends User {
    private List<Book> managedBooks = new ArrayList<>();

    // Constructor
    public Admin(String userID, String userName, String password) {
        super(userID, userName, password);
    }

    /**
     * Adds a new book to the library collection.
     *
     * @param book The book to add.
     * @return true if the book was successfully added, false otherwise.
     */
    public boolean addBook(Book book) {
        if (!managedBooks.contains(book)) {
            managedBooks.add(book);
            System.out.println("Book added to the library: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("Book already exists in the library: " + book.getDisplayText());
            return false;
        }
    }

    /**
     * Removes a book from the library collection.
     *
     * @param book The book to remove.
     * @return true if the book was successfully removed, false otherwise.
     */
    public boolean removeBook(Book book) {
        if (managedBooks.contains(book)) {
            managedBooks.remove(book);
            System.out.println("Book removed from the library: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("Book not found in the library: " + book.getDisplayText());
            return false;
        }
    }

    /**
     * Updates details of an existing book.
     *
     * @param book      The book to update.
     * @param newTitle  The new title of the book.
     * @param newAuthor The new author of the book.
     * @param isSalable Updated salability status.
     * @param isRentable Updated rentability status.
     * @return true if the book was successfully updated, false otherwise.
     */
    public boolean updateBookDetails(Book book, String newTitle, String newAuthor, boolean isSalable, boolean isRentable) {
        if (managedBooks.contains(book)) {
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setSaleable(isSalable);
            book.setAvailableForRent(isRentable);
            System.out.println("Book details updated: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("Book not found in the library: " + book.getDisplayText());
            return false;
        }
    }

    /**
     * Views all books managed by the admin.
     */
    public void viewManagedBooks() {
        if (managedBooks.isEmpty()) {
            System.out.println("No books are currently managed.");
            return;
        }
        System.out.println("Managed Books:");
        for (Book book : managedBooks) {
            System.out.println(book);
        }
    }

    /**
     * Views all reviews for a specific book.
     *
     * @param book The book whose reviews are to be viewed.
     */
    public void viewBookReviews(Book book) {
        if (!managedBooks.contains(book)) {
            System.out.println("Book not managed by admin: " + book.getDisplayText());
            return;
        }
        System.out.println("Reviews for " + book.getTitle() + ":");
        book.displayReviews();
    }

    // Additional methods will be added here
}