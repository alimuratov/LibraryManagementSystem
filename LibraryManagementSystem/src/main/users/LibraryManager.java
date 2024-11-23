package main.users;

import java.util.*;
import main.book.Book;

public class LibraryManager{
    private static LibraryManager instance = null;

    private Map<String, List<Book>> activeRentals;
    private Map<String, List<Book>> completedRentals;
    private Map<String, List<Book>> purchaseRecords;

    // Constructor
    private LibraryManager() {
        activeRentals = new HashMap<>();
        completedRentals = new HashMap<>();
        purchaseRecords = new HashMap<>();
    }

    // Singleton 
    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    // RENTING

    // Rent a book
    public void rentBook(Customer customer, Book book) {
        int activeRentalCount = activeRentals.getOrDefault(customer.getUserID(), Collections.emptyList()).size();
        if (activeRentalCount >= customer.getMembership().getMaxRentBooks()) {
            System.out.println("You have reached your rental limit.");
        }

        if (book.isRentable()) {
            book.setRentableCopies(book.getRentableCopies() - 1);

            activeRentals.computeIfAbsent(customer.getUserID(), k -> new ArrayList<>()).add(book);
            customer.addRentedBook(book);

            System.out.println("Book rented successfully: " + book.getDisplayText());
        } else {
            book.addRentingWaitList(customer);
            System.out.println("Book is not available for rent. You have been added to the waiting list: " + book.getDisplayText());
        }
    }

    // Return a rented book
    public void returnBook(Customer customer, Book book) {
        List<Book> customerRentals = activeRentals.get(customer.getUserID());
        if (customerRentals != null && customerRentals.remove(book)) {
            book.setRentableCopies(book.getRentableCopies() + 1);
            customer.removeRentedBook(book);

            completedRentals.computeIfAbsent(customer.getUserID(), k -> new ArrayList<>()).add(book);

            System.out.println("Book returned successfully: " + book.getDisplayText());

            processRentingWaitlist(book);
        } else {
            System.out.println("This book is not in your rented list: " + book.getDisplayText());
        }
    }

    // Process renting waitlist
    private void processRentingWaitlist(Book book) {
        Queue<Customer> rentingWaitList = book.getRentingWaitList();
        while (book.isRentable() && !rentingWaitList.isEmpty()) {
            Customer nextCustomer = rentingWaitList.poll();
            System.out.println("Notifying " + nextCustomer.getUserName() + " about the availability of the book: " + book.getDisplayText());
            rentBook(nextCustomer, book);
        }
    }

    // PURCHASING

    // Purchase a book
    public void purchaseBook(Customer customer, Book book) {
        if (book.isSalable()) {
            book.setSaleableCopies(book.getSaleableCopies() - 1);

            double originalPrice = book.getBookPrice();
            double discountedPrice = customer.getMembership().calculateDiscountedPrice(originalPrice);

            System.out.println("Original Price: $" + originalPrice);
            System.out.println("Discounted Price for " + customer.getUserName() + ": $" + discountedPrice);

            customer.addPurchasedBook(book);
            purchaseRecords.computeIfAbsent(customer.getUserID(), k -> new ArrayList<>()).add(book);

            System.out.println("Book purchased successfully: " + book.getDisplayText());

            processSellingWaitlist(book);
        } else {
            book.addSellingWaitList(customer);
            System.out.println("Book is not available for purchase. You have been added to the waiting list: " + book.getDisplayText());
        }
    }

    // Process selling waitlist
    private void processSellingWaitlist(Book book) {
        Queue<Customer> sellingWaitList = book.getSellingWaitList();
        while (book.isSalable() && !sellingWaitList.isEmpty()) {
            Customer nextCustomer = sellingWaitList.poll();
            System.out.println("Notifying " + nextCustomer.getUserName() + " about the availability of the book for purchase: " + book.getDisplayText());
            purchaseBook(nextCustomer, book);
        }
    }
}