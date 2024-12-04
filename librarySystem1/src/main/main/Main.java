package main.main;

import main.users.*;
import main.authentication.*;
import main.book.*;
import main.transaction.*;
import main.exceptions.*;
import main.recommendation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.math.BigDecimal;

public class Main {
	private static boolean isRunning = true;
	private static String username;
	private static String parole;
	private static Password password = null;
	private static Customer customer = null;
	private static AuthenticationService authenticationService = AuthenticationService.getInstance();
	private static Admin admin = Admin.getInstance();
	private static RecommendationService service;
	private static Data data;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		initialization();

		while (isRunning) {
			int response = openScreen(scanner);
			switch (response) {
			case 1:
			case 2:
				mainMenu(scanner);
				break;
			case 3:
				adminMenu(scanner);
				break;
			default:
				System.out.println("Thank you for using our service! Have a good day!");
				break;
			}
		}
		scanner.close(); // Close the scanner only once here
	}

	private static void initialization() {
		Book book1 = new Book("978-3-16-148410-0", 
				"Book1", "Dr. Emily Waters",
				"Oceanic Press", 
				"2023-05-15", 
				"A comprehensive guide to marine biology and underwater marine ecosystems research techniques.", 45.99, // Price
				10,
				5 
		);

		Book book2 = new Book("978-1-4028-9462-6", "Book2", "Prof. Liam Fisher", "DeepSea Publications", "2022-08-22",
				"Exploring the ocean depths through advanced marine research methodologies.", 39.50, 8, 4);

		Book book3 = new Book("978-0-545-01022-1", "Book3", "Dr. Sophia Marine", "BlueWater Books", "2021-11-10",
				"The role of marine biology ecosystems in ocean research and biodiversity studies.", 50.00, 12, 6);

		Book book4 = new Book("978-0-262-13472-9", "Book4", "Ali Hassan", "Litres", "2024-01-01",
				"Underwater archaeology: uncovering marine biology history and oceanic mysteries.", 10.0, 2, 2);

		Book book5 = new Book("978-0-306-40615-7", "Book5", "Ms. Isabella Dance", "Rhythm House", "2020-03-18",
				"An introduction to dance and choreography for aspiring performers.", 25.75, 7, 3);

		Book book6 = new Book("978-1-86197-876-9", "Book6", "Mr. Ethan Ballet", "Graceful Publishers", "2019-07-30",
				"Ballet history and the evolution of modern dance styles.", 30.00, 5, 2);

		Book book7 = new Book("978-0-14-044911-2", "Book7", "Ms. Ava Rhythm", "MoveIt Press", "2021-12-05",
				"Dance as an art form: exploring rhythm, movement, and creative expression.", 28.50, 0, 0);

		data = new Data();

		Customer customer2 = new Customer("Vladimir");
		Customer customer3 = new Customer("Arsen");

		List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6, book7);
		data.setBooks(books);
		for (Book book : books) {
			Book.addBook(book);
		}

		Map<Customer, Map<Book, BigDecimal>> userRatings = new HashMap<>();

		Map<Book, BigDecimal> user2Ratings = new HashMap<>();
		user2Ratings.put(book3, BigDecimal.valueOf(7.0));
		user2Ratings.put(book4, BigDecimal.valueOf(8.0));
		user2Ratings.put(book6, BigDecimal.valueOf(8.0));
		userRatings.put(customer2, user2Ratings);

		Map<Book, BigDecimal> user3Ratings = new HashMap<>();
		user3Ratings.put(book1, BigDecimal.valueOf(5.5));
		user3Ratings.put(book2, BigDecimal.valueOf(6.0));
		user3Ratings.put(book3, BigDecimal.valueOf(7.5));
		user3Ratings.put(book5, BigDecimal.valueOf(3.0));
		user3Ratings.put(book7, BigDecimal.valueOf(2.5));
		userRatings.put(customer3, user3Ratings);

		data.setUserRatings(userRatings);

		service = new RecommendationService();
	}

	private static int openScreen(Scanner scanner) {
		customer = null;
		username = "";
		parole = "";
		System.out.println("Welcome to Library+");
		System.out.println("###############################################");
		System.out.println("Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close");
		System.out.print("Input: ");
		int response = -1;
		while (response < 0 || response > 3) {
			try {
				String strResponse = scanner.nextLine();
				response = Integer.parseInt(strResponse);
				if (response == 1) {
					boolean done = false;
					while (!done) {
						try {
							System.out.print("Define your username: ");
							username = scanner.nextLine();
							System.out.print("Define your password: ");
							parole = scanner.nextLine();
							customer = authenticationService.registerCustomer(username, parole);
							done = true;
						} catch (ExInvalidPassword e) {
							System.out.print(e.getMessage());
							System.out.println("Try again");
						} catch (ExTakenUsername e) {
							System.out.print(e.getMessage());
							System.out.println("Try again");
						}
					}
				} else if (response == 2) {
					boolean done = false;
					while (!done) {
						try {
							System.out.print("Enter your username: ");
							username = scanner.nextLine();
							System.out.print("Enter your password: ");
							parole = scanner.nextLine();

							if (username.equals("admin")) {
								authenticationService.loginAdmin(parole);
								response = 3;
							} else
								authenticationService.login(username, parole);

							done = true;
						} catch (Exception e) {
							System.out.print(e.getMessage());
							System.out.println("Try again");
						}
					}
				} else if (response == 0)
					isRunning = false;
				else {
					System.out.println("Invalid selection. Please enter 0, 1, or 2.");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}
		return response;
	}

	private static void adminMenu(Scanner scanner) {
		while (true) {
			System.out.println("Admin Menu");
			System.out.println("###############################################");
			System.out.println("Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 0 to Exit");
			System.out.print("Input: ");

			int choice = -1;
			while (choice < 0 || choice > 2) {
				try {
					String strChoice = scanner.nextLine();
					choice = Integer.parseInt(strChoice);

					switch (choice) {
					case 1:
						addBook(scanner);
						break;
					case 2:
						removeBook(scanner);
						break;
					case 0:
						System.out.println("Logging out...");
						return;
					default:
						System.out.println("Invalid option selected. Please try again.");
					}
				} catch (NumberFormatException e) {
					System.out.print("Input is not a number. Try again.\nInput: ");
				}
			}
		}
	}

	private static void addBook(Scanner scanner) {
		System.out.print("Enter ISBN: ");
		String isbn = scanner.nextLine().trim();

		System.out.print("Enter Title: ");
		String title = scanner.nextLine().trim();

		System.out.print("Enter Author: ");
		String author = scanner.nextLine().trim();

		System.out.print("Enter Publisher: ");
		String publisher = scanner.nextLine().trim();

		System.out.print("Enter Publication Date (YYYY-MM-DD): ");
		String publicationDate = scanner.nextLine().trim();

		System.out.print("Enter Book Description: ");
		String bookDescription = scanner.nextLine().trim();

		double price = 0.0;
		while (true) {
			try {
				System.out.print("Enter Book Price: ");
				String priceInput = scanner.nextLine().trim();
				price = Double.parseDouble(priceInput);
				if (price < 0) {
					System.out.println("Price cannot be negative. Please enter a valid price.");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input for price. Please enter a numeric value.");
			}
		}

		int rentableCopies = 0;
		while (true) {
			try {
				System.out.print("Enter Number of Rentable Copies: ");
				String rentableInput = scanner.nextLine().trim();
				rentableCopies = Integer.parseInt(rentableInput);
				if (rentableCopies < 0) {
					System.out.println("Number of rentable copies cannot be negative. Please enter a valid number.");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input for rentable copies. Please enter an integer value.");
			}
		}

		int saleableCopies = 0;
		while (true) {
			try {
				System.out.print("Enter Number of Saleable Copies: ");
				String saleableInput = scanner.nextLine().trim();
				saleableCopies = Integer.parseInt(saleableInput);
				if (saleableCopies < 0) {
					System.out.println("Number of saleable copies cannot be negative. Please enter a valid number.");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input for saleable copies. Please enter an integer value.");
			}
		}

		// Creating the Book instance
		Book book = new Book(isbn, title, author, publisher, publicationDate, bookDescription, price, rentableCopies,
				saleableCopies);

		admin.addBook(book);

		System.out.println("\nBook Added!");
	}

	private static void removeBook(Scanner scanner) {
		List<Book> allBooks = Book.getAllBooks();

		if (allBooks.isEmpty()) {
			System.out.println("No books are currently in the system.");
			return;
		}

		System.out.println("Books:");
		for (int i = 0; i < allBooks.size(); i++) {
			Book book = allBooks.get(i);
			System.out.println((i) + ". " + book.getBookTitle());
		}

		System.out.print("Enter the number of the book you want to remove: ");
		int bookChoice = -1;

		while (bookChoice < 0 || bookChoice > allBooks.size() - 1) {
			try {
				String strBookChoice = scanner.nextLine();
				bookChoice = Integer.parseInt(strBookChoice);
				if (bookChoice < 0 || bookChoice > allBooks.size() - 1) {
					System.out.println("Invalid selection. Please choose a valid book number.");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		Book selectedBook = allBooks.get(bookChoice);
		admin.removeBook(selectedBook);
	}

	private static void mainMenu(Scanner scanner) {
		while (true) {
			System.out.println("Main Menu");
			System.out.println("###############################################");
			System.out.println(
					"Enter 1 to Rent a Book | Enter 2 to Purchase a Book | Enter 3 to Return a Book | Enter 4 to Get Book Recommendations | \nEnter 5 to Rate a Book | Enter 6 to Search for a Book | Enter 0 to Exit");
			System.out.print("Input: ");

			int choice = -1;
			while (choice < 0 || choice > 6) {
				try {
					String strChoice = scanner.nextLine();
					choice = Integer.parseInt(strChoice);

					switch (choice) {
					case 1:
						requestBookRent(scanner);
						break;
					case 2:
						purchaseBook(scanner);
						break;
					case 3:
						returnBook(scanner);
						break;
					case 4:
						getRecommendations(scanner);
						break;
					case 5:
						rateBook(scanner);
						break;
					case 6:
						searchBook(scanner);
						break;
					case 0:
						System.out.println("Logging out...");
						return;
					default:
						System.out.println("Invalid option selected. Please try again.");
					}
				} catch (NumberFormatException e) {
					System.out.print("Input is not a number. Try again.\nInput: ");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private static void requestBookRent(Scanner scanner) {
		List<Book> allBooks = Book.getAllBooks();

		if (allBooks.isEmpty()) {
			System.out.println("No books are currently available for rent.");
			return;
		}

		System.out.println("Books:");
		for (int i = 0; i < allBooks.size(); i++) {
			Book book = allBooks.get(i);
			System.out.println((i) + ". " + book.getBookTitle());
		}

		System.out.print("Enter the number of the book you want to rent: ");
		int bookChoice = -1;

		while (bookChoice < 0 || bookChoice > allBooks.size() - 1) {
			try {
				String strBookChoice = scanner.nextLine();
				bookChoice = Integer.parseInt(strBookChoice);
				if (bookChoice < 0 || bookChoice > allBooks.size() - 1) {
					System.out.println("Invalid selection. Please choose a valid book number.");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		Book selectedBook = allBooks.get(bookChoice);
		customer.rentBook(selectedBook);
	}

	private static void purchaseBook(Scanner scanner) {
		List<Book> allBooks = Book.getAllBooks();

		if (allBooks.isEmpty()) {
			System.out.println("No books are currently available for sale.");
			return;
		}

		System.out.println("Books:");
		for (int i = 0; i < allBooks.size(); i++) {
			Book book = allBooks.get(i);
			System.out.println((i) + ". " + book.getBookTitle());
		}

		System.out.print("Enter the number of the book you want to buy: ");
		int bookChoice = -1;

		while (bookChoice < 0 || bookChoice > allBooks.size() - 1) {
			try {
				String strBookChoice = scanner.nextLine();
				bookChoice = Integer.parseInt(strBookChoice);
				if (bookChoice < 0 || bookChoice > allBooks.size() - 1) {
					System.out.println("Invalid selection. Please choose a valid book number.");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		Book selectedBook = allBooks.get(bookChoice);

		String paymentMethod;

		while (true) {
			System.out.print("Enter the payment method (WeChat or CreditCard): ");
			paymentMethod = scanner.nextLine().trim();
			if (paymentMethod.equals("WeChat") || paymentMethod.equals("CreditCard")) {
				break;
			} else {
				System.out.println("Invalid payment method. Try again.");
			}

		}

		customer.purchaseBook(selectedBook, paymentMethod);
	}

	private static void returnBook(Scanner scanner) {
		Set<Book> rentedBooks = customer.getRentedBooks();

		if (rentedBooks.isEmpty()) {
			System.out.println("No books to return.");
			return;
		}

		System.out.println("Books:");
		List<Book> bookList = new ArrayList<>(rentedBooks);
		for (int i = 0; i < bookList.size(); i++) {
			Book book = bookList.get(i);
			System.out.println(i + ". " + book.getBookTitle());
		}

		System.out.print("Enter the number of the book you want to return: ");
		int bookChoice = -1;

		while (bookChoice < 0 || bookChoice > bookList.size() - 1) {
			try {
				String strBookChoice = scanner.nextLine();
				bookChoice = Integer.parseInt(strBookChoice);
				if (bookChoice < 0 || bookChoice > bookList.size() - 1) {
					System.out.println("Invalid selection. Please choose a valid book number.");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		Book selectedBook = bookList.get(bookChoice);

		customer.returnBook(selectedBook);
	}

	private static void getRecommendations(Scanner scanner) {
		String recommendationStrategy;

		while (true) {
			System.out.print(
					"Enter the recommendation strategy ('content' for Content-Filtering, 'collaborative' for Collaborative-Filtering): ");
			recommendationStrategy = scanner.nextLine().trim();
			if (recommendationStrategy.equals("content") || recommendationStrategy.equals("collaborative")) {
				break;
			} else {
				System.out.println("Invalid recommendation strategy. Try again.");
			}
		}

		service.setStrategy(recommendationStrategy, data);

		System.out.print("Enter the required number of recommendations: ");
		int recommendationsNumber = -1;

		while (true) {
			try {
				String recommendationNumberString = scanner.nextLine();
				recommendationsNumber = Integer.parseInt(recommendationNumberString);
				if (recommendationsNumber > 0) {
					break;
				} else {
					System.out.println("Please enter a positive integer.");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		List<Book> recommendations = Arrays.asList(service.getRecommendations(customer, recommendationsNumber));

		System.out.println("Recommendations:");

		for (Book recommendation : recommendations) {
			System.out.print("Book Title: " + '"' + recommendation.getBookTitle() + "\"; ");
			System.out.println("Book Description: " + '"' + recommendation.getBookDescription() + '"');
		}
	}

	private static void rateBook(Scanner scanner) {
		Map<Customer, Map<Book, BigDecimal>> userRatings = data.getUserRatings();
		Map<Book, BigDecimal> bookRating = userRatings.computeIfAbsent(customer, k -> new HashMap<>());

		List<Book> books = Book.getAllBooks();

		if (books.isEmpty()) {
			System.out.println("No books available to rate.");
			return;
		}

		System.out.println("Books available for rating:");
		for (int i = 0; i < books.size(); i++) {
			Book book = books.get(i);
			System.out.println(i + ". " + book.getBookTitle());
		}

		System.out.print("Enter the number of the book you want to rate: ");
		int bookChoice = -1;

		while (bookChoice < 0 || bookChoice > books.size() - 1) {
			try {
				String strBookChoice = scanner.nextLine();
				bookChoice = Integer.parseInt(strBookChoice);
				if (bookChoice < 0 || bookChoice > books.size() - 1) {
					System.out.println("Invalid selection. Please choose a valid book number.");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		Book selectedBook = books.get(bookChoice);

		System.out.print("Enter rating (from 0 to 8): ");
		double rating = -1;

		while (rating < 0 || rating > 8) {
			try {
				String strRating = scanner.nextLine();
				rating = Double.parseDouble(strRating);
				if (rating < 0 || rating > 8.0) {
					System.out.println("Invalid rating. Please choose a rating between 0 and 8 (inclusive).");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a valid number. Try again.\nInput: ");
			}
		}

		bookRating.put(selectedBook, BigDecimal.valueOf(rating));
		System.out.println("Book successfully rated!");
	}

	private static void searchBook(Scanner scanner) {
		System.out.print("Enter search keywords: ");
		String searchString = scanner.nextLine();

		if (searchString.isEmpty()) {
			System.out.println("Search keywords cannot be empty.");
			return;
		}

		List<Book> searchResults = Book.search(searchString);

		if (searchResults == null || searchResults.isEmpty()) {
			System.out.println("No books found matching your search.");
		} else {
			System.out.println("Search Results:");
			for (Book book : searchResults) {
				System.out.println(book); // Ensure Book.toString() is meaningful
			}
		}
	}

	public static void resetState() {
	    isRunning = true;
	    username = null;
	    parole = null;
	    password = null;
	    customer = null;
	    authenticationService = AuthenticationService.getInstance();
	    admin = Admin.getInstance();
	    service = null;
	    data = null;
	    Book.resetAllBooks();
	}

}
