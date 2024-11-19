package main.users;

import java.util.*;

import main.authentication.Password;
import main.book.Book;
import main.book.RentalBookCopy;
import main.book.SalableBookCopy;
import main.book.RentalRecord;

public class Librarian extends User {
    private static Librarian instance = null;

    private Map<String, List<RentalRecord>> activeRentals;
    private Map<String, List<RentalRecord>> completedRentals;
    private Map<String, List<Book>> purchaseRecords;

    // Constructor
    private Librarian(String userName, Password password) {
        super(userName, password);
        activeRentals = new HashMap<>();
        completedRentals = new HashMap<>();
        purchaseRecords = new HashMap<>();
    }

    // Singleton
    public static Librarian getInstance(String userName, Password password) {
        if (instance == null) {
            instance = new Librarian(userName, password);
        }
        return instance;
    }


    // RENTING
    private int getActiveRentalCount(Customer customer) {
        return activeRentals.getOrDefault(customer.getUserID(), Collections.emptyList()).size();
    }

    // Process renting waitlist
    private void processRentingWaitlist(Book book) {
        Queue<Customer> rentingWaitList = book.getRentingWaitList();
        if (!rentingWaitList.isEmpty()) {
            Customer nextCustomer = rentingWaitList.poll();
            System.out.println("Notifying " + nextCustomer.getUserName() + " about the availability of the book: " + book.getDisplayText());
            rentBook(nextCustomer, book);
        }
    }

    // Rent a book
    public boolean rentBook(Customer customer, Book book) {
        if (getActiveRentalCount(customer) >= customer.getMembership().getMaxRentBooks()) {
            System.out.println("You have reached your rental limit.");
            return false;
        }

        RentalBookCopy availableCopy = book.getAvailableLendingCopy();
        if (availableCopy != null) {
            availableCopy.rent(customer);

            int rentalPeriod = customer.getMembership().getRentalPeriodDays();
            RentalRecord rentalRecord = new RentalRecord(customer, availableCopy, rentalPeriod);
            availableCopy.addRentalRecord(rentalRecord);

            activeRentals.computeIfAbsent(customer.getUserID(), k -> new ArrayList<>()).add(rentalRecord);

            customer.addRentedBook(book);

            System.out.println("Book rented successfully: " + book.getDisplayText());
            return true;
        } else {
            book.addLendingWaitList(customer);
            System.out.println("Book is not available for rent. You have been added to the waiting list: " + book.getDisplayText());
            return false;
        }
    }

    // Return a rented book
    public boolean returnBook(Customer customer, Book book) {
        List<RentalRecord> customerRentals = activeRentals.get(customer.getUserID());
        if (customerRentals != null) {
            RentalRecord recordToClose = null;
            for (RentalRecord record : customerRentals) {
                if (record.getRentalCopy().getBook().equals(book) && !record.isClosed()) {
                    recordToClose = record;
                    break;
                }
            }

            if (recordToClose != null) {
                RentalBookCopy rentedCopy = recordToClose.getRentalCopy();
                rentedCopy.returnBook();

                customer.removeRentedBook(book);

                recordToClose.closeRecord();

                customerRentals.remove(recordToClose);
                completedRentals.computeIfAbsent(customer.getUserID(), k -> new ArrayList<>()).add(recordToClose);

                System.out.println("Book returned successfully: " + book.getDisplayText());

                processRentingWaitlist(book);

                return true;
            }
        }
        System.out.println("This book is not in your rented list: " + book.getDisplayText());
        return false;
    }


    // PURCHASE
    public void processSellingWaitlist(Book book) {
        Queue<Customer> sellingWaitList = book.getSellingWaitList();
        while (book.isSalable() && !sellingWaitList.isEmpty()) {
            Customer nextCustomer = sellingWaitList.poll();
            System.out.println("Notifying " + nextCustomer.getUserName() + " about the availability of the book for purchase: " + book.getDisplayText());
            purchaseBook(nextCustomer, book);
        }
    } 

    public boolean purchaseBook(Customer customer, Book book) {
        if (!book.isSalable()) {
            book.addSellingWaitList(customer);
            System.out.println("Book is not available for purchase. You have been added to the waiting list: " + book.getDisplayText());
            return false;
        } else {
            SalableBookCopy saleCopy = book.getSaleableCopies().remove(0);

            double originalPrice = book.getBookPrice();
            double discountedPrice = customer.calculateDiscountedPrice(originalPrice);

            System.out.println("Original Price: $" + originalPrice);
            System.out.println("Discounted Price for " + customer.getUserName() + ": $" + discountedPrice);

            customer.addPurchasedBook(book);
            purchaseRecords.computeIfAbsent(customer.getUserID(), k -> new ArrayList<>()).add(book);

            System.out.println("Book purchased successfully: " + book.getDisplayText());

            return true;
        }
    }
}
