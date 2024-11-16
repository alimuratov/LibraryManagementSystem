package book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Book {
	private String isbn;
    private String title;
    private String author;
    private String publisher; 
    private String publicationDate; 
    private int bookPrice;
    private List<RentalBookCopy> rentalCopies; 
    private List<SalableBookCopy> saleCopies; 
    private ArrayList<Customer_stub> rentingWaitList;
    private ArrayList<Customer_stub> sellingWaitList;
    private ArrayList<SalableBookCopy> soldCopies;
    private List<Review> allReviews;

    public Book(String isbn, String title, String author, String publisher, String publicationDate, int Price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.bookPrice = Price;
        this.rentalCopies = new ArrayList<>();
        this.saleCopies = new ArrayList<>();
        this.soldCopies = new ArrayList<>();
        this.rentingWaitList = new ArrayList<>();
        this.sellingWaitList = new ArrayList<>();
        this.allReviews = new ArrayList<>();
    }
    
    //selling book
    public boolean isSalable() {
    	if (saleCopies.isEmpty()) {
    		return false;
    	}else {
    		return true;
    	}
    }
    
    public void Buy(Customer_stub customer) {
        if (!isSalable()) {
        	addSellingWaitList(customer);
        	System.out.print("No book available for selling.\n"); 
        }else{
        	SalableBookCopy sellingCopy = saleCopies.removeFirst();
        	sellingCopy.sold(customer);
        	soldCopies.add(sellingCopy);
           	System.out.print("Buying Successfully for book\n");
        }
    	
    }
    
    public void addSellingWaitList(Customer_stub customer) {
    	sellingWaitList.add(customer);
	}
    
    public List<SalableBookCopy> getSoldCopies() {
        return new ArrayList<>(soldCopies);
    } 

	//renting book
    public void Lend(Customer_stub customer) {
    	RentalBookCopy rentingCopy = getAvailableLendingCopy();
        if (rentingCopy == null) { 
            addLendingWaitList(customer);
            System.out.print("No book available for lending.\n"); 
        }else{
        	rentingCopy.rent(customer);
           	System.out.print("Lending Successfully for book\n");
        }

	}
    
    public RentalBookCopy getAvailableLendingCopy() {
    	for (RentalBookCopy copy: this.rentalCopies) {
    		if (!copy.isRented()) {
    			return copy;
    		}
    	}
		return null;
	}

	public void addLendingWaitList(Customer_stub observer) {
    	rentingWaitList.add(observer);
	}
	

    //rental copies add & get
    public String showAvailableRentalCopies() {
    	int n = 0;
    	for (RentalBookCopy r: rentalCopies) {
    		if (!r.isRented())
    			n++;
    	}
        return n + "available rentable copy";
    }

    public void addRentalCopy(RentalBookCopy copy) {
        rentalCopies.add(copy);
        System.out.print("Add one rentable copy of book\n");
    }

    //sale copies add & get
    public void addSaleableCopy(SalableBookCopy copy) {
        saleCopies.add(copy);
        System.out.print("Add one seleable copy of book\n");
    }
    public String showAvailableSellableCopies() {
        return this.saleCopies.size() + "available sellable copy";
    }

    //review add & get
    public void addReview(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null.");
        }
        allReviews.add(review);
    }
    
    public void displayReviews() {
        if (allReviews.isEmpty()) {
            System.out.println("No reviews yet.");
        } else {
            for (Review review : allReviews) {
                System.out.println(review);
            }
        }
    }
    
    public int getBookPrice() {
    	return this.bookPrice;
    }
    
	public String getDisplayText() {
		return title + " by " + author + " ISBN: " + isbn + " Publisher: " + publisher + " Publication Date: " + publicationDate + " Price: " + bookPrice;
	}
}