package book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import users.*;

public class RentalRecord {
    private final Customer customer; 
    private final RentalBookCopy rentalCopy;
    private final LocalDate rentalDate; 
    private LocalDate returnDate; 

    private static final int DEFAULT_RENTAL_PERIOD_DAYS = 14;

    public RentalRecord(Customer customer, RentalBookCopy rentalCopy) {
        if (customer == null || rentalCopy == null) {
            throw new IllegalArgumentException("Customer and rental copy cannot be null.");
        }
        this.customer = customer;
        this.rentalCopy = rentalCopy;
        this.rentalDate = LocalDate.now(); 
        this.returnDate = rentalDate.plusDays(customer.getMembership().getRentalPeriodDays()); 
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getRentalCopyId() {
        return rentalCopy.getCopyId();
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        if (returnDate.isBefore(rentalDate)) {
            throw new IllegalArgumentException("Return date cannot be before rental date.");
        }
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returnDate != null && returnDate.isBefore(LocalDate.now());
    }

    public void extendRentalPeriod(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Extension days must be positive.");
        }
        this.returnDate = returnDate.plusDays(days);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "RentalRecord {" +
                "Customer=" + customer +
                ", RentalCopy=" + rentalCopy.getCopyId() +
                ", RentalDate=" + rentalDate.format(formatter) +
                ", ReturnDate=" + (returnDate != null ? returnDate.format(formatter) : "Not Returned") +
                '}';
    }
}