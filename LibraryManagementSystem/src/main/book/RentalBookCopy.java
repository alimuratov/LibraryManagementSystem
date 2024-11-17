package main.book;

import java.util.ArrayList;
import java.util.List;

import main.users.*;

public class RentalBookCopy extends BookCopy {
    private boolean isRented; 
    private Customer customer;
    private final List<RentalRecord> rentalHistory; 

    public RentalBookCopy(Book book) {
        super(book);
        this.isRented = false; 
        this.customer = null; 
        this.rentalHistory = new ArrayList<>();
    }

    
    public boolean isRented() {
        return isRented;
    }

	public void rent(Customer customer2) {
		if (isRented) {
            System.out.print("This copy is already rented out");
        }else {
        	this.isRented = true;
    		this.customer = customer2;
        }
	}

	public void returnBook() {
		this.isRented = false;
		this.customer = null;
	}
	
	public void addRentalRecord(RentalRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Rental record cannot be null");
        }
        rentalHistory.add(record);
    }
	
	public void displayCopyDetails() {
        System.out.println("Rental Copy ID: " + getCopyId());
    }


}


