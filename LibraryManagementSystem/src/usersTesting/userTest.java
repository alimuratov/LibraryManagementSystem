package usersTesting;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.kocka.Password;
import main.users.Customer;

class userTest {

	@Test
	void test_01() {
		Password password = new Password("Password");
		Customer customer = new Customer("Aleksandar", password);
		System.out.println(customer.toString());
		
	}
	
	@Test
	void test_02() {
		
	}
	
	@Test
	void test_03() {
		
	}
	
	@Test
	void test_04() {
		
	}
	
	@Test
	void test_05() {
		
	}
}
