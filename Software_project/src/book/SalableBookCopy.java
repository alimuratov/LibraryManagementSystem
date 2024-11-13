package book;

import java.util.ArrayList;
import java.util.List;

import book.Book;
import book.BookCopy;
import book.Customer_stub;

public class SalableBookCopy extends BookCopy {
	private boolean isSold;  
    
    public SalableBookCopy(Book book) {
        super(book);
        this.isSold = false;
    }
   
	public boolean isSold() {
		return isSold;
	}
	
	public void sold(Customer_stub customer) {
		isSold = true;
		
	}
	
}
