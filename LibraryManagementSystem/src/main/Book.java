package main;

public class Book {
	private String bookName;
	private Integer bookId;
	private String bookDescription;
	
	public Book() { }
	
	public Book(String bookName, String bookDescription) {
		this.bookName = bookName;
		this.bookDescription = bookDescription;
	}
	
	public Book(String bookName, String bookDescription, Integer id) {
		this.bookName = bookName; 
		this.bookDescription = bookDescription;
		this.bookId = id;
	}
	
	public String getBookName() {
		return bookName;
	}
	
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
	@Override
	public String toString() {
		return "Book [bookName=" + bookName + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Book))
			return false;
		Book other = (Book) obj;
		return bookName.equals(other.bookName);
	}
	
	@Override
	public int hashCode() {
		if (bookId == null) {
			return bookName.hashCode();
		}
		return bookId.hashCode();
	}

	public String getBookDescription() {
		return bookDescription;
	}

	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}
}
