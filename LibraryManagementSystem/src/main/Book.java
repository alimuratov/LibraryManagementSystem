package main;

public class Book {
	private String bookName;
	
	public Book() { }
	
	public Book(String bookName) {
		this.bookName = bookName;
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
		return bookName.hashCode();
	}
}
