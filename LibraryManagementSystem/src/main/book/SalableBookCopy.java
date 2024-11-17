package main.book;

import main.users.Customer;

public class SalableBookCopy extends BookCopy {
	private boolean isSold;  
    
    public SalableBookCopy(Book book) {
        super(book);
        this.isSold = false;
    }
   
	public boolean isSold() {
		return isSold;
	}
	
	public void sold(Customer customer) {
		isSold = true;
		
	}
}
