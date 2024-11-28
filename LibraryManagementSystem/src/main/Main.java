package main;

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
		// AS.registerAdmin(cred, cred);
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
		Book book1 = new Book("Book1", "A comprehensive guide to marine biology and underwater research techniques.");
		Book book2 = new Book("Book2", "Exploring the ocean depths through advanced marine research methodologies.");
		Book book3 = new Book("Book3",
				"The role of marine biology ecosystems in ocean research and biodiversity studies.");
		Book book4 = new Book("000-0000000000", "Book4", "Ali", "Litres", "2024-01-01",
				"Underwater archaeology: uncovering marine biology history and oceanic mysteries.", 10.0, 2, 2);

		Book book5 = new Book("Book5", "An introduction to dance and choreography for aspiring performers.");
		Book book6 = new Book("Book6", "The history of ballet and the evolution of modern dance styles.");
		Book book7 = new Book("Book7", "Dance as an art form: exploring rhythm, movement, and creative expression.");

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
				String strResponse = scanner.next();
				response = Integer.parseInt(strResponse);
				if (response == 1) {
					boolean done = false;
					while (!done) {
						try {

							System.out.print("Define your username: ");
							username = scanner.next();
							System.out.print("Define your password: ");
							parole = scanner.next();
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
							username = scanner.next();
							System.out.print("Enter your password: ");
							parole = scanner.next();

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
					System.out.println("GLUP SI KO KURAC, TO UOPSTE NIJE TOKEN KOJI TREBA DA KORISTIS");
				}
			} /*
				 * catch (ExInvalidToken e) { System.out.print(e.getMessage()); }
				 */ catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}
		return response;
	}

	private static void adminMenu(Scanner scanner) {
		while (true) {
			System.out.println("Main Menu");
			System.out.println("###############################################");
			System.out.println("Enter 1 to Add a Book | Enter 2 to Remove a Book | Enter 0 to Exit");
			System.out.print("Input: ");

			int choice = -1;
			while (choice < 0 || choice > 2) {
				try {
					String strChoice = scanner.next();
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
		String isbn = scanner.nextLine();

		System.out.print("Enter Title: ");
		String title = scanner.nextLine();

		System.out.print("Enter Author: ");
		String author = scanner.nextLine();

		System.out.print("Enter Publisher: ");
		String publisher = scanner.nextLine();

		System.out.print("Enter Publication Date (YYYY-MM-DD): ");
		String publicationDate = scanner.nextLine();

		System.out.print("Enter Book Description: ");
		String bookDescription = scanner.nextLine();

		System.out.print("Enter Book Price: ");
		double price = scanner.nextDouble();

		System.out.print("Enter Number of Rentable Copies: ");
		int rentableCopies = scanner.nextInt();

		System.out.print("Enter Number of Saleable Copies: ");
		int saleableCopies = scanner.nextInt();

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
				String strBookChoice = scanner.next();
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
					String strChoice = scanner.next();
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
				String strBookChoice = scanner.next();
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
				String strBookChoice = scanner.next();
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
			paymentMethod = scanner.next().trim();
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
				String strBookChoice = scanner.next();
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
			recommendationStrategy = scanner.next().trim();
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
				String recommendationNumberString = scanner.next();
				recommendationsNumber = Integer.parseInt(recommendationNumberString);
				if (recommendationsNumber > 0) {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		List<Book> recommendations = Arrays.asList(service.getRecommendations(customer, recommendationsNumber));

		System.out.println("Recommendations:");

		for (Book recommendation : recommendations) {
			System.out.println(recommendation.getBookTitle());
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
				String strBookChoice = scanner.next();
				bookChoice = Integer.parseInt(strBookChoice);
				if (bookChoice < 0 || bookChoice > books.size() - 1) {
					System.out.println("Invalid selection. Please choose a valid book number.");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		Book selectedBook = books.get(bookChoice);

		System.out.print("Enter rating: ");
		double rating = -1;

		while (rating < 0 || rating > 8) {
			try {
				String strRating = scanner.next();
				rating = Double.parseDouble(strRating);
				if (rating < 0 || rating > 8.0) {
					System.out.println("Invalid rating. Please choose a rating between 0 and 8 (inclusive).");
				}
			} catch (NumberFormatException e) {
				System.out.print("Input is not a number. Try again.\nInput: ");
			}
		}

		bookRating.put(selectedBook, BigDecimal.valueOf(rating));
		System.out.println("Book successfully rated!");
	}

	private static void searchBook(Scanner scanner) {
		System.out.print("Enter search keywords: ");
		String searchString = scanner.next();

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

}