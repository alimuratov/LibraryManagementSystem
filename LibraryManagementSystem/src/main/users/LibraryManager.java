package main.users;

import java.time.LocalDate;
import java.util.*;
import main.book.Book;
import main.book.PurchaseRecord;
import main.book.RentalRecord;
import main.transaction.PaymentFactory;
import main.transaction.Transaction;

public class LibraryManager{
    private static LibraryManager instance = null;

    private final TreeMap<LocalDate, List<RentalRecord>> activeRentals;
    private final TreeMap<LocalDate, List<PurchaseRecord>> refundablePurchases;

    // Constructor
    public LibraryManager() {
        activeRentals = new TreeMap<>();
        refundablePurchases = new TreeMap<>(); 
    }

    // Singleton 
    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }
    
    // Getters
    public TreeMap<LocalDate, List<RentalRecord>> getActiveRentals() {
    	return activeRentals;
    }
    
    public TreeMap<LocalDate, List<PurchaseRecord>> getRefundablePurchases() {
    	return refundablePurchases;
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
                if (record.getCustomer().equals(customer) && record.getBook().equals(book)) {
                    customer.getMembership().addXP(10);

                    recordIterator.remove();
                    if (records.isEmpty()) {
                        iterator.remove();
                    }

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
            Book book = record.getBook();
            Customer customer = record.getCustomer();
            
            book.setRentableCopies(book.getRentableCopies() + 1);
            customer.removeRentedBook(book);
            customer.getMembership().addXP(10);
            

            System.out.println("Book returned automatically: " + book.getDisplayText() + " by " + customer.getUserName());

            processRentingWaitlist(book);
        }
    }

    // Process renting waitlist
    public void processRentingWaitlist(Book book) {
        Queue<Customer> rentingWaitList = book.getRentingWaitList();
        while (book.isRentable() && !rentingWaitList.isEmpty()) {
            Customer nextCustomer = rentingWaitList.poll();
            System.out.println("Notifying " + nextCustomer.getUserName() + " about the availability of the book: " + book.getDisplayText());
            rentBook(nextCustomer, book);
        }
    }

    // PURCHASING

    // Purchase a book
    public void purchaseBook(Customer customer, Book book, String method) {
        if (customer == null || book == null) {
            System.out.println("Customer or Book cannot be null.");
            return;
        }

        if (book.isSalable()) {
            double originalPrice = book.getBookPrice();
            double discountedPrice = customer.getMembership().calculateDiscountedPrice(originalPrice);

            // Create Transaction
            String transactionID = UUID.randomUUID().toString();
            Transaction transaction = PaymentFactory.createTransaction(transactionID, discountedPrice, method, method);

            // Process Payment
            transaction.processPayment();
            book.setSaleableCopies(book.getSaleableCopies() - 1);

            customer.addPurchasedBook(book);
            customer.getMembership().addXP(20);

            PurchaseRecord purchaseRecord = new PurchaseRecord(customer, book, transaction, method);
            refundablePurchases.computeIfAbsent(purchaseRecord.getRefundExpiryDate(), k -> new ArrayList<>()).add(purchaseRecord);

            System.out.println("Book purchased successfully: " + book.getDisplayText());
            if (book.getBookPrice() != discountedPrice) {
            	System.out.printf("Discounted Price: HK$%.2f\n", discountedPrice);
            }
            
        } else {
            book.addSellingWaitList(customer);
            System.out.println("Book is not available for purchase. You have been added to the waiting list: " + book.getDisplayText());
        }
    }

    public void refundBook(Customer customer, Book book) {
        if (customer == null || book == null) {
            System.out.println("Customer or Book cannot be null.");
            return;
        }

        // Iterate through refundablePurchases to find the corresponding PurchaseRecord
        boolean recordFound = false;
        Iterator<Map.Entry<LocalDate, List<PurchaseRecord>>> iterator = refundablePurchases.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<LocalDate, List<PurchaseRecord>> entry = iterator.next();
            List<PurchaseRecord> records = entry.getValue();

            Iterator<PurchaseRecord> recordIterator = records.iterator();
            while (recordIterator.hasNext()) {
                PurchaseRecord record = recordIterator.next();
                if (record.getCustomer().equals(customer) && record.getBook().equals(book)) {
                    // Check if within 7 days from purchase date
                    if (LocalDate.now().isAfter(record.getRefundExpiryDate())) {
                        System.out.println("Refund period has expired for this purchase.");
                        return;
                    }

                    // Process refund
                    record.getTransaction().processRefund();

                    recordIterator.remove();
                    if (records.isEmpty()) {
                        iterator.remove();
                    }

                    book.setSaleableCopies(book.getSaleableCopies() + 1);
                    customer.removePurchasedBook(book);
                    customer.getMembership().deductXP(20);

                    System.out.println("Book refunded successfully: " + book.getDisplayText());
                    processSellingWaitlist(book, record.getRefundMethod());

                    recordFound = true;
                    break;
                }
            }

            if (recordFound) {
                break;
            }
        }

        if (!recordFound) {
            System.out.println("This book is not in your purchased list or refund period has expired: " + book.getDisplayText());
        }
    }

    public void processRefunds(LocalDate currentDate) {
        NavigableMap<LocalDate, List<PurchaseRecord>> expiredRefunds = refundablePurchases.headMap(currentDate, true);

        for (Map.Entry<LocalDate, List<PurchaseRecord>> entry : expiredRefunds.entrySet()) {
            List<PurchaseRecord> records = entry.getValue();

            for (PurchaseRecord record : records) {
                System.out.println("Refund period expired for purchase: " + record.getBook().getDisplayText()
                        + " by " + record.getCustomer().getUserName());
                processSellingWaitlist(record.getBook(), record.getRefundMethod());
            }
        }

        expiredRefunds.clear();
    }

    // Process selling waitlist
    public void processSellingWaitlist(Book book, String method) {
        Queue<Customer> sellingWaitList = book.getSellingWaitList();
        while (book.isSalable() && !sellingWaitList.isEmpty()) {
            Customer nextCustomer = sellingWaitList.poll();
            System.out.println("Notifying " + nextCustomer.getUserName() + " about the availability of the book for purchase: " + book.getDisplayText());
            purchaseBook(nextCustomer, book, method);
        }
    }
}