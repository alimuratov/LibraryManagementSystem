package main.users;

import java.time.LocalDate;
import java.util.*;
import main.book.Book;
import main.book.RentalRecord;
import main.transaction.PaymentFactory;
import main.transaction.PaymentMethod;
import main.transaction.RefundMethod;
import main.transaction.Transaction;

public class LibraryManager{
    private static LibraryManager instance = null;

    private final TreeMap<LocalDate, List<RentalRecord>> activeRentals;
    // private Map<String, List<Book>> completedRentals;
    // private Map<String, List<Book>> purchaseRecords;

    // Constructor
    private LibraryManager() {
        activeRentals = new TreeMap<>();
        // completedRentals = new HashMap<>();
        // purchaseRecords = new HashMap<>();
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
        if (customer == null || book == null) {
            System.out.println("Customer or Book cannot be null.");
            return;
        }

        if (customer.getMembership().getMaxRentBooks() <= customer.getRentedBooks().size()) {
            System.out.println("You have reached your rental limit.");
            return;
        }

        if (book.isRentable()) {
            book.setRentableCopies(book.getRentableCopies() - 1);

            RentalRecord rentalRecord = new RentalRecord(customer, book);

            activeRentals.computeIfAbsent(rentalRecord.getReturnDate(), k -> new ArrayList<>()).add(rentalRecord);

            customer.addRentedBook(book);

            System.out.println("Book rented successfully: " + book.getDisplayText());
            System.out.println("Return Date: " + rentalRecord.getReturnDate());
        } else {
            book.addRentingWaitList(customer);
            System.out.println("Book is not available for rent. You have been added to the waiting list: " + book.getDisplayText());
        }
    }

    // Return a rented book
    public void returnBook(Customer customer, Book book) {
        if (customer == null || book == null) {
            System.out.println("Customer or Book cannot be null.");
            return;
        }

        // Iterate through the activeRentals to find the corresponding RentalRecord
        boolean recordFound = false;
        Iterator<Map.Entry<LocalDate, List<RentalRecord>>> iterator = activeRentals.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<LocalDate, List<RentalRecord>> entry = iterator.next();
            List<RentalRecord> records = entry.getValue();

            Iterator<RentalRecord> recordIterator = records.iterator();
            while (recordIterator.hasNext()) {
                RentalRecord record = recordIterator.next();
                if (record.getCustomer().equals(customer) && record.getBook().equals(book) && !record.isReturned()) {
                    record.markAsReturned();

                    recordIterator.remove();
                    if (records.isEmpty()) {
                        iterator.remove();
                    }

                    // completedRentals.computeIfAbsent(customer.getUserID(), k -> new ArrayList<>()).add(record);

                    book.setRentableCopies(book.getRentableCopies() + 1);

                    customer.removeRentedBook(book);

                    System.out.println("Book returned successfully: " + book.getDisplayText());
                    processRentingWaitlist(book);

                    recordFound = true;
                    break;
                }
            }

            if (recordFound) {
                break;
            }
        }

        if (!recordFound) {
            System.out.println("This book is not in your rented list: " + book.getDisplayText());
        }
    }

    public void processReturns(LocalDate currentDate) {
        List<RentalRecord> recordsToReturn = activeRentals.remove(currentDate);
        if (recordsToReturn == null || recordsToReturn.isEmpty()) {
            System.out.println("No rentals to return on " + currentDate);
            return;
        }

        for (RentalRecord record : recordsToReturn) {
            record.markAsReturned();

            // completedRentals.computeIfAbsent(record.getCustomer().getUserID(), k -> new ArrayList<>()).add(record);

            Book book = record.getBook();
            book.setRentableCopies(book.getRentableCopies() + 1);

            Customer customer = record.getCustomer();
            customer.removeRentedBook(book);

            System.out.println("Book returned automatically: " + book.getDisplayText() + " by " + customer.getUserName());

            processRentingWaitlist(book);
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
        if (customer == null || book == null) {
            System.out.println("Customer or Book cannot be null.");
            return;
        }

        if (book.isSalable()) {
            double originalPrice = book.getBookPrice();
            double discountedPrice = customer.getMembership().calculateDiscountedPrice(originalPrice);

            // Create Transaction
            String transactionID = UUID.randomUUID().toString();
            PaymentMethod paymentMethod = PaymentFactory.createPaymentMethod("CreditCard"); // Example payment method
            RefundMethod refundMethod = PaymentFactory.createRefundMethod("CreditCard");   // Example refund method
            Transaction transaction = PaymentFactory.createTransaction(transactionID, discountedPrice, "CreditCard", "CreditCard");

            // Process Payment
            transaction.processPayment();
            if (transaction.isPaymentProcessed()) {
                book.setSaleableCopies(book.getSaleableCopies() - 1);

                customer.addPurchasedBook(book);

                // purchaseRecords.computeIfAbsent(customer.getUserID(), k -> new ArrayList<>()).add(book);

                System.out.println("Book purchased successfully: " + book.getDisplayText());
                System.out.printf("Discounted Price: HK$%.2f\n", discountedPrice);

                processSellingWaitlist(book);
            } else {
                System.out.println("Payment failed. Cannot complete the purchase.");
            }
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