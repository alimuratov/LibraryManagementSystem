package main.exceptions;

public class UserRatingsNotFound extends RuntimeException {
	 public UserRatingsNotFound(String message) {
	        super(message);
    }
}