package main.users;

import java.util.ArrayList;
import java.util.List;

import main.authentication.*;
import main.book.*;

public class Admin extends User {
    private static Admin instance;
    private List<Book> managedBooks = new ArrayList<>();

    // Constructor
    public Admin(String userName, Password password) {
        super(userName, password);
    }

    public static synchronized Admin getInstance(String userName, Password password) {
        if (instance == null) {
            instance = new Admin(userName, password);
        }
        return instance;
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
     * @return true if the book was successfully updated, false otherwise.
     */
    public boolean updateBookDetails(Book book, String newTitle, String newAuthor) {
        if (managedBooks.contains(book)) {
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            System.out.println("Book details updated: " + book.getDisplayText());
            return true;
        } else {
            System.out.println("Book not found in the library: " + book.getDisplayText());
            return false;
        }
    }

    /**
     * Adds a rental copy to a book.
     *
     * @param book The book to which the rental copy will be added.
     * @param copy The rental copy to add.
     */
    public void addRentalCopy(Book book, RentalBookCopy copy) {
        if (managedBooks.contains(book)) {
            book.addRentalCopy(copy);
            System.out.println("Rental copy added to book: " + book.getDisplayText());
        } else {
            System.out.println("Book not managed by admin: " + book.getDisplayText());
        }
    }

    /**
     * Adds a saleable copy to a book.
     *
     * @param book The book to which the saleable copy will be added.
     * @param copy The saleable copy to add.
     */
    public void addSaleableCopy(Book book, SalableBookCopy copy) {
        if (managedBooks.contains(book)) {
            book.addSaleableCopy(copy);
            System.out.println("Saleable copy added to book: " + book.getDisplayText());
        } else {
            System.out.println("Book not managed by admin: " + book.getDisplayText());
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
            System.out.println(book.getDisplayText());
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
        System.out.println("Reviews for " + book.getDisplayText() + ":");
        book.displayReviews();
    }

    // Additional methods will be added here
}