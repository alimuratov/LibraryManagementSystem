package main.users;

import java.util.List;

import main.authentication.*;
import main.book.*;

public class Admin extends User {
    private static Admin instance;

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
    public void addBook(Book book) {
        List<Book> allBooks = Book.getAllBooks();
        if (!allBooks.contains(book)) {
            allBooks.add(book);
            System.out.println("Book added to the library: " + book.getDisplayText());
        } else {
            System.out.println("Book already exists in the library: " + book.getDisplayText());
        }
    }

    /**
     * Removes a book from the library collection.
     *
     * @param book The book to remove.
     * @return true if the book was successfully removed, false otherwise.
     */
    public void removeBook(Book book) {
        List<Book> allBooks = Book.getAllBooks();
        if (allBooks.contains(book)) {
            allBooks.remove(book);
            System.out.println("Book removed from the library: " + book.getDisplayText());
        } else {
            System.out.println("Book not found in the library: " + book.getDisplayText());
        }
    }
}