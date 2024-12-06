package test.testMain;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.main.Main;

class TestMain {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUp() {
        // Reset the Main class state before each test
        Main.resetState();
        
        // Prepare output and input streams
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setIn(originalIn);
    }

    @AfterEach
    public void tearDown() {
        // Restore original system streams
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    public void testMain_01() throws Exception {
    	//test signin screen
        setInput("0\n"); // Simulated user input
        Main.main(new String[] {}); // Run the main method
        
        String expectedOutput = "Welcome to Library+\n" + 
                                "###############################################\n" + 
                                "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n" + 
                                "Input: " + 
                                "Thank you for using our service! Have a good day!\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMain_02() throws Exception {
    	//test admin login and user sign
        setInput("2\nadmin\nAdmin123\n0\n1\nUser\nUser\nUser\nUser123\n0\n1\nUser\nUser123\nUser2\nPassword123\n0\n0\n"); // Simulated user input
        Main.main(new String[] {}); // Run the main method
        String expectedOutput = "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Enter your username: Enter your password: Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Define your username: Define your password: Password must contain at least 6 characters\n"
        		+ "Try again\n"
        		+ "Define your username: Define your password: Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Define your username: Define your password: The username is already taken\n"
        		+ "Try again\n"
        		+ "Define your username: Define your password: Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Thank you for using our service! Have a good day!\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testMain_03() throws Exception {
    	//test admin login and add and remove book
        setInput("2.2\n2\nadmin\nAdmin123\nok\n10\n1\n1\nBookTitle\nAuthor\nPublisher\n2022-10-10\nDescription\n-1\nok\n100\n-1\nok\n2\n-1\nok\n2\n2\nok\n-1\n7\n0\n0\n"); // Simulated user input
        Main.main(new String[] {}); // Run the main method
        String expectedOutput = "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Input is not a number. Try again.\n"
        		+ "Input: Enter your username: Enter your password: Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Input is not a number. Try again.\n"
        		+ "Input: Invalid option selected. Please try again.\n"
        		+ "Enter ISBN: Enter Title: Enter Author: Enter Publisher: Enter Publication Date (YYYY-MM-DD): Enter Book Description: Enter Book Price: Price cannot be negative. Please enter a valid price.\n"
        		+ "Enter Book Price: Invalid input for price. Please enter a numeric value.\n"
        		+ "Enter Book Price: Enter Number of Rentable Copies: Number of rentable copies cannot be negative. Please enter a valid number.\n"
        		+ "Enter Number of Rentable Copies: Invalid input for rentable copies. Please enter an integer value.\n"
        		+ "Enter Number of Rentable Copies: Enter Number of Saleable Copies: Number of saleable copies cannot be negative. Please enter a valid number.\n"
        		+ "Enter Number of Saleable Copies: Invalid input for saleable copies. Please enter an integer value.\n"
        		+ "Enter Number of Saleable Copies: Book added to the library: BookTitle by Author ISBN: 1 Publisher: Publisher Publication Date: 2022-10-10 Price: $100.0\n"
        		+ "\n"
        		+ "Book Added!\n"
        		+ "Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Books:\n"
        		+ "0. Book1\n"
        		+ "1. Book2\n"
        		+ "2. Book3\n"
        		+ "3. Book4\n"
        		+ "4. Book5\n"
        		+ "5. Book6\n"
        		+ "6. Book7\n"
        		+ "7. BookTitle\n"
        		+ "Enter the number of the book you want to remove: Input is not a number. Try again.\n"
        		+ "Input: Invalid selection. Please choose a valid book number.\n"
        		+ "Book removed from the library: BookTitle by Author ISBN: 1 Publisher: Publisher Publication Date: 2022-10-10 Price: $100.0\n"
        		+ "Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Thank you for using our service! Have a good day!\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testMain_04() throws Exception {
    	//test user login and search and rent and return book and admin process return
    	setInput("2\nadmin\nAdmin123\n0\n1\nUser33\nUser123\n6\n\n6\nphysicsbook\n6\nmarine biology\n1\nok\n-1\n20\n2\n3\nok\n-1\n20\n0\n0\n2\nadmin\nAdmin123\n3\n2024-12-06\n0\n0\n"); // Simulated user input
        Main.main(new String[] {}); // Run the main method
        String expectedOutput = "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Enter your username: Enter your password: Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Define your username: Define your password: Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Enter search keywords: Search keywords cannot be empty.\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Enter search keywords: No books found matching your search.\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Enter search keywords: Search Results:\n"
        		+ "Book{, Title='Book1', Author='Dr. Emily Waters', Publisher='Oceanic Press', Publication Date='2023-05-15', Description='A comprehensive guide to marine biology and underwater marine ecosystems research techniques.', Price=$45.99, Rentable Copies=10, Saleable Copies=5, Renting Waitlist Size=0, Selling Waitlist Size=0}\n"
        		+ "Book{, Title='Book2', Author='Prof. Liam Fisher', Publisher='DeepSea Publications', Publication Date='2022-08-22', Description='Exploring the ocean depths through advanced marine research methodologies.', Price=$39.5, Rentable Copies=8, Saleable Copies=4, Renting Waitlist Size=0, Selling Waitlist Size=0}\n"
        		+ "Book{, Title='Book3', Author='Dr. Sophia Marine', Publisher='BlueWater Books', Publication Date='2021-11-10', Description='The role of marine biology ecosystems in ocean research and biodiversity studies.', Price=$50.0, Rentable Copies=12, Saleable Copies=6, Renting Waitlist Size=0, Selling Waitlist Size=0}\n"
        		+ "Book{, Title='Book4', Author='Ali Hassan', Publisher='Litres', Publication Date='2024-01-01', Description='Underwater archaeology: uncovering marine biology history and oceanic mysteries.', Price=$10.0, Rentable Copies=2, Saleable Copies=2, Renting Waitlist Size=0, Selling Waitlist Size=0}\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Books:\n"
        		+ "0. Book1\n"
        		+ "1. Book2\n"
        		+ "2. Book3\n"
        		+ "3. Book4\n"
        		+ "4. Book5\n"
        		+ "5. Book6\n"
        		+ "6. Book7\n"
        		+ "Enter the number of the book you want to rent: Input is not a number. Try again.\n"
        		+ "Input: Invalid selection. Please choose a valid book number.\n"
        		+ "Invalid selection. Please choose a valid book number.\n"
        		+ "Book rented successfully: Book3 by Dr. Sophia Marine ISBN: 978-0-545-01022-1 Publisher: BlueWater Books Publication Date: 2021-11-10 Price: $50.0\n"
        		+ "Return Date: 2024-12-20\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Books:\n"
        		+ "0. Book3\n"
        		+ "Enter the number of the book you want to return: Input is not a number. Try again.\n"
        		+ "Input: Invalid selection. Please choose a valid book number.\n"
        		+ "Invalid selection. Please choose a valid book number.\n"
        		+ "Added 10 XP. Current XP: 10/100.\n"
        		+ "Book returned successfully: Book3 by Dr. Sophia Marine ISBN: 978-0-545-01022-1 Publisher: BlueWater Books Publication Date: 2021-11-10 Price: $50.0\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Thank you for using our service! Have a good day!\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testMain_05() throws Exception {
    	//test user login and purchase and rate and get recommendation
    	setInput("2\nadmin\nAdmin123\n1\n123\nBookTitle\nAuthor\nPublisher\n2022-10-10\nDescription\n100\n3\n3\n0\n1\nUser3\nUser123\nok\n9\n2\n50\nok\n7\ncash\nCreditCard\n5\nok\n-1\n20\n6\nok\n-1\n10\n7.5\n4\ncon\ncollaborative\nok\n-1\n3\n0\n0\n");
    	Main.main(new String[] {}); // Run the main method
        String expectedOutput = "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Enter your username: Enter your password: Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Enter ISBN: Enter Title: Enter Author: Enter Publisher: Enter Publication Date (YYYY-MM-DD): Enter Book Description: Enter Book Price: Enter Number of Rentable Copies: Enter Number of Saleable Copies: Book added to the library: BookTitle by Author ISBN: 123 Publisher: Publisher Publication Date: 2022-10-10 Price: $100.0\n"
        		+ "\n"
        		+ "Book Added!\n"
        		+ "Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Define your username: Define your password: Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Input is not a number. Try again.\n"
        		+ "Input: Invalid option selected. Please try again.\n"
        		+ "Books:\n"
        		+ "0. Book1\n"
        		+ "1. Book2\n"
        		+ "2. Book3\n"
        		+ "3. Book4\n"
        		+ "4. Book5\n"
        		+ "5. Book6\n"
        		+ "6. Book7\n"
        		+ "7. BookTitle\n"
        		+ "Enter the number of the book you want to buy: Invalid selection. Please choose a valid book number.\n"
        		+ "Input is not a number. Try again.\n"
        		+ "Input: Enter the payment method (WeChat or CreditCard): Invalid payment method. Try again.\n"
        		+ "Enter the payment method (WeChat or CreditCard): Processing credit card payment of: HK$100.00.\n"
        		+ "Transaction was processed successfully.\n"
        		+ "Added 20 XP. Current XP: 20/100.\n"
        		+ "Book purchased successfully: BookTitle by Author ISBN: 123 Publisher: Publisher Publication Date: 2022-10-10 Price: $100.0\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Books available for rating:\n"
        		+ "0. Book1\n"
        		+ "1. Book2\n"
        		+ "2. Book3\n"
        		+ "3. Book4\n"
        		+ "4. Book5\n"
        		+ "5. Book6\n"
        		+ "6. Book7\n"
        		+ "7. BookTitle\n"
        		+ "Enter the number of the book you want to rate: Input is not a number. Try again.\n"
        		+ "Input: Invalid selection. Please choose a valid book number.\n"
        		+ "Invalid selection. Please choose a valid book number.\n"
        		+ "Enter rating (from 0 to 8): Input is not a valid number. Try again.\n"
        		+ "Input: Invalid rating. Please choose a rating between 0 and 8 (inclusive).\n"
        		+ "Invalid rating. Please choose a rating between 0 and 8 (inclusive).\n"
        		+ "Book successfully rated!\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Enter the recommendation strategy ('content' for Content-Filtering, 'collaborative' for Collaborative-Filtering): Invalid recommendation strategy. Try again.\n"
        		+ "Enter the recommendation strategy ('content' for Content-Filtering, 'collaborative' for Collaborative-Filtering): Enter the required number of recommendations: Input is not a number. Try again.\n"
        		+ "Input: Please enter a positive integer.\n"
        		+ "Recommendations:\n"
        		+ "Book Title: \"Book3\"; Book Description: \"The role of marine biology ecosystems in ocean research and biodiversity studies.\"\n"
        		+ "Book Title: \"Book2\"; Book Description: \"Exploring the ocean depths through advanced marine research methodologies.\"\n"
        		+ "Book Title: \"Book1\"; Book Description: \"A comprehensive guide to marine biology and underwater marine ecosystems research techniques.\"\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Thank you for using our service! Have a good day!\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testMain_06() throws Exception {
    	//test user login and purchase and refund and admin process refund
    	setInput("2\nadmin\nAdmin123\n1\n123\nBookTitle\nAuthor\nPublisher\n2022-10-10\nDescription\n100\n3\n3\n0\n1\nUser333\nUser123\n7\nok\n9\n2\n7\ncash\nCreditCard\n7\nok\n10\n0\n0\n2\nadmin\nAdmin123\n4\n2024-12-06\n0\n0\n");
    	Main.main(new String[] {}); // Run the main method
        String expectedOutput = "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Enter your username: Enter your password: Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Enter ISBN: Enter Title: Enter Author: Enter Publisher: Enter Publication Date (YYYY-MM-DD): Enter Book Description: Enter Book Price: Enter Number of Rentable Copies: Enter Number of Saleable Copies: Book added to the library: BookTitle by Author ISBN: 123 Publisher: Publisher Publication Date: 2022-10-10 Price: $100.0\n"
        		+ "\n"
        		+ "Book Added!\n"
        		+ "Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Define your username: Define your password: Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: No books are currently available for refund.\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Input is not a number. Try again.\n"
        		+ "Input: Invalid option selected. Please try again.\n"
        		+ "Books:\n"
        		+ "0. Book1\n"
        		+ "1. Book2\n"
        		+ "2. Book3\n"
        		+ "3. Book4\n"
        		+ "4. Book5\n"
        		+ "5. Book6\n"
        		+ "6. Book7\n"
        		+ "7. BookTitle\n"
        		+ "Enter the number of the book you want to buy: Enter the payment method (WeChat or CreditCard): Invalid payment method. Try again.\n"
        		+ "Enter the payment method (WeChat or CreditCard): Processing credit card payment of: HK$100.00.\n"
        		+ "Transaction was processed successfully.\n"
        		+ "Added 20 XP. Current XP: 20/100.\n"
        		+ "Book purchased successfully: BookTitle by Author ISBN: 123 Publisher: Publisher Publication Date: 2022-10-10 Price: $100.0\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Books:\n"
        		+ "0. BookTitle\n"
        		+ "Enter the number of the book you want to refund: Input is not a number. Try again.\n"
        		+ "Input: Invalid selection. Please choose a valid book number.\n"
        		+ "Processing credit card refund of: HK$100.00.\n"
        		+ "Transaction was refunded successfully.\n"
        		+ "Deducted 20 XP. Current XP: 0/100.\n"
        		+ "Book refunded successfully: BookTitle by Author ISBN: 123 Publisher: Publisher Publication Date: 2022-10-10 Price: $100.0\n"
        		+ "Main Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \n"
        		+ "Enter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 7 to Refund a Book | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Enter your username: Enter your password: Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Enter Publication Date (YYYY-MM-DD): Converted LocalDate: 2024-12-06\n"
        		+ "\n"
        		+ "Refunds processed!\n"
        		+ "Admin Menu\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 3 to Process Returns | Enter 4 to Process Refunds | Enter 0 to Exit\n"
        		+ "Input: Logging out...\n"
        		+ "Welcome to Library+\n"
        		+ "###############################################\n"
        		+ "Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close\n"
        		+ "Input: Thank you for using our service! Have a good day!\n";

        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
}

