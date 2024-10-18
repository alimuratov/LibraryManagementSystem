package main;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Data {

    // Method to retrieve the data
    Map<User, HashMap<Book, BigDecimal>> getData();
    
    // Method to retrieve the books
    List<Book> getBooks();

}
